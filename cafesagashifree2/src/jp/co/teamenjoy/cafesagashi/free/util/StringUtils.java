package jp.co.teamenjoy.cafesagashi.free.util;

import java.util.List;

public class StringUtils {

	public static String join(List<String> params, String string) {
		// TODO Auto-generated method stub
		
		//�z��ɕϊ� 
        String[] array = (String[])params.toArray(new String[0]); 
		return join(array,string);
	}
	public static String join(String[] arry, String with) {
        StringBuffer buf = new StringBuffer();
        for (String s: arry) {
            if (buf.length()>0) {
                buf.append(with);
            }
            buf.append(s);
         }
         return buf.toString();
    }
	
	/**
	 * ���{��\�L�̎��Ԃ��擾
	 * @param lastUpdateStr
	 * @return
	 */
	public static String feedJapanTimeText(String lastUpdateStr) {
		
		String timeText = null;
		if(lastUpdateStr != null){
			String[] timeParam = lastUpdateStr.split("[-/: ]");
			if(timeParam.length == 6){
				StringBuilder sb = new StringBuilder();
				sb.append(timeParam[0]+"�N");
				sb.append(timeParam[1]+"��");
				sb.append(timeParam[2]+"��");
				sb.append(timeParam[3]+"��");
				sb.append(timeParam[4]+"��");
				timeText = sb.toString();
				
			}else{
				timeText="�Z�b�g����Ă܂���";
			}
		}
		return timeText;
	}
}
