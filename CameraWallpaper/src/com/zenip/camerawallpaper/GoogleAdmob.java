package com.zenip.camerawallpaper;

import java.util.WeakHashMap;

import android.app.Activity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class GoogleAdmob {
    
    public static LinearLayout createLayoutWithAd(Activity activity) {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        AdView goodAdv = new AdView(activity); //, com.google.ads.AdSize.BANNER, GOOGLE_AD_ID
        if (LiveWallpaperSettings.DEBUG) {
        	goodAdv.setAdUnitId("");
		} else {
			goodAdv.setAdUnitId("ca-app-pub-7879767097814866/4363554030");
		}
        goodAdv.setAdSize(AdSize.BANNER);
        layout.addView(goodAdv);
        AdRequest.Builder builder = new Builder();
        goodAdv.loadAd(builder.build());
        return layout;
    } 
    
    
    public static synchronized InterstitialAd requestIntersitionAd(Activity activity) {
    	InterstitialAd ad = mFullScreenAdMap.get(activity);
    	if (null == ad) {
    		System.out.println("requestIntersitionAd new create");
    		ad =  new InterstitialAd(activity);
    		if (LiveWallpaperSettings.DEBUG) {
    			ad.setAdUnitId("");
    		} else {
    			ad.setAdUnitId("ca-app-pub-7879767097814866/2886820835");
    		}
    		
    		mFullScreenAdMap.put(activity, ad);
        	ad.loadAd(new Builder().build());
    	} else {
    		System.out.println("requestIntersitionAd from cache");
    	}
    	return ad;
    }
    
    private static WeakHashMap<Activity, InterstitialAd> mFullScreenAdMap = new WeakHashMap<Activity, InterstitialAd>();
}
