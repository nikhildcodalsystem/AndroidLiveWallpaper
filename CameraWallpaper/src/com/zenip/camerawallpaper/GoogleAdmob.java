package com.zenip.camerawallpaper;

import java.util.WeakHashMap;

import android.app.Activity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.google.ads.AdRequest;
import com.google.ads.InterstitialAd;
import com.google.ads.ac;

public class GoogleAdmob {
    
    private static String GOOGLE_AD_ID = "a1520506c5f2244";

    public static LinearLayout createLayoutWithAd(Activity activity) {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        com.google.ads.AdView goodAdv = new com.google.ads.AdView(activity, com.google.ads.AdSize.BANNER, GOOGLE_AD_ID);
        layout.addView(goodAdv);
        goodAdv.loadAd(new AdRequest());
        
        return layout;
    } 
    
    
    public static synchronized InterstitialAd requestIntersitionAd(Activity activity) {
    	InterstitialAd ad = mFullScreenAdMap.get(activity);
    	if (null == ad) {
    		System.out.println("requestIntersitionAd new create");
    		ad =  new InterstitialAd(activity, GOOGLE_AD_ID);
    		mFullScreenAdMap.put(activity, ad);
        	ad.loadAd(new AdRequest());
    	} else {
    		System.out.println("requestIntersitionAd from cache");
    	}
    	return ad;
    }
    
    private static WeakHashMap<Activity, InterstitialAd> mFullScreenAdMap = new WeakHashMap<Activity, InterstitialAd>();
}
