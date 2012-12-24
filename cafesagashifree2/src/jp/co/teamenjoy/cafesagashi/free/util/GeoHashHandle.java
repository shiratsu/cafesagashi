package jp.co.teamenjoy.cafesagashi.free.util;

import java.util.ArrayList;
import java.util.List;

public class GeoHashHandle {

	/**
	 * 
	 * @param lat
	 * @param lon
	 * @param distance 
	 * @return
	 */
	public static List<String> feedCalcLatLon(double lat, double lon, double distance) {
		// TODO Auto-generated method stub
		int base = 30;
		double baseDist = 0.000277778;
		
		List<String> latlonAry = new ArrayList<String>();
		
		double overLat = lat+(baseDist*distance/base);
		double overLon = lon+(baseDist*distance/base);
		
		double underLat = lat-(baseDist*distance/base);
		double underLon = lon-(baseDist*distance/base);
		latlonAry.add(String.valueOf(underLat));
		latlonAry.add(String.valueOf(underLon));
		latlonAry.add(String.valueOf(overLat));
		latlonAry.add(String.valueOf(overLon));
		return latlonAry;
	}

	
}
