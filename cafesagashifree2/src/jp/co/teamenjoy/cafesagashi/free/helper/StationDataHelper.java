package jp.co.teamenjoy.cafesagashi.free.helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class StationDataHelper extends SQLiteOpenHelper{

	
	//The Android のデフォルトでのデータベースパス  
    private static String DB_PATH = "/data/data/jp.co.teamenjoy.cafesagashi.free/databases/"; 
	
	// データベースのファイル名
    static final String DB_NAME_ASSET="lineData.db";
    
    // データベースのファイル名
    static final String DB_NAME_ZIP="lineData.db.zip";
    
    private static String DB_NAME = "lineData";
    
    // テーブルの名前
    static final String TABLE = "lineData";
    static final String TAG = "LineDataHelper";
    
    // データベースのバージョン
    static final int DB_VERSION=1;
    
    private final Context mContext; 
    private SQLiteDatabase mDataBase;
	
	public StationDataHelper(Context context) {
		// データベースのファイル名とバージョンを指定
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	/** 
     * asset に格納したデータベースをコピーするための空のデータベースを作成する 
     *  
     **/  
    public void createEmptyDataBase() throws IOException{  
        boolean dbExist = checkDataBaseExists();  
  
        if(dbExist){  
            // すでにデータベースは作成されている  
        }else{  
            // このメソッドを呼ぶことで、空のデータベースが  
            // アプリのデフォルトシステムパスに作られる  
            this.getReadableDatabase();  
            try {  
                // asset に格納したデータベースをコピーする  
                //copyDataBaseFromAsset();   
                copyDataBaseFromAssetForZip();
                
            } catch (IOException e) {
            	Log.e("TAG",e.getMessage());
                throw new Error("Error copying database");  
            } 
            
        }  
    }
	
    /**
     * 解凍してコピー
     */
	private void copyDataBaseFromAssetForZip() throws IOException {
		// TODO Auto-generated method stub
		AssetManager assetManager = this.mContext.getAssets();			
		InputStream inputStream = assetManager.open(DB_NAME_ZIP, AssetManager.ACCESS_STREAMING);

		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		while (zipEntry != null) { 
			String entryName = zipEntry.getName();
			int n;
			FileOutputStream fileOutputStream;
			// デフォルトのデータベースパスに作成した空のDB  
	        String outFileName = DB_PATH + DB_NAME;
			fileOutputStream = new FileOutputStream(outFileName); 
			byte[] buf = new byte[1024];
			while ((n = zipInputStream.read(buf, 0, 1024)) > -1) {
				fileOutputStream.write(buf, 0, n);
			}

			fileOutputStream.close(); 
			zipInputStream.closeEntry();
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
		return;
	}

	//コピー
	private void copyDataBaseFromAsset() throws IOException {
		// TODO Auto-generated method stub
		
		// asset 内のデータベースファイルにアクセス  
        InputStream mInput = mContext.getAssets().open(DB_NAME_ASSET); 
        // デフォルトのデータベースパスに作成した空のDB  
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
     // コピー  
        byte[] buffer = new byte[1024];  
        int size;  
        while ((size = mInput.read(buffer)) > 0){
            mOutput.write(buffer, 0, size);  
        }
        //Close the streams  
        mOutput.flush();  
        mOutput.close();  
        mInput.close();
	}

	//データベースがあるか確認
	private boolean checkDataBaseExists() {
		SQLiteDatabase checkDb = null;  
		   
        String dbPath = DB_PATH + DB_NAME;  
        try{  
            checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);  
        }catch(SQLiteException e){  
            // データベースはまだ存在していない
        	Log.e(TAG,"ErrorOutput");
            Log.e(TAG,e.toString());
            System.out.println(e.toString());
        }  
   
        if(checkDb != null){  
            checkDb.close();  
        }  
        return checkDb != null ? true : false;
	}

	public SQLiteDatabase openDataBase() throws SQLException{  
        //Open the database  
        String myPath = DB_PATH + DB_NAME;  
        mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);  
        return mDataBase;  
    } 
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		Log.w(TAG,
                "Version mismatch :" + oldVersion + 
                " to " + newVersion );
		
	}

}
