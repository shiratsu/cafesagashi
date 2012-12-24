package jp.co.teamenjoy.cafesagashi.free.util;




import android.database.Cursor;
import android.os.Handler;
import android.os.Message;


public class RequestRunnableFav implements Runnable {

	private FavFinder mFavFinder;
	private Handler mHandler;
	
	
	public RequestRunnableFav(FavFinder finder, Handler handler) {
        // �w�����I�u�W�F�N�g
        this.mFavFinder = finder;

        // WebAPI�ւ̃A�N�Z�X���I��������Ƃ�m�点�邽�߂̃n���h��
        this.mHandler = handler;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		mFavFinder.feedCafe();
		
		// �n���h���ɒʒm
        Message msg = new Message();
        msg.obj = "complete";
        mHandler.sendMessage(msg);
			
		
	}

}
