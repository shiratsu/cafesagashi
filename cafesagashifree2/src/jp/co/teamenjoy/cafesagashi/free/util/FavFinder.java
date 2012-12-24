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
    
    // �e�[�u���̖��O
    static final String TABLE = "favoriteList";
    
    public FavFinder(CafeDataHelper cdh2) {
		// TODO Auto-generated constructor stub
    	this.cdh = cdh2;
    	compFlag = false;
    	
	}
    
    
    /**
     * ���C�ɓ���̃J�t�F�̃f�[�^���擾
     * @param lat
     * @param lon
     */
    public void feedCafe(){
    	
    	cafeList = new ArrayList<HashMap>();
    	
    	// ���ׂẴf�[�^�̃J�[�\�����擾
        Cursor c = fetchAllCafe();
        cafeList = setCafeList(c);
        c.close();
        compFlag = true;
        return;
    }
    /**
     * �J�t�F�ꗗ���Z�b�g
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
     * �J�[�\�����쐬
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
	 * �擾�����J�t�F�̃T�C�Y���Q�b�g
	 * @return
	 */
	public int size() {
		// TODO Auto-generated method stub
		
		return cafeList.size();
	}
	
	/**
	 * �J�t�F�̃f�[�^���P�����擾
	 * @param i
	 * @return
	 */
	public HashMap getCafe(int i) {
		// TODO Auto-generated method stub
		
		return cafeList.get(i);
	}
	
	
	/**
	 * �J�t�F�̓X�ܖ����擾
	 * @param cafe
	 * @return
	 */
	public String getCafeName(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeMainName");
	}
	
	
	/**
	 * �J�t�F�̎x�X�����擾
	 * @param cafe
	 * @return
	 */
	public String getCafeSubName(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeSubName");
	}
	
	
	
	/**
	 * �J�t�F��ID���擾
	 * @param cafe
	 * @return
	 */
	public String getCafeId(HashMap cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("cafeId");
	}
	
	
	
	/**
	 * DB��������
	 */
	public void open() {
		// TODO Auto-generated method stub
		try {
	       	 
        	this.cdh.createEmptyDataBase();  
            mDb = this.cdh.openDataBase();
         } catch (SQLiteException e) {
             // �f�B�X�N�t���ȂǂŃf�[�^���������߂Ȃ��ꍇ
         	
             mDb = this.cdh.getReadableDatabase();
         } catch (IOException e) {
         	throw new Error("Unable to create database");
         }
	}


	/**
	 * �����ς݃t���O��Ԃ�
	 * @return
	 */
	public Object getCompFlag() {
		// TODO Auto-generated method stub
		return compFlag;
	}

	/**
	 * �J�t�F�̏Z�����擾
	 * @param cafe
	 * @return
	 */
	public String getCafeAddress(HashMap<String, String> cafe) {
		// TODO Auto-generated method stub
		return (String) cafe.get("storeAddress");
	}
}
