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
		
		text1.setText("�J�t�F�������̃K�C�h�y�[�W�ł��B�J�t�F�������́A���Ȃ��̂���ꏊ����߂��̃J�t�F���ȒP�ɒT�����Ƃ��ł���A�v���P�[�V�����ł��B\n" +
				"\n" +
				"����{�I�Ȏg����\n" +
				"�g�����͊ȒP�ł��B�J�t�F��T�������Ƃ��Ɂu�J�t�F�������v�𗧂��グ�邾���I�@���Ȃ��̂���ꏊ����߂��̃J�t�F��T�����Ƃ��ł��܂��B\n" +
				"�������ʂ̕W���\���́A�n�}�r���[�ł��B�E��ɂ���u���X�g�v�{�^�����^�b�v����ƁA\n" +
				"���X�g�r���[�ɐؑւ����܂��B�Ȃ��A���ݒn�ȊO�̃J�t�F��T�������ꍇ�́A�n�}�r���[/���X�g�r���[�̏㕔�ɂ��錟���{�b�N�X�ŁA\n" +
				"���̏ꏊ�̏Z�����邢�͋߂��̉w/�����h�}�[�N����͂���������ƈꗗ���\������܂��B\n" +
				"\n" +
				"�������I�v�V�������g�����Ȃ���\n" +
				"�����I�v�V�����ł́A�T���������Ȃ��̍D�݂ɐݒ肷�邱�Ƃ��ł��܂��B�������������X�̎�ނ�I�񂾂�A�����������Ƀ`�F�b�N������Ƃ��Ȃ��d�l�̃J�t�F�����A�v���ɁB\n" +
				"�Ȃ��A�����������Ƀ`�F�b�N�𑽂�����߂���ƁA�G���A�ɂ���Ă͌������ʐ�����������ꍇ������܂��B���̏ꍇ�͏������ɂ߂Ă��������B\n" +
				"�����O����LAN�T�[�r�X�ɂ��āF���ۂɗ��p�ł���T�[�r�X�͓X�܂ɂ��₢���킹���������B\n" +
				"\n" +
				"�������͂���������\n" +
				"���Ȃ����J�t�F�ɍs�����Ƃ��A���̂��X�������Ă�����X�܂̏ڍ׃y�[�W�ɂ���u�������Ă�v�{�^�����^�b�v���Ă��������B�A�v�����p�҂ł��̏�񂪋��L����A����������ɕ֗��ɂȂ�܂��B\n" +
				"���N�����������͑��̐l�ɋ��L����܂���B\n" +
				"\n" +
				"���]�����悤\n" +
				"���Ȃ�������J�t�F���C�ɓ�������A�X�܂̏ڍ׃y�[�W�ɂ���u�i�C�X�I�v�������Ă��������B�A�v�����p�҂ł��̏�񂪋��L����A����������ɕ֗��ɂȂ�܂��B\n" +
				"�����X�g�r���[�̃\�[�g�u�i�C�X���v�ɗ��p����܂��B\n" +
				"���N�����������͑��̐l�ɋ��L����܂���B\n" +
				"\n" +
				"�����C�ɓ���\n" +
				"�C�ɓ������J�t�F�A�C�ɂȂ�J�t�F������΁u���C�ɓ���v�ɓo�^���܂��傤�I�@�o�^������ƁA�t�b�^�[���j���[�u���C�ɓ���v����ȒP�ɃA�N�Z�X�ł���悤�ɂȂ�܂��B�o�^���@�́A�X�܂̏ڍ׃y�[�W�E��ɂ���u���C�ɓ���v�{�^�����^�b�v���Ă��������B����ŁA�t�b�^�[���j���[�́u���C�ɓ���v�ɕۑ�����܂��B���C�ɓ���ɓo�^�ł��錏���́A30���ł��B\n");
		
		text2.setText("\n" +
				"�����X���̓��e�E�ҏW�ɂ���\n" +
				"���X�̏�񂪖����ꍇ�́A�X�܏��𓊍e���邱�Ƃ��ł��܂��B���Ȃ����ҏW�҂ɂȂ��Ă��X���𓊍e�E�ҏW���Ă݂܂��񂩁H\n" +
				"\n" +
				"�����e�܂ł̎菇��\n" +
				"1.�Y���̓X�܂��������܂�\n" +
				"2.�������ʂɖ����ꍇ�͉�ʍ���ɂ���u�{�v�{�^�����^�b�v\n" +
				"3.��ʂ̎w���ɏ]�����X������͂��u�ۑ��v�{�^�����^�b�v\n" +
				"\n" +
				"�����X����ҏW����ꍇ��\n" +
				"1.���̏C�������������X�̏ڍ׃y�[�W���J���܂�\n" +
				"2.�����́u�X�܏��v���^�b�v\n" +
				"3.���j���[�u���̓X�܏���ҏW����v���^�b�v\n" +
				"4.�C���ӏ����C�����u�ۑ��v�{�^�����^�b�v\n" +
				"\n" +
				"�����X�����폜����ꍇ��\n" +
				"���X���͍폜���邱�Ƃ��ł��܂��B�X���Ă���ꍇ�́A���̍폜�ɂ����͂��������B\n" +
				"1.�폜���������X�̏ڍ׃y�[�W���J���܂�\n" +
				"2.�����́u�X�܏��v���^�b�v\n" +
				"3.���j���[�u���̓X�܏����폜����v���^�b�v\n" +
				"4.�폜�����̃��b�Z�[�W���\������܂�\n" +
				"\n" +
				"���X�܂̏d��/���񂹂ɂ��ā�\n" +
				"���Z���A�����̂̓X�܂��������݂���ꍇ�́A�Ǘ��ґ��œX�܂𖼊񂹂���ꍇ������܂��B���������������B\n" +
				"\n" +
				"�������ȓ��e�ɂ��ā�\n" +
				"�c�ƒ��̓X�܂𑽐��폜����A���邢�̓f�^�����ȏ���o�^���闘�p�҂ɂ��ẮA�����炩��x��������ꍇ������܂��B\n" +
				"�f�[�^�̕i���Ǘ��́A����I�Ƀ`�F�b�N���s���Ă܂���܂��B\n" +
				"���i���Ǘ��A�ҏW���ɂ��Ă͊Ǘ��ґ��ɂ���܂��B���������������B\n");
		StringBuilder sb = new StringBuilder();
		
		sb.append("<br />" +
				"�����̃A�v���ɂ���<br />" +
				"���́u�J�t�F�������v�́A3�l�̗L�u�ō���Ă��܂��B������Ή��G���A�̊g���@�\���P���s���Ă܂���܂��B<br />" +
				"<br />" +
				"Team Enjoy! This Product by...<br />" +
				"Producer�FKai<br />" +
				"Programmer�F�_���e<br />" +
				"illustration & Photo�F���R<br />" +
				"<br />" +
				"SupportSite�F<a href=\"http://d.hatena.ne.jp/fun_enjoy/\">Team Enjoy! �T�|�[�g�T�C�g</a><br />" +
				"�u���J�t�F�������v�̋L���^�O���������������B<br />" +
				"<br />" +
				"Information...<br />" +
				"<a href=\"http://blog.65x1.hacca.jp/?pid=1\">���R Website</a><br /><br />" +
				"<br />" +
				"");
		// LinkMovementMethod �̃C���X�^���X���擾���܂�
        MovementMethod movementmethod = LinkMovementMethod.getInstance();
        
        // TextView �� LinkMovementMethod ��o�^���܂�
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
