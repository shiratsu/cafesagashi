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
	// テーブルの名前
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
        
        
        
        //遷移元から都道府県コードを受け取る
        Bundle extras = getIntent().getExtras();
        
        if(extras != null){
        	prefCode = extras.getString("prefCode");
        }
        
        
        ListView listView = (ListView) findViewById(android.R.id.list);
        //都道府県のリストをセット
        // データベースを書き込み用にオープンする
        LineDataHelper ldh = 
               new LineDataHelper(this);
      
        try {
    	 
           ldh.createEmptyDataBase();  
           mDb = ldh.openDataBase();
        } catch (SQLiteException e) {
            // ディスクフルなどでデータが書き込めない場合
        	
            mDb = ldh.getReadableDatabase();
        } catch (IOException e) {
        	throw new Error("Unable to create database");
        }
        
        //都道府県一覧を取得
        // すべてのデータのカーソルを取得
        Cursor c = fetchAllLine(prefCode);
        lineList = setLineList(c);
        c.close();
        //ListViewにセット
        LineDataAdapter pda = new LineDataAdapter(this, R.layout.line_row, lineList);
        setListAdapter(pda);
        
      //トップへボタン
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(LinePageActivity.this,TopPageActivity.class);
		        startActivity(intent);
            }

         });
		
		//ListViewの各項目選択の処理をセット
		 // リストビューのアイテムがクリックされた時に呼び出されるコールバックリスナーを登録します
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	
            	String lineCode = (String) lineList.get(position).get("lineCode");
                
            	// インテントへのインスタンス生成  
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

	
    //県を全部取得
	private Cursor fetchAllLine(String prefCode) {
		
		String sqlstr = "select distinct line_cd,line_name from lineData where pref_cd = '"+prefCode+"'";
		
		return mDb.rawQuery(sqlstr, null);
	}

	//路線一覧をセット
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

