package jp.co.teamenjoy.cafesagashi.free;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jp.co.teamenjoy.cafesagashi.free.R;
import jp.co.teamenjoy.cafesagashi.free.adapter.CafeListAdapter;

import jp.co.teamenjoy.cafesagashi.free.helper.CafeDataHelper;

import jp.co.teamenjoy.cafesagashi.free.helper.LocationHelper;

import jp.co.teamenjoy.cafesagashi.free.util.CafeFinder;
import jp.co.teamenjoy.cafesagashi.free.util.GeoCompare;
import jp.co.teamenjoy.cafesagashi.free.util.HistoryListTask;

import jp.co.teamenjoy.cafesagashi.free.util.RequestRunnable;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;

import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

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


public class CafeListPageActivity extends ListActivity implements OnKeyListener{

	private static final String LOGTAG_CLASS = CafeListPageActivity.class.getSimpleName();
	
	
	private LocationManager mLocationManager;
    
	private final String siteId = "2495";
	private final String locationId = "2862";

	
    
    // 画面コントロール
    // ダイアログ
    private ProgressDialog mDialog;
    private Button topButton;
    private CafeFinder mCafeFinder;// カフェ検索
    
    private GeoPoint savePoint;
    // リソース
    private Resources mRes;
    
    // GPSへの問い合わせ周期
    static final int MIN_TIME = 0;
    static final int MIN_METER = 0;
	
    private CafeListAdapter pda;
    
    private Geocoder geocoder;
    
    private String keyword;
    
    private SQLiteDatabase mDb;
    
    private List<HashMap> cafeList = new ArrayList<HashMap>();
    
    // テーブルの名前
    static final String TABLE = "cafeMaster";
    private CafeDataHelper cdh;
    
    private EditText searchWordText;
    
    private boolean notSaveFlag = false;
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
	            Toast.makeText(CafeListPageActivity.this,
	                    "検索に失敗しました", Toast.LENGTH_SHORT)
	                    .show();
	            if(mDialog != null){
					mDialog.dismiss();
					
				}
            }
        }
    };
    
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
    	cafeList = new ArrayList<HashMap>();
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
        	HashMap cafeMap = new HashMap();
        	double distance = mCafeFinder.getCafeDistance(cafe);
        	int iine = mCafeFinder.getCafeNice(cafe);
        	String cafeDistance = Math.round(distance) + "m";
        	cafe.put("distance", cafeDistance);
        	cafe.put("iine", Integer.valueOf(iine));
        	cafe.put("sortDistance", Integer.valueOf((int) Math.round(distance)));
            cafeList.add(cafe);
        }
        
        //距離でソート
        GeoCompare comparator = new GeoCompare();
        Collections.sort(cafeList, comparator );
        
        
        
        //ListViewにセット
        pda = null;
        if(pda == null){
        	//pda.clear();
        	pda = new CafeListAdapter(this, R.layout.cafelist_row, cafeList);
        }
        setListAdapter(pda);
        
        ListView listView = (ListView) findViewById(android.R.id.list);
        
      //ListViewの各項目選択の処理をセット
		 // リストビューのアイテムがクリックされた時に呼び出されるコールバックリスナーを登録します
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view,
                   int position, long id) {
           	
           		String cafeId = (String) cafeList.get(position).get("cafeId");
           	
           		cdh.close();
           		// インテントへのインスタンス生成  
		        Intent intent = new Intent(CafeListPageActivity.this,CafeDetailPageActivity.class);
		        intent.putExtra("cafeId", cafeId);
		        startActivity(intent);
           	
           }
       });
       if(notSaveFlag != true){
	       	double lat = (double) savePoint.getLatitudeE6() / (double) 1E6;
	       	double lon = (double) savePoint.getLongitudeE6() / (double) 1E6;
	       	String latStr = String.valueOf(lat);
	       	String lonStr = String.valueOf(lon);
	       	HistoryListTask task = new HistoryListTask(this,geocoder);
	   		task.execute(keyword,latStr,lonStr);
       }
       notSaveFlag = false;
       keyword = null;
	}
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cafe_list);
        
		
        //トップへボタン
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(CafeListPageActivity.this,TopPageActivity.class);
		        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		        startActivity(intent);
            }

         });
        
        // リソース
        mRes = getResources();

        // データベースを書き込み用にオープンする
        cdh = new CafeDataHelper(this);
        
        
     // ジオコーダ生成
        geocoder = new Geocoder(this, Locale.JAPAN);
        
        //カフェ検索オブジェクト
        mCafeFinder = new CafeFinder(cdh,geocoder);
        //DBをオープン
        mCafeFinder.open();
        
        
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        
        searchWordText = (EditText) findViewById(R.id.searchWord);
        searchWordText.setOnKeyListener(this);
        searchWordText.setFocusable(false);
        searchWordText.setFocusableInTouchMode(true);
        
        searchWordText.setOnKeyListener(new OnKeyListener() {
        	@Override
        	public boolean onKey(View v, int keyCode, KeyEvent event) {

                       //EnterKeyが押されたかを判定
        		if (event.getAction() == KeyEvent.ACTION_DOWN
        				&& keyCode == KeyEvent.KEYCODE_ENTER) {

                                //ソフトキーボードを閉じる
        			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        			inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

        			keyword = searchWordText.getText().toString();
					searchAddressMap(keyword);
        			
        			return true;
        		}
        		return false;
        	}
        });
        
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
	}

	@Override
	protected void onResume() {
		super.onResume();
		//mc.start();
	}

	
	
	@Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	//メニューインフレーターを取得
    	MenuInflater inflater = getMenuInflater();
    	//xmlのリソースファイルを使用してメニューにアイテムを追加
    	inflater.inflate(R.menu.list_menu, menu);
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
			case R.id.option:
				cdh.close();
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(CafeListPageActivity.this,PreferencePageActivity.class);
		        startActivity(intent);
				break;
