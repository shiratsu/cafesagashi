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
		
		tweetCaption.setText("�Ԃ₫�������āu���e�v�{�^���������Ă��������B" +
				"���Ȃ��̂Ԃ₫�ƓX�܏�񂪃c�C�[�g����܂��B" +
				"��j�u����������͍ō��I�v�u�܂����肵�Ă�Ȃ��v");
		
		
		//�g�b�v�փ{�^��
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(TwitPageActivity.this,TopPageActivity.class);
		        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		        startActivity(intent);
            }

         });
		
		//�Ԃ₭�{�^��
		sendTweetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	//�Ԃ₫�𑗐M����
            	sendTweet();
            }

         });
		
		//�����l���Z�b�g
		setDefault();
		
		
		
		
	}

	
	
	

	
	/**
	 * �Ԃ₭
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
	 * �l�̃Z�b�g
	 */
	private void setDefault() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("twitPref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		
		tweetFlag = pref.getString("tweetFlag", null);
		tweetId = pref.getString("tweetId", null);
		password = pref.getString("password", null);
		
		//�J�ڌ�����s���{���R�[�h���󂯎��
        Bundle extras = getIntent().getExtras();
        
        if(extras != null){
        	tweetDefaultStr = extras.getString("tweetDefaultStr");
        }
		
		//�������Z�b�g����ĂȂ���ΏI��
		if(!"1".equals(tweetFlag) || tweetId == null || password == null){
			Toast.makeText(TwitPageActivity.this,
                    "�c�C�[�g�ł��܂���", Toast.LENGTH_SHORT)
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
