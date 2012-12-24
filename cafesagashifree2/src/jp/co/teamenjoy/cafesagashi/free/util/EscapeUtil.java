package jp.co.teamenjoy.cafesagashi.free.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * æ–?­—å?ã«ã‚¨ã‚¹ã‚±ãƒ¼ãƒ—å?ç?‚’è¡Œã†ã‚¯ãƒ©ã‚¹
 *
 * @author g
 *
 */
public class EscapeUtil {

    /**
     * æ–?­—å?ã«å«ã¾ã‚Œã‚‹HTMLç‰¹æ®Šæ–‡å­—ã‚’ã‚¨ã‚¹ã‚±ãƒ¼ãƒ—å¤‰æ›ã™ã‚‹ãƒ¡ã‚½ãƒ?ƒ‰
     *
     * @param p å¤‰æ›å…?–‡å­—å?
     * @return å¤‰æ›å¾Œæ–‡å­—å?
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
     * ã‚ªãƒ–ã‚¸ã‚§ã‚¯ãƒˆã‚’æ–?­—å?ã«å¤‰æ›ã™ã‚‹ãƒ¡ã‚½ãƒ?ƒ‰
     *
     * @param object s å¤‰æ›å…?–‡å­—å?
     * @return å¤‰æ›å¾Œæ–‡å­—å?
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
     * æ–?­—å?ã«å«ã¾ã‚Œã‚‹HTMLç‰¹æ®Šæ–‡å­—ã‚’ã‚¨ã‚¹ã‚±ãƒ¼ãƒ—å¤‰æ›ã™ã‚‹ãƒ¡ã‚½ãƒ?ƒ‰
     *
     * @param object s å¤‰æ›å…?–‡å­—å?
     * @return å¤‰æ›å¾Œæ–‡å­—å?
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
     * æ–?­—å?ã«å«ã¾ã‚Œã‚‹SQLç‰¹æ®Šæ–‡å­—ã‚’ã‚¨ã‚¹ã‚±ãƒ¼ãƒ—å?ç?™ã‚‹ãƒ¡ã‚½ãƒ?ƒ‰
     * 
     * @param p å¤‰æ›å…?–‡å­—å?
     * @return å¤‰æ›å¾Œæ–‡å­—å?
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