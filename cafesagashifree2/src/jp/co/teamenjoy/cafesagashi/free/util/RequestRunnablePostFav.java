package jp.co.teamenjoy.cafesagashi.free.util;




import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.teamenjoy.cafesagashi.free.exception.FavException;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeWriteDataHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


public class RequestRunnablePostFav implements Runnable {

	private CafeWriteDataHelper cdh;
	private Handler mHandler;
	private String cafeId;
	private String storeMainName;
	private String storeSubName;
	private String storeAddress;
	private SQLiteDatabase mDb;
	
	public RequestRunnablePostFav(CafeWriteDataHelper cdh2, 
								  Handler handler, 
								  String cafeId2, 
								  String storeMainName2, 
								  String storeSubName2, 
								  String storeAddress2) {
        // 駅検索オブジェクト
        this.cdh = cdh2;

        // WebAPIへのアクセスが終わったことを知らせるためのハンドラ
        this.mHandler = handler;
        cafeId = cafeId2;
        storeMainName = storeMainName2;
        storeSubName = storeSubName2;
        storeAddress = storeAddress2;
        
        try {
          	 
        	cdh.createEmptyDataBase();  
            mDb = cdh.openDataBase();
         } catch (SQLiteException e) {
            
         } catch (IOException e) {
        	 
         }
         return;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// ハンドラに通知
        Message msg = new Message();
		
        try{
			// すべてのデータのカーソルを取得
	        Cursor c = fetchOneCafe(cafeId);
	        int count = setCafeList(c);
	        c.close();
			
	        if(count > 0){
	        	//アップデート
	        	deleteFav(cafeId);
	        	msg.obj = "-1";
	        }else{
	        	//３０件に達してないか確認
	        	c = fetchAllCafe();
		        count = setCafeList(c);
		        c.close();
		        
		        if(count < 30){
		        	//お気に入りに追加
		        	insertFav(cafeId,storeMainName,storeSubName,storeAddress);
		        	msg.obj = "1";
		        }else{
		        	msg.obj = "0";
		        }
	        }
        }catch (Exception e) {
        	msg.obj = "2";
        }
        mHandler.sendMessage(msg);
	}

	/**
	 * お気に入りにデータを突っ込む
	 * @param cafeId
	 * @param storeMainName
	 * @param storeSubName
	 * @param storeAddress
	 */
	private void insertFav(String cafeId, String storeMainName, String storeSubName, String storeAddress) {
		// TODO Auto-generated method stub
		String updateSql = "insert into favoritelist" +
		" values('"+cafeId+"','"+storeMainName+"','"+storeSubName+"','"+storeAddress+"')";
		mDb.execSQL(updateSql);
	}

	/**
	 * とりあえず全件取得
	 * @return
	 */
	private Cursor fetchAllCafe() {
		// TODO Auto-generated method stub
		String sqlstr = "select count(*) from favoritelist";   
		return mDb.rawQuery(sqlstr, null);
	}

	/**
	 * 削除する
	 * @param cafeId
	 */
	private void deleteFav(String cafeId) {
		// TODO Auto-generated method stub
		String updateSql = "delete from favoritelist" +
		" where cafeId = '"+cafeId+"'";
		mDb.execSQL(updateSql);
		return;
	}

	private int setCafeList(Cursor c) {
		// TODO Auto-generated method stub
		int cafeCount = 0;
		String cafeCountStr = null;
		if(c.moveToFirst()){
			do{
				cafeCountStr = c.getString(c.getColumnIndex("count(*)"));
				
			}while(c.moveToNext());
		}
		if(cafeCountStr != null && !"".equals(cafeCountStr) && !"null".equals(cafeCountStr)){
			Pattern pattern = Pattern.compile("^[0-9]+$");
			Matcher matcher = pattern.matcher(cafeCountStr);
			//0~9以外のものにマッチしなければよい
			if(matcher.matches()){
				return Integer.valueOf(cafeCountStr);
			}	
		}
		return 0;
	}

	/**
	 * お気に入りリストから取得
	 * @param cafeId
	 * @return
	 */
	private Cursor fetchOneCafe(String cafeId) {
		// TODO Auto-generated method stub
		String sqlstr = "select count(*) from favoritelist where cafeId = '"+cafeId+"'";  
		return mDb.rawQuery(sqlstr, null);
	}
	
}
