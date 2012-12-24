package jp.co.teamenjoy.cafesagashi.free.util;




import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;


public class RequestRunnableList implements Runnable {

	private CafeFinder mCafeFinder;
	private Handler mHandler;
	
	
	public RequestRunnableList(CafeFinder finder, Handler handler) {
        // 駅検索オブジェクト
        this.mCafeFinder = finder;

        // WebAPIへのアクセスが終わったことを知らせるためのハンドラ
        this.mHandler = handler;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Message msg = new Message();
		try {
			mCafeFinder.feedCafe();
			// ハンドラに通知
	        msg.obj = mCafeFinder.getGPoint();
	        mHandler.sendMessage(msg);

		} catch (XmlPullParserException e) {
			 msg.obj = null;
		     mHandler.sendMessage(msg);    
	        
		} catch (IOException e) {
			 msg.obj = null;
		     mHandler.sendMessage(msg);
		} catch (Exception e) {
			 msg.obj = null;
		     mHandler.sendMessage(msg);
		}
			
		
	}

}
