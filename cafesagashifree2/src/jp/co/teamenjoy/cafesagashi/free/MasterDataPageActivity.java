package jp.co.teamenjoy.cafesagashi.free;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;



import jp.co.teamenjoy.cafesagashi.free.R;

import jp.co.teamenjoy.cafesagashi.free.helper.CafeWriteDataHelper;

import jp.co.teamenjoy.cafesagashi.free.util.RequestRunnableMaster;
import jp.co.teamenjoy.cafesagashi.free.util.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MasterDataPageActivity extends Activity{
	
	private ProgressDialog mDialog;
	private SQLiteDatabase mDb;
    // テーブルの名前
    static final String TABLE = "cafeMaster";
    private CafeWriteDataHelper cdh;
    private String cafeApiUrl = "http://teamenjoy-cafe.appspot.com/cafemap/apiFeedUpdate/?";
	private List<HashMap> resultList;
	private TextView updateText;
	private String lastUpdateStr;
	private Button topButton;
	private final String siteId = "2495";
	private final String locationId = "2862";

	
	 /*
     * DBへのアクセスが完了すると呼び出される
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String updateTime = (String) msg.obj;
            if (mDialog != null) {
                mDialog.dismiss();
            }
            if(updateTime != null){
            	if(!"0".equals(updateTime)){
	            	updateText.setText(updateTime);
	            	Toast.makeText(MasterDataPageActivity.this,
	                        "データを更新しました", Toast.LENGTH_SHORT)
	                        .show();
            	}else{
            		Toast.makeText(MasterDataPageActivity.this,
	                        "更新情報はありませんでした", Toast.LENGTH_SHORT)
	                        .show();
            	}
            }else{
            	Toast.makeText(MasterDataPageActivity.this,
                        "更新できませんでした", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.master_data);
		
		Button sButton = (Button) this.findViewById(R.id.dataUpdateButton);
		TextView setsumeiText = (TextView)this.findViewById(R.id.setsumeiText);
		updateText = (TextView) findViewById(R.id.updateText);
		setsumeiText.setText("\nカフェさがしでは、検索をスピーディに行っていただくためにカフェ情報を" +
				"アプリケーション内部に保管しています。そのため不定期に更新されるカフェ情報を" +
				"ご自身でアップデートしていただく必要があります。" +
				"" +
				"以下の「アップデートを行う」ボタンを" +
				"押して情報を更新してください。" +
				"" +
				"更新を行わない場合は" +
				"最適な情報でない場合があります。\n" +
				"※3/11に発生した東日本大震災の影響で、東北地方を中心に、営業時間が現状と異なる店舗がある可能性があります。" +
				"東北・関東エリアの店舗をご利用される場合は必ず事前確認をお願いします。\n\n");
		
		//マスターデータ更新
		sButton.setOnClickListener(new OnClickListener(){  
  
            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//マスターアップデート
				masterUpdate();
				
				
			}

        });
		
		//DBを初期化
		initDb();
		
		//最終更新日時を取得
		feedLastUpdate();
		
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	

	/**
	 * 最終更新日時を取得
	 */
	private void feedLastUpdate() {
		// TODO Auto-generated method stub
		// すべてのデータのカーソルを取得
        Cursor c = fetchLastUpdateCursor();
        fetchLastUpdateDate(c);
        c.close();
        setLastUpdate();
	}

	/**
	 * 最終更新日時を画面にセット
	 */
	private void setLastUpdate() {
		// TODO Auto-generated method stub
		String timeText = StringUtils.feedJapanTimeText(lastUpdateStr);
		updateText.setText(timeText);
		return;
		
	}

	/**
	 * 最終更新日時を取得
	 */
	private void fetchLastUpdateDate(Cursor c) {
		// TODO Auto-generated method stub
		if(c.moveToFirst()){
			do{
				lastUpdateStr = c.getString(c.getColumnIndex("updateTime"));
				
			}while(c.moveToNext());
		}
		return;
	}

	/**
	 * カーソルを取得
	 * @return
	 */
	private Cursor fetchLastUpdateCursor() {
		String sqlstr = "select * from updateCheck";
		return mDb.rawQuery(sqlstr, null);
	}

	/**
	 * データベースを初期化する
	 */
	private void initDb() {
		// TODO Auto-generated method stub
		//都道府県のリストをセット
        // データベースを書き込み用にオープンする
        cdh = 
               new CafeWriteDataHelper(this);
      
        try {
    	 
        	cdh.createEmptyDataBase();  
        	mDb = cdh.openDataBase();
        } catch (SQLiteException e) {
            // ディスクフルなどでデータが書き込めない場合
        	
            mDb = cdh.getWritableDatabase();
        } catch (IOException e) {
        	throw new Error("Unable to create database");
        }
	}
	@Override
    protected void onRestart() {
		super.onRestart();
		
		
		
	}	

	/**
	 * マスターデータ更新
	 */
	private void masterUpdate() {
		// タスクを起動する
		/*
		MasterUpdateTask task = new MasterUpdateTask(this);
		task.execute(cafeApiUrl);
		*/
		if (mDialog == null || !mDialog.isShowing()) {
			mDialog = new ProgressDialog(this);
			mDialog.setMessage("データを更新中...時間がかかります。");
			mDialog.setIndeterminate(true);
			mDialog.show();
		}
		new Thread(new RequestRunnableMaster(this, cafeApiUrl, mHandler,cdh,mDb)).start();
        return;
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
