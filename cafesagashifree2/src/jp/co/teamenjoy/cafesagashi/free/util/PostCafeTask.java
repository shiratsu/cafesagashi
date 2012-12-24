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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import jp.co.teamenjoy.cafesagashi.free.AddCafePageActivity;
import jp.co.teamenjoy.cafesagashi.free.MasterDataPageActivity;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeWriteDataHelper;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Xml;
import android.widget.Toast;

public class PostCafeTask extends AsyncTask<Object, Integer, String> {

	private AddCafePageActivity mActivity;
	private ProgressDialog mProgressDialog;
	
	private CafeWriteDataHelper cdh;
	private List<PostItem> resultList = new ArrayList<PostItem>();
	private SQLiteDatabase mDb;
	public HashMap xmlData;
	// コンストラクタ
	public PostCafeTask(AddCafePageActivity activity) {
		mActivity = activity;
		// データベースを書き込み用にオープンする
        cdh = new CafeWriteDataHelper(mActivity);
        
        try {
       	 
        	cdh.createEmptyDataBase();  
            mDb = cdh.openDataBase();
         } catch (SQLiteException e) {
             // ディスクフルなどでデータが書き込めない場合
        	 onStopExecute();
             mDb = cdh.getReadableDatabase();
             // DB書き込めないので、メッセージを表示して終わり
             Toast.makeText(mActivity,
                    "ディスクがいっぱいのため書き込めませんでした", Toast.LENGTH_SHORT)
                    .show();
             
             return;
         } catch (IOException e) {
        	 // DB書き込めないので、メッセージを表示して終わり
        	 onStopExecute();
        	 Toast.makeText(mActivity,
                    "データベースが開けませんでした", Toast.LENGTH_SHORT)
                    .show();
             
             return;
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
		try {
	        
			// HTTP経由でアクセスし、InputStreamを取得する
			URL url = new URL(location+"?"+query);
			InputStream is = url.openConnection().getInputStream();
			parseXml(is);
			
			//アップデート
			updateDatabase();
			
			
		} catch (Exception e) {
			onStopExecute();
       	 	Toast.makeText(mActivity,
                   "データの送信に失敗しました", Toast.LENGTH_SHORT)
                   .show();
       	 	return null;
		}
		cdh.close();
		long timeMillisEnd = System.currentTimeMillis(); 
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String updateTime = sdf1.format(new Date(timeMillisEnd));
		return updateTime;
	}
	
	/**
	 * DBに入れる
	 */
	private void insertDatabase() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * データベースを更新
	 * @throws Exception 
	 */
	private void updateDatabase() throws Exception {
		// TODO Auto-generated method stub
		if(resultList.size() == 1){
			PostItem tmpItem = resultList.get(0);
			Cursor c = fetchCafeMasterCount(tmpItem);
			int count = feedCount(c);
			c.close();
			String statusCode = tmpItem.getStatusCode();
			//ステータスが２００なら、サーバ側が成功
			if("200".equals(statusCode)){
				String cafeId = tmpItem.getCafeKey();
				String storeName = tmpItem.getStoreName();
				String storeMainName = tmpItem.getStoreMainName();
				String storeSubName = tmpItem.getStoreSubName();
				String storeAddress = tmpItem.getStoreAddress();
				String phoneNumber = tmpItem.getPhoneNumber();
				String zipCode = tmpItem.getZipCode();
				String lat = tmpItem.getLat();
				String lon = tmpItem.getLon();
				String storeCaption = tmpItem.getStoreCaption();
				String iine = tmpItem.getIine();
				String storeFlag = tmpItem.getStoreFlag();
				String deleteFlag = tmpItem.getDelFlag();
				String userSendFlag = tmpItem.getUserSendFlag();
				String tabako = tmpItem.getTabako();
				String kinen = tmpItem.getKinen();
				String koshitsu = tmpItem.getKoshitsu();
				String pc = tmpItem.getPc();
				String wifi = tmpItem.getWifi();
				String shinya = tmpItem.getShinya();
				String pet = tmpItem.getPet();
				String terace = tmpItem.getTerace();
				
				if(count == 0){
					String insertSql = "insert into cafeMaster " +
							"			(cafeId" +
							"			 ,storeName" +
							"			 ,storeSubName" +
							"			 ,storeMainName" +
							"			 ,storeAddress" +
							"			 ,phoneNumber" +
							"			 ,zipCode" +
							"			 ,lat" +
							"			 ,lon" +
							"			 ,storeCaption" +
							"			 ,iine" +
							"			 ,storeFlag" +
							"			 ,deleteFlag" +
							"			 ,userSendFlag" +
							"			 ,tabako" +
							"			 ,kinen" +
							"			 ,koshitsu" +
							"			 ,pc" +
							"			 ,wifi" +
							"			 ,shinya" +
							"			 ,terace" +
							"			 ,pet" +
							"			 ,updateTime) " +
							"			values('"+cafeId+"'" +
									"		   ,'"+storeName+"'" +
											"  ,'"+storeSubName+"'" +
											"  ,'"+storeMainName+"'" +
											"  ,'"+storeAddress+"'" +
											"  ,'"+phoneNumber+"'" +
											"  ,'"+zipCode+"'" +
											"  ,'"+lat+"'" +
											"  ,'"+lon+"'" +
											"  ,'"+storeCaption+"'" +
											"  ,'"+iine+"'" +
											"  ,'"+storeFlag+"'" +
											"  ,'"+deleteFlag+"'" +
											"  ,'"+userSendFlag+"'" +
											"  ,'"+tabako+"'" +
											"  ,'"+kinen+"'" +
											"  ,'"+koshitsu+"'" +
											"  ,'"+pc+"'" +
											"  ,'"+wifi+"'" +
											"  ,'"+shinya+"'" +
											"  ,'"+terace+"'" +
											"  ,'"+pet+"'" +
											"  ,current_timestamp)";
					mDb.execSQL(insertSql);
											
				}else{
					String updateSql = "update cafeMaster set " +
							"			storeName = '"+storeName+"'" +
									"	,storeMainName = '"+storeMainName+"'" +
									"	,storeSubName = '"+storeSubName+"'" +
									"	,storeAddress = '"+storeAddress+"'" +
									"	,phoneNumber = '"+phoneNumber+"'" +
									"	,updateTime = current_timestamp" +
									"	,zipCode = '"+zipCode+"'" +
									"	,lat = '"+lat+"'" +
									"	,lon = '"+lon+"'" +
									"	,storeCaption = '"+storeCaption+"'" +
									"	,iine = '"+iine+"'" +
									"	,storeFlag = '"+storeFlag+"'" +
									"	,deleteFlag = '"+deleteFlag+"'" +
									"	,tabako = '"+tabako+"'" +
									"	,kinen = '"+kinen+"'" +
									"	,koshitsu = '"+koshitsu+"'" +
									"	,pc = '"+pc+"'" +
									"	,wifi = '"+wifi+"'" +
									"	,shinya = '"+shinya+"'" +
									"	,terace = '"+terace+"'" +
									"	,pet = '"+pet+"' where cafeId = '"+cafeId+"'";
					mDb.execSQL(updateSql);	
					
				}
			}else{
				throw new Exception("データの送信に失敗しました");
			}
		}else{
			throw new Exception("データの送信に失敗しました");
		}
	}

	/**
	 * 件数を取得
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
	 * カウントのカーソルを取得
	 * @param tmpItem
	 * @return
	 */
	private Cursor fetchCafeMasterCount(PostItem tmpItem) {
		// TODO Auto-generated method stub
		String cafeId = tmpItem.getCafeKey();
		
		String sqlstr = "SELECT count(*) FROM cafeMaster where cafeId = '"+cafeId+"'";
		return mDb.rawQuery(sqlstr, null);
		
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
						} else if (tag.equals("storeSubName")) {
							currentItem.setStoreSubName(parser.nextText());
						} else if (tag.equals("storeName")) {
							currentItem.setStoreName(parser.nextText());
						} else if (tag.equals("storeMainName")) {
							currentItem.setStoreMainName(parser.nextText());
						} else if (tag.equals("storeCaption")) {
							currentItem.setStoreCaption(parser.nextText());	
						} else if (tag.equals("zipCode")) {
							currentItem.setZipCode(parser.nextText());
						} else if (tag.equals("phoneNumber")) {
							currentItem.setPhoneNumber(parser.nextText());
						} else if (tag.equals("storeFlag")) {
							currentItem.setStoreFlag(parser.nextText());
						} else if (tag.equals("storeAddress")) {
							currentItem.setStoreAddress(parser.nextText());
						} else if (tag.equals("lat")) {
							currentItem.setLat(parser.nextText());
						} else if (tag.equals("lon")) {
							currentItem.setLon(parser.nextText());
						} else if (tag.equals("tabako")) {
							currentItem.setTabako(parser.nextText());
						} else if (tag.equals("kinen")) {
							currentItem.setKinen(parser.nextText());
						} else if (tag.equals("pc")) {
							currentItem.setPc(parser.nextText());
						} else if (tag.equals("wifi")) {
							currentItem.setWifi(parser.nextText());
						} else if (tag.equals("koshitsu")) {
							currentItem.setKoshitsu(parser.nextText());
						} else if (tag.equals("shinya")) {
							currentItem.setShinya(parser.nextText());
						} else if (tag.equals("pet")) {
							currentItem.setPet(parser.nextText());
						} else if (tag.equals("iine")) {
							currentItem.setIine(parser.nextText());
						} else if (tag.equals("userSendFlag")) {
							currentItem.setUserSendFlag(parser.nextText());
						} else if (tag.equals("delFlag")) {
							currentItem.setDelFlag(parser.nextText());
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

	/**
	 * 最終更新日を取得
	 * @return
	 */
	private String feedLastUpdate() {
		// TODO Auto-generated method stub
		// すべてのデータのカーソルを取得
        Cursor c = fetchLastUpdateCursor();
        String dbDate = setDbDate(c);
        c.close();
		return dbDate;
	}

	/**
	 * 最終更新日を取得
	 * @param c
	 * @return
	 */
	private String setDbDate(Cursor c) {
		// TODO Auto-generated method stub
		String updateTime = null;
		if(c.moveToFirst()){
			do{
				updateTime = c.getString(c.getColumnIndex("updateTime"));
			}while(c.moveToNext());
		}	
		return updateTime;
	}
	/**
	 * 最終更新日を取得するカーソルを作成
	 * @return
	 */
	private Cursor fetchLastUpdateCursor() {
		// TODO Auto-generated method stub
		String sqlstr = "SELECT * FROM updateCheck";
		return mDb.rawQuery(sqlstr, null);
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
		mActivity.finish();
	}
}
