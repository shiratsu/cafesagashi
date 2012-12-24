package jp.co.teamenjoy.cafesagashi.free.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import jp.co.teamenjoy.cafesagashi.free.AddCafePageActivity;
import jp.co.teamenjoy.cafesagashi.free.CafeDetailPageActivity;
import jp.co.teamenjoy.cafesagashi.free.MapPageActivity;
import jp.co.teamenjoy.cafesagashi.free.MasterDataPageActivity;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeWriteDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.HistoryDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.LocationHelper;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Xml;
import android.widget.Toast;

public class HistoryMapTask extends AsyncTask<Object, Integer, String> {

	private MapPageActivity mActivity;
	private ProgressDialog mProgressDialog;
	
	private HistoryDataHelper hdh;
	private List<PostItem> resultList = new ArrayList<PostItem>();
	private SQLiteDatabase mDb;
	public HashMap xmlData;
	private int niceCount = 0;
	private String keyword;
	private Geocoder mGeocoder;
	private double mLat;
	private double mLng;
	// コンストラクタ
	public HistoryMapTask(MapPageActivity activity, Geocoder geocoder) {
		mActivity = activity;
		mGeocoder = geocoder;
		// データベースを書き込み用にオープンする
        hdh = new HistoryDataHelper(mActivity);
        
        try {
       	 
        	hdh.createEmptyDataBase();  
            mDb = hdh.openDataBase();
         } catch (SQLiteException e) {
             // ディスクフルなどでデータが書き込めない場合
        	 onStopExecute();
             mDb = hdh.getWritableDatabase();
             Looper.prepare();
             // DB書き込めないので、メッセージを表示して終わり
             Toast.makeText(mActivity,
                    "ディスクがいっぱいのため書き込めませんでした", Toast.LENGTH_SHORT)
                    .show();
             Looper.loop();
             return;
         } catch (IOException e) {
        	 // DB書き込めないので、メッセージを表示して終わり
        	 onStopExecute();
        	 Looper.prepare();
        	 Toast.makeText(mActivity,
                    "データベースが開けませんでした", Toast.LENGTH_SHORT)
                    .show();
             Looper.loop();
             return;
         }
	}
	
	
	
	@Override
    protected void onPreExecute() {
        return;
    }
	
	@Override
	protected String doInBackground(Object... params) {
		// TODO Auto-generated method stu
		keyword = (String) params[0];
		String latStr = (String) params[1];
		String lonStr = (String) params[2];
		this.mLat = Double.valueOf(latStr);
		this.mLng = Double.valueOf(lonStr);
		if(keyword == null){
			keyword = feedAddressFromLatLon();
		}
		try {
	        
			if(keyword != null){
				//アップデート
				updateDatabase();
	        }
	        
	        //アップデート
			//updateDatabase();
			
		} catch (Exception e) {
			onStopExecute();
			
			Looper.prepare();
       	 	/*
			Toast.makeText(mActivity,
                   "検索履歴に保存できませんでした", Toast.LENGTH_SHORT)
                   .show();
       	 	*/
       	 	Looper.loop();
       	 	return null;
		}
		hdh.close();
		long timeMillisEnd = System.currentTimeMillis(); 
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String updateTime = sdf1.format(new Date(timeMillisEnd));
		return updateTime;
	}
	
	/**
	 * 住所を取得
	 * @return
	 * @throws IOException 
	 */
	private String feedAddressFromLatLon() {
		// TODO Auto-generated method stub
		String string = new String();

		try {
			
			List<Address> list_address = this.mGeocoder.getFromLocation(this.mLat, this.mLng, 5);
			if(!list_address.isEmpty()){
				Address address = list_address.get(0);
				//住所に変換
				string = LocationHelper.convertAddressName(address);

			
			}
		} catch (IOException e) {
			//string = feedNotAddressText();
		}
		return string;
	}

	/**
	 * 住所を取得できなかった場合のテキストをセット
	 * @return
	 */
	private String feedNotAddressText() {
		// TODO Auto-generated method stub
		long timeMillisEnd = System.currentTimeMillis(); 
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd HH時mm分");
		String updateTime = sdf1.format(new Date(timeMillisEnd));
		return "住所が取得できませんでした（検索時間："+updateTime+"）";
	}
	
	/**
	 * データベースを更新
	 * @throws Exception 
	 */
	private void updateDatabase() throws Exception {
		// TODO Auto-generated method stub
		
		Cursor c = historyCountSql();
		int count = feedCount(c);
		c.close();
		if(count > 10){
			//一件削除
			deleteHistory();
		}
		//すでに同じ緯度経度が入ってないか確認
		c = checkDouble(); 
		int checkCount = feedCount(c);
		c.close();
		
		if(checkCount == 0){
			//一件投入
			insertHistory();
		}	
		return;
	}

	
	/**
	 * ２重チェック
	 * @return
	 */
	private Cursor checkDouble() {
		// TODO Auto-generated method stub
		String sqlstr = "select count(*) from searchHistory where address = '"+keyword+"'";
		return mDb.rawQuery(sqlstr, null);
	}

	// メインスレッド上で実行される
	protected void onStopExecute() {
		
	}

	// メインスレッド上で実行される
	@Override
	protected void onPostExecute(String cafeId) {
		/*
		Toast.makeText(mActivity,
                "データを更新しました", Toast.LENGTH_SHORT)
                .show();
        */        
	}
	/**
	 * 検索履歴テーブルにデータを投入
	 */
	private void insertHistory() {
		// TODO Auto-generated method stub
		String insertSql = "insert into searchHistory(address,lat,lon) values('"+keyword+"','"+this.mLat+"','"+this.mLng+"')";
		mDb.execSQL(insertSql);
	}
	/**
	 * 検索履歴テーブルから削除
	 */
	private void deleteHistory() {
		// TODO Auto-generated method stub
		String deleteSql = "delete from searchHistory where _id = (select _id from searchHistory order by _id limit 1)";
		mDb.execSQL(deleteSql);
	}
	/**
	 * 検索履歴から件数を取得
	 * @param c
	 * @return
	 */
	private int feedCount(Cursor c) {
		// TODO Auto-generated method stub
		int count=0;
		String countString = null;
		if(c.moveToFirst()){
			do{
				countString = c.getString(c.getColumnIndex("count(*)"));
			}while(c.moveToNext());
		}	
		if(countString != null){
			count = Integer.valueOf(countString);
		}
		return count;
	}
	/**
	 * 検索履歴テーブルから件数を取得
	 * @return
	 */
	private Cursor historyCountSql() {
		String sqlstr = "select count(*) from searchHistory";
		return mDb.rawQuery(sqlstr, null);
	}
	
}
