package jp.co.teamenjoy.cafesagashi.free;



import jp.co.teamenjoy.cafesagashi.free.R;


import android.app.Activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.Window;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class TwitPrefPageActivity extends Activity
{
	
	
	private Button topButton;
    private CheckBox twitPref;
    private EditText tweetIdText;
    private EditText passwordText;
    
    private Button btnBack;
    private String tweetFlag;
	private String tweetId;
	private String password;
	
    
    private String postApiUrl = "http://teamenjoy-cafe.appspot.com/cafemap/apiPostCafe3/";
    
    private final String siteId = "2495";
	private final String locationId = "2862";

	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.twit_pref);
		
		twitPref = (CheckBox) findViewById(R.id.TwitPref);
		
		tweetIdText = (EditText) this.findViewById(R.id.twitterIdText);
		passwordText = (EditText) this.findViewById(R.id.password);
		

		
		
		//初期値をセット
		setDefault();
		
		
	}


	/**
	 * 値のセット
	 */
	private void setDefault() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("twitPref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		
		tweetFlag = pref.getString("tweetFlag", null);
		tweetId = pref.getString("tweetId", null);
		password = pref.getString("password", null);
		
		if(tweetFlag != null){
			twitPref.setChecked(true);
		}
		
		if(tweetId != null){
			tweetIdText.setText(tweetId);
		}
		if(password != null){
			passwordText.setText(password);
		}
		return;
	}

	/**
	 * 値をSharedにセット
	 */
	private void setSharedData() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("twitPref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		tweetId = tweetIdText.getText().toString();
		password = passwordText.getText().toString();
		boolean checked = twitPref.isChecked();
		if(checked == true){
			tweetFlag = "1";
		}else{
			tweetFlag = null;
		}
		Editor e = pref.edit();
		e.putString("tweetId", tweetId);
		e.putString("tweetFlag", tweetFlag);
		e.putString("password", password);
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
		setSharedData();
		
    }

	
    



}
