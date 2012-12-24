package jp.co.teamenjoy.cafesagashi.free;

import jp.co.teamenjoy.cafesagashi.free.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Window;


public class SplashPageActivity extends Activity {
	
	
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.splash);
	
		Handler hdl = new Handler();
		hdl.postDelayed(new splashHandler(), 3000);
		/*
		WindowManager wm = (WindowManager)getSystemService(this.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		int width = disp.getWidth();
		int height = disp.getHeight();
		
		//送信失敗という旨のメッセージを表示
		
		Toast.makeText(SplashPageActivity.this,
                "width:"+width+",height:"+height, Toast.LENGTH_SHORT)
                .show();
		*/
	}
	class splashHandler implements Runnable {
		public void run() {
			Intent i = new Intent(getApplication(), TopPageActivity.class);
			startActivity(i);
			SplashPageActivity.this.finish();
		}
	}
}
