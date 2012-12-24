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
	// テーブルの名前
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
        
        
		
        //遷移元から都道府県コードを受け取る
        Bundle extras = getIntent().getExtras();
        
        if(extras != null){
        	areaCode = extras.getString("areaCode");
        }
        
        
        ListView listView = (ListView) findViewById(android.R.id.list);
        
        //都道府県のリストをセット
        // データベースを書き込み用にオープンする
        pdh = 
               new PrefDataHelper(this);
      
        try {
    	 
           pdh.createEmptyDataBase();  
           mDb = pdh.openDataBase();
        } catch (SQLiteException e) {
            // ディスクフルなどでデータが書き込めない場合
        	
            mDb = pdh.getReadableDatabase();
        } catch (IOException e) {
        	throw new Error("Unable to create database");
        }
        
        //都道府県一覧を取得
        // すべてのデータのカーソルを取得
        Cursor c = fetchAllPref();
        pList = setPrefList(c);
        c.close();
        //ListViewにセット
        PrefDataAdapter pda = new PrefDataAdapter(this, R.layout.row, pList);
        setListAdapter(pda);
        
        
		
		//ListViewの各項目選択の処理をセット
		 // リストビューのアイテムがクリックされた時に呼び出されるコールバックリスナーを登録します
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	
            	String prefCode = (String) pList.get(position).get("prefCode");
            	
            	// インテントへのインスタンス生成  
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
      

    //県を全部取得
	private Cursor fetchAllPref() {
		String sqlstr = "select distinct prefCode,prefName from prefData " +
					"where areaCode = '"+areaCode+"'";
		return mDb.rawQuery(sqlstr, null);
	}

	//都道府県一覧をセット
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