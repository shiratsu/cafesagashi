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
    
    // ��ʃR���g���[��
    // �_�C�A���O
    private ProgressDialog mDialog;
    
    
    public ItemsMapView map_view;
    private MapController mapController;
    private CafeFinder mCafeFinder;// �J�t�F����
    private int zoomLevel = 15;
    
    private GeoPoint savePoint;
    
    // ���\�[�X
    private Resources mRes;
    private EditText searchWordText;
    
    // GPS�ւ̖₢���킹����
    static final int MIN_TIME = 0;
    static final int MIN_METER = 0;
    private Button topButton;
    private Geocoder geocoder;
    
    private String keyword;
    
    private SQLiteDatabase mDb;
    // �e�[�u���̖��O
    static final String TABLE = "cafeMaster";
    private CafeDataHelper cdh;
    private HistoryDataHelper hdh;
    private List<String> pinAry = new ArrayList<String>();
    private boolean notSaveFlag = false;
    private boolean restartFlag = false;
    
    private final String siteId = "2495";
	private final String locationId = "2862";

    /*
     * �J�t�F�f�[�^�ւ̃A�N�Z�X����������ƌĂяo�����
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
            	// ���ݒn���擾�ł��Ȃ��ꍇ�̓��b�Z�[�W��\�����ďI���
	            Toast.makeText(MapPageActivity.this,
	                    "�����Ɏ��s���܂���", Toast.LENGTH_SHORT)
	                    .show();
	            if(mDialog != null){
					mDialog.dismiss();
					
				}
            }
        }
    };
    
    /**
     * �J�t�F��z�u
     * @param loadPoint
     */
    private void displayCafe(GeoPoint loadPoint) {
		// TODO Auto-generated method stub
    	if (loadPoint == null) {
            return;
        }
    	savePoint = loadPoint;
    	
    	//�V�X�e�����ɕۑ�����
    	saveGeoData(savePoint);
    	
        // �S�J�t�F������傫���Ɋg�傷��Ƃ��Ɏg��
        int nowLat = loadPoint.getLatitudeE6();
        int nowLon = loadPoint.getLongitudeE6();
    	int maxLat = loadPoint.getLatitudeE6();
        int maxLng = loadPoint.getLongitudeE6();
        int minLat = maxLat;
        int minLng = maxLng;
        
        // ���ݒn�ƃJ�t�F�̒��S�����߁A�����Ɉړ�����Ƃ��Ɏg��
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
	        	
	        	// �ړ��Ɗg��̂��߂̃f�[�^�v�Z
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
            // ��O�Ȃ������ł�����
            counter++;
            
        }
        map_view.moveGPoint(new GeoPoint(nowLat, nowLon));
        /*
        if (0 < counter) {
        	// �����Ɖw������傫���ɒn�}���g�傷��
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
     * ���[�J���V�X�e�����Ƀf�[�^��ۑ�����
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
		
		
		
		//MapView�����o��
		map_view = (ItemsMapView) findViewById(R.id.map_view);
		map_view.setClickable(true);
		map_view.setBuiltInZoomControls(true);
		mapController = map_view.getController();
		mapController.setZoom(17);
		        
        
        //�L�[���[�h����
		/*
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showSearchDialog();
            }
         });
        */
        
		//�g�b�v�փ{�^��
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(MapPageActivity.this,TopPageActivity.class);
		        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		        startActivity(intent);
            }

         });
        
		searchWordText = (EditText) findViewById(R.id.searchWord);
        searchWordText.setOnKeyListener(this);
        searchWordText.setFocusable(false);
        searchWordText.setFocusableInTouchMode(true);
        
        
        
        // ���\�[�X
        mRes = getResources();

        // �f�[�^�x�[�X��ǂݍ��ݗp�ɃI�[�v������
        cdh = new CafeDataHelper(this);
        
        // �f�[�^�x�[�X���������ݗp�ɃI�[�v������
        hdh = new HistoryDataHelper(this);
        
        // �W�I�R�[�_����
        geocoder = new Geocoder(this, Locale.JAPAN);
        
        //�J�t�F�����I�u�W�F�N�g
        mCafeFinder = new CafeFinder(cdh,geocoder);
        
        //DB���I�[�v��
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
    	//���j���[�C���t���[�^�[���擾
    	MenuInflater inflater = getMenuInflater();
    	//xml�̃��\�[�X�t�@�C�����g�p���ă��j���[�ɃA�C�e����ǉ�
    	inflater.inflate(R.menu.map_menu, menu);
    	//�ł�����true��Ԃ�
    	return true;
    }
	
	/**
	 * �{�^�����������Ƃ�
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		int id = item.getItemId();
		
		//XML���̃��j���[�{�^���ɃA�N�Z�X����ɂ�R.id�ȉ��𗘗p����
		switch (id) {
			case R.id.now:
				//���ݒn���擾
            	loadNowPosition();
				break;
			case R.id.center:
				//�n�}�̒��S�n�̃J�t�F���擾
            	loadCenterPosition();
				break;
			case R.id.option:
				cdh.close();
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(MapPageActivity.this,PreferencePageActivity.class);
		        startActivity(intent);
				break;
			
//			case R.id.add:
//				// �C���e���g�ւ̃C���X�^���X����  
//		        Intent inAdd = new Intent(MapPageActivity.this,AddCafePageActivity.class);
//		        startActivity(inAdd);
//				break;
			case R.id.list:
				cdh.close();
            	// �C���e���g�ւ̃C���X�^���X���� 
		        Intent itlist = new Intent(MapPageActivity.this,CafeListPageActivity.class);
		        startActivity(itlist);
				break;	
		}

		return true;
	}
	
	/**
	 * ���[�J���f�[�^���擾
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
	 * �n�}�̒��S�n�̃J�t�F���擾
	 */
	private void loadCenterPosition() {
		// TODO Auto-generated method stub
		GeoPoint gp = map_view.getMapCenter();
		notSaveFlag = true;
		if (gp == null) {
            // ���ݒn���擾�ł��Ȃ��ꍇ�̓��b�Z�[�W��\�����ďI���
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
        // ���ݒn���擾�ł���΋߂��̉w��T���ĕ\������
        // ���\�����̃J�t�F���N���A
        //map_view.clearCafes();

        //���ݒn����ܓx�o�x���擾���ăZ�b�g
		double distance = (21-zoomLevel)*85;
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
        // SQLITE3�փA�N�Z�X
		mCafeFinder.setPreference(pref);
        // SQLITE3�փA�N�Z�X
        mCafeFinder.setGPoint(gp,distance);
        new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
        return;
	}
	
	
	/**
	 * �Z������
	 * @param keyword
	 * @throws IOException 
	 */
	private void searchAddressMap(String keyword){
		// TODO Auto-generated method stub
		//�Z�������v���O���X�_�C�A���O�\��
		if (mDialog == null || !mDialog.isShowing()) {
			mDialog = ProgressDialog.show(this,
	               getString(R.string.progressDialog_addressSearchTitle),
	               getString(R.string.progressDialog_addressSearchMessage));
		}
		pinAry = new ArrayList<String>();
		//�Z�����擾
		zoomLevel = map_view.getZoomLevel();
		//address�I�u�W�F�N�g����ܓx�o�x���擾���ăZ�b�g
		double distance = (21-zoomLevel)*85;
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
        // SQLITE3�փA�N�Z�X
		mCafeFinder.setPreference(pref);
		mCafeFinder.setKeywordDist(keyword,distance);
		
		new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
		
	    return;
	}

	
	
	@Override
    protected void onStart() {
		super.onStart();
		//���[�J������f�[�^���擾
        savePoint = feedLocalData();
        
        //�J�ڌ�����L�[���[�h���󂯎��
    	Bundle extras = getIntent().getExtras();
    	String stationLat = null;
    	String stationLon = null;
    	GeoPoint stationGp = null;
    	
    	//�߂��Ă����ꍇ�́A����
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
    		//���ݒn����ܓx�o�x���擾���ăZ�b�g
    		double distance = (21-zoomLevel)*85;
    		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
            // SQLITE3�փA�N�Z�X
    		mCafeFinder.setPreference(pref);
            mCafeFinder.setGPoint(stationGp,distance);
            new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
            return;
    	}else if(keyword != null){
    		
    		//�Z������
    		searchAddressMap(keyword);
    		
    		
    	}else if(savePoint != null){
    		//���ݒn����ܓx�o�x���擾���ăZ�b�g
    		notSaveFlag = true;
    		double distance = (21-zoomLevel)*85;
    		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
            // SQLITE3�փA�N�Z�X
    		mCafeFinder.setPreference(pref);
            mCafeFinder.setGPoint(savePoint,distance);
            mCafeFinder.setCenterFlag(true);
            new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
        	return;
    	}
    	
        
		
    }
	
	
	
	
	
	/**
	 * ���ݒn���擾
	 */
	private void loadNowPosition() {
		
		//�Z�������v���O���X�_�C�A���O�\��
		if (mDialog == null || !mDialog.isShowing()) {
			mDialog = new ProgressDialog(this);
			mDialog.setCancelable(true);
			mDialog.setTitle(getString(R.string.progressDialog_getLocationTitle));
			mDialog.setMessage(getString(R.string.progressDialog_getLocationMessage));
			mDialog.show();
		}
		
		//���x�A�d�͏���Ȃǂ���K�؂Ȉʒu���T�[�r�X��I������
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        //�g���钆�ōł������Ƀq�b�g����ʒu���T�[�r�X���擾����
        final String bestProvider_ = mLocationManager.getBestProvider(criteria, true);

        // �Ō�Ɏ擾�ł����ʒu���5���ȓ��̂��̂ł���ΗL���Ƃ��܂��B
		final Location lastKnownLocation = mLocationManager.getLastKnownLocation(bestProvider_);
		// XXX - �K�v�ɂ�蔻�f�̊��ύX���Ă��������B
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
	 * ���ݒn���Z�b�g
	 * @param lastKnownLocation
	 */
	private void setNowLocation(Location nowLocation) {
		// TODO Auto-generated method stub
		GeoPoint kgp = LocationHelper.getGeoPointLatLong(nowLocation.getLatitude(), nowLocation.getLongitude());
    	//���ݒn����ܓx�o�x���擾���ăZ�b�g
        zoomLevel = map_view.getZoomLevel();
		//address�I�u�W�F�N�g����ܓx�o�x���擾���ăZ�b�g
		double distance = (21-zoomLevel)*85;
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);

        // SQLITE3�փA�N�Z�X
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
	 * ���[�J���f�[�^���擾
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
		//�߂��Ă��Ďn�܂����ꍇ
		restartFlag = true;
		/*
		mapController.setZoom(17);
		//DB���I�[�v��
        mCafeFinder.open();
		if(savePoint != null){
			
			map_view.clearCafes();
			pinAry = new ArrayList<String>();
        	//���ݒn����ܓx�o�x���擾���ăZ�b�g
    		double distance = (21-zoomLevel)*85;
    		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
            // SQLITE3�փA�N�Z�X
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
