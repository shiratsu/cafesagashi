package jp.co.teamenjoy.cafesagashi.free.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import jp.co.teamenjoy.cafesagashi.free.AddCafePageActivity;
import jp.co.teamenjoy.cafesagashi.free.CafeDetailPageActivity;
import jp.co.teamenjoy.cafesagashi.free.MasterDataPageActivity;
import jp.co.teamenjoy.cafesagashi.free.TwitPageActivity;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeWriteDataHelper;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Xml;
import android.widget.Toast;

public class TweetTask extends AsyncTask<Object, Integer, String> {

	private TwitPageActivity mActivity;
	private ProgressDialog mProgressDialog;
	
	private String consumer_key = "g7cPNp0xkBaYC9hTSi1bg";
	private String consumer_secret = "jG63f4nxZuH09kTTMeOP3B16mSAxK3JbSJAL6CwtU0";
	
	// コンストラクタ
	public TweetTask(TwitPageActivity activity) {
		mActivity = activity;
		// データベースを書き込み用にオープンする
        
        
	}
	
	@Override
    protected void onPreExecute() {
        // バックグラウンドの処理前にUIスレッドでダイアログ表示
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setMessage("ツイートを送信中...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.show();
    }
	
	@Override
	protected String doInBackground(Object... params) {
		// TODO Auto-generated method stu
		String user = (String) params[0];
		String password = (String) params[1];
		String content = (String) params[2];
		
		ConfigurationBuilder confbuilder =new ConfigurationBuilder();
		confbuilder.setPassword(password);
		confbuilder.setUser(user);
		confbuilder.setOAuthConsumerKey(consumer_key);
		confbuilder.setOAuthConsumerSecret(consumer_secret);

		TwitterFactory twitterfactory = new TwitterFactory(confbuilder.build());
		Twitter twitter = twitterfactory.getInstance();
		
		try {
			AccessToken token = twitter.getOAuthAccessToken(user,password);
			twitter.setOAuthAccessToken(token);
			twitter.updateStatus(content);
			
			
		} catch (Exception e) {
			onStopExecute();
			Looper.prepare();
       	 	Toast.makeText(mActivity,
                   "ツイートの送信に失敗しました\nIDとパスワードを確認してください", Toast.LENGTH_SHORT)
                   .show();
       	 	Looper.loop();
       	 	return null;
		}
		
		long timeMillisEnd = System.currentTimeMillis(); 
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String updateTime = sdf1.format(new Date(timeMillisEnd));
		return updateTime;
	}

	

	

	
	

	// メインスレッド上で実行される
	protected void onStopExecute() {
		mProgressDialog.dismiss();
	}

	// メインスレッド上で実行される
	@Override
	protected void onPostExecute(String cafeId) {
		mProgressDialog.dismiss();
		Toast.makeText(mActivity,
                "ツイートを送信しました", Toast.LENGTH_SHORT)
                .show();
	}
}
