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
        // �w�����I�u�W�F�N�g
        this.mCafeFinder = finder;

        // WebAPI�ւ̃A�N�Z�X���I��������Ƃ�m�点�邽�߂̃n���h��
        this.mHandler = handler;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Message msg = new Message();
		try {
			mCafeFinder.feedCafe();
			// �n���h���ɒʒm
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
