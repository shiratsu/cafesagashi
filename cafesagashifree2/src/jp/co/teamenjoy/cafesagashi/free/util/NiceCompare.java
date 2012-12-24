package jp.co.teamenjoy.cafesagashi.free.util;

import java.util.Comparator;
import java.util.HashMap;


public class NiceCompare implements Comparator<HashMap>  {

    private boolean order = true;  
     
    public void setOrder(boolean order) {  
        this.order = order;  
    }

    public int compare(HashMap m0, HashMap m1) {  
        if (this.order) {  
         //¸‡ 
         return ((Integer) m1.get("iine")).compareTo((Integer) m0.get("iine"));
        } else {  
         //~‡ 
         return ((Integer) m0.get("iine")).compareTo((Integer) m1.get("iine"));
        }  
       }  

}
