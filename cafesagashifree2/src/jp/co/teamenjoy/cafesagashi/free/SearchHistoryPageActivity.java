package jp.co.teamenjoy.cafesagashi.free;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import jp.co.teamenjoy.cafesagashi.free.R;
import jp.co.teamenjoy.cafesagashi.free.adapter.HistoryDataAdapter;
import jp.co.teamenjoy.cafesagashi.free.adapter.PrefDataAdapter;
import jp.co.teamenjoy.cafesagashi.free.helper.HistoryDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.PrefDataHelper;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import android.view.View;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SearchHistoryPageActivity extends ListActivity{
	
	private List<HashMap> pList = new ArrayList<HashMap>();
	private SQLiteDatabase mDb;
	private HistoryDataHelper hdh;
	// �e�[�u���̖��O
    static final String TABLE = "searchHistory";
	static final String TAG = "HistoryDataHelper";
	
	private String areaCode;
	private Button topButton;
	private ListView listView;
	
	private final String siteId = "2495";
	private final String locationId = "2862";

	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.history);
        
        
        
        listView = (ListView) findViewById(android.R.id.list);
        
        //�s���{���̃��X�g���Z�b�g
        // �f�[�^�x�[�X���������ݗp�ɃI�[�v������
        hdh = 
               new HistoryDataHelper(this);
      
        try {
    	 
           hdh.createEmptyDataBase();  
           mDb = hdh.openDataBase();
        } catch (SQLiteException e) {
            // �f�B�X�N�t���ȂǂŃf�[�^���������߂Ȃ��ꍇ
        	
            mDb = hdh.getReadableDatabase();
        } catch (IOException e) {
        	throw new Error("Unable to create database");
        }
        
        
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	
    public void onDestroy() {
        super.onDestroy();
        hdh.close();
        
       
    }
    @Override
	protected void onPause() {
		super.onPause();
		
	}
	
    
    @Override
    public void onStart(){
    	super.onStart();
    	pList = new ArrayList<HashMap>();
    	//�s���{���ꗗ���擾
        // ���ׂẴf�[�^�̃J�[�\�����擾
        Cursor c = fetchAllHistory();
        pList = setHistoryList(c);
        c.close();
        //ListView�ɃZ�b�g
        HistoryDataAdapter hda = new HistoryDataAdapter(this, R.layout.history_row, pList);
        setListAdapter(hda);
        
        
		
		//ListView�̊e���ڑI���̏������Z�b�g
		 // ���X�g�r���[�̃A�C�e�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	
            	String latStr = (String) pList.get(position).get("latStr");
            	String lonStr = (String) pList.get(position).get("lonStr");
            	String address = (String) pList.get(position).get("address");
            	
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(SearchHistoryPageActivity.this,MapPageActivity.class);
		        intent.putExtra("stationLat", latStr);
		        intent.putExtra("stationLon", lonStr);
		        intent.putExtra("stationName", address);
		        intent.putExtra("notSaveFlag", true);
		        startActivity(intent);
                
            }
        });
        
        
    	
    }

    //����������S���擾
	private Cursor fetchAllHistory() {
		String sqlstr = "select * from searchHistory";
		return mDb.rawQuery(sqlstr, null);
	}

	//�s���{���ꗗ���Z�b�g
	private List<HashMap> setHistoryList(Cursor c) {
		if(c.moveToFirst()){
			do{
				HashMap<String,String> historyData = new HashMap<String,String>();
				String id = c.getString(c.getColumnIndex("_id"));
				String address = c.getString(c.getColumnIndex("address"));
				String latStr = c.getString(c.getColumnIndex("lat"));
				String lonStr = c.getString(c.getColumnIndex("lon"));
				
				historyData.put("id",id);
				historyData.put("address",address);
				historyData.put("latStr",latStr);
				historyData.put("lonStr",lonStr);
				
				
				pList.add(historyData);
				
			}while(c.moveToNext());
		}
		return pList;
	}
	
}