package jp.co.teamenjoy.cafesagashi.free;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import jp.co.teamenjoy.cafesagashi.free.R;
import jp.co.teamenjoy.cafesagashi.free.adapter.CafeDetailDataAdapter;
import jp.co.teamenjoy.cafesagashi.free.adapter.LineDataAdapter;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeWriteDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.LineDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.LocationHelper;
import jp.co.teamenjoy.cafesagashi.free.util.AppConst;
import jp.co.teamenjoy.cafesagashi.free.util.CafeDetailFinder;
import jp.co.teamenjoy.cafesagashi.free.util.CafeFinder;

import jp.co.teamenjoy.cafesagashi.free.util.NiceCafeTask;
import jp.co.teamenjoy.cafesagashi.free.util.PostCafeTask;
import jp.co.teamenjoy.cafesagashi.free.util.RequestRunnable;
import jp.co.teamenjoy.cafesagashi.free.util.RequestRunnableDetail;
import jp.co.teamenjoy.cafesagashi.free.util.RequestRunnablePostFav;
import jp.co.teamenjoy.cafesagashi.free.view.ItemsMapDetailView;
import jp.co.teamenjoy.cafesagashi.free.view.ItemsMapView;

import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ZoomControls;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class CafeDetailPageActivity extends MapActivity implements DialogInterface.OnClickListener{

	private static final String LOGTAG_CLASS = CafeDetailPageActivity.class.getSimpleName();
    
	private final String siteId = "2495";
	private final String locationId = "2862";

	
    // ��ʃR���g���[��
    // �_�C�A���O
    private ProgressDialog mDialog;
    private TextView lblCurrAddress;
    private Button btnPhone;
    
    private Button btnNice;
    private Button btnTweet;
    private Button btnUpdate;
    private Button btnMail;
    private Button topButton;
    private Button btnDelete;
    private Button navicon1;
    private Button navicon2;
    public ItemsMapDetailView map_view;
    private MapController mapController;
    private CafeDetailFinder mCafeDetailFinder;// �J�t�F����
    private String cafeId = "";
    private HashMap cafe;
    private List<HashMap> cafeDetail = new ArrayList<HashMap>();
    private String postApiUrl = "http://teamenjoy-cafe.appspot.com/cafemap/apiPostCafe3/";
    
    // ���\�[�X
    private Resources mRes;
    
    private TextView tv;
    
    // GPS�ւ̖₢���킹����
    static final int MIN_TIME = 0;
    static final int MIN_METER = 0;
    
    private Geocoder geocoder;
    
    
    private SQLiteDatabase mDb;
    // �e�[�u���̖��O
    static final String TABLE = "cafeMaster";
    private CafeWriteDataHelper cdh;
    
    private String cafeName;
    private String storeAddress;
    private String storeFlag;
    private String storeSubName;
    private String storeMainName;
    private String phoneNumber;
    private boolean favFlag;
    
    private String tabako;
	private String kinen;
	private String wifi;
	private String pc;
	private String koshitsu;
	private String terace;
	private String pet;
	private String shinya;
    private String storeCategory;
    
    private String lat;
    private String lon;
    
    /*
     * DB�ւ̃A�N�Z�X����������ƌĂяo�����
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Object checkObj = msg.obj;
            if (mDialog != null) {
                mDialog.dismiss();
            }
            if(checkObj instanceof GeoPoint){
            	GeoPoint gp = (GeoPoint) checkObj;
            	displayCafe(gp);
            }else if(checkObj instanceof String){
            	if("0".equals(checkObj)){
            		
            		toastMessage("�f�[�^���R�O���ɒB���Ă��邽�߁A�ǉ��ł��܂���");
            	}else if("2".equals(checkObj)){
            		
            		toastMessage("���C�ɓ�����̍X�V�Ɏ��s���܂���");
            	}	
            }
            
        }

		
    };
    
    /**
     * Toast���b�Z�[�W��\������
     * @param string
     */
    private void toastMessage(String string) {
		// TODO Auto-generated method stub
    	Toast.makeText(CafeDetailPageActivity.this,
                string, Toast.LENGTH_SHORT)
                .show();
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
        
        int size = mCafeDetailFinder.size();
        int counter = 0;
        
    	cafe = mCafeDetailFinder.getCafe(0);
    	
    	//ListView�ɃZ�b�g
    	
 
    	GeoPoint cafePoint = mCafeDetailFinder.getCafeLocation(cafe);
    	storeSubName = mCafeDetailFinder.getCafeSubName(cafe);
    	cafeName = mCafeDetailFinder.getCafeName(cafe);
    	storeMainName = mCafeDetailFinder.getStoreMainName(cafe);
    	storeFlag = mCafeDetailFinder.getStoreFlag(cafe);
    	String cafeId = mCafeDetailFinder.getCafeId(cafe);
    	storeAddress = mCafeDetailFinder.getAddress(cafe);
    	String kodawari = mCafeDetailFinder.getKodawari(cafe);
    	phoneNumber = mCafeDetailFinder.getPhoneNumber(cafe);
    	
    	//�ܓx�o�x���擾
    	lat = mCafeDetailFinder.getLat(cafe);
    	lon = mCafeDetailFinder.getLon(cafe);
    	
    	
    	String userSendMessage = mCafeDetailFinder.getUserSendMessage(cafe);
    	
    	
    	
    	int count = mCafeDetailFinder.feedFavCount();
    	
    	if("1".equals(storeFlag)){
    		map_view.addStab(cafePoint, cafeName,cafeId);
    		storeCategory = "�X�^�[�o�b�N�X";
    	}else if("2".equals(storeFlag)){
    		map_view.addDtour(cafePoint, cafeName,cafeId);
    		storeCategory = "�h�g�[��";
    	}else if("3".equals(storeFlag)){
    		map_view.addTurrys(cafePoint, cafeName,cafeId);
    		storeCategory = "�^���[�Y";
    	}else if("4".equals(storeFlag)){
    		map_view.addSanmaruk(cafePoint, cafeName,cafeId);
    		storeCategory = "�T���}���N�J�t�F";
    	}else if("5".equals(storeFlag)){
    		map_view.addExcelShiorl(cafePoint, cafeName,cafeId);
    		storeCategory = "�G�N�Z���V�I�[��";
    	}else if("6".equals(storeFlag)){
    		map_view.addShiatols(cafePoint, cafeName,cafeId);
    		storeCategory = "�V�A�g���Y�x�X�g";
    	}else if("7".equals(storeFlag)){
    		map_view.addVeroche(cafePoint, cafeName,cafeId);
    		storeCategory = "�x���[�`�F";
    	}else if("8".equals(storeFlag)){
    		map_view.addCafedo(cafePoint, cafeName,cafeId);
    		storeCategory = "�J�t�F�h�N���G";
    	}else if("9".equals(storeFlag)){
    		map_view.addKomeda(cafePoint, cafeName,cafeId);
    		storeCategory = "�R���_����";
    	}else if("Z".equals(storeFlag)){
    		map_view.addKoten(cafePoint, cafeName,cafeId);
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
    		
    	
        // ��O�Ȃ������ł�����
        counter++;
            
        //map_view.moveGPoint(new GeoPoint(nowLat, nowLon));
        
        
    	
        btnPhone = (Button) findViewById(R.id.btnPhone);
        if(phoneNumber != null && !"".equals(phoneNumber)){
        	btnPhone.setVisibility(View.VISIBLE);
        	btnPhone.setEnabled(true);
        	btnPhone.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	
	            	//�d�b������
	            	callLogic();
	            }
				
	         });
        }else{
        	btnPhone.setVisibility(View.INVISIBLE);
        	btnPhone.setEnabled(false);
        }
        
    	TextView hogeText = (TextView) findViewById(R.id.storeName);
    	if(!"Z".equals(storeFlag)){
    		hogeText.setText(storeMainName+"\n"+storeSubName);
    	}else{
    		hogeText.setText(storeSubName);
    	}
        TextView addressText = (TextView) findViewById(R.id.storeAddress);
        addressText.setText(storeAddress);
        
        TextView phoneText = (TextView) findViewById(R.id.phoneNumber);
        phoneText.setText(phoneNumber);
        
        TextView kodawariText = (TextView) findViewById(R.id.kodawari);
        kodawariText.setText(kodawari);
        
        TextView userSendMessageText = (TextView) findViewById(R.id.userSendMessage);
        userSendMessageText.setText(userSendMessage);
        //cdh.close();
        return;
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.cafe_detail);
		
		//�J�ڌ�����s���{���R�[�h���󂯎��
        Bundle extras = getIntent().getExtras();
        
        //AdMaker.removeViewAt(R.id.admakerview);
        
        if(extras != null){
        	cafeId = extras.getString("cafeId");
        }
		
        if(cafeId == null || "".equals(cafeId)){
        	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // �A���[�g�_�C�A���O�̃^�C�g����ݒ肵�܂�
            alertDialogBuilder.setTitle("�J�t�F�擾�G���[");
            // �A���[�g�_�C�A���O�̃��b�Z�[�W��ݒ肵�܂�
            alertDialogBuilder.setMessage("�Ώۂ̃J�t�F��������܂���");
            // �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	finish();
                        }
                    });
        	
        }
        
		//MapView�����o��
		
        map_view = (ItemsMapDetailView) findViewById(R.id.map_view);
		//map_view.setClickable(true);
		//map_view.setBuiltInZoomControls(true);
        
		//mapController = map_view.getController();
		//mapController.setZoom(15);
        
        
        
        //�ҏW
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(CafeDetailPageActivity.this,AddCafePageActivity.class);
		        intent.putExtra("cafeId", cafeId);
		        startActivity(intent);
            }
         });
        
        //�i�C�X�{�^��
        btnNice = (Button) findViewById(R.id.btnNice);
        btnNice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//�i�C�X���M
            	niceSend();
            }

			

			
         });
        
        //�c�C�[�g�{�^��
        btnTweet = (Button) findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//�c�C�[�g���M�y�[�W��
            	toTweetPage();
            }
			
         });
        
        //���[���{�^��
        btnMail = (Button) findViewById(R.id.btnMail);
        btnMail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//���[�����M��
            	toMail();
            }

			
			
         });
        //�g�b�v�փ{�^��
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(CafeDetailPageActivity.this,TopPageActivity.class);
		        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		        startActivity(intent);
            }

         });
		
		navicon1 = (Button) findViewById(R.id.navicon1);
		navicon1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	connectNavicon();
            }
         });
