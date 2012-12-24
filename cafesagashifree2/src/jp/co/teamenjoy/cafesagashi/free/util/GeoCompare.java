package jp.co.teamenjoy.cafesagashi.free.util;

import java.util.Comparator;
import java.util.HashMap;

public class GeoCompare implements Comparator<HashMap>  {

    //
    private boolean order = true;  
     
    public void setOrder(boolean order) {  
        this.order = order;  
    }

    public int compare(HashMap m0, HashMap m1) {  
        if (this.order) {  
         //è∏èá  
         return ((Integer) m0.get("sortDistance")).compareTo((Integer) m1.get("sortDistance"));  
        } else {  
         //ç~èá 
         return ((Integer) m1.get("sortDistance")).compareTo((Integer) m0.get("sortDistance"));
        }  
       }  

}
