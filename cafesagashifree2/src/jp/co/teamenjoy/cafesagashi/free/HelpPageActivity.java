package jp.co.teamenjoy.cafesagashi.free;



import jp.co.teamenjoy.cafesagashi.free.R;

import android.app.Activity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;

import android.view.Window;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpPageActivity extends Activity{
	
	private Button btnBack;
	private TextView text1;
	private TextView text2;
	private TextView text3;
	private ImageView image1;
	private ImageView image2;
	private Button topButton;
	private final String siteId = "2495";
	private final String locationId = "2862";

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help);
		
		
		
		text1 = (TextView) this.findViewById(R.id.text1);
		text2 = (TextView) this.findViewById(R.id.text2);
		text3 = (TextView) this.findViewById(R.id.text3);
		
		image1 = (ImageView) this.findViewById(R.id.image1);
		image2 = (ImageView) this.findViewById(R.id.image2);
		
		text1.setText("カフェさがしのガイドページです。カフェさがしは、あなたのいる場所から近くのカフェを簡単に探すことができるアプリケーションです。\n" +
				"\n" +
				"■基本的な使い方\n" +
				"使い方は簡単です。カフェを探したいときに「カフェさがし」を立ち上げるだけ！　あなたのいる場所から近くのカフェを探すことができます。\n" +
				"検索結果の標準表示は、地図ビューです。右上にある「リスト」ボタンをタップすると、\n" +
				"リストビューに切替えられます。なお、現在地以外のカフェを探したい場合は、地図ビュー/リストビューの上部にある検索ボックスで、\n" +
				"その場所の住所あるいは近くの駅/ランドマークを入力し検索すると一覧が表示されます。\n" +
				"\n" +
				"■検索オプションを使いこなそう\n" +
				"検索オプションでは、探し方をあなたの好みに設定することができます。検索したいお店の種類を選んだり、こだわり条件にチェックを入れるとあなた仕様のカフェ検索アプリに。\n" +
				"なお、こだわり条件にチェックを多く入れ過ぎると、エリアによっては検索結果数が激減する場合があります。その場合は条件を緩めてください。\n" +
				"※公衆無線LANサービスについて：実際に利用できるサービスは店舗にお問い合わせください。\n" +
				"\n" +
				"＜ご協力ください＞\n" +
				"あなたがカフェに行ったとき、そのお店がすいていたら店舗の詳細ページにある「今すいてる」ボタンをタップしてください。アプリ利用者でその情報が共有され、検索がさらに便利になります。\n" +
				"※誰が押したかは他の人に共有されません。\n" +
				"\n" +
				"■評価しよう\n" +
				"あなたがいるカフェが気に入ったら、店舗の詳細ページにある「ナイス！」を押してください。アプリ利用者でその情報が共有され、検索がさらに便利になります。\n" +
				"※リストビューのソート「ナイス順」に利用されます。\n" +
				"※誰が押したかは他の人に共有されません。\n" +
				"\n" +
				"■お気に入り\n" +
				"気に入ったカフェ、気になるカフェがあれば「お気に入り」に登録しましょう！　登録をすると、フッターメニュー「お気に入り」から簡単にアクセスできるようになります。登録方法は、店舗の詳細ページ右上にある「お気に入り」ボタンをタップしてください。これで、フッターメニューの「お気に入り」に保存されます。お気に入りに登録できる件数は、30件です。\n");
		
		text2.setText("\n" +
				"■お店情報の投稿・編集について\n" +
				"お店の情報が無い場合は、店舗情報を投稿することができます。あなたが編集者になってお店情報を投稿・編集してみませんか？\n" +
				"\n" +
				"＜投稿までの手順＞\n" +
				"1.該当の店舗を検索します\n" +
				"2.検索結果に無い場合は画面左上にある「＋」ボタンをタップ\n" +
				"3.画面の指示に従いお店情報を入力し「保存」ボタンをタップ\n" +
				"\n" +
				"＜お店情報を編集する場合＞\n" +
				"1.情報の修正をしたいお店の詳細ページを開きます\n" +
				"2.左下の「店舗情報」をタップ\n" +
				"3.メニュー「この店舗情報を編集する」をタップ\n" +
				"4.修正箇所を修正し「保存」ボタンをタップ\n" +
				"\n" +
				"＜お店情報を削除する場合＞\n" +
				"お店情報は削除することもできます。閉店している場合は、情報の削除にご協力ください。\n" +
				"1.削除したいお店の詳細ページを開きます\n" +
				"2.左下の「店舗情報」をタップ\n" +
				"3.メニュー「この店舗情報を削除する」をタップ\n" +
				"4.削除完了のメッセージが表示されます\n" +
				"\n" +
				"＜店舗の重複/名寄せについて＞\n" +
				"同住所、同名称の店舗が複数存在する場合は、管理者側で店舗を名寄せする場合があります。ご了承ください。\n" +
				"\n" +
				"＜悪質な投稿について＞\n" +
				"営業中の店舗を多数削除する、あるいはデタラメな情報を登録する利用者については、こちらから警告をする場合があります。\n" +
				"データの品質管理は、定期的にチェックを行ってまいります。\n" +
				"※品質管理、編集権については管理者側にあります。ご了承ください。\n");
		StringBuilder sb = new StringBuilder();
		
		sb.append("<br />" +
				"■このアプリについて<br />" +
				"この「カフェさがし」は、3人の有志で作られています。今後も対応エリアの拡大や機能改善を行ってまいります。<br />" +
				"<br />" +
				"Team Enjoy! This Product by...<br />" +
				"Producer：Kai<br />" +
				"Programmer：ダンテ<br />" +
				"illustration & Photo：ロコ<br />" +
				"<br />" +
				"SupportSite：<a href=\"http://d.hatena.ne.jp/fun_enjoy/\">Team Enjoy! サポートサイト</a><br />" +
				"「※カフェさがし」の記事タグをご覧ください。<br />" +
				"<br />" +
				"Information...<br />" +
				"<a href=\"http://blog.65x1.hacca.jp/?pid=1\">ロコ Website</a><br /><br />" +
				"<br />" +
				"");
		// LinkMovementMethod のインスタンスを取得します
        MovementMethod movementmethod = LinkMovementMethod.getInstance();
        
        // TextView に LinkMovementMethod を登録します
        text3.setMovementMethod(movementmethod);
		String htmlText = sb.toString();
		CharSequence spanned = Html.fromHtml(htmlText);
		text3.setText(spanned);
		
		
        //AdMaker.removeViewAt(R.id.admakerview);
        
	}
	@Override
    protected void onRestart() {
		super.onRestart();
		
		
		
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

	
 
    
    

	
}
