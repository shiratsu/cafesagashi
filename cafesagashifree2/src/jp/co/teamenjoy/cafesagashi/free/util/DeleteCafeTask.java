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
	// �R���X�g���N�^
	public DeleteCafeTask(CafeDetailPageActivity activity, CafeWriteDataHelper cdh2) {
		mActivity = activity;
		// �f�[�^�x�[�X���������ݗp�ɃI�[�v������
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
        // �o�b�N�O���E���h�̏����O��UI�X���b�h�Ń_�C�A���O�\��
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setMessage("�f�[�^���X�V��...");
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
			//0~9�ȊO�̂��̂Ƀ}�b�`���Ȃ���΂悢
			if(matcher.matches()){
				niceCount = Integer.valueOf(niceString);
			}	
		}
		try {
	        
			// HTTP�o�R�ŃA�N�Z�X���AInputStream���擾����
			URL url = new URL(location+"?"+query);
			InputStream is = url.openConnection().getInputStream();
			parseXml(is);
			
			//�A�b�v�f�[�g
			updateDatabase();
			
			
		} catch (Exception e) {
			onStopExecute();
			Looper.prepare();
       	 	Toast.makeText(mActivity,
                   "�f�[�^�̑��M�Ɏ��s���܂���", Toast.LENGTH_SHORT)
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
	 * �f�[�^�x�[�X���X�V
	 * @throws Exception 
	 */
	private void updateDatabase() throws Exception {
		// TODO Auto-generated method stub
		if(resultList.size() == 1){
			PostItem tmpItem = resultList.get(0);
			String statusCode = tmpItem.getStatusCode();
			//�X�e�[�^�X���Q�O�O�Ȃ�A�T�[�o��������
			if("200".equals(statusCode)){
				String cafeId = tmpItem.getCafeKey();
				String updateSql = "update cafeMaster set " +
								"	iine = "+niceCount+" " +
								"	,updateTime = current_timestamp " +
								" where cafeId = '"+cafeId+"'";
				mDb.execSQL(updateSql);	
					
				
			}else{
				throw new Exception("�f�[�^�̑��M�Ɏ��s���܂���");
			}
		}else{
			throw new Exception("�f�[�^�̑��M�Ɏ��s���܂���");
		}
	}

	// XML���p�[�X����
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

	

	
	

	// ���C���X���b�h��Ŏ��s�����
	protected void onStopExecute() {
		mProgressDialog.dismiss();
	}

	// ���C���X���b�h��Ŏ��s�����
	@Override
	protected void onPostExecute(String cafeId) {
		mProgressDialog.dismiss();
		Toast.makeText(mActivity,
                "�f�[�^���X�V���܂���", Toast.LENGTH_SHORT)
                .show();
	}
}
