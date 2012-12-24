package jp.co.teamenjoy.cafesagashi.free.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import jp.co.teamenjoy.cafesagashi.free.helper.CafeDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.HistoryDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.LocationHelper;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Xml;

import com.google.android.maps.GeoPoint;



public class CafeFinder {
	private GeoPoint mCurrentGPoint;
	
	private CafeDataHelper cdh;
    private double mLat;
    private double mLng;
    private double mdistance;
    private String mKeyword;
    private List<HashMap> cafeList = new ArrayList<HashMap>();
    private List<PositionItem> pointAry = new ArrayList<PositionItem>();
    
    static final double ERROR_DOUBLE = 10000.0;
    static final String ERROR_STRING = "NULL";
    
    private SQLiteDatabase mDb;
    
    private String storeFlag1;
	private String storeFlag2;
	private String storeFlag3;
	private String storeFlag4;
	private String storeFlag5;
	private String storeFlag6;
	private String storeFlag7;
	private String storeFlag8;
	private String storeFlag9;
	private String storeFlagZ;
	private String tabako;
	private String kinen;
	private String wifi;
	private String pc;
	private String koshitsu;
	private String terace;
	private String pet;
	private String shinya;
    private SharedPreferences pref;
    // テーブルの名前
    static final String TABLE = "cafeMaster";
    private Geocoder mGeocoder;
    private boolean centerFlag;
    
    private String pointSearchApi = "http://teamenjoy-cafe.appspot.com/cafemap/apiFeedLatLon/";
    
    public CafeFinder(CafeDataHelper cdh2, Geocoder geocoder) {
		// TODO Auto-generated constructor stub
    	this.cdh = cdh2;
        this.mGeocoder = geocoder;
        
    	
	}
    /** 現在地を設定する、この場所の周辺駅を探すことになる */
    public void setGPoint(GeoPoint currentGPoint,double distance) {
        this.mCurrentGPoint = currentGPoint;
        this.mLat = (double) currentGPoint.getLatitudeE6() / (double) 1E6;
        this.mLng = (double) currentGPoint.getLongitudeE6() / (double) 1E6;
        this.mdistance = distance;
    }

    /** 現在地のgetter */
    public GeoPoint getGPoint() {
        return mCurrentGPoint;
    }
    
    
    
