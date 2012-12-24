package jp.co.teamenjoy.cafesagashi.free.adapter;

import java.util.HashMap;
import java.util.List;

import jp.co.teamenjoy.cafesagashi.free.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CafeListAdapter extends ArrayAdapter<HashMap>{

	private List<HashMap> items;
    private LayoutInflater inflater;
	
	public CafeListAdapter(Context context, int textViewResourceId,
			List<HashMap> object) {
		super(context, textViewResourceId, object);
		// TODO Auto-generated constructor stub
		
		this.items = object;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	

	//1行分の処理
    //とにかく何をするにしてもこのgetViewというメソッドが1行操作するごとに呼ばれるので
    //追加処理とかもここに押し込む
    @Override
    public View getView(int position, View convertView, ViewGroup parent){  
        //操作対象のViewを見る
        //完全に新規に作る場合はnullがわたってくる
        //それにしてもViewを引数にとっているのにgetViewとは・・・なんだか変な話だ  
        View v = convertView;  
        if(v == null){  
            //1行分layoutからViewの塊を生成
            v = inflater.inflate(R.layout.cafelist_row, null);  
        }  
        //itemsからデータ
        //vから画面にくっついているViewを取り出して値をマッピングする
        HashMap<String,String> cafeData = (HashMap)items.get(position);
        ImageView cafePin = (ImageView)v.findViewById(R.id.cafePin);
        TextView hogeText = (TextView)v.findViewById(R.id.storeName);
        TextView addressText = (TextView)v.findViewById(R.id.storeAddress);
        TextView distanceText = (TextView)v.findViewById(R.id.distance);
        
        hogeText.setText(cafeData.get("storeName"));
        String addressStr = cafeData.get("storeAddress");
        String addressLabel = null;
        if(addressStr.length() < 18){
        	addressLabel = addressStr;
        }else{
        	addressLabel = addressStr.substring(0, 17)+"...";
        }
        
        addressText.setText(addressLabel);
        distanceText.setText(cafeData.get("distance")+" ナイス数:"+String.valueOf(cafeData.get("iine")));
        String storeFlag = cafeData.get("storeFlag");
        
        if("1".equals(storeFlag)){
        	cafePin.setImageResource(R.drawable.pins_1);
        }else if("2".equals(storeFlag)){
        	cafePin.setImageResource(R.drawable.pins_2);
        }else if("3".equals(storeFlag)){
        	cafePin.setImageResource(R.drawable.pins_3);
        }else if("4".equals(storeFlag)){
        	cafePin.setImageResource(R.drawable.pins_4);
        }else if("5".equals(storeFlag)){
        	cafePin.setImageResource(R.drawable.pins_5);
        }else if("6".equals(storeFlag)){
        	cafePin.setImageResource(R.drawable.pins_6);
        }else if("7".equals(storeFlag)){
        	cafePin.setImageResource(R.drawable.pins_7);
        }else if("8".equals(storeFlag)){
        	cafePin.setImageResource(R.drawable.pins_8);
        }else if("9".equals(storeFlag)){
        	cafePin.setImageResource(R.drawable.pins_9);
        }else if("Z".equals(storeFlag)){
        	cafePin.setImageResource(R.drawable.pins_z);
        }
        
        return v;  
    }

}
