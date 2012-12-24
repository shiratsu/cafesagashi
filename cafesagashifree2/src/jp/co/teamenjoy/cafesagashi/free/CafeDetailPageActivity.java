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

	
    // 画面コントロール
    // ダイアログ
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
    private CafeDetailFinder mCafeDetailFinder;// カフェ検索
    private String cafeId = "";
    private HashMap cafe;
    private List<HashMap> cafeDetail = new ArrayList<HashMap>();
    private String postApiUrl = "http://teamenjoy-cafe.appspot.com/cafemap/apiPostCafe3/";
    
    // リソース
    private Resources mRes;
    
    private TextView tv;
    
    // GPSへの問い合わせ周期
    static final int MIN_TIME = 0;
    static final int MIN_METER = 0;
    
    private Geocoder geocoder;
    
    
    private SQLiteDatabase mDb;
    // テーブルの名前
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
     * DBへのアクセスが完了すると呼び出される
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
            		
            		toastMessage("データが３０件に達しているため、追加できません");
            	}else if("2".equals(checkObj)){
            		
            		toastMessage("お気に入り情報の更新に失敗しました");
            	}	
            }
            
        }

		
    };
    
    /**
     * Toastメッセージを表示する
     * @param string
     */
    private void toastMessage(String string) {
		// TODO Auto-generated method stub
    	Toast.makeText(CafeDetailPageActivity.this,
                string, Toast.LENGTH_SHORT)
                .show();
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
        
        int size = mCafeDetailFinder.size();
        int counter = 0;
        
    	cafe = mCafeDetailFinder.getCafe(0);
    	
    	//ListViewにセット
    	
 
    	GeoPoint cafePoint = mCafeDetailFinder.getCafeLocation(cafe);
    	storeSubName = mCafeDetailFinder.getCafeSubName(cafe);
    	cafeName = mCafeDetailFinder.getCafeName(cafe);
    	storeMainName = mCafeDetailFinder.getStoreMainName(cafe);
    	storeFlag = mCafeDetailFinder.getStoreFlag(cafe);
    	String cafeId = mCafeDetailFinder.getCafeId(cafe);
    	storeAddress = mCafeDetailFinder.getAddress(cafe);
    	String kodawari = mCafeDetailFinder.getKodawari(cafe);
    	phoneNumber = mCafeDetailFinder.getPhoneNumber(cafe);
    	
    	//緯度経度を取得
    	lat = mCafeDetailFinder.getLat(cafe);
    	lon = mCafeDetailFinder.getLon(cafe);
    	
    	
    	String userSendMessage = mCafeDetailFinder.getUserSendMessage(cafe);
    	
    	
    	
    	int count = mCafeDetailFinder.feedFavCount();
    	
    	if("1".equals(storeFlag)){
    		map_view.addStab(cafePoint, cafeName,cafeId);
    		storeCategory = "スターバックス";
    	}else if("2".equals(storeFlag)){
    		map_view.addDtour(cafePoint, cafeName,cafeId);
    		storeCategory = "ドトール";
    	}else if("3".equals(storeFlag)){
    		map_view.addTurrys(cafePoint, cafeName,cafeId);
    		storeCategory = "タリーズ";
    	}else if("4".equals(storeFlag)){
    		map_view.addSanmaruk(cafePoint, cafeName,cafeId);
    		storeCategory = "サンマルクカフェ";
    	}else if("5".equals(storeFlag)){
    		map_view.addExcelShiorl(cafePoint, cafeName,cafeId);
    		storeCategory = "エクセルシオール";
    	}else if("6".equals(storeFlag)){
    		map_view.addShiatols(cafePoint, cafeName,cafeId);
    		storeCategory = "シアトルズベスト";
    	}else if("7".equals(storeFlag)){
    		map_view.addVeroche(cafePoint, cafeName,cafeId);
    		storeCategory = "ベローチェ";
    	}else if("8".equals(storeFlag)){
    		map_view.addCafedo(cafePoint, cafeName,cafeId);
    		storeCategory = "カフェドクリエ";
    	}else if("9".equals(storeFlag)){
    		map_view.addKomeda(cafePoint, cafeName,cafeId);
    		storeCategory = "コメダ珈琲";
    	}else if("Z".equals(storeFlag)){
    		map_view.addKoten(cafePoint, cafeName,cafeId);
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
    		
    	
        // 例外なく処理できた数
        counter++;
            
        //map_view.moveGPoint(new GeoPoint(nowLat, nowLon));
        
        
    	
        btnPhone = (Button) findViewById(R.id.btnPhone);
        if(phoneNumber != null && !"".equals(phoneNumber)){
        	btnPhone.setVisibility(View.VISIBLE);
        	btnPhone.setEnabled(true);
        	btnPhone.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	
	            	//電話かける
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
		
		//遷移元から都道府県コードを受け取る
        Bundle extras = getIntent().getExtras();
        
        //AdMaker.removeViewAt(R.id.admakerview);
        
        if(extras != null){
        	cafeId = extras.getString("cafeId");
        }
		
        if(cafeId == null || "".equals(cafeId)){
        	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // アラートダイアログのタイトルを設定します
            alertDialogBuilder.setTitle("カフェ取得エラー");
            // アラートダイアログのメッセージを設定します
            alertDialogBuilder.setMessage("対象のカフェが見つかりません");
            // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	finish();
                        }
                    });
        	
        }
        
		//MapViewを取り出す
		
        map_view = (ItemsMapDetailView) findViewById(R.id.map_view);
		//map_view.setClickable(true);
		//map_view.setBuiltInZoomControls(true);
        
		//mapController = map_view.getController();
		//mapController.setZoom(15);
        
        
        
        //編集
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(CafeDetailPageActivity.this,AddCafePageActivity.class);
		        intent.putExtra("cafeId", cafeId);
		        startActivity(intent);
            }
         });
        
        //ナイスボタン
        btnNice = (Button) findViewById(R.id.btnNice);
        btnNice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//ナイス送信
            	niceSend();
            }

			

			
         });
        
        //ツイートボタン
        btnTweet = (Button) findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//ツイート送信ページへ
            	toTweetPage();
            }
			
         });
        
        //メールボタン
        btnMail = (Button) findViewById(R.id.btnMail);
        btnMail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//メール送信へ
            	toMail();
            }

			
			
         });
        //トップへボタン
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
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
        
        
        // リソース
        mRes = getResources();

        // データベースを書き込み用にオープンする
        cdh = new CafeWriteDataHelper(this);
        
        
        //カフェ検索オブジェクト
        mCafeDetailFinder = new CafeDetailFinder(cdh);
        
        //DBをオープン
        mCafeDetailFinder.open();
        
        // ジオコーダ生成
        geocoder = new Geocoder(this, Locale.JAPAN);
        
        
	}

	/**
	 * Naviconに連携する
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
		
		//カフェの情報を取得
        feedCafeDetail(cafeId);
		
	}

		
	/**
	 * 電話をかける
	 */
	private void callLogic() {
		// TODO Auto-generated method stub
		Uri uri = Uri.parse("tel:"+phoneNumber);
		Intent i = new Intent(Intent.ACTION_DIAL,uri);
		startActivity(i);
	}
	
	/**
	 * メール送信
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
	         // システムにインストールされているメーラーが表示される。
	         //  後は使いたいメーラーで送ればＯＫだ。アプリは起動までが責任範囲、送信結果は受け取れない。
	         startActivity(Intent.createChooser(it,"メーラー一覧"));
	      } catch (ActivityNotFoundException e ) {
	         // ※このエラーは通常はインテントの設定ミスがほとんどのはずだ。
	         String errorMsg = String.format("%s\n(%s)",
	               "メーラー一覧が取得できません",
	               e.getMessage());
	         // アラートダイアログ ※エラーメッセージを表示してバグ報告してもらおう。
	         AlertDialog.Builder errorDialog =
	            new AlertDialog.Builder(this);
	         errorDialog.setTitle("メール起動エラー");
	         errorDialog.setMessage(errorMsg);
	         errorDialog.setPositiveButton(R.string.button_close, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	               // ひとまずギャラリーは使えるのでダイアログを閉じるだけ
	               // メーラー処理がメインならアプリ終了かな
	            }
	         });
	         errorDialog.show();
	      }
	}
	
	/**
	 * ツイッターページへ遷移
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
			tweetDefaultStr = "【"+storeCategory+"/"+storeSubName+"】 "+storeAddress+" http://bit.ly/iTLOET #cafe_sagashi";
		}else{
			tweetDefaultStr = "【"+storeSubName+"】 "+storeAddress+" http://bit.ly/iTLOET #cafe_sagashi";
		}
		// インテントへのインスタンス生成  
		//こいつがセットされてなければ終了
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
	 * お気に入り処理
	 */
	private void favLogic() {
		// TODO Auto-generated method stub
		
		//お気に入りに追加か削除
		/*
		FavHandleTask fav = new FavHandleTask(this,btnFav);
		fav.execute(cafeId,storeMainName,storeSubName,storeAddress);
		*/
		new Thread(new RequestRunnablePostFav(cdh, mHandler,cafeId,storeMainName,storeSubName,storeAddress)).start();
    	return;
		
		/*
		if(favFlag == true){
			favFlag = false;
			btnFav.setText("お気に入りから削除");
			
		}else{
			favFlag = true;
			btnFav.setText("お気に入りに追加");
		}
		*/
	}
	
	/**
	 * ナイスを送信
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
	 * カフェの情報を取得
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
		//DBをオープン
        mCafeDetailFinder.open();
		
		//カフェの情報を取得
        feedCafeDetail(cafeId);
		
		
	}	
   
 
    

	
	
}
