package jp.co.teamenjoy.cafesagashi.free.util;




import android.database.Cursor;
import android.os.Handler;
import android.os.Message;


public class RequestRunnableDetail implements Runnable {

	private CafeDetailFinder mCafeDetailFinder;
	private Handler mHandler;
	
	
	public RequestRunnableDetail(CafeDetailFinder finder, Handler handler) {
        // �w�����I�u�W�F�N�g
        this.mCafeDetailFinder = finder;

        // WebAPI�ւ̃A�N�Z�X���I��������Ƃ�m�点�邽�߂̃n���h��
        this.mHandler = handler;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		mCafeDetailFinder.feedCafe();
		
		// �n���h���ɒʒm
        Message msg = new Message();
        msg.obj = mCafeDetailFinder.getGPoint();
        mHandler.sendMessage(msg);
			
		
	}

}
