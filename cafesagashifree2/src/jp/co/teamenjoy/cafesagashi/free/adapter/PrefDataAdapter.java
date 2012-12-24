package jp.co.teamenjoy.cafesagashi.free.adapter;

import java.util.HashMap;
import java.util.List;

import jp.co.teamenjoy.cafesagashi.free.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PrefDataAdapter extends ArrayAdapter<HashMap>{

	private List<HashMap> items;
    private LayoutInflater inflater;
	
	public PrefDataAdapter(Context context, int textViewResourceId,
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
            v = inflater.inflate(R.layout.row, null);  
        }  
        //items����f�[�^
        //v�����ʂɂ������Ă���View�����o���Ēl���}�b�s���O����
        HashMap<String,String> prefData = (HashMap)items.get(position);  
        TextView hogeText = (TextView)v.findViewById(R.id.prefName);
        hogeText.setText(prefData.get("prefName"));

        
        return v;  
    }

}
