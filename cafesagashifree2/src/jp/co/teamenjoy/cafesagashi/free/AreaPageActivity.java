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
        
        
        
        
        //�ߋE
        toKinki = (Button) findViewById(R.id.Kinki);
        toKinki.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "03");
		        startActivity(intent);
            }
         });
        //���C
        toTokai = (Button) findViewById(R.id.Tokai);
        toTokai.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "02");
		        startActivity(intent);
            }
         });
        //�֓�
        toKanto = (Button) findViewById(R.id.Kanto);
        toKanto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "01");
		        startActivity(intent);
            }
         });
        //�k�C��
        toHokkaido = (Button) findViewById(R.id.Hokkaido);
        toHokkaido.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "04");
		        startActivity(intent);
            }
         });
        //���k
        toTohoku = (Button) findViewById(R.id.Tohoku);
        toTohoku.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "05");
		        startActivity(intent);
            }
         });
        //�b�M�z�^�k��
        toHokuriku = (Button) findViewById(R.id.KoshinHokuriku);
        toHokuriku.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "06");
		        startActivity(intent);
            }
         });
        //����
        toTyugoku = (Button) findViewById(R.id.Tyugoku);
        toTyugoku.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "07");
		        startActivity(intent);
            }
         });
        //�l��
        toShikoku = (Button) findViewById(R.id.Shikoku);
        toShikoku.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
		        Intent intent = new Intent(AreaPageActivity.this,PrefPageActivity.class);
		        intent.putExtra("areaCode", "08");
		        startActivity(intent);
            }
         });
        //��B
        toKyushu = (Button) findViewById(R.id.Kyushu);
        toKyushu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// �C���e���g�ւ̃C���X�^���X����  
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