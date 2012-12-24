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
	// �R���X�g���N�^
	public HistoryMapTask(MapPageActivity activity, Geocoder geocoder) {
		mActivity = activity;
		mGeocoder = geocoder;
		// �f�[�^�x�[�X���������ݗp�ɃI�[�v������
        hdh = new HistoryDataHelper(mActivity);
        
        try {
       	 
        	hdh.createEmptyDataBase();  
            mDb = hdh.openDataBase();
         } catch (SQLiteException e) {
             // �f�B�X�N�t���ȂǂŃf�[�^���������߂Ȃ��ꍇ
        	 onStopExecute();
             mDb = hdh.getWritableDatabase();
             Looper.prepare();
             // DB�������߂Ȃ��̂ŁA���b�Z�[�W��\�����ďI���
             Toast.makeText(mActivity,
                    "�f�B�X�N�������ς��̂��ߏ������߂܂���ł���", Toast.LENGTH_SHORT)
                    .show();
             Looper.loop();
             return;
         } catch (IOException e) {
        	 // DB�������߂Ȃ��̂ŁA���b�Z�[�W��\�����ďI���
        	 onStopExecute();
        	 Looper.prepare();
        	 Toast.makeText(mActivity,
                    "�f�[�^�x�[�X���J���܂���ł���", Toast.LENGTH_SHORT)
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
				//�A�b�v�f�[�g
				updateDatabase();
	        }
	        
	        //�A�b�v�f�[�g
			//updateDatabase();
			
		} catch (Exception e) {
			onStopExecute();
			
			Looper.prepare();
       	 	/*
			Toast.makeText(mActivity,
                   "���������ɕۑ��ł��܂���ł���", Toast.LENGTH_SHORT)
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
	 * �Z�����擾
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
				//�Z���ɕϊ�
				string = LocationHelper.convertAddressName(address);

			
			}
		} catch (IOException e) {
			//string = feedNotAddressText();
		}
		return string;
	}

	/**
	 * �Z�����擾�ł��Ȃ������ꍇ�̃e�L�X�g���Z�b�g
	 * @return
	 */
	private String feedNotAddressText() {
		// TODO Auto-generated method stub
		long timeMillisEnd = System.currentTimeMillis(); 
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy�NMM��dd HH��mm��");
		String updateTime = sdf1.format(new Date(timeMillisEnd));
		return "�Z�����擾�ł��܂���ł����i�������ԁF"+updateTime+"�j";
	}
	
	/**
	 * �f�[�^�x�[�X���X�V
	 * @throws Exception 
	 */
	private void updateDatabase() throws Exception {
		// TODO Auto-generated method stub
		
		Cursor c = historyCountSql();
		int count = feedCount(c);
		c.close();
		if(count > 10){
			//�ꌏ�폜
			deleteHistory();
		}
		//���łɓ����ܓx�o�x�������ĂȂ����m�F
		c = checkDouble(); 
		int checkCount = feedCount(c);
		c.close();
		
		if(checkCount == 0){
			//�ꌏ����
			insertHistory();
		}	
		return;
	}

	
	/**
	 * �Q�d�`�F�b�N
	 * @return
	 */
	private Cursor checkDouble() {
		// TODO Auto-generated method stub
		String sqlstr = "select count(*) from searchHistory where address = '"+keyword+"'";
		return mDb.rawQuery(sqlstr, null);
	}

	// ���C���X���b�h��Ŏ��s�����
	protected void onStopExecute() {
		
	}

	// ���C���X���b�h��Ŏ��s�����
	@Override
	protected void onPostExecute(String cafeId) {
		/*
		Toast.makeText(mActivity,
                "�f�[�^���X�V���܂���", Toast.LENGTH_SHORT)
                .show();
        */        
	}
	/**
	 * ���������e�[�u���Ƀf�[�^�𓊓�
	 */
	private void insertHistory() {
		// TODO Auto-generated method stub
		String insertSql = "insert into searchHistory(address,lat,lon) values('"+keyword+"','"+this.mLat+"','"+this.mLng+"')";
		mDb.execSQL(insertSql);
	}
	/**
	 * ���������e�[�u������폜
	 */
	private void deleteHistory() {
		// TODO Auto-generated method stub
		String deleteSql = "delete from searchHistory where _id = (select _id from searchHistory order by _id limit 1)";
		mDb.execSQL(deleteSql);
	}
	/**
	 * �����������猏�����擾
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
	 * ���������e�[�u�����猏�����擾
	 * @return
	 */
	private Cursor historyCountSql() {
		String sqlstr = "select count(*) from searchHistory";
		return mDb.rawQuery(sqlstr, null);
	}
	
}
