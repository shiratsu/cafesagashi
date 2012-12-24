package jp.co.teamenjoy.cafesagashi.free;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import jp.co.teamenjoy.cafesagashi.free.R;
import jp.co.teamenjoy.cafesagashi.free.adapter.LineDataAdapter;

import jp.co.teamenjoy.cafesagashi.free.helper.LineDataHelper;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class LinePageActivity extends ListActivity{

	private List<HashMap> lineList = new ArrayList<HashMap>();
	private SQLiteDatabase mDb;
	private String lineCode = "";
	private String prefCode = "";
	// �e�[�u���̖��O
    static final String TABLE = "lineData";
	static final String TAG = "LineDataHelper";
	private Button topButton;
	private final String siteId = "2495";
	private final String locationId = "2862";

	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.linelist);
        
        
        
        //�J�ڌ�����s���{���R�[�h���󂯎��
        Bundle extras = getIntent().getExtras();
        
        if(extras != null){
        	prefCode = extras.getString("prefCode");
        }
        
        
        ListView listView = (ListView) findViewById(android.R.id.list);
        //�s���{���̃��X�g���Z�b�g
        // �f�[�^�x�[�X���������ݗp�ɃI�[�v������
        LineDataHelper ldh = 
               new LineDataHelper(this);
      
        try {
    	 
           ldh.createEmptyDataBase();  
           mDb = ldh.openDataBase();
        } catch (SQLiteException e) {
            // �f�B�X�N�t���ȂǂŃf�[�^���������߂Ȃ��ꍇ
        	
            mDb = ldh.getReadableDatabase();
        } catch (IOException e) {
        	throw new Error("Unable to create database");
        }
        
        //�s���{���ꗗ���擾
        // ���ׂẴf�[�^�̃J�[�\�����擾
        Cursor c = fetchAllLine(prefCode);
        lineList = setLineList(c);
        c.close();
        //ListView�ɃZ�b�g
        LineDataAdapter pda = new LineDataAdapter(this, R.layout.line_row, lineList);
        setListAdapter(pda);
        
      //�g�b�v�փ{�^��
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(LinePageActivity.this,TopPageActivity.class);
		        startActivity(intent);
            }

         });
		
		//ListView�̊e���ڑI���̏������Z�b�g
		 // ���X�g�r���[�̃A�C�e�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	
            	String lineCode = (String) lineList.get(position).get("lineCode");
                
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(LinePageActivity.this,StationPageActivity.class);
		        intent.putExtra("lineCode", lineCode);
		        intent.putExtra("prefCode", prefCode);
		        startActivity(intent);
            }
        });
        ldh.close();
       
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	
    //����S���擾
	private Cursor fetchAllLine(String prefCode) {
		
		String sqlstr = "select distinct line_cd,line_name from lineData where pref_cd = '"+prefCode+"'";
		
		return mDb.rawQuery(sqlstr, null);
	}

	//�H���ꗗ���Z�b�g
	private List<HashMap> setLineList(Cursor c) {
		if(c.moveToFirst()){
			do{
				HashMap<String,String> lineData = new HashMap<String,String>();
				String lineCode = c.getString(c.getColumnIndex("line_cd"));
				String lineName = c.getString(c.getColumnIndex("line_name"));
				lineData.put("lineCode",lineCode);
				lineData.put("lineName",lineName);
				lineList.add(lineData);
				
			}while(c.moveToNext());
		}
		return lineList;
	}

	@Override
    protected void onRestart() {
		super.onRestart();
		
		
		
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

