package jp.co.teamenjoy.cafesagashi.free.adapter;

import java.util.HashMap;
import java.util.List;

import jp.co.teamenjoy.cafesagashi.free.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CafeDetailDataAdapter extends ArrayAdapter<HashMap>{

	private List<HashMap> items;
    private LayoutInflater inflater;
	
	public CafeDetailDataAdapter(Context context, int textViewResourceId,
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
        HashMap<String,String> cafeData = (HashMap)items; 
        if(position == 0){
	        if(v == null){  
	            //1�s��layout����View�̉�𐶐�
	            v = inflater.inflate(R.layout.title_row, null);  
	        }  
	        //items����f�[�^
	        //v�����ʂɂ������Ă���View�����o���Ēl���}�b�s���O����
	         
	        TextView hogeText = (TextView)v.findViewById(R.id.storeName);
	        hogeText.setText(cafeData.get("storeName"));
	
        }
        if(position == 1){
	        if(v == null){  
	            //1�s��layout����View�̉�𐶐�
	            v = inflater.inflate(R.layout.button_row, null);  
	        }  
	        //items����f�[�^
	        //v�����ʂɂ������Ă���View�����o���Ēl���}�b�s���O����
	         
	        Button btnNice = (Button)v.findViewById(R.id.btnNice);
	        btnNice.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	               
	            }
	         });
	        
        }
        if(position == 2){
	        if(v == null){  
	            //1�s��layout����View�̉�𐶐�
	            v = inflater.inflate(R.layout.address_row, null);  
	        }  
	        //items����f�[�^
	        //v�����ʂɂ������Ă���View�����o���Ēl���}�b�s���O����
	         
	        TextView hogeText = (TextView)v.findViewById(R.id.storeAddress);
	        hogeText.setText(cafeData.get("storeAddress"));
	        
        }
        if(position == 3){
	        if(v == null){  
	            //1�s��layout����View�̉�𐶐�
	            v = inflater.inflate(R.layout.phone_row, null);  
	        }  
	        //items����f�[�^
	        //v�����ʂɂ������Ă���View�����o���Ēl���}�b�s���O����
	         
	        TextView hogeText = (TextView)v.findViewById(R.id.phoneNumber);
	        hogeText.setText(cafeData.get("phoneNumber"));
	        
        }
        if(position == 4){
	        if(v == null){  
	            //1�s��layout����View�̉�𐶐�
	            v = inflater.inflate(R.layout.phone_row, null);  
	        }  
	        //items����f�[�^
	        //v�����ʂɂ������Ă���View�����o���Ēl���}�b�s���O����
	         
	        TextView hogeText = (TextView)v.findViewById(R.id.phoneNumber);
	        hogeText.setText(cafeData.get("phoneNumber"));
	        
        }
        if(position == 5){
	        if(v == null){  
	            //1�s��layout����View�̉�𐶐�
	            v = inflater.inflate(R.layout.kodawari_row, null);  
	        }  
	        //items����f�[�^
	        //v�����ʂɂ������Ă���View�����o���Ēl���}�b�s���O����
	         
	        TextView hogeText = (TextView)v.findViewById(R.id.kodawari);
	        hogeText.setText(cafeData.get("kodawari"));
	        
        }
        return v;
    }

}
