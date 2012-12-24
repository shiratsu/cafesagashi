package jp.co.teamenjoy.cafesagashi.free;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import jp.co.teamenjoy.cafesagashi.free.R;

import jp.co.teamenjoy.cafesagashi.free.adapter.StationDataAdapter;
import jp.co.teamenjoy.cafesagashi.free.helper.LineDataHelper;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class StationPageActivity extends ListActivity{

	private List<HashMap> stationList = new ArrayList<HashMap>();
	private SQLiteDatabase mDb;
	private String lineCode = "";
	private String prefCode = "";
	// テーブルの名前
    static final String TABLE = "lineData";
	static final String TAG = "StationDataHelper";
	private LineDataHelper ldh;
	private Button topButton;
	
	private final String siteId = "2495";
	private final String locationId = "2862";

	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.stationlist);
        //遷移元から都道府県コードを受け取る
        Bundle extras = getIntent().getExtras();
        
        if(extras != null){
        	lineCode = extras.getString("lineCode");
        	prefCode = extras.getString("prefCode");
        }
        
        ListView listView = (ListView) findViewById(android.R.id.list);
        
        
        
        //都道府県のリストをセット
        // データベースを書き込み用にオープンする
        ldh = new LineDataHelper(this);
      
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
        Cursor c = fetchAllStation(lineCode,prefCode);
        stationList = setStationList(c);
        c.close();
        //ListViewにセット
        StationDataAdapter pda = new StationDataAdapter(this, R.layout.station_row, stationList);
        setListAdapter(pda);
        
        //トップへボタン
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(StationPageActivity.this,TopPageActivity.class);
		        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		        startActivity(intent);
            }

         });
        
		
		//ListViewの各項目選択の処理をセット
		 // リストビューのアイテムがクリックされた時に呼び出されるコールバックリスナーを登録します
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	
            	String stationName = (String) stationList.get(position).get("stationName");
            	String stationCode = (String) stationList.get(position).get("stationCode");
            	String stationLat = (String) stationList.get(position).get("lat");
            	String stationLon = (String) stationList.get(position).get("lon");
                
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(StationPageActivity.this,MapPageActivity.class);
		        intent.putExtra("stationCode", stationCode);
		        intent.putExtra("stationName", stationName+"駅");
		        intent.putExtra("stationLat", stationLat);
		        intent.putExtra("stationLon", stationLon);
		        intent.putExtra("notSaveFlag", false);
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
	private Cursor fetchAllStation(String lineCode, String prefCode2) {
		
		String sqlstr = "select distinct station_cd,station_name,lat,lon from lineData where line_cd = '"+lineCode+"' and pref_cd = '"+prefCode2+"'";
		
		return mDb.rawQuery(sqlstr, null);
	}

	//路線一覧をセット
	private List<HashMap> setStationList(Cursor c) {
		if(c.moveToFirst()){
			do{
				HashMap<String,String> stationData = new HashMap<String,String>();
				String stationCode = c.getString(c.getColumnIndex("station_cd"));
				String stationName = c.getString(c.getColumnIndex("station_name"));
				String lat = c.getString(c.getColumnIndex("lat"));
				String lon = c.getString(c.getColumnIndex("lon"));
				stationData.put("stationCode",stationCode);
				stationData.put("stationName",stationName);
				stationData.put("lat",lat);
				stationData.put("lon",lon);
				stationList.add(stationData);
				
			}while(c.moveToNext());
		}
		return stationList;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
	}
	@Override
    public void onDestroy() {
    	
		super.onDestroy();
		ldh.close();
		
    }

	
	
	@Override
    protected void onRestart() {
		super.onRestart();
		
		
		
	}	
	
}
