package jp.co.teamenjoy.cafesagashi.free.util;




import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.co.teamenjoy.cafesagashi.free.MasterDataPageActivity;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeWriteDataHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Xml;
import android.widget.Toast;


public class RequestRunnableMaster implements Runnable {

	private CafeWriteDataHelper cdh;
	private List<Item> resultList = new ArrayList<Item>();
	private SQLiteDatabase mDb;
	public HashMap xmlData;
	private int count = 0;
	private String countStr = null;
	private Handler mHandler;
	private String location=null;
	private MasterDataPageActivity mActivity;
	
	public RequestRunnableMaster(MasterDataPageActivity activity,
								String cafeApi,
								Handler handler, 
								CafeWriteDataHelper cdh2, 
								SQLiteDatabase mDb2) {
		// データベースを書き込み用にオープンする
        mActivity = activity;
        location = cafeApi;
		cdh = cdh2;
		mDb = mDb2;
        
        // WebAPIへのアクセスが終わったことを知らせるためのハンドラ
        this.mHandler = handler;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Message msg = new Message();
		try {
			mDb.beginTransaction();
			//最終更新日付を取得
	        String dbDate = feedLastUpdate();
	         
	        //HTTPデータ通信をして、更新データを取得
	        String query = "search=1&dbDate="+URLEncoder.encode(dbDate);
			
			
			// HTTP経由でアクセスし、InputStreamを取得する
			URL url = new URL(location+query);
			InputStream is = url.openConnection().getInputStream();
			parseXml(is);
			
			count = resultList.size();
			
			if(count > 0){
				//DBにデータを放り込む
				updateDatabase();
				
				//最終更新日付を取得
		        dbDate = feedLastUpdate();
		        String timeText = StringUtils.feedJapanTimeText(dbDate);
		        
		        mDb.setTransactionSuccessful();
		        msg.obj = timeText;
			    mHandler.sendMessage(msg);
			}else{
				mDb.setTransactionSuccessful();
				msg.obj = String.valueOf(count);
			    mHandler.sendMessage(msg);
			}

		} catch (XmlPullParserException e) {
			 msg.obj = null;
		     mHandler.sendMessage(msg);
		     e.printStackTrace();
	        
		} catch (IOException e) {
			 msg.obj = null;
		     mHandler.sendMessage(msg);
		     e.printStackTrace();
		} catch (Exception e) {
			 msg.obj = null;
		     mHandler.sendMessage(msg);
		     e.printStackTrace();
		}finally{
			mDb.endTransaction();
		}

	}
	
	/**
	 * データベースを更新
	 */
	private void updateDatabase() {
		// TODO Auto-generated method stub
		for(int i=0;i<resultList.size();i++){
			Item tmpItem = resultList.get(i);
			Cursor c = fetchCafeMasterCount(tmpItem);
			int count = feedCount(c);
			c.close();
			String cafeId = tmpItem.getKey();
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
			String updateTime = tmpItem.getUpdateTime();
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
						"			 ,updateTime" +
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
						"			 ,pet) " +
						"			values('"+cafeId+"'" +
								"		   ,"+DatabaseUtils.sqlEscapeString( storeName )+"" +
										"  ,"+DatabaseUtils.sqlEscapeString( storeSubName )+"" +
										"  ,"+DatabaseUtils.sqlEscapeString( storeMainName )+"" +
										"  ,"+DatabaseUtils.sqlEscapeString( storeAddress )+"" +
										"  ,'"+phoneNumber+"'" +
										"  ,'"+zipCode+"'" +
										"  ,'"+lat+"'" +
										"  ,'"+lon+"'" +
										"  ,"+DatabaseUtils.sqlEscapeString( storeCaption )+"" +
										"  ,'"+iine+"'" +
										"  ,'"+updateTime+"'" +
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
										"  ,'"+pet+"')";
				mDb.execSQL(insertSql);
										
			}else{
				String updateSql = "update cafeMaster set " +
						"			storeName = "+DatabaseUtils.sqlEscapeString( storeName )+"" +
								"	,storeMainName = "+DatabaseUtils.sqlEscapeString( storeMainName )+"" +
								"	,storeSubName = "+DatabaseUtils.sqlEscapeString( storeSubName )+"" +
								"	,storeAddress = "+DatabaseUtils.sqlEscapeString( storeAddress )+"" +
								"	,phoneNumber = '"+phoneNumber+"'" +
								"	,zipCode = '"+zipCode+"'" +
								"	,lat = '"+lat+"'" +
								"	,lon = '"+lon+"'" +
								"	,storeCaption = "+DatabaseUtils.sqlEscapeString( storeCaption )+"" +
								"	,iine = '"+iine+"'" +
								"	,updateTime = '"+updateTime+"'" +
								"	,storeFlag = '"+storeFlag+"'" +
								"	,deleteFlag = '"+deleteFlag+"'" +
								"	,userSendFlag = '"+userSendFlag+"'" +
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
			String updateTimeSql = "update updateCheck set updateTime = current_timestamp";
			mDb.execSQL(updateTimeSql);
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
	private Cursor fetchCafeMasterCount(Item tmpItem) {
		// TODO Auto-generated method stub
		String cafeId = tmpItem.getKey();
		
		String sqlstr = "SELECT count(*) FROM cafeMaster where cafeId = '"+cafeId+"'";
		return mDb.rawQuery(sqlstr, null);
		
	}

	// XMLをパースする
	public void parseXml(InputStream is) throws IOException, XmlPullParserException {
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, null);
		int eventType = parser.getEventType();
		Item currentItem = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			String tag = null;
			switch (eventType) {
				case XmlPullParser.START_TAG:
					tag = parser.getName();
					if (tag.equals("Result")) {
						currentItem = new Item();
					} else if (currentItem != null) {
						if (tag.equals("key")) {
							currentItem.setKey(parser.nextText());
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
						} else if (tag.equals("updateTime")) {
							currentItem.setUpdateTime(parser.nextText());
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
}
