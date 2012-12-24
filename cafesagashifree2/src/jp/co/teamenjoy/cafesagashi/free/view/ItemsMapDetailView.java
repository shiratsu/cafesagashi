package jp.co.teamenjoy.cafesagashi.free.view;

import java.util.Collection;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import jp.co.teamenjoy.cafesagashi.free.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ItemsMapDetailView extends MapView {

	private Resources r;
    private CafeOverlay stabOverlay;
    private CafeOverlay dtourOverlay;
    private CafeOverlay turrysOverlay;
    private CafeOverlay sanmarukuOverlay;
    private CafeOverlay verocheOverlay;
    private CafeOverlay excelShiorlOverlay;
    private CafeOverlay shiatolsOverlay;
    private CafeOverlay cafedoOverlay;
    private CafeOverlay komedaOverlay;
    private CafeOverlay kotenOverlay;
    
    private MapController controller;
    private CurrentLocOverlay curLocOverlay;
    private Context mContext;
    
    private PopupWindow mPopupWindow;
    private LayoutInflater inflater;
	
	public ItemsMapDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mContext = context;
        r = getResources();
        controller = getController();
        int zoomSize = 15;
        controller.setZoom(zoomSize);
	}

	public void moveGPoint(GeoPoint gp) {
        controller.animateTo(gp);
    }
    
    public void spanMap(int lat, int lng) {
        controller.zoomToSpan(lat, lng);
    }

    /**
     * @return nullの可能性もある
     */
    GeoPoint getMyLocation() {
        return curLocOverlay.currentLocation();
    }

    /*
     * スターバックスを配置する
     */
    public void addStab(GeoPoint gp, String title, String cafeId) {
        if (stabOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_1);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            stabOverlay = new CafeOverlay(cafe);
            getOverlays().add(stabOverlay);
        }
        stabOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * ドトールを配置する
     */
    public void addDtour(GeoPoint gp, String title, String cafeId) {
        if (dtourOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_2);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            dtourOverlay = new CafeOverlay(cafe);
            getOverlays().add(dtourOverlay);
        }
        dtourOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * サンマルクカフェを配置する
     */
    public void addSanmaruk(GeoPoint gp, String title, String cafeId) {
        if (sanmarukuOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_4);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            sanmarukuOverlay = new CafeOverlay(cafe);
            getOverlays().add(sanmarukuOverlay);
        }
        sanmarukuOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * タリーズを配置する
     */
    public void addTurrys(GeoPoint gp, String title, String cafeId) {
        if (turrysOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_3);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            turrysOverlay = new CafeOverlay(cafe);
            getOverlays().add(turrysOverlay);
        }
        turrysOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * Verocheを配置する
     */
    public void addVeroche(GeoPoint gp, String title, String cafeId) {
        if (verocheOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_7);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            verocheOverlay = new CafeOverlay(cafe);
            getOverlays().add(verocheOverlay);
        }
        verocheOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * エクセルシオールを配置する
     */
    public void addExcelShiorl(GeoPoint gp, String title, String cafeId) {
        if (excelShiorlOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_5);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            excelShiorlOverlay = new CafeOverlay(cafe);
            getOverlays().add(excelShiorlOverlay);
        }
        excelShiorlOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * シアトルズベストを配置する
     */
    public void addShiatols(GeoPoint gp, String title, String cafeId) {
        if (shiatolsOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_6);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            shiatolsOverlay = new CafeOverlay(cafe);
            getOverlays().add(shiatolsOverlay);
        }
        shiatolsOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * カフェドクリエを配置する
     */
    public void addCafedo(GeoPoint gp, String title, String cafeId) {
        if (cafedoOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_8);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            cafedoOverlay = new CafeOverlay(cafe);
            getOverlays().add(cafedoOverlay);
        }
        cafedoOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * コメダを配置する
     */
    public void addKomeda(GeoPoint gp, String title, String cafeId) {
        if (komedaOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_9);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            komedaOverlay = new CafeOverlay(cafe);
            getOverlays().add(komedaOverlay);
        }
        komedaOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * 個店を配置する
     */
    public void addKoten(GeoPoint gp, String title, String cafeId) {
        if (kotenOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_z);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            kotenOverlay = new CafeOverlay(cafe);
            getOverlays().add(kotenOverlay);
        }
        kotenOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    
    /*
     * 配置されているカフェをクリア 
     */
    public void clearCafes() {
        if (kotenOverlay != null) {
        	kotenOverlay.clearCafe();
        }
        if (komedaOverlay != null) {
        	komedaOverlay.clearCafe();
        }
        if (cafedoOverlay != null) {
        	cafedoOverlay.clearCafe();
        }
        if (shiatolsOverlay != null) {
        	shiatolsOverlay.clearCafe();
        }
        if (excelShiorlOverlay != null) {
        	excelShiorlOverlay.clearCafe();
        }
        if (verocheOverlay != null) {
        	verocheOverlay.clearCafe();
        }
        if (sanmarukuOverlay != null) {
        	sanmarukuOverlay.clearCafe();
        }
        if (turrysOverlay != null) {
        	turrysOverlay.clearCafe();
        }
        if (dtourOverlay != null) {
        	dtourOverlay.clearCafe();
        }
        if (stabOverlay != null) {
        	stabOverlay.clearCafe();
        }
        return;
    }

    
    

	/*
     * 現在地を設定する
     */
    public void setCurrentLocation(GeoPoint gp) {
        if (curLocOverlay == null) {
            Drawable human = r.getDrawable(R.drawable.icon);
            human.setBounds(0, 0, human.getIntrinsicWidth(), human
                    .getIntrinsicHeight());
            curLocOverlay = new CurrentLocOverlay(human);
            getOverlays().addAll((Collection<? extends Overlay>) curLocOverlay);
            postInvalidate();
        }
        curLocOverlay.setLocation(gp, "my location", "is here");
        invalidate();
    }

    /**
     * 地図上に複数個の駅を表示するためのオーバーレイ
     * @author adamrocker
     */
    public class CafeOverlay extends ItemizedOverlay<OverlayItem>{

        private LinkedList<OverlayItem> list;
        
        
        public CafeOverlay(Drawable defaultMarker) {
            super(defaultMarker);
            
            
            list = new LinkedList<OverlayItem>();
            // markerの基点を(0,0)から(0.5, 1)にする
            boundCenterBottom(defaultMarker);
            ((BitmapDrawable)defaultMarker).setAntiAlias(false);
            
        }

        

		public void clearCafe() {
			// TODO Auto-generated method stub
			
		}

		void addCafe(GeoPoint gp, String title, String desc) {
            OverlayItem cafe = new OverlayItem(gp, title, desc);
            list.add(cafe);
            populate();
        }

        void clearStations() {
            if (list != null) {
                list.clear();
                populate();
            }
        }

        @Override
        protected OverlayItem createItem(int index) {
            return list.get(index);
        }

        @Override
        public int size() {
            return list.size();
        }
        
        protected boolean onTap(int index) {
            final OverlayItem cafe =  createItem(index);
            
            setFocus(cafe);   //前面に表示する
            invalidate();
            String msg = cafe.getTitle();
            
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            return true;
        }
        
    }

    /**
     * 現在地に人のアイコンを表示するためのOverlay
     * @author adamrocker
     */
    private class CurrentLocOverlay extends ItemizedOverlay<OverlayItem> {
        private OverlayItem locOverlayItem;

        public CurrentLocOverlay(Drawable defaultMarker) {
            super(defaultMarker);
            boundCenterBottom(defaultMarker);
        }

        void setLocation(GeoPoint gp, String title, String desc) {
            locOverlayItem = new OverlayItem(gp, title, desc);
            populate();
        }

        /**
         * @return nullの可能性もある
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
            return locOverlayItem == null ? 0 : 1;
        }
    }

   
	
}
