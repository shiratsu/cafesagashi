package jp.co.teamenjoy.cafesagashi.free.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �?���?にエスケープ�?�?��行うクラス
 *
 * @author g
 *
 */
public class EscapeUtil {

    /**
     * �?���?に含まれるHTML特殊文字をエスケープ変換するメソ�?��
     *
     * @param p 変換�?��字�?
     * @return 変換後文字�?
     */
    static public String toHtmlString(String p) {
        String str = "";
        if(p != null){
            for (int i = 0; i < p.length(); i++) {
                Character c = new Character(p.charAt(i));
                switch (c.charValue()) {
                    case '&' :
                        str = str.concat("&amp;");
                        break;
                    case '<' :
                        str = str.concat("&lt;");
                        break;
                    case '>' :
                        str = str.concat("&gt;");
                        break;
                    case '"' :
                        str = str.concat("&quot;");
                        break;
                    case '\'' :
                        str = str.concat("&apos;");
                        break;
                    default :
                        str = str.concat(c.toString());
                        break;
                }
            }
        }
        return str;
    }
    /**
     * オブジェクトを�?���?に変換するメソ�?��
     *
     * @param object s 変換�?��字�?
     * @return 変換後文字�?
     */
    static public String toStringForObject(Object s) {
        String str = "";
        String p = "";
        if(s instanceof Object){
            if(s != null){
                p = s.toString();
            }
                
        }
        return p;
    }
    
    /**
     * �?���?に含まれるHTML特殊文字をエスケープ変換するメソ�?��
     *
     * @param object s 変換�?��字�?
     * @return 変換後文字�?
     */
    static public String toHtmlStringForObject(Object s) {
        String str = "";
        String p = "";
        if(s instanceof Object){
            if(s != null){
                p = s.toString();
            }
                
        }
        if(p != null){
            for (int i = 0; i < p.length(); i++) {
                Character c = new Character(p.charAt(i));
                switch (c.charValue()) {
                    case '&' :
                        str = str.concat("&amp;");
                        break;
                    case '<' :
                        str = str.concat("&lt;");
                        break;
                    case '>' :
                        str = str.concat("&gt;");
                        break;
                    case '"' :
                        str = str.concat("&quot;");
                        break;
                    case '\'' :
                        str = str.concat("&apos;");
                        break;
                    default :
                        str = str.concat(c.toString());
                        break;
                }
            }
        }
        
        return str;
    }
    
    /**
     * �?���?に含まれるSQL特殊文字をエスケープ�?�?��るメソ�?��
     * 
     * @param p 変換�?��字�?
     * @return 変換後文字�?
     */
    static public String toSqlString(String p) {
        String str = "";
        for (int i = 0; i < p.length(); i++) {
            Character c = new Character(p.charAt(i));
            switch (c.charValue()) {
                case '\'' :
                    str = str.concat("''");
                    break;
                case '\\' :
                    str = str.concat("\\\\");
                    break;
                default :
                    str = str.concat(c.toString());
                    break;
            }
        }
        return str;
    }
}