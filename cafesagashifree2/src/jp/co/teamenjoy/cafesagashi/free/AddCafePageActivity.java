

package jp.co.teamenjoy.cafesagashi.free;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.maps.GeoPoint;


import jp.co.teamenjoy.cafesagashi.free.R;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeDataHelper;
import jp.co.teamenjoy.cafesagashi.free.helper.CafeWriteDataHelper;
import jp.co.teamenjoy.cafesagashi.free.util.CafeDetailFinder;
import jp.co.teamenjoy.cafesagashi.free.util.MasterUpdateTask;
import jp.co.teamenjoy.cafesagashi.free.util.PostCafeTask;
import jp.co.teamenjoy.cafesagashi.free.util.RequestRunnableDetail;
import jp.co.teamenjoy.cafesagashi.free.util.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddCafePageActivity extends Activity{
	
	private CafeDetailFinder mCafeDetailFinder;// �J�t�F�ڍ�
	private String cafeId = "";
	private ProgressDialog mDialog;
	private List<HashMap> cafeDetail = new ArrayList<HashMap>();
	
	private SQLiteDatabase mDb;
    // �e�[�u���̖��O
    static final String TABLE = "cafeMaster";
    private CafeWriteDataHelper cdh;
	
    private EditText storeSubNameText;
    private EditText addressText;
    private EditText phoneText;
    private EditText captionText;
    private TextView kodawariText;
    private Button topButton;
    private ArrayAdapter<String> adapter;
    private Spinner spinnerCategory;
    
    private String tabako;
	private String kinen;
	private String wifi;
	private String pc;
	private String koshitsu;
	private String terace;
	private String pet;
	private String shinya;
    private String postApiUrl = "http://teamenjoy-cafe.appspot.com/cafemap/apiPostCafe3/";
    private final String siteId = "2495";
	private final String locationId = "2862";

    
    /*
     * Web API�ւ̃A�N�Z�X����������ƌĂяo�����
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            GeoPoint gp = (GeoPoint) msg.obj;
            displayCafeFromCafeId();
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

		
    };
    
    /**
     * �J�t�F�����e�L�X�g�{�b�N�X�ɃZ�b�g
     */
    private void displayCafeFromCafeId() {
		// TODO Auto-generated method stub
    	
    	HashMap cafe = mCafeDetailFinder.getCafe(0);
    	String storeSubName = mCafeDetailFinder.getCafeSubName(cafe);
    	String storeFlag = mCafeDetailFinder.getStoreFlag(cafe);
    	String cafeId = mCafeDetailFinder.getCafeId(cafe);
    	String storeAddress = mCafeDetailFinder.getAddress(cafe);
    	String storeCaption = mCafeDetailFinder.getStoreCaption(cafe);
    	String kodawari = mCafeDetailFinder.getKodawari(cafe);
    	String phoneNumber = mCafeDetailFinder.getPhoneNumber(cafe);
    	
    	if(storeSubName != null){
    		storeSubNameText.setText(storeSubName);
    	}
    	if(storeAddress != null){
    		addressText.setText(storeAddress);
    	}
    	if(phoneNumber != null){
    		phoneText.setText(phoneNumber);
    	}
    	if(storeCaption != null){
    		captionText.setText(storeCaption);
    	}
    	if(kodawari != null){
    		kodawariText.setText(kodawari);
    	}else{
    		kodawariText.setText("�����ݒ肳��Ă܂���");
    	}
    	
    	tabako = mCafeDetailFinder.getTabako(cafe);
    	kinen = mCafeDetailFinder.getKinen(cafe);
    	koshitsu = mCafeDetailFinder.getKoshitsu(cafe);
    	pc = mCafeDetailFinder.getPc(cafe);
    	wifi = mCafeDetailFinder.getWifi(cafe);
    	shinya = mCafeDetailFinder.getShinya(cafe);
    	terace = mCafeDetailFinder.getTerace(cafe);
    	pet = mCafeDetailFinder.getPet(cafe);
    	
    	//���������Z�b�g
    	setKodawariDefault();
    	
    	
    	if("1".equals(storeFlag)){
    		spinnerCategory.setSelection(0);
    	}else if("2".equals(storeFlag)){
    		spinnerCategory.setSelection(1);
    	}else if("3".equals(storeFlag)){
    		spinnerCategory.setSelection(2);
    	}else if("4".equals(storeFlag)){
    		spinnerCategory.setSelection(3);
    	}else if("5".equals(storeFlag)){
    		spinnerCategory.setSelection(4);
    	}else if("6".equals(storeFlag)){
    		spinnerCategory.setSelection(5);
    	}else if("7".equals(storeFlag)){
    		spinnerCategory.setSelection(6);
    	}else if("8".equals(storeFlag)){
    		spinnerCategory.setSelection(7);
    	}else if("9".equals(storeFlag)){
    		spinnerCategory.setSelection(8);
    	}else if("Z".equals(storeFlag)){
    		spinnerCategory.setSelection(9);
    	}
    	
    	return;
		
	}
    
	

    /**
     * DB����̒l���Z�b�g
     */
	private void setKodawariDefault() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("kodawariCheck",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		Editor e = pref.edit();
		
		
		e.putString("tabako", tabako);
		e.putString("kinen", kinen);
		e.putString("koshitsu", koshitsu);
		e.putString("wifi", wifi);
		e.putString("pc", pc);
		e.putString("terace", terace);
		e.putString("shinya", shinya);
		e.putString("pet", pet);
		e.commit();
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.add_cafe);
		
		//�J�ڌ�����s���{���R�[�h���󂯎��
        Bundle extras = getIntent().getExtras();
        
        if(extras != null){
        	cafeId = extras.getString("cafeId");
        }
        
        
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// �A�C�e����ǉ����܂�
        adapter.add("�X�^�[�o�b�N�X");
        adapter.add("�h�g�[��");
        adapter.add("�^���[�Y");
        adapter.add("�T���}���N�J�t�F");
        adapter.add("�G�N�Z���V�I�[��");
        adapter.add("�V�A�g���Y�x�X�g");
        adapter.add("�x���[�`�F");
        adapter.add("�J�t�F�h�N���G");
        adapter.add("�R���_����");
        adapter.add("�l�X�i��L�`�F�[���X�ȊO�j");
		TextView storeNameText = (TextView) this.findViewById(R.id.storeNameCaption);
		spinnerCategory = (Spinner) this.findViewById(R.id.storeCategory);
		storeSubNameText = (EditText) this.findViewById(R.id.storeSubName);
		
		addressText = (EditText) this.findViewById(R.id.storeAddress);
		phoneText = (EditText) this.findViewById(R.id.phoneNumber);
		captionText = (EditText) this.findViewById(R.id.storeCaption);
		Button kodawariSendButton = (Button) this.findViewById(R.id.kodawariSendButton);
		kodawariText = (TextView) this.findViewById(R.id.kodawariText);
		Button sendButton = (Button) this.findViewById(R.id.sendButton);
		topButton= (Button) this.findViewById(R.id.topButton);
		
		
		
		// �A�_�v�^�[��ݒ肵�܂�
		spinnerCategory.setAdapter(adapter);
		
		storeSubNameText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
    		
    		@Override
    		public void onFocusChange(View v, boolean flag){
    			if(flag == false){
    				InputMethodManager imm = 
                                             (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    				imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    			}
    			
    		}
    	});
		
		addressText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
    		
    		@Override
    		public void onFocusChange(View v, boolean flag){
    			if(flag == false){
    				InputMethodManager imm = 
                                             (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    				imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    			}
    			
    		}
    	});
		phoneText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
    		
    		@Override
    		public void onFocusChange(View v, boolean flag){
    			if(flag == false){
    				InputMethodManager imm = 
                                             (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    				imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    			}
    			
    		}
    	});
		
		captionText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
    		
    		@Override
    		public void onFocusChange(View v, boolean flag){
    			if(flag == false){
    				InputMethodManager imm = 
                                             (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    				imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    			}
    			
    		}
    	});
		
		//�������ݒ�y�[�W�{�^��
		kodawariSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(AddCafePageActivity.this,KodawariPageActivity.class);

		        startActivity(intent);
            }

			
         });
		
		//���M�{�^��
		sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//�f�[�^�𑗐M
            	try {
					sendData();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					//���M���s�Ƃ����|�̃��b�Z�[�W��\��
					Toast.makeText(AddCafePageActivity.this,
		                    "���M�Ɏ��s���܂���", Toast.LENGTH_SHORT)
		                    .show();
				}
            }

         });
		//�g�b�v�փ{�^��
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(AddCafePageActivity.this,TopPageActivity.class);
		        startActivity(intent);
            }

         });
		
		
		// �f�[�^�x�[�X���������ݗp�ɃI�[�v������
        cdh = new CafeWriteDataHelper(this);
        
        //�J�t�F�����I�u�W�F�N�g
        mCafeDetailFinder = new CafeDetailFinder(cdh);
        mCafeDetailFinder.open();
        
        if(cafeId != null && !"".equals(cafeId)){
        	//�����l��ݒ�
        	setDefaultValue();
        }
        
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
	}
	@Override
    public void onDestroy() {
    	
		super.onDestroy();
    }
	@Override
    protected void onRestart() {
		super.onRestart();
		
		//�������̐ݒ�
		setKodawari();
		
	}	
	
	
	

	
	/**
	 * �������y�[�W����߂��Ă����ꍇ�̒l�̃Z�b�g
	 */
	private void setKodawari() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("kodawariCheck",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		
		tabako = pref.getString("tabako", null);
		kinen = pref.getString("kinen", null);
		koshitsu = pref.getString("koshitsu", null);
		wifi = pref.getString("wifi", null);
		pc = pref.getString("pc", null);
		shinya = pref.getString("shinya", null);
		terace = pref.getString("terace", null);
		pet = pref.getString("pet", null);
		
		List<String> params = new ArrayList<String>();
		
		if("1".equals(tabako)){
			params.add("�i��OK");
		}
		if("1".equals(kinen)){
			params.add("�։��Ȃ���");
		}
		if("1".equals(koshitsu)){
			params.add("������");
		}
		if("1".equals(pc)){
			params.add("PC�d������");
		}
		if("1".equals(wifi)){
			params.add("wifi(���O����LAN)����");
		}
		if("1".equals(shinya)){
			params.add("�[��c�Ɓi�Q�Q���ȍ~�j");
		}
		if("1".equals(terace)){
			params.add("�e���X�Ȃ���");
		}
		if("1".equals(pet)){
			params.add("�y�b�g����OK");
		}
		
		String kodawariVal = StringUtils.join(params, ",");
		kodawariText.setText(kodawariVal);
		return;
	}



	/**
	 * �X�V�̍ۂ̏����l��ݒ�(�ʃX���b�h��)
	 */
	private void setDefaultValue() {
		// TODO Auto-generated method stub
		mCafeDetailFinder.setCafeId(cafeId);
        new Thread(new RequestRunnableDetail(mCafeDetailFinder, mHandler)).start();
	}

	/**
	 * �f�[�^���T�[�o���ɓ]��
	 * @throws UnsupportedEncodingException 
	 */
	private void sendData() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
		//URL�p�����[�^���쐬
		String storeSubName = storeSubNameText.getText().toString();
		String storeAddress = addressText.getText().toString();
		String phoneNumber = phoneText.getText().toString();
		String storeCaption = captionText.getText().toString();
		String storeFlag = feedPulldownSelected();
		String updateFlag = null;
		//URL�p�����[�^���쐬
		List<String> urlParam = new ArrayList<String>();
		if(storeFlag != null){
			urlParam.add("storeFlag="+storeFlag);
		}
		if(storeSubName != null){
			urlParam.add("storeName="+URLEncoder.encode(storeSubName , "UTF-8"));
		}
		if(storeAddress != null){
			urlParam.add("storeAddress="+URLEncoder.encode(storeAddress , "UTF-8"));
		}
		if(phoneNumber != null){
			urlParam.add("phoneNumber="+phoneNumber);
		}
		if(storeCaption != null){
			urlParam.add("storeCaption="+URLEncoder.encode(storeCaption , "UTF-8"));
		}
		if("1".equals(tabako)){
			urlParam.add("tabako="+tabako);
		}
		if("1".equals(kinen)){
			urlParam.add("kinen="+kinen);
		}
		if("1".equals(koshitsu)){
			urlParam.add("koshitsu="+koshitsu);
		}
		if("1".equals(wifi)){
			urlParam.add("wifi="+wifi);
		}
		if("1".equals(pc)){
			urlParam.add("pc="+pc);
		}
		if("1".equals(terace)){
			urlParam.add("terace="+terace);
		}
		if("1".equals(shinya)){
			urlParam.add("shinya="+shinya);
		}
		if("1".equals(pet)){
			urlParam.add("pet="+pet);
		}
		if(cafeId != null){
			urlParam.add("key="+cafeId);
			updateFlag="1";
		}
		
		urlParam.add("userSendFlag=1");
		urlParam.add("dataPost=1");
		
		String queryString = StringUtils.join(urlParam, "&");
		// �^�X�N���N������
		PostCafeTask task = new PostCafeTask(this);
		task.execute(postApiUrl,queryString,updateFlag);
		return;
	}



	/**
	 * �v���_�E�����j���[�őI���������̂ɉ����ăX�g�A�t���O���Z�b�g
	 * @return
	 */
	private String feedPulldownSelected() {
		// TODO Auto-generated method stub
		String storeFlag = null;
		int tmpStoreFlag = spinnerCategory.getSelectedItemPosition();
		switch (tmpStoreFlag) {
			case 0:
				storeFlag = "1";
				break;
			case 1:
				storeFlag = "2";
				break;	
			case 2:
				storeFlag = "3";
				break;
			case 3:
				storeFlag = "4";
				break;
			case 4:
				storeFlag = "5";
				break;
			case 5:
				storeFlag = "6";
				break;
			case 6:
				storeFlag = "7";
				break;
			case 7:
				storeFlag = "8";
				break;	
			case 8:
				storeFlag = "9";
				break;
			case 9:
				storeFlag = "Z";
				break;	
			default:
				break;
		}
		return storeFlag;
	}
	
}
