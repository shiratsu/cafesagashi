package jp.co.teamenjoy.cafesagashi.free;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;



import jp.co.teamenjoy.cafesagashi.free.R;

import jp.co.teamenjoy.cafesagashi.free.helper.CafeWriteDataHelper;

import jp.co.teamenjoy.cafesagashi.free.util.RequestRunnableMaster;
import jp.co.teamenjoy.cafesagashi.free.util.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MasterDataPageActivity extends Activity{
	
	private ProgressDialog mDialog;
	private SQLiteDatabase mDb;
    // �e�[�u���̖��O
    static final String TABLE = "cafeMaster";
    private CafeWriteDataHelper cdh;
    private String cafeApiUrl = "http://teamenjoy-cafe.appspot.com/cafemap/apiFeedUpdate/?";
	private List<HashMap> resultList;
	private TextView updateText;
	private String lastUpdateStr;
	private Button topButton;
	private final String siteId = "2495";
	private final String locationId = "2862";

	
	 /*
     * DB�ւ̃A�N�Z�X����������ƌĂяo�����
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String updateTime = (String) msg.obj;
            if (mDialog != null) {
                mDialog.dismiss();
            }
            if(updateTime != null){
            	if(!"0".equals(updateTime)){
	            	updateText.setText(updateTime);
	            	Toast.makeText(MasterDataPageActivity.this,
	                        "�f�[�^���X�V���܂���", Toast.LENGTH_SHORT)
	                        .show();
            	}else{
            		Toast.makeText(MasterDataPageActivity.this,
	                        "�X�V���͂���܂���ł���", Toast.LENGTH_SHORT)
	                        .show();
            	}
            }else{
            	Toast.makeText(MasterDataPageActivity.this,
                        "�X�V�ł��܂���ł���", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.master_data);
		
		Button sButton = (Button) this.findViewById(R.id.dataUpdateButton);
		TextView setsumeiText = (TextView)this.findViewById(R.id.setsumeiText);
		updateText = (TextView) findViewById(R.id.updateText);
		setsumeiText.setText("\n�J�t�F�������ł́A�������X�s�[�f�B�ɍs���Ă����������߂ɃJ�t�F����" +
				"�A�v���P�[�V���������ɕۊǂ��Ă��܂��B���̂��ߕs����ɍX�V�����J�t�F����" +
				"�����g�ŃA�b�v�f�[�g���Ă��������K�v������܂��B" +
				"" +
				"�ȉ��́u�A�b�v�f�[�g���s���v�{�^����" +
				"�����ď����X�V���Ă��������B" +
				"" +
				"�X�V���s��Ȃ��ꍇ��" +
				"�œK�ȏ��łȂ��ꍇ������܂��B\n" +
				"��3/11�ɔ������������{��k�Ђ̉e���ŁA���k�n���𒆐S�ɁA�c�Ǝ��Ԃ�����ƈقȂ�X�܂�����\��������܂��B" +
				"���k�E�֓��G���A�̓X�܂������p�����ꍇ�͕K�����O�m�F�����肢���܂��B\n\n");
		
		//�}�X�^�[�f�[�^�X�V
		sButton.setOnClickListener(new OnClickListener(){  
  
            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//�}�X�^�[�A�b�v�f�[�g
				masterUpdate();
				
				
			}

        });
		
		//DB��������
		initDb();
		
		//�ŏI�X�V�������擾
		feedLastUpdate();
		
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	

	/**
	 * �ŏI�X�V�������擾
	 */
	private void feedLastUpdate() {
		// TODO Auto-generated method stub
		// ���ׂẴf�[�^�̃J�[�\�����擾
        Cursor c = fetchLastUpdateCursor();
        fetchLastUpdateDate(c);
        c.close();
        setLastUpdate();
	}

	/**
	 * �ŏI�X�V��������ʂɃZ�b�g
	 */
	private void setLastUpdate() {
		// TODO Auto-generated method stub
		String timeText = StringUtils.feedJapanTimeText(lastUpdateStr);
		updateText.setText(timeText);
		return;
		
	}

	/**
	 * �ŏI�X�V�������擾
	 */
	private void fetchLastUpdateDate(Cursor c) {
		// TODO Auto-generated method stub
		if(c.moveToFirst()){
			do{
				lastUpdateStr = c.getString(c.getColumnIndex("updateTime"));
				
			}while(c.moveToNext());
		}
		return;
	}

	/**
	 * �J�[�\�����擾
	 * @return
	 */
	private Cursor fetchLastUpdateCursor() {
		String sqlstr = "select * from updateCheck";
		return mDb.rawQuery(sqlstr, null);
	}

	/**
	 * �f�[�^�x�[�X������������
	 */
	private void initDb() {
		// TODO Auto-generated method stub
		//�s���{���̃��X�g���Z�b�g
        // �f�[�^�x�[�X���������ݗp�ɃI�[�v������
        cdh = 
               new CafeWriteDataHelper(this);
      
        try {
    	 
        	cdh.createEmptyDataBase();  
        	mDb = cdh.openDataBase();
        } catch (SQLiteException e) {
            // �f�B�X�N�t���ȂǂŃf�[�^���������߂Ȃ��ꍇ
        	
            mDb = cdh.getWritableDatabase();
        } catch (IOException e) {
        	throw new Error("Unable to create database");
        }
	}
	@Override
    protected void onRestart() {
		super.onRestart();
		
		
		
	}	

	/**
	 * �}�X�^�[�f�[�^�X�V
	 */
	private void masterUpdate() {
		// �^�X�N���N������
		/*
		MasterUpdateTask task = new MasterUpdateTask(this);
		task.execute(cafeApiUrl);
		*/
		if (mDialog == null || !mDialog.isShowing()) {
			mDialog = new ProgressDialog(this);
			mDialog.setMessage("�f�[�^���X�V��...���Ԃ�������܂��B");
			mDialog.setIndeterminate(true);
			mDialog.show();
		}
		new Thread(new RequestRunnableMaster(this, cafeApiUrl, mHandler,cdh,mDb)).start();
        return;
	}

	@Override
	protected void onPause() {
		super.onPause();
		
	}
	@Override
    public void onDestroy() {
    	
		super.onDestroy();
		
    }

	

}
