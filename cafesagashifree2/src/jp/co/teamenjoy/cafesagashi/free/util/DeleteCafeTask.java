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
import jp.co.teamenjoy.cafesagashi.free.MasterDataPageActivity;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeWriteDataHelper;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Xml;
import android.widget.Toast;

public class DeleteCafeTask extends AsyncTask<Object, Integer, String> {

	private CafeDetailPageActivity mActivity;
	private ProgressDialog mProgressDialog;
	
	private CafeWriteDataHelper cdh;
	private List<PostItem> resultList = new ArrayList<PostItem>();
	private SQLiteDatabase mDb;
	public HashMap xmlData;
	private int niceCount = 0;
	// コンストラクタ
	public DeleteCafeTask(CafeDetailPageActivity activity, CafeWriteDataHelper cdh2) {
		mActivity = activity;
		// データベースを書き込み用にオープンする
        cdh = cdh2;
        try {
         	 
        	cdh.createEmptyDataBase();  
            mDb = cdh.openDataBase();
         } catch (SQLiteException e) {
            
         } catch (IOException e) {
        	 
         }
	}
	
	@Override
    protected void onPreExecute() {
        // バックグラウンドの処理前にUIスレッドでダイアログ表示
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setMessage("データを更新中...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.show();
    }
	
	@Override
	protected String doInBackground(Object... params) {
		// TODO Auto-generated method stu
		String location = (String) params[0];
		String query = (String) params[1];
		String updateFlag = (String) params[2];
		String niceString = (String) params[3];
		if(niceString != null && !"".equals(niceString) && !"null".equals(niceString)){
			Pattern pattern = Pattern.compile("^[0-9]+$");
			Matcher matcher = pattern.matcher(niceString);
			//0~9以外のものにマッチしなければよい
			if(matcher.matches()){
				niceCount = Integer.valueOf(niceString);
			}	
		}
		try {
	        
			// HTTP経由でアクセスし、InputStreamを取得する
			URL url = new URL(location+"?"+query);
			InputStream is = url.openConnection().getInputStream();
			parseXml(is);
			
			//アップデート
			updateDatabase();
			
			
		} catch (Exception e) {
			onStopExecute();
			Looper.prepare();
       	 	Toast.makeText(mActivity,
                   "データの送信に失敗しました", Toast.LENGTH_SHORT)
                   .show();
       	 	Looper.loop();
       	 	return null;
		}
		cdh.close();
		long timeMillisEnd = System.currentTimeMillis(); 
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String updateTime = sdf1.format(new Date(timeMillisEnd));
		return updateTime;
	}

	/**
	 * データベースを更新
	 * @throws Exception 
	 */
	private void updateDatabase() throws Exception {
		// TODO Auto-generated method stub
		if(resultList.size() == 1){
			PostItem tmpItem = resultList.get(0);
			String statusCode = tmpItem.getStatusCode();
			//ステータスが２００なら、サーバ側が成功
			if("200".equals(statusCode)){
				String cafeId = tmpItem.getCafeKey();
				String updateSql = "update cafeMaster set " +
								"	iine = "+niceCount+" " +
								"	,updateTime = current_timestamp " +
								" where cafeId = '"+cafeId+"'";
				mDb.execSQL(updateSql);	
					
				
			}else{
				throw new Exception("データの送信に失敗しました");
			}
		}else{
			throw new Exception("データの送信に失敗しました");
		}
	}

	// XMLをパースする
	public void parseXml(InputStream is) throws IOException, XmlPullParserException {
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, null);
		int eventType = parser.getEventType();
		PostItem currentItem = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			String tag = null;
			switch (eventType) {
				case XmlPullParser.START_TAG:
					tag = parser.getName();
					if (tag.equals("Result")) {
						currentItem = new PostItem();
					} else if (currentItem != null) {
						if (tag.equals("cafeKey")) {
							currentItem.setCafeKey(parser.nextText());
						} else if (tag.equals("statusCode")) {
							currentItem.setStatusCode(parser.nextText());	
						}
					}
					break;
				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.equals("Result")) {
						resultList.add(currentItem);
					}
					break;
			}
			eventType = parser.next();
		}
		return;
		
	}

	

	
	

	// メインスレッド上で実行される
	protected void onStopExecute() {
		mProgressDialog.dismiss();
	}

	// メインスレッド上で実行される
	@Override
	protected void onPostExecute(String cafeId) {
		mProgressDialog.dismiss();
		Toast.makeText(mActivity,
                "データを更新しました", Toast.LENGTH_SHORT)
                .show();
	}
}
