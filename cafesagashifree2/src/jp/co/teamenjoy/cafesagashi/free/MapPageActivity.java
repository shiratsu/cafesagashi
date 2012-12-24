package jp.co.teamenjoy.cafesagashi.free;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jp.co.teamenjoy.cafesagashi.free.R;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.HistoryDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.LineDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.LocationHelper;
import jp.co.teamenjoy.cafesagashi.free.util.AppConst;
import jp.co.teamenjoy.cafesagashi.free.util.CafeFinder;
import jp.co.teamenjoy.cafesagashi.free.util.HistoryMapTask;
import jp.co.teamenjoy.cafesagashi.free.util.MasterUpdateTask;
import jp.co.teamenjoy.cafesagashi.free.util.RequestRunnable;
import jp.co.teamenjoy.cafesagashi.free.view.ItemsMapView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;


import android.app.ProgressDialog;

import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ZoomControls;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class MapPageActivity extends MapActivity implements OnKeyListener{

	private static final String LOGTAG_CLASS = MapPageActivity.class.getSimpleName();
	
    private LocationManager mLocationManager;
    
    // 画面コントロール
    // ダイアログ
    private ProgressDialog mDialog;
    
    
    public ItemsMapView map_view;
    private MapController mapController;
    private CafeFinder mCafeFinder;// カフェ検索
    private int zoomLevel = 15;
    
    private GeoPoint savePoint;
    
    // リソース
    private Resources mRes;
    private EditText searchWordText;
    
    // GPSへの問い合わせ周期
    static final int MIN_TIME = 0;
    static final int MIN_METER = 0;
    private Button topButton;
    private Geocoder geocoder;
    
    private String keyword;
    
    private SQLiteDatabase mDb;
    // テーブルの名前
    static final String TABLE = "cafeMaster";
    private CafeDataHelper cdh;
    private HistoryDataHelper hdh;
    private List<String> pinAry = new ArrayList<String>();
    private boolean notSaveFlag = false;
    private boolean restartFlag = false;
    
    private final String siteId = "2495";
	private final String locationId = "2862";

    /*
     * カフェデータへのアクセスが完了すると呼び出される
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            GeoPoint gp = (GeoPoint) msg.obj;
            if(gp != null){
            	displayCafe(gp);
            	if (mDialog != null) {
            		mDialog.dismiss();
            	}
            }else{
            	// 現在地が取得できない場合はメッセージを表示して終わり
	            Toast.makeText(MapPageActivity.this,
	                    "検索に失敗しました", Toast.LENGTH_SHORT)
	                    .show();
	            if(mDialog != null){
					mDialog.dismiss();
					
				}
            }
        }
    };
    
    /**
     * カフェを配置
     * @param loadPoint
     */
    private void displayCafe(GeoPoint loadPoint) {
		// TODO Auto-generated method stub
    	if (loadPoint == null) {
            return;
        }
    	savePoint = loadPoint;
    	
    	//システム内に保存する
    	saveGeoData(savePoint);
    	
        // 全カフェが入る大きさに拡大するときに使う
        int nowLat = loadPoint.getLatitudeE6();
        int nowLon = loadPoint.getLongitudeE6();
    	int maxLat = loadPoint.getLatitudeE6();
        int maxLng = loadPoint.getLongitudeE6();
        int minLat = maxLat;
        int minLng = maxLng;
        
        // 現在地とカフェの中心を求め、そこに移動するときに使う
        int midLat = maxLat;
        int midLng = maxLng;
        
        int size = mCafeFinder.size();
        int counter = 0;
        for (int i = 0; i < size; ++i) {
        	HashMap cafe = mCafeFinder.getCafe(i);
        	GeoPoint cafePoint = mCafeFinder.getCafeLocation(cafe);
        	String cafeName = mCafeFinder.getCafeName(cafe);
        	String storeFlag = mCafeFinder.getStoreFlag(cafe);
        	String cafeId = mCafeFinder.getCafeId(cafe);
        	if(!pinAry.contains(cafeId)){
	        	double distance = mCafeFinder.getCafeDistance(cafe);
	        	String cafeDistance = Math.round(distance) + "m";
	        	String msg = cafeName+ " (" + cafeDistance + ")";
	        	
	        	if("1".equals(storeFlag)){
	        		map_view.addStab(cafePoint, msg,cafeId,map_view,MapPageActivity.this);
	        	}else if("2".equals(storeFlag)){
	        		map_view.addDtour(cafePoint, msg,cafeId,map_view,MapPageActivity.this);
	        	}else if("3".equals(storeFlag)){
	        		map_view.addTurrys(cafePoint, msg,cafeId,map_view,MapPageActivity.this);
	        	}else if("4".equals(storeFlag)){
	        		map_view.addSanmaruk(cafePoint, msg,cafeId,map_view,MapPageActivity.this);
	        	}else if("5".equals(storeFlag)){
	        		map_view.addExcelShiorl(cafePoint, msg,cafeId,map_view,MapPageActivity.this);
	        	}else if("6".equals(storeFlag)){
	        		map_view.addShiatols(cafePoint, msg,cafeId,map_view,MapPageActivity.this);
	        	}else if("7".equals(storeFlag)){
	        		map_view.addVeroche(cafePoint, msg,cafeId,map_view,MapPageActivity.this);
	        	}else if("8".equals(storeFlag)){
	        		map_view.addCafedo(cafePoint, msg,cafeId,map_view,MapPageActivity.this);
	        	}else if("9".equals(storeFlag)){
	        		map_view.addKomeda(cafePoint, msg,cafeId,map_view,MapPageActivity.this);
	        	}else if("Z".equals(storeFlag)){
	        		map_view.addKoten(cafePoint, msg,cafeId,map_view,MapPageActivity.this);
	        	}
	        	
	        	// 移動と拡大のためのデータ計算
	            int lat = cafePoint.getLatitudeE6();
	            int lng = cafePoint.getLongitudeE6();
	            midLat += lat;
	            midLng += lng;
	            maxLat = maxLat < lat ? lat : maxLat;
	            minLat = lat < minLat ? lat : minLat;
	            maxLng = maxLng < lng ? lng : maxLng;
	            minLng = lng < minLng ? lng : minLng;
        		pinAry.add(cafeId);
        	}
            // 例外なく処理できた数
            counter++;
            
        }
        map_view.moveGPoint(new GeoPoint(nowLat, nowLon));
        /*
        if (0 < counter) {
        	// 自分と駅が入る大きさに地図を拡大する
            map_view.spanMap(maxLat - minLat, maxLng - minLng);
        }
        */
        if(notSaveFlag != true){
        	double lat = (double) savePoint.getLatitudeE6() / (double) 1E6;
        	double lon = (double) savePoint.getLongitudeE6() / (double) 1E6;
        	String latStr = String.valueOf(lat);
        	String lonStr = String.valueOf(lon);
        	HistoryMapTask task = new HistoryMapTask(this,geocoder);
    		task.execute(keyword,latStr,lonStr);
        }
        notSaveFlag = false;
        keyword = null;
        restartFlag = false;
	}
    
    /**
     * ローカルシステム内にデータを保存する
     * @param savePoint2
     */
	private void saveGeoData(GeoPoint savePoint2) {
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
			Editor e = pref.edit();
			e.putInt("save_lat", savePoint2.getLatitudeE6());
			e.putInt("save_lon", savePoint2.getLongitudeE6());
			e.commit();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		setContentView(R.layout.map);
		
		
		
		//MapViewを取り出す
		map_view = (ItemsMapView) findViewById(R.id.map_view);
		map_view.setClickable(true);
		map_view.setBuiltInZoomControls(true);
		mapController = map_view.getController();
		mapController.setZoom(17);
		        
        
        //キーワード検索
		/*
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showSearchDialog();
            }
         });
        */
        
		//トップへボタン
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(MapPageActivity.this,TopPageActivity.class);
		        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		        startActivity(intent);
            }

         });
        
		searchWordText = (EditText) findViewById(R.id.searchWord);
        searchWordText.setOnKeyListener(this);
        searchWordText.setFocusable(false);
        searchWordText.setFocusableInTouchMode(true);
        
        
        
        // リソース
        mRes = getResources();

        // データベースを読み込み用にオープンする
        cdh = new CafeDataHelper(this);
        
        // データベースを書き込み用にオープンする
        hdh = new HistoryDataHelper(this);
        
        // ジオコーダ生成
        geocoder = new Geocoder(this, Locale.JAPAN);
        
        //カフェ検索オブジェクト
        mCafeFinder = new CafeFinder(cdh,geocoder);
        
        //DBをオープン
        mCafeFinder.open();
        
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        
        
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	
	
	@Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	//メニューインフレーターを取得
    	MenuInflater inflater = getMenuInflater();
    	//xmlのリソースファイルを使用してメニューにアイテムを追加
    	inflater.inflate(R.menu.map_menu, menu);
    	//できたらtrueを返す
    	return true;
    }
	
	/**
	 * ボタンをおしたとき
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		int id = item.getItemId();
		
		//XML中のメニューボタンにアクセスするにはR.id以下を利用する
		switch (id) {
			case R.id.now:
				//現在地を取得
            	loadNowPosition();
				break;
			case R.id.center:
				//地図の中心地のカフェを取得
            	loadCenterPosition();
				break;
			case R.id.option:
				cdh.close();
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(MapPageActivity.this,PreferencePageActivity.class);
		        startActivity(intent);
				break;
			
//			case R.id.add:
//				// インテントへのインスタンス生成  
//		        Intent inAdd = new Intent(MapPageActivity.this,AddCafePageActivity.class);
//		        startActivity(inAdd);
//				break;
			case R.id.list:
				cdh.close();
            	// インテントへのインスタンス生成 
		        Intent itlist = new Intent(MapPageActivity.this,CafeListPageActivity.class);
		        startActivity(itlist);
				break;	
		}

		return true;
	}
	
	/**
	 * ローカルデータを取得
	 * @return
	 */
	private GeoPoint feedLocalData() {
		// TODO Auto-generated method stub
		notSaveFlag=true;
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		int lat = pref.getInt("save_lat", 0);
		int lon = pref.getInt("save_lon", 0);
		return new GeoPoint(lat, lon);
	}

	/**
	 * 地図の中心地のカフェを取得
	 */
	private void loadCenterPosition() {
		// TODO Auto-generated method stub
		GeoPoint gp = map_view.getMapCenter();
		notSaveFlag = true;
		if (gp == null) {
            // 現在地が取得できない場合はメッセージを表示して終わり
            Toast.makeText(MapPageActivity.this,
                    mRes.getString(R.string.warning_locationNotAvailable), Toast.LENGTH_SHORT)
                    .show();
            return;

        }

/*		
        String title = mRes.getString(R.string.load_dialog_title);
        String msg = mRes.getString(R.string.load_dialog_msg);
        mDialog = ProgressDialog.show(this, title, msg, true, true);
*/
        // 現在地が取得できれば近くの駅を探して表示する
        // 今表示中のカフェをクリア
        //map_view.clearCafes();

        //現在地から緯度経度を取得してセット
		double distance = (21-zoomLevel)*85;
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
        // SQLITE3へアクセス
		mCafeFinder.setPreference(pref);
        // SQLITE3へアクセス
        mCafeFinder.setGPoint(gp,distance);
        new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
        return;
	}
	
	
	/**
	 * 住所検索
	 * @param keyword
	 * @throws IOException 
	 */
	private void searchAddressMap(String keyword){
		// TODO Auto-generated method stub
		//住所検索プログレスダイアログ表示
		if (mDialog == null || !mDialog.isShowing()) {
			mDialog = ProgressDialog.show(this,
	               getString(R.string.progressDialog_addressSearchTitle),
	               getString(R.string.progressDialog_addressSearchMessage));
		}
		pinAry = new ArrayList<String>();
		//住所を取得
		zoomLevel = map_view.getZoomLevel();
		//addressオブジェクトから緯度経度を取得してセット
		double distance = (21-zoomLevel)*85;
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
        // SQLITE3へアクセス
		mCafeFinder.setPreference(pref);
		mCafeFinder.setKeywordDist(keyword,distance);
		
		new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
		
	    return;
	}

	
	
	@Override
    protected void onStart() {
		super.onStart();
		//ローカルからデータを取得
        savePoint = feedLocalData();
        
        //遷移元からキーワードを受け取る
    	Bundle extras = getIntent().getExtras();
    	String stationLat = null;
    	String stationLon = null;
    	GeoPoint stationGp = null;
    	
    	//戻ってきた場合は、無視
    	if(restartFlag == false){
	    	if(extras != null){
	    		keyword = extras.getString("stationName");
	    		stationLat = extras.getString("stationLat");
	    		stationLon = extras.getString("stationLon");
	    		notSaveFlag = extras.getBoolean("notSaveFlag");
	    	}
    	}
    	if(stationLat != null && stationLon != null){
    		double lat = Double.valueOf(stationLat);
    		double lon = Double.valueOf(stationLon);
    		int int_lat = (int) (Double.valueOf(lat)*1E6);
    		int int_lon = (int) (Double.valueOf(lon)*1E6);
    		stationGp = new GeoPoint(int_lat, int_lon);
    	}
        
    	if(stationGp != null){
    		//現在地から緯度経度を取得してセット
    		double distance = (21-zoomLevel)*85;
    		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
            // SQLITE3へアクセス
    		mCafeFinder.setPreference(pref);
            mCafeFinder.setGPoint(stationGp,distance);
            new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
            return;
    	}else if(keyword != null){
    		
    		//住所検索
    		searchAddressMap(keyword);
    		
    		
    	}else if(savePoint != null){
    		//現在地から緯度経度を取得してセット
    		notSaveFlag = true;
    		double distance = (21-zoomLevel)*85;
    		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
            // SQLITE3へアクセス
    		mCafeFinder.setPreference(pref);
            mCafeFinder.setGPoint(savePoint,distance);
            mCafeFinder.setCenterFlag(true);
            new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
        	return;
    	}
    	
        
		
    }
	
	
	
	
	
	/**
	 * 現在地を取得
	 */
	private void loadNowPosition() {
		
		//住所検索プログレスダイアログ表示
		if (mDialog == null || !mDialog.isShowing()) {
			mDialog = new ProgressDialog(this);
			mDialog.setCancelable(true);
			mDialog.setTitle(getString(R.string.progressDialog_getLocationTitle));
			mDialog.setMessage(getString(R.string.progressDialog_getLocationMessage));
			mDialog.show();
		}
		
		//速度、電力消費などから適切な位置情報サービスを選択する
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        //使える中で最も条件にヒットする位置情報サービスを取得する
        final String bestProvider_ = mLocationManager.getBestProvider(criteria, true);

        // 最後に取得できた位置情報が5分以内のものであれば有効とします。
		final Location lastKnownLocation = mLocationManager.getLastKnownLocation(bestProvider_);
		// XXX - 必要により判断の基準を変更してください。
		if (lastKnownLocation != null && (new Date().getTime() - lastKnownLocation.getTime()) <= (5 * 60 * 1000L)) {
			setNowLocation(lastKnownLocation);
			return;
		}
        
        
        mLocationManager.requestLocationUpdates(
				bestProvider_,
//                LocationManager.NETWORK_PROVIDER,
                0,
                0,
                onLocationUpdate);
        return;
	}
	
	/**
	 * 現在地をセット
	 * @param lastKnownLocation
	 */
	private void setNowLocation(Location nowLocation) {
		// TODO Auto-generated method stub
		GeoPoint kgp = LocationHelper.getGeoPointLatLong(nowLocation.getLatitude(), nowLocation.getLongitude());
    	//現在地から緯度経度を取得してセット
        zoomLevel = map_view.getZoomLevel();
		//addressオブジェクトから緯度経度を取得してセット
		double distance = (21-zoomLevel)*85;
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);

        // SQLITE3へアクセス
		mCafeFinder.setPreference(pref);
		mCafeFinder.setGPoint(kgp,distance);
        new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
	}

	private LocationListener onLocationUpdate = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			setNowLocation(location);
			mLocationManager.removeUpdates(onLocationUpdate);
			
		}
        public void onProviderDisabled(String provider) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
	
	
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * ローカルデータを取得
	 * @return
	 */
	private GeoPoint feedLocalData1() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		int lat = pref.getInt("lat", 0);
		int lon = pref.getInt("lon", 0);
		return new GeoPoint(lat, lon);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_CENTER:
				case KeyEvent.KEYCODE_ENTER:
					keyword = searchWordText.getText().toString();
					searchAddressMap(keyword);
					break;
	
			}
			
		}
		return false;
	}



	@Override
    protected void onRestart() {
		super.onRestart();
		
		
		
		map_view.clearCafes();
		pinAry = new ArrayList<String>();
		mapController.setZoom(17);
		notSaveFlag = true;
		//戻ってきて始まった場合
		restartFlag = true;
		/*
		mapController.setZoom(17);
		//DBをオープン
        mCafeFinder.open();
		if(savePoint != null){
			
			map_view.clearCafes();
			pinAry = new ArrayList<String>();
        	//現在地から緯度経度を取得してセット
    		double distance = (21-zoomLevel)*85;
    		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
            // SQLITE3へアクセス
    		mCafeFinder.setPreference(pref);
            mCafeFinder.setGPoint(savePoint,distance);
            new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
        	return;
        }
        */
	}	
	

	@Override
	protected void onPause() {
		
		if (mLocationManager != null) {
            mLocationManager.removeUpdates(onLocationUpdate);
        }
        if (mDialog != null) {
    		mDialog.dismiss();
    	}
		super.onPause();
		
	}
	@Override
    public void onDestroy() {
    	
		super.onDestroy();
		cdh.close();
	
    }

	

	
	
}
