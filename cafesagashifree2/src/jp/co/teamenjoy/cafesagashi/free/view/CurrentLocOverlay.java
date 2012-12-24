package jp.co.teamenjoy.cafesagashi.free.view;


import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class CurrentLocOverlay extends ItemizedOverlay<OverlayItem>  {

	private OverlayItem locOverlayItem;
	
	public CurrentLocOverlay(Drawable defaultMarker) {
		super(defaultMarker);
		// TODO Auto-generated constructor stub
		boundCenterBottom(defaultMarker);
	}

	void setLocation(GeoPoint gp, String title, String desc) {
        locOverlayItem = new OverlayItem(gp, title, desc);
        populate();
    }
	
	 /**
     * @return nullÇÃâ¬î\ê´Ç‡Ç†ÇÈ
     */
    GeoPoint currentLocation() {
        return locOverlayItem.getPoint();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return locOverlayItem;
    }

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