//			case R.id.add:
//				// インテントへのインスタンス生成  
//		        Intent inAdd = new Intent(CafeListPageActivity.this,AddCafePageActivity.class);
//		        startActivity(inAdd);
//				break;
			
			
		}

		return true;
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
			// back keyを有効に設定
			mDialog = new ProgressDialog(this);
			mDialog.setCancelable(true);
			mDialog.setTitle(getString(R.string.progressDialog_addressSearchTitle));
			mDialog.setMessage(getString(R.string.progressDialog_addressSearchMessage));
			mDialog.show();
		}
		
		//addressオブジェクトから緯度経度を取得してセット
		double distance = 1000.0;
		
		
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
        feedLocalData();
        if(savePoint != null){
        	//現在地から緯度経度を取得してセット
        	notSaveFlag=true;
    		double distance = 1000.0;
    		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
            // SQLITE3へアクセス
    		mCafeFinder.setPreference(pref);
            // SQLITE3へアクセス
            mCafeFinder.setGPoint(savePoint,distance);
            new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
        	return;
        }else{
        
        	
        	if (keyword != null) {
                //住所検索
        		searchAddressMap(keyword);
                
             }
        	
        }
        
        
        
    }
	
	/**
	 * 設定ファイルからデータを取得
	 */
	private void feedLocalData() {
		
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		int locallat = pref.getInt("save_lat", 0);
		int locallon = pref.getInt("save_lon", 0);
		savePoint = new GeoPoint(locallat, locallon);
		return;
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
        //使える中で最も条件にヒットする位置情報サービスを取得する
        final String bestProvider_ = mLocationManager.getBestProvider(criteria, true);

        //以前に取得した位置情報を取得
        /*
        final Location location = mLocationManager.getLastKnownLocation(bestProvider_);
		if(location == null){
			mLocationManager.requestLocationUpdates(
					bestProvider_,
	//                LocationManager.NETWORK_PROVIDER,
	                0,
	                0,
	                this);
		}else{
			GeoPoint kgp = LocationHelper.getGeoPointLatLong(location.getLatitude(), location.getLongitude());
	    	//現在地から緯度経度を取得してセット
	        
			//addressオブジェクトから緯度経度を取得してセット
			double distance = 350;
			SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);

	        // SQLITE3へアクセス
			mCafeFinder.setPreference(pref);
			mCafeFinder.setGPoint(kgp,distance);
	        new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
		}
		*/
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
        
		//addressオブジェクトから緯度経度を取得してセット
		double distance = 1000;
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
	
	
		
	private void showSearchDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View comment_view = factory.inflate(R.layout.search_dialog, null);
		AlertDialog ab = new AlertDialog.Builder(CafeListPageActivity.this)
		.setView(comment_view)
		.setTitle("地名を入力してください")
		.setPositiveButton("検索する", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				EditText edtbx = (EditText) comment_view.findViewById(R.id.edit_search);
				String keyword = edtbx.getText().toString();
				searchAddressMap(keyword);
				
				
				
			}
	            }).setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    // TODO 自動生成されたメソッド・スタブ
	                    //Log.d("AlertDialog", "Negative which :" + which);
	             
	                }
	            })
		.create();
		ab.show();
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
	protected void onPause() {
		super.onPause();
		
	}
	@Override
    public void onDestroy() {
    	
		super.onDestroy();
		cdh.close();
		
    }
	@Override
    protected void onRestart() {
		super.onRestart();
		
		
		
	}	
   
	
	
}
