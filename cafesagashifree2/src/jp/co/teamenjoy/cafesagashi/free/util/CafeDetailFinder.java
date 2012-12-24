package jp.co.teamenjoy.cafesagashi.free.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.teamenjoy.cafesagashi.free.helper.CafeDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeWriteDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.LocationHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;

import com.google.android.maps.GeoPoint;



public class CafeDetailFinder {
	private GeoPoint mCurrentGPoint;
	private CafeWriteDataHelper cdh;
    private double mLat;
    private double mLng;
    private String nLat;
    private String nLon;
    
    private String cafeId;
    private double mdistance;
    private List<HashMap> cafeDetail = new ArrayList<HashMap>();
    
    static final double ERROR_DOUBLE = 10000.0;
    static final String ERROR_STRING = "NULL";
    
    private SQLiteDatabase mDb;
    // テーブルの名前
    static final String TABLE = "cafeMaster";
    
    private int count = 0;
    
    
    
    public String getLat(HashMap cafe) {
		return (String) cafe.get("lat");
	}
    public String getLon(HashMap cafe) {
		return (String) cafe.get("lon");
	}
	
	public CafeDetailFinder(CafeWriteDataHelper cdh2) {
		// TODO Auto-generated constructor stub
    	this.cdh = cdh2;
        
    	
	}
    /** 現在地を設定する、この場所の周辺駅を探すことになる */
    public void setGPoint(HashMap cafe) {
    	// TODO Auto-generated method stub
		String lat = (String) cafe.get("lat");
		String lon = (String) cafe.get("lon");
		if(lat != null 
		&& lon != null 
		&& !"".equals(lat) 
		&& !"".equals(lon)){
			this.mLat = (double) (Double.valueOf(lat)*1E6);
			this.mLng = (double) (Double.valueOf(lon)*1E6);
			this.mCurrentGPoint = LocationHelper.getGeoPointLatLong(mLat, mLng);
		}
    }
    
    /** カフェのIDをセットする */
    public void setCafeId(String cafeId) {
        this.cafeId = cafeId;
    }

    /** 現在地のgetter */
    public GeoPoint getGPoint() {
        return mCurrentGPoint;
    }
    
    
    
    /**
     * カフェのデータを取得
     * @param lat
     * @param lon
     */
    public void feedCafe(){
    	cafeDetail = new ArrayList<HashMap>();
    	List<String> latLonAry = new ArrayList<String>();
    	latLonAry = GeoHashHandle.feedCalcLatLon(this.mLat,this.mLng,this.mdistance);
    	// すべてのデータのカーソルを取得
        Cursor c = fetchOneCafe(this.cafeId);
        cafeDetail = setCafeList(c);
        c.close();
        
        //お気に入りかうんとを取得
        // すべてのデータのカーソルを取得
        c = fetchFavCheckCafe(cafeId);
        count = setCheckCount(c);
        c.close();
        
        //カフェの位置をセット
        HashMap cafeTmp = getCafe(0);
        setGPoint(cafeTmp);
        
        return;
    }
    
