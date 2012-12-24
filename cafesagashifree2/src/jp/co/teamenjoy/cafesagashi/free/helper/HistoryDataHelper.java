package jp.co.teamenjoy.cafesagashi.free.helper;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class HistoryDataHelper extends SQLiteOpenHelper{

	
	//The Android �̃f�t�H���g�ł̃f�[�^�x�[�X�p�X  
    private static String DB_PATH = "/data/data/jp.co.teamenjoy.cafesagashi.free/databases/"; 
	
	// �f�[�^�x�[�X�̃t�@�C����
    static final String DB_NAME_ASSET="searchHistory.db";
    
    private static String DB_NAME = "searchHistory";
    
    // �e�[�u���̖��O
    static final String TABLE = "searchHistory";
    static final String TAG = "HistoryDataHelper";
    
    // �f�[�^�x�[�X�̃o�[�W����
    static final int DB_VERSION=1;
    
    private final Context mContext; 
    private SQLiteDatabase mDataBase;
	
	public HistoryDataHelper(Context context) {
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
                copyDataBaseFromAsset();
            } catch (IOException e) {
            	throw new Error(e.toString());
            	
            } 
            
        }  
    }
	
    
	//�R�s�[
	private void copyDataBaseFromAsset() throws IOException {
		// TODO Auto-generated method stub
		
		//try{
			
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
		/*
		}catch(Exception e){
			Log.e("TAG",e.getMessage());
			e.printStackTrace(); 
		}*/
	}

	//�f�[�^�x�[�X�����邩�m�F
	private boolean checkDataBaseExists() {
		SQLiteDatabase checkDb = null;  
		   
        String dbPath = DB_PATH + DB_NAME;  
        try{  
            checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);  
        }catch(SQLiteException e){  
            // �f�[�^�x�[�X�͂܂����݂��Ă��Ȃ�
        	
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
        mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);  
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
	/** 
     * �t�@�C�����當�����ǂݍ��݂܂��B 
     * @param is 
     * @return �t�@�C���̕����� 
     * @throws IOException 
     */  
    private String readFile(InputStream is) throws IOException{  
        BufferedReader br = null;  
        try {  
            br = new BufferedReader(new InputStreamReader(is,"SJIS"));  
  
            StringBuilder sb = new StringBuilder();      
            String str;        
            while((str = br.readLine()) != null){        
                sb.append(str +"\n");       
            }      
            return sb.toString();  
        } finally {  
            if (br != null) br.close();  
        }  
    }

}