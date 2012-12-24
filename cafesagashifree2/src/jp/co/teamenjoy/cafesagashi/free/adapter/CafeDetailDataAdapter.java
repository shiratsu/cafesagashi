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
	
	//1行分の処理
    //とにかく何をするにしてもこのgetViewというメソッドが1行操作するごとに呼ばれるので
    //追加処理とかもここに押し込む
    @Override
    public View getView(int position, View convertView, ViewGroup parent){  
        //操作対象のViewを見る
        //完全に新規に作る場合はnullがわたってくる
        //それにしてもViewを引数にとっているのにgetViewとは・・・なんだか変な話だ  
        View v = convertView;
        HashMap<String,String> cafeData = (HashMap)items; 
        if(position == 0){
	        if(v == null){  
	            //1行分layoutからViewの塊を生成
	            v = inflater.inflate(R.layout.title_row, null);  
	        }  
	        //itemsからデータ
	        //vから画面にくっついているViewを取り出して値をマッピングする
	         
	        TextView hogeText = (TextView)v.findViewById(R.id.storeName);
	        hogeText.setText(cafeData.get("storeName"));
	
        }
        if(position == 1){
	        if(v == null){  
	            //1行分layoutからViewの塊を生成
	            v = inflater.inflate(R.layout.button_row, null);  
	        }  
	        //itemsからデータ
	        //vから画面にくっついているViewを取り出して値をマッピングする
	         
	        Button btnNice = (Button)v.findViewById(R.id.btnNice);
	        btnNice.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	               
	            }
	         });
	        
        }
        if(position == 2){
	        if(v == null){  
	            //1行分layoutからViewの塊を生成
	            v = inflater.inflate(R.layout.address_row, null);  
	        }  
	        //itemsからデータ
	        //vから画面にくっついているViewを取り出して値をマッピングする
	         
	        TextView hogeText = (TextView)v.findViewById(R.id.storeAddress);
	        hogeText.setText(cafeData.get("storeAddress"));
	        
        }
        if(position == 3){
	        if(v == null){  
	            //1行分layoutからViewの塊を生成
	            v = inflater.inflate(R.layout.phone_row, null);  
	        }  
	        //itemsからデータ
	        //vから画面にくっついているViewを取り出して値をマッピングする
	         
	        TextView hogeText = (TextView)v.findViewById(R.id.phoneNumber);
	        hogeText.setText(cafeData.get("phoneNumber"));
	        
        }
        if(position == 4){
	        if(v == null){  
	            //1行分layoutからViewの塊を生成
	            v = inflater.inflate(R.layout.phone_row, null);  
	        }  
	        //itemsからデータ
	        //vから画面にくっついているViewを取り出して値をマッピングする
	         
	        TextView hogeText = (TextView)v.findViewById(R.id.phoneNumber);
	        hogeText.setText(cafeData.get("phoneNumber"));
	        
        }
        if(position == 5){
	        if(v == null){  
	            //1行分layoutからViewの塊を生成
	            v = inflater.inflate(R.layout.kodawari_row, null);  
	        }  
	        //itemsからデータ
	        //vから画面にくっついているViewを取り出して値をマッピングする
	         
	        TextView hogeText = (TextView)v.findViewById(R.id.kodawari);
	        hogeText.setText(cafeData.get("kodawari"));
	        
        }
        return v;
    }

}