//		navicon2 = (Button) findViewById(R.id.navicon2);
//		navicon2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            	connectNavicon();
//            }
//         });
        
        
        // ���\�[�X
        mRes = getResources();

        // �f�[�^�x�[�X���������ݗp�ɃI�[�v������
        cdh = new CafeWriteDataHelper(this);
        
        
        //�J�t�F�����I�u�W�F�N�g
        mCafeDetailFinder = new CafeDetailFinder(cdh);
        
        //DB���I�[�v��
        mCafeDetailFinder.open();
        
        // �W�I�R�[�_����
        geocoder = new Geocoder(this, Locale.JAPAN);
        
        
	}

	/**
	 * Navicon�ɘA�g����
	 */
	protected void connectNavicon() {
		// TODO Auto-generated method stub
		
		Uri uri = null;
		try {
			uri = Uri.parse("navicon://navicon.denso.co.jp/setPOI?ver=1.4&ll="+lat+","+lon+"&appName=Y9v74WIe&title="+URLEncoder.encode(storeMainName, "UTF8")+" "+URLEncoder.encode(storeSubName, "UTF8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			uri = Uri.parse("navicon://navicon.denso.co.jp/setPOI?ver=1.4&ll="+lat+","+lon+"&appName=Y9v74WIe");
		}
		Intent i = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(i);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//�J�t�F�̏����擾
        feedCafeDetail(cafeId);
		
	}

		
	/**
	 * �d�b��������
	 */
	private void callLogic() {
		// TODO Auto-generated method stub
		Uri uri = Uri.parse("tel:"+phoneNumber);
		Intent i = new Intent(Intent.ACTION_DIAL,uri);
		startActivity(i);
	}
	
	/**
	 * ���[�����M
	 */
	private void toMail() {
		// TODO Auto-generated method stub
		
		// TODO Auto-generated method stub
		
		String tweetDefaultStr = null;
		
		
		Intent it = new Intent(Intent.ACTION_SEND);
		it.setType("text/plain");
        //it.putExtra(Intent.EXTRA_SUBJECT, storeCategory+" "+storeSubName);
        //it.putExtra(Intent.EXTRA_TEXT, storeCategory+" "+storeSubName+"\n"+storeAddress+"\n"+Uri.parse("tel:" + phoneNumber));
		it.putExtra(Intent.EXTRA_SUBJECT, storeCategory+" "+storeSubName);
		it.putExtra(Intent.EXTRA_TEXT, storeCategory+" "+storeSubName+"\n"+storeAddress+"\n"+Uri.parse("tel:" + phoneNumber));
		try {
	         // �V�X�e���ɃC���X�g�[������Ă��郁�[���[���\�������B
	         //  ��͎g���������[���[�ő���΂n�j���B�A�v���͋N���܂ł��ӔC�͈́A���M���ʂ͎󂯎��Ȃ��B
	         startActivity(Intent.createChooser(it,"���[���[�ꗗ"));
	      } catch (ActivityNotFoundException e ) {
	         // �����̃G���[�͒ʏ�̓C���e���g�̐ݒ�~�X���قƂ�ǂ̂͂����B
	         String errorMsg = String.format("%s\n(%s)",
	               "���[���[�ꗗ���擾�ł��܂���",
	               e.getMessage());
	         // �A���[�g�_�C�A���O ���G���[���b�Z�[�W��\�����ăo�O�񍐂��Ă��炨���B
	         AlertDialog.Builder errorDialog =
	            new AlertDialog.Builder(this);
	         errorDialog.setTitle("���[���N���G���[");
	         errorDialog.setMessage(errorMsg);
	         errorDialog.setPositiveButton(R.string.button_close, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	               // �ЂƂ܂��M�������[�͎g����̂Ń_�C�A���O����邾��
	               // ���[���[���������C���Ȃ�A�v���I������
	            }
	         });
	         errorDialog.show();
	      }
	}
	
	/**
	 * �c�C�b�^�[�y�[�W�֑J��
	 */
	protected void toTweetPage() {
		
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("twitPref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		
		String tweetFlag = pref.getString("tweetFlag", null);
		String tweetId = pref.getString("tweetId", null);
		String password = pref.getString("password", null);

		// TODO Auto-generated method stub
		String tweetDefaultStr = null;
		
		if(storeCategory != null){
			tweetDefaultStr = "�y"+storeCategory+"/"+storeSubName+"�z "+storeAddress+" http://bit.ly/iTLOET #cafe_sagashi";
		}else{
			tweetDefaultStr = "�y"+storeSubName+"�z "+storeAddress+" http://bit.ly/iTLOET #cafe_sagashi";
		}
		// �C���e���g�ւ̃C���X�^���X����  
		//�������Z�b�g����ĂȂ���ΏI��
		if(!"1".equals(tweetFlag) || tweetId == null || password == null){
			Intent intent = new Intent(CafeDetailPageActivity.this,TwitPrefPageActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(CafeDetailPageActivity.this,TwitPageActivity.class);
			intent.putExtra("tweetDefaultStr", tweetDefaultStr);
			startActivity(intent);
		}	
	}

	/**
	 * ���C�ɓ��菈��
	 */
	private void favLogic() {
		// TODO Auto-generated method stub
		
		//���C�ɓ���ɒǉ����폜
		/*
		FavHandleTask fav = new FavHandleTask(this,btnFav);
		fav.execute(cafeId,storeMainName,storeSubName,storeAddress);
		*/
		new Thread(new RequestRunnablePostFav(cdh, mHandler,cafeId,storeMainName,storeSubName,storeAddress)).start();
    	return;
		
		/*
		if(favFlag == true){
			favFlag = false;
			btnFav.setText("���C�ɓ��肩��폜");
			
		}else{
			favFlag = true;
			btnFav.setText("���C�ɓ���ɒǉ�");
		}
		*/
	}
	
	/**
	 * �i�C�X�𑗐M
	 */
	private void niceSend() {
		// TODO Auto-generated method stub
		int niceCount = mCafeDetailFinder.getCafeNice(cafe);
		niceCount++;
		String query = "iine="+niceCount+"&dataPost=1&key="+cafeId;
		
		NiceCafeTask task = new NiceCafeTask(this,cdh);
		task.execute(postApiUrl,query,"1",String.valueOf(niceCount));
		return;
		
	}
	

	/**
	 * �J�t�F�̏����擾
	 * @param cafeId2
	 */
	private void feedCafeDetail(String cafeId2) {
		mCafeDetailFinder.setCafeId(cafeId2);
        new Thread(new RequestRunnableDetail(mCafeDetailFinder, mHandler)).start();
	}

	@Override
    protected void onStart() {
		super.onStart();

		
      
    }
	
	

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
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
		//DB���I�[�v��
        mCafeDetailFinder.open();
		
		//�J�t�F�̏����擾
        feedCafeDetail(cafeId);
		
		
	}	
   
 
    

	
	
}
