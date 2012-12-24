package jp.co.teamenjoy.cafesagashi.free;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import jp.co.teamenjoy.cafesagashi.free.R;
import jp.co.teamenjoy.cafesagashi.free.adapter.PrefDataAdapter;
import jp.co.teamenjoy.cafesagashi.free.helper.PrefDataHelper;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


public class PrefPageActivity extends ListActivity{
	
	private List<HashMap> pList = new ArrayList<HashMap>();
	private SQLiteDatabase mDb;
	private PrefDataHelper pdh;
	// �e�[�u���̖��O
    static final String TABLE = "prefData";
	static final String TAG = "PrefDataHelper";
	private Button topButton;
	private String areaCode;
	
	private final String siteId = "2495";
	private final String locationId = "2862";

	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        
		
        //�J�ڌ�����s���{���R�[�h���󂯎��
        Bundle extras = getIntent().getExtras();
        
        if(extras != null){
        	areaCode = extras.getString("areaCode");
        }
        
        
        ListView listView = (ListView) findViewById(android.R.id.list);
        
        //�s���{���̃��X�g���Z�b�g
        // �f�[�^�x�[�X���������ݗp�ɃI�[�v������
        pdh = 
               new PrefDataHelper(this);
      
        try {
    	 
           pdh.createEmptyDataBase();  
           mDb = pdh.openDataBase();
        } catch (SQLiteException e) {
            // �f�B�X�N�t���ȂǂŃf�[�^���������߂Ȃ��ꍇ
        	
            mDb = pdh.getReadableDatabase();
        } catch (IOException e) {
        	throw new Error("Unable to create database");
        }
        
        //�s���{���ꗗ���擾
        // ���ׂẴf�[�^�̃J�[�\�����擾
        Cursor c = fetchAllPref();
        pList = setPrefList(c);
        c.close();
        //ListView�ɃZ�b�g
        PrefDataAdapter pda = new PrefDataAdapter(this, R.layout.row, pList);
        setListAdapter(pda);
        
        
		
		//ListView�̊e���ڑI���̏������Z�b�g
		 // ���X�g�r���[�̃A�C�e�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	
            	String prefCode = (String) pList.get(position).get("prefCode");
            	
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(PrefPageActivity.this,LinePageActivity.class);
		        intent.putExtra("prefCode", prefCode);
		        startActivity(intent);
                
            }
        });
        
        pdh.close();

        
	}
    @Override
    protected void onRestart() {
		super.onRestart();
		
		
		
	}	

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	
    public void onDestroy() {
        super.onDestroy();
        pdh.close();
        
    }
      

    //����S���擾
	private Cursor fetchAllPref() {
		String sqlstr = "select distinct prefCode,prefName from prefData " +
					"where areaCode = '"+areaCode+"'";
		return mDb.rawQuery(sqlstr, null);
	}

	//�s���{���ꗗ���Z�b�g
	private List<HashMap> setPrefList(Cursor c) {
		if(c.moveToFirst()){
			do{
				HashMap<String,String> prefData = new HashMap<String,String>();
				String prefCode = c.getString(c.getColumnIndex("prefCode"));
				String prefName = c.getString(c.getColumnIndex("prefName"));
				prefData.put("prefCode",prefCode);
				prefData.put("prefName",prefName);
				pList.add(prefData);
				
			}while(c.moveToNext());
		}
		return pList;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
	}
	

}