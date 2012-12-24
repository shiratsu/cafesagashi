package jp.co.teamenjoy.cafesagashi.free;


import jp.co.teamenjoy.cafesagashi.free.R;

import jp.co.teamenjoy.cafesagashi.free.util.TweetTask;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.view.View;
import android.view.Window;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TwitPageActivity extends Activity{
	
	
	
    private EditText tweetContent;
    private TextView tweetCaption;
    
    private Button btnBack;
    private Button sendTweetButton;
    private String tweetFlag;
	private String tweetId;
	private String password;
	private String tweetDefaultStr;
	private Button topButton;
	private final String siteId = "2495";
	private final String locationId = "2862";

	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.twitter);
		
		
		sendTweetButton = (Button) findViewById(R.id.sendTweetButton);
		tweetContent = (EditText) this.findViewById(R.id.tweetContent);
		tweetCaption = (TextView) this.findViewById(R.id.tweetCaption);
		
		Bundle bunble = tweetContent.getInputExtras(true);
		if ( bunble != null ) bunble.putBoolean("allowEmoji",true);
		
		tweetCaption.setText("つぶやきを書いて「投稿」ボタンを押してください。" +
				"あなたのつぶやきと店舗情報がツイートされます。" +
				"例）「ここの珈琲は最高！」「まったりしてるなう」");
		
		
		//トップへボタン
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(TwitPageActivity.this,TopPageActivity.class);
		        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		        startActivity(intent);
            }

         });
		
		//つぶやくボタン
		sendTweetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	//つぶやきを送信する
            	sendTweet();
            }

         });
		
		//初期値をセット
		setDefault();
		
		
		
		
	}

	
	
	

	
	/**
	 * つぶやく
	 */
	protected void sendTweet() {
		// TODO Auto-generated method stub
		String content = tweetContent.getText().toString();
		
		String hoge="";
		for( byte ans : content.getBytes() ) hoge = hoge + ans + ":";
		System.out.println(hoge);
		
		
		String tweetCompStr = content+" "+tweetDefaultStr;
		TweetTask tt = new TweetTask(this);
		tt.execute(tweetId,password,tweetCompStr);
		return;
		
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
		
		//遷移元から都道府県コードを受け取る
        Bundle extras = getIntent().getExtras();
        
        if(extras != null){
        	tweetDefaultStr = extras.getString("tweetDefaultStr");
        }
		
		//こいつがセットされてなければ終了
		if(!"1".equals(tweetFlag) || tweetId == null || password == null){
			Toast.makeText(TwitPageActivity.this,
                    "ツイートできません", Toast.LENGTH_SHORT)
                    .show();
			finish();
		}
		return;
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
