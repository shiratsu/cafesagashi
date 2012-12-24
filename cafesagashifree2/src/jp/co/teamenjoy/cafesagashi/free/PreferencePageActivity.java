package jp.co.teamenjoy.cafesagashi.free;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import jp.co.teamenjoy.cafesagashi.free.R;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;


public class PreferencePageActivity extends ListActivity{
	
	private List<HashMap> pList = new ArrayList<HashMap>();
	private String storeFlag1;
	private String storeFlag2;
	private String storeFlag3;
	private String storeFlag4;
	private String storeFlag5;
	private String storeFlag6;
	private String storeFlag7;
	private String storeFlag8;
	private String storeFlag9;
	private String storeFlagZ;
	private String tabako;
	private String kinen;
	private String wifi;
	private String pc;
	private String koshitsu;
	private String terace;
	private String pet;
	private String shinya;
	private ListView listView;
	private Button topButton;
	// ListView �ɕ\�������镶����  
	private static final String[] PREFERNCE = new String[] {  
		"�X�^�[�o�b�N�X", "�h�g�[��", "�^���[�Y", "�T���}���N�J�t�F",  
		"�G�N�Z���V�I�[��", "�V�A�g���Y�x�X�g", "�x���[�`�F" ,"�J�t�F�h�N���G","�R���_",
		"�l�X","�i��OK","�։��Ȃ���(or�X���։�)","������","wifi(���O����)����",
		"PC�d������","�[��c��","�e���X�Ȃ���","�y�b�gOK"
	};
	private final String siteId = "2495";
	private final String locationId = "2862";

	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.preference_list);
        
        
        
        listView = (ListView) findViewById(android.R.id.list);
      //�g�b�v�փ{�^��
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(PreferencePageActivity.this,TopPageActivity.class);
		        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		        startActivity(intent);
            }

         });
        //List�Ɉڂ�
        for(int i=0;i<PREFERNCE.length;i++){
        	HashMap pMap = new HashMap();
        	pMap.put("prefName", PREFERNCE[i]);
        	if(i < 10){
        		pMap.put("cafeFlag", "1");
        		
        	}else{
        		pMap.put("cafeFlag", null);
        	}
        	pList.add(pMap);
        }
        
        //�`�F�b�N�{�b�N�X�Ƀ`�F�b�N
        setCheckData();
        
        // �I���̕����̐ݒ�  
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        PreferenceDataAdapter pda = new PreferenceDataAdapter(this, R.layout.preference_row, pList);
        setListAdapter(pda);
     // �A�_�v�^�̍쐬  
        /*
        listView.setAdapter(new ArrayAdapter<String>(  
            this,  
            android.R.layout.simple_list_item_multiple_choice,  
            PREFERNCE)  
        );
        */
     // �t�H�[�J�X��������Ȃ��悤�ݒ�  
        listView.setItemsCanFocus(false);  
 
        
        //AdMaker.removeViewAt(R.id.admakerview);
        
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}
	@Override
    protected void onRestart() {
		super.onRestart();
		
		
		
	}	
	
    /**
     * �`�F�b�N�{�b�N�X�̏����`�F�b�N
     */
    private void setCheckData() {
		// TODO Auto-generated method stub
    	// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		storeFlag1 = pref.getString("storeFlag1", null);
		storeFlag2 = pref.getString("storeFlag2", null);
		storeFlag3 = pref.getString("storeFlag3", null);
		storeFlag4 = pref.getString("storeFlag4", null);
		storeFlag5 = pref.getString("storeFlag5", null);
		storeFlag6 = pref.getString("storeFlag6", null);
		storeFlag7 = pref.getString("storeFlag7", null);
		storeFlag8 = pref.getString("storeFlag8", null);
		storeFlag9 = pref.getString("storeFlag9", null);
		storeFlagZ = pref.getString("storeFlagZ", null);
		tabako = pref.getString("tabako", null);
		kinen = pref.getString("kinen", null);
		koshitsu = pref.getString("koshitsu", null);
		wifi = pref.getString("wifi", null);
		pc = pref.getString("pc", null);
		shinya = pref.getString("shinya", null);
		terace = pref.getString("terace", null);
		pet = pref.getString("pet", null);
		
		if("1".equals(storeFlag1)){
			pList.get(0).put("checked", new String("true"));
		}else{
			pList.get(0).put("checked", new String("false"));
		}
		if("1".equals(storeFlag2)){
			pList.get(1).put("checked", new String("true"));
		}else{
			pList.get(1).put("checked", new String("false"));
		}
		if("1".equals(storeFlag3)){
			pList.get(2).put("checked", new String("true"));
		}else{
			pList.get(2).put("checked", new String("false"));
		}
		if("1".equals(storeFlag4)){
			pList.get(3).put("checked", new String("true"));
		}else{
			pList.get(3).put("checked", new String("false"));
		}
		if("1".equals(storeFlag5)){
			pList.get(4).put("checked", new String("true"));
		}else{
			pList.get(4).put("checked", new String("false"));
		}
		if("1".equals(storeFlag6)){
			pList.get(5).put("checked", new String("true"));
		}else{
			pList.get(5).put("checked", new String("false"));
		}
		if("1".equals(storeFlag7)){
			pList.get(6).put("checked", new String("true"));
		}else{
			pList.get(6).put("checked", new String("false"));
		}
		if("1".equals(storeFlag8)){
			pList.get(7).put("checked", new String("true"));
		}else{
			pList.get(7).put("checked", new String("false"));
		}
		if("1".equals(storeFlag9)){
			pList.get(8).put("checked", new String("true"));
		}else{
			pList.get(8).put("checked", new String("false"));
		}
		if("1".equals(storeFlagZ)){
			pList.get(9).put("checked", new String("true"));
		}else{
			pList.get(9).put("checked", new String("false"));
		}
		if("1".equals(tabako)){
			pList.get(10).put("checked", new String("true"));
		}else{
			pList.get(10).put("checked", new String("false"));
		}
		if("1".equals(kinen)){
			pList.get(11).put("checked", new String("true"));
		}else{
			pList.get(11).put("checked", new String("false"));
		}
		if("1".equals(koshitsu)){
			pList.get(12).put("checked", new String("true"));
		}else{
			pList.get(12).put("checked", new String("false"));
		}
		if("1".equals(wifi)){
			pList.get(13).put("checked", new String("true"));
		}else{
			pList.get(13).put("checked", new String("false"));
		}
		if("1".equals(pc)){
			pList.get(14).put("checked", new String("true"));
		}else{
			pList.get(14).put("checked", new String("false"));
		}
		if("1".equals(shinya)){
			pList.get(15).put("checked", new String("true"));
		}else{
			pList.get(15).put("checked", new String("false"));
		}
		if("1".equals(terace)){
			pList.get(16).put("checked", new String("true"));
		}else{
			pList.get(16).put("checked", new String("false"));
		}
		if("1".equals(pet)){
			pList.get(17).put("checked", new String("true"));
		}else{
			pList.get(17).put("checked", new String("false"));
		}
		return;
	}

	/**
     * �ݒ���`�F�b�N�����Ƃ��̌����I�v�V�����ݒ�
     * @param position
     */
    private void clickPrefernceItem(int position) {
		// TODO Auto-generated method stub
    	
    	switch(position){
            case 0:
    			//�X�^�o
            	if(storeFlag1 == null){
            		storeFlag1 = "1";
            	}else{
            		storeFlag1 = null;
            	}
    			break;
    		case 1:
    			//�h�g�[��
    			if(storeFlag2 == null){
            		storeFlag2 = "1";
            	}else{
            		storeFlag2 = null;
            	}
    			break;
    		case 2:
    			//�^���[�Y
    			if(storeFlag3 == null){
            		storeFlag3 = "1";
            	}else{
            		storeFlag3 = null;
            	}
    			break;
    		case 3:
    			//�T���}���N�J�t�F
    			if(storeFlag4 == null){
            		storeFlag4 = "1";
            	}else{
            		storeFlag4 = null;
            	}
    			break;
    		case 4:
    			//�G�N�Z���V�I�[��
    			if(storeFlag5 == null){
            		storeFlag5 = "1";
            	}else{
            		storeFlag5 = null;
            	}
    			break;
    		case 5:
    			//�V�A�g���Y�x�X�g
    			if(storeFlag6 == null){
            		storeFlag6 = "1";
            	}else{
            		storeFlag6 = null;
            	}
    			break;	
    		case 6:
    			//�x���[�`�F
    			if(storeFlag7 == null){
            		storeFlag7 = "1";
            	}else{
            		storeFlag7 = null;
            	}
    			break;
    		case 7:
    			//�J�t�F�h�N���G
    			if(storeFlag8 == null){
            		storeFlag8 = "1";
            	}else{
            		storeFlag8 = null;
            	}
    			break;
    		case 8:
    			//�R���_
    			if(storeFlag9 == null){
            		storeFlag9 = "1";
            	}else{
            		storeFlag9 = null;
            	}
    			break;
    		case 9:
    			//�R���_
    			if(storeFlagZ == null){
            		storeFlagZ = "1";
            	}else{
            		storeFlagZ = null;
            	}
    			break;
    		case 10:
    			//���΂�
    			if(tabako == null){
    				tabako = "1";
            	}else{
            		tabako = null;
            	}
    			break;
    		case 11:
    			//�։�
    			if(kinen == null){
    				kinen = "1";
            	}else{
            		kinen = null;
            	}
    			break;
    		case 12:
    			//��
    			if(koshitsu == null){
    				koshitsu = "1";
            	}else{
            		koshitsu = null;
            	}
    			break;
    		case 13:
    			//wifi
    			if(wifi == null){
    				wifi = "1";
            	}else{
            		wifi = null;
            	}
    			break;
    		case 14:
    			//pc
    			if(pc == null){
    				pc = "1";
            	}else{
            		pc = null;
            	}
    			break;
    		case 15:
    			//�[��
    			if(shinya == null){
    				shinya = "1";
            	}else{
            		shinya = null;
            	}
    			break;
    		case 16:
    			//�e���X
    			if(terace == null){
    				terace = "1";
            	}else{
            		terace = null;
            	}
    			break;
    		case 17:
    			//�y�b�gOK
    			if(pet == null){
    				pet = "1";
            	}else{
            		pet = null;
            	}
    			break;
        }
		
	}  
    
    /**
     * �`�F�b�N�f�[�^��SharedPreference�ɕۑ�
     */
	private void setCheckItem() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("cafeSagaShi_pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		Editor e = pref.edit();
		
		e.putString("storeFlag1", storeFlag1);
		e.putString("storeFlag2", storeFlag2);
		e.putString("storeFlag3", storeFlag3);
		e.putString("storeFlag4", storeFlag4);
		e.putString("storeFlag5", storeFlag5);
		e.putString("storeFlag6", storeFlag6);
		e.putString("storeFlag7", storeFlag7);
		e.putString("storeFlag8", storeFlag8);
		e.putString("storeFlag9", storeFlag9);
		e.putString("storeFlagZ", storeFlagZ);
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
	protected void onStop() {
		// TODO Auto-generated method stub
		
		super.onStop();
	}
	
	
   

    
    //�A�_�v�^(ListView��)
    /**
     * 
     */
    public class PreferenceDataAdapter extends ArrayAdapter<HashMap>{

    	private List<HashMap> items;
        private LayoutInflater inflater;
    	
    	public PreferenceDataAdapter(Context context, int textViewResourceId,
    			List<HashMap> object) {
    		super(context, textViewResourceId, object);
    		// TODO Auto-generated constructor stub
    		
    		this.items = object;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	}
    	
    	//1�s���̏���
        //�Ƃɂ�����������ɂ��Ă�����getView�Ƃ������\�b�h��1�s���삷�邲�ƂɌĂ΂��̂�
        //�ǉ������Ƃ��������ɉ�������
        @Override
        public View getView(int position, View convertView, ViewGroup parent){  
            //����Ώۂ�View������
            //���S�ɐV�K�ɍ��ꍇ��null���킽���Ă���
            //����ɂ��Ă�View�������ɂƂ��Ă���̂�getView�Ƃ́E�E�E�Ȃ񂾂��ςȘb��  
            View v = convertView;  
            if(v == null){  
                //1�s��layout����View�̉�𐶐�
                v = inflater.inflate(R.layout.preference_row, null);  
            }  
            //items����f�[�^
            //v�����ʂɂ������Ă���View�����o���Ēl���}�b�s���O����
            HashMap<String,String> prefData = (HashMap)items.get(position);  
            final CheckedTextView check = (CheckedTextView) v.findViewById(R.id.prefChecked);
            
            check.setText((CharSequence) items.get(position).get("prefName"));
            
            final int p = position;
            
            
            
            switch(p){
	            case 0:
	            	if( "1".equals(items.get(p).get("cafeFlag"))){
	            		ImageView pinImageView = ( ImageView ) v.findViewById( R.id.storeImageView );
	            		pinImageView.setImageResource(R.drawable.pins_1);
	            		pinImageView=null;
	            	}	
	    			break;
	    		case 1:
	    			if( "1".equals(items.get(p).get("cafeFlag"))){
	    				ImageView pinImageView = ( ImageView ) v.findViewById( R.id.storeImageView );
	    				pinImageView.setImageResource(R.drawable.pins_2);
	    				pinImageView=null;
	    			}
	    			break;
	    		case 2:
	    			if( "1".equals(items.get(p).get("cafeFlag"))){
	    				ImageView pinImageView = ( ImageView ) v.findViewById( R.id.storeImageView );
	    				pinImageView.setImageResource(R.drawable.pins_3);
	    				pinImageView=null;
	    			}
	    			break;
	    		case 3:
	    			if( "1".equals(items.get(p).get("cafeFlag"))){
	    				ImageView pinImageView = ( ImageView ) v.findViewById( R.id.storeImageView );
	    				pinImageView.setImageResource(R.drawable.pins_4);
	    				pinImageView=null;
	    			}
	    			break;
	    		case 4:
	    			if( "1".equals(items.get(p).get("cafeFlag"))){
	    				ImageView pinImageView = ( ImageView ) v.findViewById( R.id.storeImageView );
	    				pinImageView.setImageResource(R.drawable.pins_5);
	    				pinImageView=null;
	    			}
	    			break;
	    		case 5:
	    			if( "1".equals(items.get(p).get("cafeFlag"))){
	    				ImageView pinImageView = ( ImageView ) v.findViewById( R.id.storeImageView );
	    				pinImageView.setImageResource(R.drawable.pins_6);
	    				pinImageView=null;
	    			}
	    			break;	
	    		case 6:
	    			if( "1".equals(items.get(p).get("cafeFlag"))){
	    				ImageView pinImageView = ( ImageView ) v.findViewById( R.id.storeImageView );
	    				pinImageView.setImageResource(R.drawable.pins_7);
	    				pinImageView=null;
	    			}
	    			break;
	    		case 7:
	    			if( "1".equals(items.get(p).get("cafeFlag"))){
	    				ImageView pinImageView = ( ImageView ) v.findViewById( R.id.storeImageView );
	    				pinImageView.setImageResource(R.drawable.pins_8);
	    				pinImageView=null;
	    			}
	    			break;
	    		case 8:
	    			if( "1".equals(items.get(p).get("cafeFlag"))){
	    				ImageView pinImageView = ( ImageView ) v.findViewById( R.id.storeImageView );
	    				pinImageView.setImageResource(R.drawable.pins_9);
	    				pinImageView=null;
	    			}
	    			break;
	    		case 9:
	    			if( "1".equals(items.get(p).get("cafeFlag"))){
	    				ImageView pinImageView = ( ImageView ) v.findViewById( R.id.storeImageView );
	    				pinImageView.setImageResource(R.drawable.pins_z);
	    				pinImageView=null;
	    			}
	    			break;
	    		default:
	    			ImageView pinImageView = ( ImageView ) v.findViewById( R.id.storeImageView );
    				pinImageView.setImageResource(0);
	    		break;
            }	
            
            check.setOnClickListener(new View.OnClickListener() { 
                public void onClick(View view) { // Toggle whether the Row is selected or not. 
                    boolean checked = Boolean.valueOf((String) items.get(p).get("checked"));
                    if(checked == true){
                        items.get(p).put("checked", new String("false"));
                        
                    }else{
                        items.get(p).put("checked", new String("true"));
                    }
                    clickPrefernceItem(p);
                    check.setChecked(Boolean.valueOf((String) items.get(p).get("checked"))); 
                } 
            });
            
            
            check.setChecked(Boolean.valueOf((String) items.get(position).get("checked")));
            return v;  
        }

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