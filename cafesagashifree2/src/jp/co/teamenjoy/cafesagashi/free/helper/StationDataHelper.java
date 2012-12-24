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

	
	//The Android �̃f�t�H���g�ł̃f�[�^�x�[�X�p�X  
    private static String DB_PATH = "/data/data/jp.co.teamenjoy.cafesagashi.free/databases/"; 
	
	// �f�[�^�x�[�X�̃t�@�C����
    static final String DB_NAME_ASSET="lineData.db";
    
    // �f�[�^�x�[�X�̃t�@�C����
    static final String DB_NAME_ZIP="lineData.db.zip";
    
    private static String DB_NAME = "lineData";
    
    // �e�[�u���̖��O
    static final String TABLE = "lineData";
    static final String TAG = "LineDataHelper";
    
    // �f�[�^�x�[�X�̃o�[�W����
    static final int DB_VERSION=1;
    
    private final Context mContext; 
    private SQLiteDatabase mDataBase;
	
	public StationDataHelper(Context context) {
		// �f�[�^�x�[�X�̃t�@�C�����ƃo�[�W�������w��
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	/** 
     * asset �Ɋi�[�����f�[�^�x�[�X���R�s�[���邽�߂̋�̃f�[�^�x�[�X���쐬���� 
     *  
     **/  
    public void createEmptyDataBase() throws IOException{  
        boolean dbExist = checkDataBaseExists();  
  
        if(dbExist){  
            // ���łɃf�[�^�x�[�X�͍쐬����Ă���  
        }else{  
            // ���̃��\�b�h���ĂԂ��ƂŁA��̃f�[�^�x�[�X��  
            // �A�v���̃f�t�H���g�V�X�e���p�X�ɍ����  
            this.getReadableDatabase();  
            try {  
                // asset �Ɋi�[�����f�[�^�x�[�X���R�s�[����  
                //copyDataBaseFromAsset();   
                copyDataBaseFromAssetForZip();
                
            } catch (IOException e) {
            	Log.e("TAG",e.getMessage());
                throw new Error("Error copying database");  
            } 
            
        }  
    }
	
    /**
     * �𓀂��ăR�s�[
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
			// �f�t�H���g�̃f�[�^�x�[�X�p�X�ɍ쐬�������DB  
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

	//�R�s�[
	private void copyDataBaseFromAsset() throws IOException {
		// TODO Auto-generated method stub
		
		// asset ���̃f�[�^�x�[�X�t�@�C���ɃA�N�Z�X  
        InputStream mInput = mContext.getAssets().open(DB_NAME_ASSET); 
        // �f�t�H���g�̃f�[�^�x�[�X�p�X�ɍ쐬�������DB  
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
     // �R�s�[  
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

	//�f�[�^�x�[�X�����邩�m�F
	private boolean checkDataBaseExists() {
		SQLiteDatabase checkDb = null;  
		   
        String dbPath = DB_PATH + DB_NAME;  
        try{  
            checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);  
        }catch(SQLiteException e){  
            // �f�[�^�x�[�X�͂܂����݂��Ă��Ȃ�
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