    /**
     * お気に入りにあるか取得
     * @param c
     * @return
     */
    private int setCheckCount(Cursor c) {
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
     * お気に入りに入ってるか確認
     * @param cafeId2
     * @return
     */
    private Cursor fetchFavCheckCafe(String cafeId2) {
    	// TODO Auto-generated method stub
		String sqlstr = "select count(*) from favoritelist where cafeId = '"+cafeId2+"'";
		   
		return mDb.rawQuery(sqlstr, null);
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
				String cafeId = c.getString(c.getColumnIndex("cafeId"));
				String storeName = c.getString(c.getColumnIndex("storeName"));
				String storeSubName = c.getString(c.getColumnIndex("storeSubName"));
				String storeMainName = c.getString(c.getColumnIndex("storeMainName"));
				String storeAddress = c.getString(c.getColumnIndex("storeAddress"));
				String phoneNumber = c.getString(c.getColumnIndex("phoneNumber"));
				String zipCode = c.getString(c.getColumnIndex("zipCode"));
				String lat = c.getString(c.getColumnIndex("lat"));
				String lon = c.getString(c.getColumnIndex("lon"));
				String storeFlag = c.getString(c.getColumnIndex("storeFlag"));
				String iine = c.getString(c.getColumnIndex("iine"));
				String userSendFlag = c.getString(c.getColumnIndex("userSendFlag"));
				String tabako = c.getString(c.getColumnIndex("tabako"));
				String kinen = c.getString(c.getColumnIndex("kinen"));
				String koshitsu = c.getString(c.getColumnIndex("koshitsu"));
				String pc = c.getString(c.getColumnIndex("pc"));
				String wifi = c.getString(c.getColumnIndex("wifi"));
				String shinya = c.getString(c.getColumnIndex("shinya"));
				String terace = c.getString(c.getColumnIndex("terace"));
				String pet = c.getString(c.getColumnIndex("pet"));
				cafeData.put("cafeId",cafeId);
				cafeData.put("storeName",storeName);
				cafeData.put("storeSubName",storeSubName);
				cafeData.put("storeMainName",storeMainName);
				cafeData.put("storeAddress",storeAddress);
				cafeData.put("phoneNumber",phoneNumber);
				cafeData.put("zipCode",zipCode);
				cafeData.put("lat",lat);
				cafeData.put("lon",lon);
				cafeData.put("storeFlag",storeFlag);
				cafeData.put("iine",iine);
				cafeData.put("userSendFlag",userSendFlag);
				cafeData.put("tabako",tabako);
				cafeData.put("kinen",kinen);
				cafeData.put("koshitsu",koshitsu);
				cafeData.put("pc",pc);
				cafeData.put("wifi",wifi);
				cafeData.put("shinya",shinya);
				cafeData.put("terace",terace);
				cafeData.put("pet",pet);
				cafeDetail.add(cafeData);
				
			}while(c.moveToNext());
		}
		return cafeDetail;
	}
    
	/**
     * カーソルを作成
     * @param latLonAry
     * @return
     */
	private Cursor fetchOneCafe(String cafeId) {
		// TODO Auto-generated method stub
		String sqlstr = "select * from cafeMaster where cafeId = '"+cafeId+"' and deleteFlag != '1' limit 1";
		   
		return mDb.rawQuery(sqlstr, null);
	}
	/**
	 * 取得したカフェのサイズをゲット
	 * @return
	 */
	public int size() {
		// TODO Auto-generated method stub
		
		return cafeDetail.size();
	}
	
	/**
	 * カフェのデータを１件ずつ取得
	 * @param i
	 * @return
	 */
	public HashMap getCafe(int i) {
		// TODO Auto-generated method stub
		
		return cafeDetail.get(i);
	}
	
	/**
	 * カフェの位置を取得
	 * @param cafe
	 * @return
	 */
	public GeoPoint getCafeLocation(HashMap cafe) {
		// TODO Auto-generated method stub
		String lat = (String) cafe.get("lat");
		String lon = (String) cafe.get("lon");
		
		int lat_int = (int) (Double.valueOf(lat)*1E6);
		int lon_int = (int) (Double.valueOf(lon)*1E6);
		
		if (lat_int == ERROR_DOUBLE || lon_int == ERROR_DOUBLE) {
            return null;
        } else {
            return new GeoPoint(lat_int, lon_int);
        }
	}
	/**
	 * カフェの店舗名を取得
	 * @param cafe
	 * @return
	 */
	public String getCafeName(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeName");
	}
	
	
	/**
	 * 店舗フラグを取得
	 * @param cafe
	 * @return
	 */
	public String getStoreFlag(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeFlag");
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
	 * こだわりを取得
	 * @param cafe
	 * @return
	 */
	public String getKodawari(HashMap cafe) {
		// TODO Auto-generated method stub
		List<String> params = new ArrayList<String>();
		if("1".equals(cafe.get("tabako"))){
			params.add("喫煙OK");
		}
		if("1".equals(cafe.get("kinen"))){
			params.add("禁煙席あり");
		}
		if("1".equals(cafe.get("koshitsu"))){
			params.add("個室あり");
		}
		if("1".equals(cafe.get("pc"))){
			params.add("PC電源あり");
		}
		if("1".equals(cafe.get("wifi"))){
			params.add("wifi(公衆無線LAN)あり");
		}
		if("1".equals(cafe.get("shinya"))){
			params.add("深夜営業（２２時以降）");
		}
		if("1".equals(cafe.get("terace"))){
			params.add("テラス席あり");
		}
		if("1".equals(cafe.get("pet"))){
			params.add("ペット同伴OK");
		}
		
		return StringUtils.join(params, ",");
	}
	
	/**
	 * カフェの住所を取得
	 * @param cafe
	 * @return
	 */
	public String getAddress(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeAddress");
	}
	/**
	 * カフェの電話番号を取得
	 * @param cafe
	 * @return
	 */
	public String getPhoneNumber(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("phoneNumber");
	}
	/**
	 * ユーザ投稿フラグがあれば、メッセージをセット
	 * @param cafe
	 * @return
	 */
	public String getUserSendMessage(HashMap cafe) {
		// TODO Auto-generated method stub
		String userSendMessage = null;
		if("1".equals(cafe.get("userSendFlag"))){
			userSendMessage = "＊このカフェはユーザからの投稿です。";
		}
		return userSendMessage;
	}
	
	/**
	 * イイネを取得
	 * @param cafe
	 * @return
	 */
	public int getCafeNice(HashMap cafe) {
		// TODO Auto-generated method stub
		String iine = EscapeUtil.toHtmlStringForObject(cafe.get("iine"));
		if(iine != null && !"".equals(iine) && !"null".equals(iine)){
			Pattern pattern = Pattern.compile("^[0-9]+$");
			Matcher matcher = pattern.matcher(iine);
			//0~9以外のものにマッチしなければよい
			if(matcher.matches()){
				return Integer.valueOf(iine);
			}	
		}
		return 0;
	}
	
	/**
	 * 店舗名称のサブネームを取得
	 * @param cafe
	 * @return
	 */
	public String getCafeSubName(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeSubName");
	}
	
	/**
	 * 店舗の説明を取得
	 * @param cafe
	 * @return
	 */
	public String getStoreCaption(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeCaption");
	}
	
	/**
	 * DBを開く
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
	 * お気に入り件数を取得
	 * @return
	 */
	public int feedFavCount() {
		// TODO Auto-generated method stub
		return count;
	}
	/**
	 * カフェの店舗名を取得
	 * @param cafe
	 * @return
	 */
	public String getStoreMainName(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeMainName");
	}
	
	/**
	 * たばこ
	 * @param cafe
	 * @return
	 */
	public String getTabako(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("tabako");
	}
	/**
	 * 禁煙
	 * @param cafe
	 * @return
	 */
	public String getKinen(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("kinen");
	}
	
	/**
	 * 個室
	 * 
	 * @param cafe
	 * @return
	 */
	public String getKoshitsu(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("koshitsu");
	}
	
	/**
	 * PC
	 * @param cafe
	 * @return
	 */
	public String getPc(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("pc");
	}
	
	/**
	 * wifi
	 * @param cafe
	 * @return
	 */
	public String getWifi(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("wifi");
	}
	
	/**
	 * 深夜
	 * @param cafe
	 * @return
	 */
	public String getShinya(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("shinya");
	}
	
	/**
	 * 
	 * @param cafe
	 * @return
	 */
	public String getTerace(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("terace");
	}
	
	/**
	 * ペット
	 * @param cafe
	 * @return
	 */
	public String getPet(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("pet");
	}
	
	
}
