package jp.co.teamenjoy.cafesagashi.free.util;




import android.database.Cursor;
import android.os.Handler;
import android.os.Message;


public class RequestRunnableDetail implements Runnable {

	private CafeDetailFinder mCafeDetailFinder;
	private Handler mHandler;
	
	
	public RequestRunnableDetail(CafeDetailFinder finder, Handler handler) {
        // 駅検索オブジェクト
        this.mCafeDetailFinder = finder;

        // WebAPIへのアクセスが終わったことを知らせるためのハンドラ
        this.mHandler = handler;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		mCafeDetailFinder.feedCafe();
		
		// ハンドラに通知
        Message msg = new Message();
        msg.obj = mCafeDetailFinder.getGPoint();
        mHandler.sendMessage(msg);
			
		
	}

}
