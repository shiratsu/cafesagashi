package jp.co.teamenjoy.cafesagashi.free;




import jp.co.teamenjoy.cafesagashi.free.R;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.view.Window;

import android.widget.Button;


public class AreaPageActivity extends Activity{
	
	private Button toKinki;
	private Button toTokai;
	private Button toKanto;
	private Button toHokkaido;
	private Button toTohoku;
	private Button toTyugoku;
	private Button toShikoku;
	private Button toKyushu;
	private Button toHokuriku;
	private Button topButton;
	private final String siteId = "2495";
	private final String locationId = "2862";

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.area);
        
        
        
        
        //近畿
        toKinki = (Button) findViewById(R.id.Kinki);
        toKinki.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "03");
		        startActivity(intent);
            }
         });
        //東海
        toTokai = (Button) findViewById(R.id.Tokai);
        toTokai.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "02");
		        startActivity(intent);
            }
         });
        //関東
        toKanto = (Button) findViewById(R.id.Kanto);
        toKanto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "01");
		        startActivity(intent);
            }
         });
        //北海道
        toHokkaido = (Button) findViewById(R.id.Hokkaido);
        toHokkaido.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "04");
		        startActivity(intent);
            }
         });
        //東北
        toTohoku = (Button) findViewById(R.id.Tohoku);
        toTohoku.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "05");
		        startActivity(intent);
            }
         });
        //甲信越／北陸
        toHokuriku = (Button) findViewById(R.id.KoshinHokuriku);
        toHokuriku.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "06");
		        startActivity(intent);
            }
         });
        //中国
        toTyugoku = (Button) findViewById(R.id.Tyugoku);
        toTyugoku.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "07");
		        startActivity(intent);
            }
         });
        //四国
        toShikoku = (Button) findViewById(R.id.Shikoku);
        toShikoku.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "08");
		        startActivity(intent);
            }
         });
        //九州
        toKyushu = (Button) findViewById(R.id.Kyushu);
        toKyushu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// インテントへのインスタンス生成  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "09");
		        startActivity(intent);
            }
         });
        
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
		
		
		
	}	
    
    
}