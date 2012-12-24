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

	
    
    // ��ʃR���g���[��
    // �_�C�A���O
    private ProgressDialog mDialog;
    private Button topButton;
    private CafeFinder mCafeFinder;// �J�t�F����
    
    private GeoPoint savePoint;
    // ���\�[�X
    private Resources mRes;
    
    // GPS�ւ̖₢���킹����
    static final int MIN_TIME = 0;
    static final int MIN_METER = 0;
	
    private CafeListAdapter pda;
    
    private Geocoder geocoder;
    
    private String keyword;
    
    private SQLiteDatabase mDb;
    
    private List<HashMap> cafeList = new ArrayList<HashMap>();
    
    // �e�[�u���̖��O
    static final String TABLE = "cafeMaster";
    private CafeDataHelper cdh;
    
    private EditText searchWordText;
    
    private boolean notSaveFlag = false;
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
	            Toast.makeText(CafeListPageActivity.this,
	                    "�����Ɏ��s���܂���", Toast.LENGTH_SHORT)
	                    .show();
	            if(mDialog != null){
					mDialog.dismiss();
					
				}
            }
        }
    };
    
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
    	cafeList = new ArrayList<HashMap>();
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
        	HashMap cafeMap = new HashMap();
        	double distance = mCafeFinder.getCafeDistance(cafe);
        	int iine = mCafeFinder.getCafeNice(cafe);
        	String cafeDistance = Math.round(distance) + "m";
        	cafe.put("distance", cafeDistance);
        	cafe.put("iine", Integer.valueOf(iine));
        	cafe.put("sortDistance", Integer.valueOf((int) Math.round(distance)));
            cafeList.add(cafe);
        }
        
        //�����Ń\�[�g
        GeoCompare comparator = new GeoCompare();
        Collections.sort(cafeList, comparator );
        
        
        
        //ListView�ɃZ�b�g
        pda = null;
        if(pda == null){
        	//pda.clear();
        	pda = new CafeListAdapter(this, R.layout.cafelist_row, cafeList);
        }
        setListAdapter(pda);
        
        ListView listView = (ListView) findViewById(android.R.id.list);
        
      //ListView�̊e���ڑI���̏������Z�b�g
		 // ���X�g�r���[�̃A�C�e�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view,
                   int position, long id) {
           	
           		String cafeId = (String) cafeList.get(position).get("cafeId");
           	
           		cdh.close();
           		// �C���e���g�ւ̃C���X�^���X����  
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
        
		
        //�g�b�v�փ{�^��
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(CafeListPageActivity.this,TopPageActivity.class);
		        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		        startActivity(intent);
            }

         });
        
        // ���\�[�X
        mRes = getResources();

        // �f�[�^�x�[�X���������ݗp�ɃI�[�v������
        cdh = new CafeDataHelper(this);
        
        
     // �W�I�R�[�_����
        geocoder = new Geocoder(this, Locale.JAPAN);
        
        //�J�t�F�����I�u�W�F�N�g
        mCafeFinder = new CafeFinder(cdh,geocoder);
        //DB���I�[�v��
        mCafeFinder.open();
        
        
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        
        searchWordText = (EditText) findViewById(R.id.searchWord);
        searchWordText.setOnKeyListener(this);
        searchWordText.setFocusable(false);
        searchWordText.setFocusableInTouchMode(true);
        
        searchWordText.setOnKeyListener(new OnKeyListener() {
        	@Override
        	public boolean onKey(View v, int keyCode, KeyEvent event) {

                       //EnterKey�������ꂽ���𔻒�
        		if (event.getAction() == KeyEvent.ACTION_DOWN
        				&& keyCode == KeyEvent.KEYCODE_ENTER) {

                                //�\�t�g�L�[�{�[�h�����
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
    	//���j���[�C���t���[�^�[���擾
    	MenuInflater inflater = getMenuInflater();
    	//xml�̃��\�[�X�t�@�C�����g�p���ă��j���[�ɃA�C�e����ǉ�
    	inflater.inflate(R.menu.list_menu, menu);
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
			case R.id.option:
				cdh.close();
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(CafeListPageActivity.this,PreferencePageActivity.class);
		        startActivity(intent);
				break;
//			case R.id.add:
//				// �C���e���g�ւ̃C���X�^���X����  
//		        Intent inAdd = new Intent(CafeListPageActivity.this,AddCafePageActivity.class);
//		        startActivity(inAdd);
//				break;
			
			
		}

		return true;
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
			// back key��L���ɐݒ�
			mDialog = new ProgressDialog(this);
			mDialog.setCancelable(true);
			mDialog.setTitle(getString(R.string.progressDialog_addressSearchTitle));
			mDialog.setMessage(getString(R.string.progressDialog_addressSearchMessage));
			mDialog.show();
		}
		
		//address�I�u�W�F�N�g����ܓx�o�x���擾���ăZ�b�g
		double distance = 1000.0;
		
		
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
        feedLocalData();
        if(savePoint != null){
        	//���ݒn����ܓx�o�x���擾���ăZ�b�g
        	notSaveFlag=true;
    		double distance = 1000.0;
    		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
            // SQLITE3�փA�N�Z�X
    		mCafeFinder.setPreference(pref);
            // SQLITE3�փA�N�Z�X
            mCafeFinder.setGPoint(savePoint,distance);
            new Thread(new RequestRunnable(mCafeFinder, mHandler)).start();
        	return;
        }else{
        
        	
        	if (keyword != null) {
                //�Z������
        		searchAddressMap(keyword);
                
             }
        	
        }
        
        
        
    }
	
	/**
	 * �ݒ�t�@�C������f�[�^���擾
	 */
	private void feedLocalData() {
		
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		int locallat = pref.getInt("save_lat", 0);
		int locallon = pref.getInt("save_lon", 0);
		savePoint = new GeoPoint(locallat, locallon);
		return;
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
        //�g���钆�ōł������Ƀq�b�g����ʒu���T�[�r�X���擾����
        final String bestProvider_ = mLocationManager.getBestProvider(criteria, true);

        //�ȑO�Ɏ擾�����ʒu�����擾
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
	    	//���ݒn����ܓx�o�x���擾���ăZ�b�g
	        
			//address�I�u�W�F�N�g����ܓx�o�x���擾���ăZ�b�g
			double distance = 350;
			SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);

	        // SQLITE3�փA�N�Z�X
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
	 * ���ݒn���Z�b�g
	 * @param lastKnownLocation
	 */
	private void setNowLocation(Location nowLocation) {
		// TODO Auto-generated method stub
		GeoPoint kgp = LocationHelper.getGeoPointLatLong(nowLocation.getLatitude(), nowLocation.getLongitude());
    	//���ݒn����ܓx�o�x���擾���ăZ�b�g
        
		//address�I�u�W�F�N�g����ܓx�o�x���擾���ăZ�b�g
		double distance = 1000;
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
	
	
		
	private void showSearchDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View comment_view = factory.inflate(R.layout.search_dialog, null);
		AlertDialog ab = new AlertDialog.Builder(CafeListPageActivity.this)
		.setView(comment_view)
		.setTitle("�n������͂��Ă�������")
		.setPositiveButton("��������", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				EditText edtbx = (EditText) comment_view.findViewById(R.id.edit_search);
				String keyword = edtbx.getText().toString();
				searchAddressMap(keyword);
				
				
				
			}
	            }).setNegativeButton("�L�����Z��", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    // TODO �����������ꂽ���\�b�h�E�X�^�u
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