    /**
     * カフェのデータを取得
     * @param lat
     * @param lon
     * @throws IOException 
     * @throws XmlPullParserException 
     */
    public void feedCafe() throws IOException, XmlPullParserException{
    	
    	
    	
    	if(this.mKeyword != null){
    		//GeoPoint kgp = feedGeoPointFromKeyword(this.mKeyword);
    		
    		
    		List<Address> list = this.mGeocoder.getFromLocationName(this.mKeyword, 1);
    		Address address = list.get(0);
    		GeoPoint kgp = LocationHelper.getGeoPointLatLong(address.getLatitude(), address.getLongitude());
    		
    		setGPoint(kgp, mdistance);
    	}
    	
    	if (mCurrentGPoint == null) {
            return;
        }
    	
    	cafeList = new ArrayList<HashMap>();
    	List<String> latLonAry = new ArrayList<String>();
    	latLonAry = GeoHashHandle.feedCalcLatLon(this.mLat,this.mLng,this.mdistance);
    	String whereSql = createWhere(latLonAry);
    	// すべてのデータのカーソルを取得
        Cursor c = fetchAllCafe(whereSql);
        cafeList = setCafeList(c);
        c.close();
        
        
        this.mKeyword = null;
        this.centerFlag = false;
        return;
    }
    
    

   
	
	
	/**
     * キーワード検索APIで検索（android標準は落ちやすいから不採用）
     * @param mKeyword2
     * @return
     * @throws IOException 
     * @throws XmlPullParserException 
     */
    private GeoPoint feedGeoPointFromKeyword(String mKeyword2) throws IOException, XmlPullParserException {
		// TODO Auto-generated method stub
    	String query = "pointName="+URLEncoder.encode(mKeyword2)+"&search=1";
    	// HTTP経由でアクセスし、InputStreamを取得する
		URL url = new URL(pointSearchApi+"?"+query);
		InputStream is = url.openConnection().getInputStream();
		parseXml(is);
		
		PositionItem tmpItem = pointAry.get(0);
		String pointlat = tmpItem.getPositionlat();
		String pointlon = tmpItem.getPositionlon();
		double d_lat = Double.valueOf(pointlat);
		double d_lon = Double.valueOf(pointlon);
		int int_lat = (int) (Double.valueOf(d_lat)*1E6);
		int int_lon = (int) (Double.valueOf(d_lon)*1E6);
		GeoPoint keywordGp = new GeoPoint(int_lat, int_lon);
		
		return keywordGp;
	}
    /**
     * XMLをパースする
     * @param is
     * @throws XmlPullParserException 
     * @throws IOException 
     */
	private void parseXml(InputStream is) throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, null);
		int eventType = parser.getEventType();
		PositionItem currentItem = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			String tag = null;
			switch (eventType) {
				case XmlPullParser.START_TAG:
					tag = parser.getName();
					if (tag.equals("Position")) {
						currentItem = new PositionItem();
					} else if (currentItem != null) {
						if (tag.equals("positionlat")) {
							currentItem.setPositionlat(parser.nextText());
						} else if (tag.equals("statusCode")) {
							currentItem.setStatusCode(parser.nextText());	
						} else if (tag.equals("positionlon")) {
							currentItem.setPositionlon(parser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.equals("Position")) {
						pointAry.add(currentItem);
					}
					break;
			}
			eventType = parser.next();
		}
		
	}
	/**
     * カフェ一覧をセット
     * @param c
     * @return
     */
	private List<HashMap> setCafeList(Cursor c) {
		cafeList = new ArrayList<HashMap>();
		if(c.moveToFirst()){
			do{
				HashMap<String,String> cafeData = new HashMap<String,String>();
				String storeName = c.getString(c.getColumnIndex("storeName"));
				String cafeId = c.getString(c.getColumnIndex("cafeId"));
				String storeAddress = c.getString(c.getColumnIndex("storeAddress"));
				String lat = c.getString(c.getColumnIndex("lat"));
				String lon = c.getString(c.getColumnIndex("lon"));
				String storeFlag = c.getString(c.getColumnIndex("storeFlag"));
				String iine = c.getString(c.getColumnIndex("iine"));
				cafeData.put("storeName",storeName);
				cafeData.put("cafeId",cafeId);
				cafeData.put("storeAddress",storeAddress);
				cafeData.put("lat",lat);
				cafeData.put("lon",lon);
				cafeData.put("storeFlag",storeFlag);
				cafeData.put("iine",iine);
				cafeList.add(cafeData);
				
			}while(c.moveToNext());
		}
		return cafeList;
	}
    /**
     * where句を作成
     * @param latLonAry
     * @return
     */
    private String createWhere(List<String> latLonAry) {
		// TODO Auto-generated method stub
    	double lat1 = Double.valueOf(latLonAry.get(0));
    	double lon1 = Double.valueOf(latLonAry.get(1));
    	double lat2 = Double.valueOf(latLonAry.get(2));
    	double lon2 = Double.valueOf(latLonAry.get(3));
    	
		storeFlag1 = this.pref.getString("storeFlag1", null);
		storeFlag2 = this.pref.getString("storeFlag2", null);
		storeFlag3 = this.pref.getString("storeFlag3", null);
		storeFlag4 = this.pref.getString("storeFlag4", null);
		storeFlag5 = this.pref.getString("storeFlag5", null);
		storeFlag6 = this.pref.getString("storeFlag6", null);
		storeFlag7 = this.pref.getString("storeFlag7", null);
		storeFlag8 = this.pref.getString("storeFlag8", null);
		storeFlag9 = this.pref.getString("storeFlag9", null);
		storeFlagZ = this.pref.getString("storeFlagZ", null);
		tabako = this.pref.getString("tabako", null);
		kinen = this.pref.getString("kinen", null);
		koshitsu = this.pref.getString("koshitsu", null);
		wifi = this.pref.getString("wifi", null);
		pc = this.pref.getString("pc", null);
		shinya = this.pref.getString("shinya", null);
		terace = this.pref.getString("terace", null);
		pet = this.pref.getString("pet", null);
		
    	String whereSql = "where (lat between '"+lat1+"' and '"+ lat2
    					  +"') and (lon between '"+lon1+"' and '"+lon2+"')";
    	
    	if(!"1".equals(storeFlag1) 
		   || !"1".equals(storeFlag2) 
		   || !"1".equals(storeFlag3) 
		   || !"1".equals(storeFlag4) 
		   || !"1".equals(storeFlag5) 
		   || !"1".equals(storeFlag6) 
		   || !"1".equals(storeFlag7) 
		   || !"1".equals(storeFlag8) 
		   || !"1".equals(storeFlag9) 
		   | !"1".equals(storeFlagZ) 
			){
    		whereSql += " and (";
    		List<String> storeFlagAry = new ArrayList<String> ();
    		if("1".equals(storeFlag1)){
    			storeFlagAry.add("storeFlag = '1'");
    		}
    		if("1".equals(storeFlag2)){
    			storeFlagAry.add("storeFlag = '2'");
    		}
    		if("1".equals(storeFlag3)){
    			storeFlagAry.add("storeFlag = '3'");
    		}
    		if("1".equals(storeFlag4)){
    			storeFlagAry.add("storeFlag = '4'");
    		}
    		if("1".equals(storeFlag5)){
    			storeFlagAry.add("storeFlag = '5'");
    		}
    		if("1".equals(storeFlag6)){
    			storeFlagAry.add("storeFlag = '6'");
    		}
    		if("1".equals(storeFlag7)){
    			storeFlagAry.add("storeFlag = '7'");
    		}
    		if("1".equals(storeFlag8)){
    			storeFlagAry.add("storeFlag = '8'");
    		}
    		if("1".equals(storeFlag9)){
    			storeFlagAry.add("storeFlag = '9'");
    		}
    		if("1".equals(storeFlagZ)){
    			storeFlagAry.add("storeFlag = 'Z'");
    		}
    		String storeSql = StringUtils.join(storeFlagAry, " or ");
    		whereSql += storeSql+ ")";
    	}
    	
    	if("1".equals(tabako)){
    		whereSql += " and tabako = '1' ";
    	}
    	if("1".equals(kinen)){
    		whereSql += " and kinen = '1' ";
    	}
    	if("1".equals(koshitsu)){
    		whereSql += " and koshitsu = '1' ";
    	}
    	if("1".equals(pc)){
    		whereSql += " and pc = '1' ";
    	}
    	if("1".equals(wifi)){
    		whereSql += " and wifi = '1' ";
    	}
    	if("1".equals(terace)){
    		whereSql += " and terace = '1' ";
    	}
    	if("1".equals(pet)){
    		whereSql += " and pet = '1' ";
    	}
    	if("1".equals(shinya)){
    		whereSql += " and shinya = '1' ";
    	}
    	
		return whereSql;
	}
	/**
     * カーソルを作成
     * @param latLonAry
     * @return
     */
	private Cursor fetchAllCafe(String whereSql) {
		// TODO Auto-generated method stub
		String sqlstr = "SELECT storeName," +
								"cafeId," +
								"storeAddress," +
								"lat," +
								"lon," +
								"storeFlag," +
								"iine " +
								"FROM cafeMaster "+whereSql;
		   
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
	 * 地図の中心地からカフェまでの距離を取得
	 * @param cafe
	 * @return
	 */
	public double getCafeDistance(HashMap cafe) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String lat = (String) cafe.get("lat");
		String lon = (String) cafe.get("lon");
		double distance = 0.0;
		double d_lat = Double.valueOf(lat);
		double d_lon = Double.valueOf(lon);
		float[] results = new float[1];
		Location.distanceBetween(mLat, mLng, d_lat, d_lon, results);
		distance = results[0];
        return distance;
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
	 * 
	 * @param pref
	 */
	public void setPreference(SharedPreferences pref) {
		// TODO Auto-generated method stub
		this.pref = pref;
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
	 * キーワードと距離をセット
	 * @param keyword
	 * @param distance
	 */
	public void setKeywordDist(String keyword, double distance) {
		// TODO Auto-generated method stub
		this.mKeyword = keyword;
		this.mdistance = distance;
		return;
		
	}
	/**
	 * 中心地のカフェを取得であることをセット
	 * @param b
	 */
	public void setCenterFlag(boolean b) {
		// TODO Auto-generated method stub
		this.centerFlag = b;
		
	}
	
	
	
}
