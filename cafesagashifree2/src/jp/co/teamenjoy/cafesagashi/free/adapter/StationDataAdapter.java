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

public class StationDataAdapter extends ArrayAdapter<HashMap>{

	private List<HashMap> items;
    private LayoutInflater inflater;
	
	public StationDataAdapter(Context context, int textViewResourceId,
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
            v = inflater.inflate(R.layout.station_row, null);  
        }  
        //itemsからデータ
        //vから画面にくっついているViewを取り出して値をマッピングする
        HashMap<String,String> prefData = (HashMap)items.get(position);  
        TextView hogeText = (TextView)v.findViewById(R.id.stationName);
        hogeText.setText(prefData.get("stationName"));

        
        return v;  
    }

}
