package jp.co.teamenjoy.cafesagashi.free;


import jp.co.teamenjoy.cafesagashi.free.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TopPageActivity extends Activity{
	
	
	private final String siteId = "2495";
	private final String locationId = "2862";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.top);
		
		Button ssButton = (Button) this.findViewById(R.id.stationSearchButton);
		Button lsButton = (Button) this.findViewById(R.id.lastSearchButton);
		Button helpButton = (Button) this.findViewById(R.id.helpButton);
		Button twitButton = (Button) this.findViewById(R.id.twitPrefButton);
		Button masterUpdateButton = (Button) this.findViewById(R.id.masterUpdateButton);
		Button historySearchButton = (Button) this.findViewById(R.id.historySearchButton);
		
		//検索設定のデフォルト値をセット
		setSearchPrefDefault();
		
		//駅検索
		ssButton.setOnClickListener(new OnClickListener(){  
  
            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// インテントへのインスタンス生成  
		        Intent intent = new Intent(TopPageActivity.this,AreaPageActivity.class);
		        startActivity(intent);
				
			}  
              
        });
		
		//最終検索地点を検索
		lsButton.setOnClickListener(new OnClickListener(){  
  
            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
				int lat = pref.getInt("save_lat", 0);
				int lon = pref.getInt("save_lon", 0);
				if(lat != 0 && lon != 0){
				
					// インテントへのインスタンス生成  
					Intent intent = new Intent(TopPageActivity.this,MapPageActivity.class);
					startActivity(intent);
				}else{
					// 現在地が取得できない場合はメッセージを表示して終わり
		            Toast.makeText(TopPageActivity.this,
		                    "まだ、一度も検索してません。", Toast.LENGTH_SHORT)
		                    .show();
				}
			}  
              
        });
		//ヘルプ
		helpButton.setOnClickListener(new OnClickListener(){  
  
            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// インテントへのインスタンス生成  
		        Intent intent = new Intent(TopPageActivity.this,HelpPageActivity.class);
		        startActivity(intent);
				
			}  
              
        });
		//マスター更新
		masterUpdateButton.setOnClickListener(new OnClickListener(){  
  
            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// インテントへのインスタンス生成  
		        Intent intent = new Intent(TopPageActivity.this,MasterDataPageActivity.class);
		        startActivity(intent);
				
			}  
              
        });
		
		//ツイート設定ボタン
		twitButton.setOnClickListener(new OnClickListener(){  
  
            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// インテントへのインスタンス生成  
		        Intent intent = new Intent(TopPageActivity.this,TwitPrefPageActivity.class);
		        startActivity(intent);
				
			}  
              
        });
		//検索履歴ボタン
		historySearchButton.setOnClickListener(new OnClickListener(){  
  
            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// インテントへのインスタンス生成  
		        Intent intent = new Intent(TopPageActivity.this,SearchHistoryPageActivity.class);
		        startActivity(intent);
				
			}  
              
        });
		
		
	}

	
	/**
	 * 検索設定のデフォルト値をセット
	 */
	private void setSearchPrefDefault() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		String storeFlag1 = pref.getString("storeFlag1", null);
		String storeFlag2 = pref.getString("storeFlag2", null);
		String storeFlag3 = pref.getString("storeFlag3", null);
		String storeFlag4 = pref.getString("storeFlag4", null);
		String storeFlag5 = pref.getString("storeFlag5", null);
		String storeFlag6 = pref.getString("storeFlag6", null);
		String storeFlag7 = pref.getString("storeFlag7", null);
		String storeFlag8 = pref.getString("storeFlag8", null);
		String storeFlag9 = pref.getString("storeFlag9", null);
		String storeFlagZ = pref.getString("storeFlagZ", null);
		
		if(!"1".equals(storeFlag1) 
		   && !"1".equals(storeFlag2) 
		   && !"1".equals(storeFlag3) 
		   && !"1".equals(storeFlag4) 
		   && !"1".equals(storeFlag5) 
		   && !"1".equals(storeFlag6) 
		   && !"1".equals(storeFlag7) 
		   && !"1".equals(storeFlag8) 
		   && !"1".equals(storeFlag9) 
		   && !"1".equals(storeFlagZ) 
			){
			storeFlag1 = "1";
			storeFlag2 = "1";
			storeFlag3 = "1";
			storeFlag4 = "1";
			storeFlag5 = "1";
			storeFlag6 = "1";
			storeFlag7 = "1";
			storeFlag8 = "1";
			storeFlag9 = "1";
			storeFlagZ = "1";
		}
		
		Editor e = pref.edit();
		e.putString("storeFlag1", storeFlag1);
		e.putString("storeFlag2", storeFlag2);
		e.putString("storeFlag3", storeFlag3);
		e.putString("storeFlag4", storeFlag4);
		e.putString("storeFlag5", storeFlag5);
		e.putString("storeFlag6", storeFlag6);
		e.putString("storeFlag7", storeFlag7);
		e.putString("storeFlag8", storeFlag8);
		e.putString("storeFlag9", storeFlag9);
		e.putString("storeFlagZ", storeFlagZ);
		e.commit();
		
		pref = getSharedPreferences("kodawariCheck",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		e = pref.edit();
		
		
		e.putString("tabako", null);
		e.putString("kinen", null);
		e.putString("koshitsu", null);
		e.putString("wifi", null);
		e.putString("pc", null);
		e.putString("terace", null);
		e.putString("shinya", null);
		e.putString("pet", null);
		e.commit();
	}

	@Override
    protected void onRestart() {
		super.onRestart();
		
		
		
	}	
	@Override
	protected void onResume() {
		super.onResume();
		
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
