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
import android.widget.TextView;
import android.widget.Toast;
import jp.co.teamenjoy.cafesagashi.free.R;

public class MasterUpdateTask extends AsyncTask<Object, Integer, Integer> {

	private MasterDataPageActivity mActivity;
	private ProgressDialog mProgressDialog;
	
	private CafeWriteDataHelper cdh;
	private List<Item> resultList = new ArrayList<Item>();
	private SQLiteDatabase mDb;
	public HashMap xmlData;
	private int count = 0;
	private String countStr = null;
	private TextView updateText;
	
	// �R���X�g���N�^
	public MasterUpdateTask(MasterDataPageActivity activity) {
		mActivity = activity;
		updateText = (TextView) mActivity.findViewById(R.id.updateText);
		
		// �f�[�^�x�[�X���������ݗp�ɃI�[�v������
        cdh = new CafeWriteDataHelper(mActivity);
        
        try {
       	 
        	cdh.createEmptyDataBase();  
            mDb = cdh.openDataBase();
         } catch (SQLiteException e) {
             // �f�B�X�N�t���ȂǂŃf�[�^���������߂Ȃ��ꍇ
        	 onStopExecute();
             mDb = cdh.getReadableDatabase();
             // DB�������߂Ȃ��̂ŁA���b�Z�[�W��\�����ďI���
             Looper.prepare();
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
        // �o�b�N�O���E���h�̏����O��UI�X���b�h�Ń_�C�A���O�\��
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setMessage("�f�[�^���X�V��...���Ԃ�������܂��B");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.show();
    }
	
	@Override
	protected Integer doInBackground(Object... params) {
		// TODO Auto-generated method stu
		String location = (String) params[0];
		
		try {
			//�ŏI�X�V���t���擾
	        String dbDate = feedLastUpdate();
	         
	        //HTTP�f�[�^�ʐM�����āA�X�V�f�[�^���擾
	        String query = "search=1&dbDate="+URLEncoder.encode(dbDate);
			
			
			// HTTP�o�R�ŃA�N�Z�X���AInputStream���擾����
			URL url = new URL(location+query);
			InputStream is = url.openConnection().getInputStream();
			parseXml(is);
			
			count = resultList.size();
			
			//DB�Ƀf�[�^����荞��
			updateDatabase();
			
			//�ŏI�X�V���t���擾
	        dbDate = feedLastUpdate();
	        String timeText = StringUtils.feedJapanTimeText(dbDate);
	        updateText.setText(timeText);
			
		} catch (Exception e) {
			onStopExecute();
       	 	Looper.prepare();
			Toast.makeText(mActivity,
                   e.toString(), Toast.LENGTH_SHORT)
                   .show();
            Looper.loop();
            return 0;
		}
		/*
		long timeMillisEnd = System.currentTimeMillis(); 
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updateTime = sdf1.format(new Date(timeMillisEnd));
		*/
		return Integer.valueOf(count);
	}
	
	/**
	 * �f�[�^�x�[�X���X�V
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
						"			storeName = '"+storeName+"'" +
								"	,storeMainName = '"+storeMainName+"'" +
								"	,storeSubName = '"+storeSubName+"'" +
								"	,storeAddress = '"+storeAddress+"'" +
								"	,phoneNumber = '"+phoneNumber+"'" +
								"	,zipCode = '"+zipCode+"'" +
								"	,lat = '"+lat+"'" +
								"	,lon = '"+lon+"'" +
								"	,storeCaption = '"+storeCaption+"'" +
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
	 * �������擾
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
	 * �J�E���g�̃J�[�\�����擾
	 * @param tmpItem
	 * @return
	 */
	private Cursor fetchCafeMasterCount(Item tmpItem) {
		// TODO Auto-generated method stub
		String cafeId = tmpItem.getKey();
		
		String sqlstr = "SELECT count(*) FROM cafeMaster where cafeId = '"+cafeId+"'";
		return mDb.rawQuery(sqlstr, null);
		
	}

	// XML���p�[�X����
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
	 * �ŏI�X�V�����擾
	 * @return
	 */
	private String feedLastUpdate() {
		// TODO Auto-generated method stub
		// ���ׂẴf�[�^�̃J�[�\�����擾
        Cursor c = fetchLastUpdateCursor();
        String dbDate = setDbDate(c);
        c.close();
		return dbDate;
	}

	/**
	 * �ŏI�X�V�����擾
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
	 * �ŏI�X�V�����擾����J�[�\�����쐬
	 * @return
	 */
	private Cursor fetchLastUpdateCursor() {
		// TODO Auto-generated method stub
		String sqlstr = "SELECT * FROM updateCheck";
		return mDb.rawQuery(sqlstr, null);
	}  

	// ���C���X���b�h��Ŏ��s�����
	protected void onStopExecute() {
		mProgressDialog.dismiss();
		/*
		Looper.prepare();
		Toast.makeText(mActivity,
                "�f�[�^���X�V�ł��܂���ł���", Toast.LENGTH_SHORT)
                .show();
		Looper.loop();
        */        
	}

	// ���C���X���b�h��Ŏ��s�����
	@Override
	protected void onPostExecute(Integer count) {
		mProgressDialog.dismiss();
		if(count > 0){
			Toast.makeText(mActivity,
                "�f�[�^���X�V���܂���", Toast.LENGTH_SHORT)
                .show();
		}else{
			Toast.makeText(mActivity,
	                "�X�V���͂���܂���ł���", Toast.LENGTH_SHORT)
	                .show();
		}
		
	}
	
	
}
