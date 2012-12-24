package jp.co.teamenjoy.cafesagashi.free.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.teamenjoy.cafesagashi.free.helper.CafeDataHelper;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;

import com.google.android.maps.GeoPoint;



public class FavFinder {
	
	private CafeDataHelper cdh;
    
    private List<HashMap> cafeList = new ArrayList<HashMap>();
    
    static final double ERROR_DOUBLE = 10000.0;
    static final String ERROR_STRING = "NULL";
    
    private SQLiteDatabase mDb;
    
    private boolean compFlag = false;
    
    // テーブルの名前
    static final String TABLE = "favoriteList";
    
    public FavFinder(CafeDataHelper cdh2) {
		// TODO Auto-generated constructor stub
    	this.cdh = cdh2;
    	compFlag = false;
    	
	}
    
    
    /**
     * お気に入りのカフェのデータを取得
     * @param lat
     * @param lon
     */
    public void feedCafe(){
    	
    	cafeList = new ArrayList<HashMap>();
    	
    	// すべてのデータのカーソルを取得
        Cursor c = fetchAllCafe();
        cafeList = setCafeList(c);
        c.close();
        compFlag = true;
        return;
    }
    /**
     * カフェ一覧をセット
     * @param c
     * @return
     */
	private List<HashMap> setCafeList(Cursor c) {
		if(c.moveToFirst()){
			do{
				HashMap<String,String> cafeData = new HashMap<String,String>();
				String storeMainName = c.getString(c.getColumnIndex("storeMainName"));
				String storeSubName = c.getString(c.getColumnIndex("storeSubName"));
				String cafeId = c.getString(c.getColumnIndex("cafeId"));
				String storeAddress = c.getString(c.getColumnIndex("storeAddress"));
				
				cafeData.put("storeMainName",storeMainName);
				cafeData.put("storeSubName",storeSubName);
				cafeData.put("cafeId",cafeId);
				cafeData.put("storeAddress",storeAddress);
				
				cafeList.add(cafeData);
				
			}while(c.moveToNext());
		}
		return cafeList;
	}
    
	/**
     * カーソルを作成
     * @param latLonAry
     * @return
     */
	private Cursor fetchAllCafe() {
		// TODO Auto-generated method stub
		String sqlstr = "SELECT * " +
								"FROM favoriteList";
		   
		return mDb.rawQuery(sqlstr, null);
	}
	/**
	 * 取得したカフェのサイズをゲット
	 * @return
	 */
	public int size() {
		// TODO Auto-generated method stub
		
		return cafeList.size();
	}
	
	/**
	 * カフェのデータを１件ずつ取得
	 * @param i
	 * @return
	 */
	public HashMap getCafe(int i) {
		// TODO Auto-generated method stub
		
		return cafeList.get(i);
	}
	
	
	/**
	 * カフェの店舗名を取得
	 * @param cafe
	 * @return
	 */
	public String getCafeName(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeMainName");
	}
	
	
	/**
	 * カフェの支店名を取得
	 * @param cafe
	 * @return
	 */
	public String getCafeSubName(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeSubName");
	}
	
	
	
	/**
	 * カフェのIDを取得
	 * @param cafe
	 * @return
	 */
	public String getCafeId(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("cafeId");
	}
	
	
	
	/**
	 * DBを初期化
	 */
	public void open() {
		// TODO Auto-generated method stub
		try {
	       	 
        	this.cdh.createEmptyDataBase();  
            mDb = this.cdh.openDataBase();
         } catch (SQLiteException e) {
             // ディスクフルなどでデータが書き込めない場合
         	
             mDb = this.cdh.getReadableDatabase();
         } catch (IOException e) {
         	throw new Error("Unable to create database");
         }
	}


	/**
	 * 処理済みフラグを返す
	 * @return
	 */
	public Object getCompFlag() {
		// TODO Auto-generated method stub
		return compFlag;
	}

	/**
	 * カフェの住所を取得
	 * @param cafe
	 * @return
	 */
	public String getCafeAddress(HashMap<String, String> cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeAddress");
	}
}
