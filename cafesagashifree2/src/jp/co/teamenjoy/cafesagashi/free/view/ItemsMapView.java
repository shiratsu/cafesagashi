package jp.co.teamenjoy.cafesagashi.free.view;

import java.util.Collection;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;

import jp.co.teamenjoy.cafesagashi.free.R;
import jp.co.teamenjoy.cafesagashi.free.CafeDetailPageActivity;
import jp.co.teamenjoy.cafesagashi.free.LinePageActivity;
import jp.co.teamenjoy.cafesagashi.free.MapPageActivity;
import jp.co.teamenjoy.cafesagashi.free.PrefPageActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ItemsMapView extends MapView {

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
	
	public ItemsMapView(Context context, AttributeSet attrs) {
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
    public void addStab(GeoPoint gp, String title, String cafeId, ItemsMapView mapView, MapPageActivity mapPageActivity) {
        if (stabOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_1);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            stabOverlay = new CafeOverlay(cafe,mapView,mapPageActivity);
            getOverlays().add(stabOverlay);
        }
        stabOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * ドトールを配置する
     */
    public void addDtour(GeoPoint gp, String title, String cafeId, ItemsMapView mapView, MapPageActivity mapPageActivity) {
        if (dtourOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_2);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            dtourOverlay = new CafeOverlay(cafe,mapView,mapPageActivity);
            getOverlays().add(dtourOverlay);
        }
        dtourOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * サンマルクカフェを配置する
     */
    public void addSanmaruk(GeoPoint gp, String title, String cafeId, ItemsMapView mapView, MapPageActivity mapPageActivity) {
        if (sanmarukuOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_4);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            sanmarukuOverlay = new CafeOverlay(cafe,mapView,mapPageActivity);
            getOverlays().add(sanmarukuOverlay);
        }
        sanmarukuOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * タリーズを配置する
     */
    public void addTurrys(GeoPoint gp, String title, String cafeId, ItemsMapView mapView, MapPageActivity mapPageActivity) {
        if (turrysOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_3);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            turrysOverlay = new CafeOverlay(cafe,mapView,mapPageActivity);
            getOverlays().add(turrysOverlay);
        }
        turrysOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * Verocheを配置する
     */
    public void addVeroche(GeoPoint gp, String title, String cafeId, ItemsMapView mapView, MapPageActivity mapPageActivity) {
        if (verocheOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_7);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            verocheOverlay = new CafeOverlay(cafe,mapView,mapPageActivity);
            getOverlays().add(verocheOverlay);
        }
        verocheOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * エクセルシオールを配置する
     */
    public void addExcelShiorl(GeoPoint gp, String title, String cafeId, ItemsMapView mapView, MapPageActivity mapPageActivity) {
        if (excelShiorlOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_5);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            excelShiorlOverlay = new CafeOverlay(cafe,mapView,mapPageActivity);
            getOverlays().add(excelShiorlOverlay);
        }
        excelShiorlOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * シアトルズベストを配置する
     */
    public void addShiatols(GeoPoint gp, String title, String cafeId, ItemsMapView mapView, MapPageActivity mapPageActivity) {
        if (shiatolsOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_6);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            shiatolsOverlay = new CafeOverlay(cafe,mapView,mapPageActivity);
            getOverlays().add(shiatolsOverlay);
        }
        shiatolsOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * カフェドクリエを配置する
     */
    public void addCafedo(GeoPoint gp, String title, String cafeId, ItemsMapView mapView, MapPageActivity mapPageActivity) {
        if (cafedoOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_8);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            cafedoOverlay = new CafeOverlay(cafe,mapView,mapPageActivity);
            getOverlays().add(cafedoOverlay);
        }
        cafedoOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * コメダを配置する
     */
    public void addKomeda(GeoPoint gp, String title, String cafeId, ItemsMapView mapView, MapPageActivity mapPageActivity) {
        if (komedaOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_9);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            komedaOverlay = new CafeOverlay(cafe,mapView,mapPageActivity);
            getOverlays().add(komedaOverlay);
        }
        komedaOverlay.addCafe(gp, title, cafeId);
        invalidate();
    }
    /*
     * 個店を配置する
     */
    public void addKoten(GeoPoint gp, String title, String cafeId, ItemsMapView mapView, MapPageActivity mapPageActivity) {
        if (kotenOverlay == null) {
            Drawable cafe = r.getDrawable(R.drawable.pins_z);
            ((BitmapDrawable)cafe).setAntiAlias(true);
            cafe.setBounds(0, 0, cafe.getIntrinsicWidth(), cafe
                    .getIntrinsicHeight());
            kotenOverlay = new CafeOverlay(cafe,mapView,mapPageActivity);
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
        	kotenOverlay=null;
        }
        if (komedaOverlay != null) {
        	komedaOverlay.clearCafe();
        	komedaOverlay=null;
        }
        if (cafedoOverlay != null) {
        	cafedoOverlay.clearCafe();
        	cafedoOverlay=null;
        }
        if (shiatolsOverlay != null) {
        	shiatolsOverlay.clearCafe();
        	shiatolsOverlay=null;
        }
        if (excelShiorlOverlay != null) {
        	excelShiorlOverlay.clearCafe();
        	excelShiorlOverlay=null;
        }
        if (verocheOverlay != null) {
        	verocheOverlay.clearCafe();
        	verocheOverlay=null;
        }
        if (sanmarukuOverlay != null) {
        	sanmarukuOverlay.clearCafe();
        	sanmarukuOverlay=null;
        }
        if (turrysOverlay != null) {
        	turrysOverlay.clearCafe();
        	turrysOverlay=null;
        }
        if (dtourOverlay != null) {
        	dtourOverlay.clearCafe();
        	dtourOverlay=null;
        }
        if (stabOverlay != null) {
        	stabOverlay.clearCafe();
        	stabOverlay=null;
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
    public class CafeOverlay extends ItemizedOverlay<OverlayItem> implements OnClickListener {

        private LinkedList<OverlayItem> list;
        private ItemsMapView mapView;
        private MapPageActivity activity;
        final View popupView;
        final PopupWindow popupWindow;
        
        public CafeOverlay(Drawable defaultMarker,ItemsMapView mapView, MapPageActivity mapPageActivity) {
            super(defaultMarker);
            
            this.mapView = mapView;	
            this.activity = mapPageActivity;
            list = new LinkedList<OverlayItem>();
            setLastFocusedIndex(-1);
            // markerの基点を(0,0)から(0.5, 1)にする
            boundCenterBottom(defaultMarker);
            ((BitmapDrawable)defaultMarker).setAntiAlias(false);
            
            popupView = LayoutInflater.from(this.activity).inflate(R.layout.cafe_popup, (ViewGroup) mapView.getParent(), false);
            popupView.setOnClickListener((OnClickListener) this);
            popupWindow = new PopupWindow(popupView, 220, 60);

            
        }

        /**
         * マップのActivityを返す
         * @return
         */
        private MapPageActivity getActivity() {
			// TODO Auto-generated method stub
			return this.activity;
		}

		public void clearCafe() {
			// TODO Auto-generated method stub
			if (list != null) {
                list.clear();
                setLastFocusedIndex(-1);
                populate();
                list = new LinkedList<OverlayItem>();
            }
		}

		void addCafe(GeoPoint gp, String title, String desc) {
            OverlayItem cafe = new OverlayItem(gp, title, desc);
            list.add(cafe);
            populate();
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
            GeoPoint geo = cafe.getPoint();
            Point pt = this.mapView.getProjection().toPixels(geo, null);
            
            setFocus(cafe);   //前面に表示する
            invalidate();
            final String msg = cafe.getTitle();
            
            TextView titleText = (TextView)popupView.findViewById(R.id.cafename_view);
	       	titleText.setText(msg);
	   		Button btnDetail=(Button)popupView.findViewById(R.id.detailButton);
	   		btnDetail.setOnClickListener(new OnClickListener(){
	   		    @Override
	   		    public void onClick(View v){
	   		    	
	   		    	Intent intent = new Intent(getActivity(),CafeDetailPageActivity.class);
			        intent.putExtra("cafeId", cafe.getSnippet());
			        getActivity().startActivity(intent);
	   		    }
	   	    });
	   		this.popupWindow.showAtLocation((ViewGroup) this.mapView.getParent(), Gravity.NO_GRAVITY, pt.x, pt.y);
            
            return true;
        }

        @Override
        public boolean onTap(GeoPoint p, MapView mapView) {
            dismissPopup(true);
            return super.onTap(p, mapView);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event, MapView mapView) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                dismissPopup(true);
            }
            return super.onTouchEvent(event, mapView);
        }
        public void dismissPopup(boolean fade) {
            if (!fade) {
                popupWindow.setAnimationStyle(0);
                popupWindow.update();
            }
            
            popupWindow.dismiss();
        }
        
        
        
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
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
