package jp.co.teamenjoy.cafesagashi.free.util;




import android.database.Cursor;
import android.os.Handler;
import android.os.Message;


public class RequestRunnableFav implements Runnable {

	private FavFinder mFavFinder;
	private Handler mHandler;
	
	
	public RequestRunnableFav(FavFinder finder, Handler handler) {
        // 駅検索オブジェクト
        this.mFavFinder = finder;

        // WebAPIへのアクセスが終わったことを知らせるためのハンドラ
        this.mHandler = handler;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		mFavFinder.feedCafe();
		
		// ハンドラに通知
        Message msg = new Message();
        msg.obj = "complete";
        mHandler.sendMessage(msg);
			
		
	}

}
