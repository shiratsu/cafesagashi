package jp.co.teamenjoy.cafesagashi.free;

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

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;

import android.widget.ListView;

import android.widget.AdapterView.OnItemClickListener;

public class KodawariPageActivity extends ListActivity{
	
	private List<HashMap> pList = new ArrayList<HashMap>();
	
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
	private final String siteId = "2495";
	private final String locationId = "2862";

	
	// ListView に表示させる文字列  
	private static final String[] KODAWARI = new String[] {  
		"喫煙OK","禁煙席あり(or店内禁煙)","個室あり","wifi(公衆無線)あり",
		"PC電源あり","深夜営業","テラス席あり","ペットOK"
	};  
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kodawari_list);
        
        listView = (ListView) findViewById(android.R.id.list);
        
        
        
        
      //Listに移す
        for(int i=0;i<KODAWARI.length;i++){
        	HashMap pMap = new HashMap();
        	pMap.put("kName", KODAWARI[i]);
        	pList.add(pMap);
        }
        
        
        //チェックボックスにチェック
        setCheckData();
        
        
     // アダプタの作成
        KodawariDataAdapter kda = new KodawariDataAdapter(this, R.layout.kodawari_row, pList);
        setListAdapter(kda);
     // フォーカスが当たらないよう設定  
        listView.setItemsCanFocus(false);  
      
        // 選択の方式の設定  
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);  
        
        // アイテムがクリックされたときに呼び出されるコールバックを登録  
        
        listView.setOnItemClickListener(new OnItemClickListener() {  
	          @Override  
	          public void onItemClick(AdapterView<?> parent,  
	                  View view, int position, long id) {  
	              	  // クリックされた時の処理
	        	  	  clickPrefernceItem(position);
	          }

			
        }); 
      //トップへボタン
        topButton = (Button) findViewById(R.id.topButton);
		topButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(KodawariPageActivity.this,TopPageActivity.class);
		        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		        startActivity(intent);
            }

         });
 
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	
    
    /**
     * チェックボックスの初期チェック
     */
    private void setCheckData() {
		// TODO Auto-generated method stub
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
		
		
		if("1".equals(tabako)){
			pList.get(0).put("checked", new String("true"));
		}else{
			pList.get(0).put("checked", new String("false"));
		}
		if("1".equals(kinen)){
			pList.get(1).put("checked", new String("true"));
		}else{
			pList.get(1).put("checked", new String("false"));
		}
		if("1".equals(koshitsu)){
			pList.get(2).put("checked", new String("true"));
		}else{
			pList.get(2).put("checked", new String("false"));
		}
		if("1".equals(wifi)){
			pList.get(3).put("checked", new String("true"));
		}else{
			pList.get(3).put("checked", new String("false"));
		}
		if("1".equals(pc)){
			pList.get(4).put("checked", new String("true"));
		}else{
			pList.get(4).put("checked", new String("false"));
		}
		if("1".equals(shinya)){
			pList.get(5).put("checked", new String("true"));
		}else{
			pList.get(5).put("checked", new String("false"));
		}
		if("1".equals(terace)){
			pList.get(6).put("checked", new String("true"));
		}else{
			pList.get(6).put("checked", new String("false"));
		}
		if("1".equals(pet)){
			pList.get(7).put("checked", new String("true"));
		}else{
			pList.get(7).put("checked", new String("false"));
		}
	}

	/**
     * 設定をチェックしたときの検索オプション設定
     * @param position
     */
    private void clickPrefernceItem(int position) {
		// TODO Auto-generated method stub
    	
    	switch(position){
            
    		case 0:
    			//たばこ
    			if("1".equals(tabako)){
    				tabako = null;
            	}else{
            		tabako = "1";
            	}
    			break;
    		case 1:
    			//禁煙
    			if("1".equals(kinen)){
    				kinen = null;
            	}else{
            		kinen = "1";
            	}
    			break;
    		case 2:
    			//個室
    			if("1".equals(koshitsu)){
    				koshitsu = null;
            	}else{
            		koshitsu = "1";
            	}
    			break;
    		case 3:
    			//wifi
    			if("1".equals(wifi)){
    				wifi = null;
            	}else{
            		wifi = "1";
            	}
    			break;
    		case 4:
    			//pc
    			if("1".equals(pc)){
    				pc = null;
            	}else{
            		pc = "1";
            	}
    			break;
    		case 5:
    			//深夜
    			if("1".equals(shinya)){
    				shinya = null;
            	}else{
            		shinya = "1";
            	}
    			break;
    		case 6:
    			//テラス
    			if("1".equals(terace)){
    				terace = null;
            	}else{
            		terace = "1";
            	}
    			break;
    		case 7:
    			//ペットOK
    			if("1".equals(pet)){
    				pet = null;
            	}else{
            		pet = "1";
            	}
    			break;
        }
		
	}  
    
    /**
     * チェックデータをSharedPreferenceに保存
     */
	private void setCheckItem() {
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
	protected void onStop() {
		// TODO Auto-generated method stub
		
		super.onStop();
	}
	
	
    // アクティビティがフォアグラウンドになったタイミングでデータを表示する
    
    //アダプタ(ListViewの)
    /**
     * 
     */
    public class KodawariDataAdapter extends ArrayAdapter<HashMap>{

    	private List<HashMap> items;
        private LayoutInflater inflater;
    	
    	public KodawariDataAdapter(Context context, int textViewResourceId,
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
                v = inflater.inflate(R.layout.kodawari_row, null);  
            }  
            //itemsからデータ
            //vから画面にくっついているViewを取り出して値をマッピングする
            HashMap<String,String> prefData = (HashMap)items.get(position);  
            final CheckedTextView check = (CheckedTextView) v.findViewById(R.id.kodawariChecked);
            
            check.setText((CharSequence) items.get(position).get("kName"));
            
            final int p = position;
            
            
            
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
    protected void onRestart() {
		super.onRestart();
		
		
		
	}	
	

	@Override
	protected void onPause() {
		super.onPause();
		
	}
	@Override
    public void onDestroy() {
    	
		super.onDestroy();
		setCheckItem();
    }

	
	
}
