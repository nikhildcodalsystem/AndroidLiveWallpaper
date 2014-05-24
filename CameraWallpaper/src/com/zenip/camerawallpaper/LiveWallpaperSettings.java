package com.zenip.camerawallpaper;

import com.google.ads.InterstitialAd;
import com.zenip.common.UmengBaseActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class LiveWallpaperSettings extends UmengBaseActivity implements OnClickListener{

	private InterstitialAd intersitialAd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		findViewById(R.id.Button01).setOnClickListener(this);
		findViewById(R.id.Button02).setOnClickListener(this);

		intersitialAd = GoogleAdmob.requestIntersitionAd(this);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.adlayout);
		layout.addView(GoogleAdmob.createLayoutWithAd(this));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	

	@Override
	public void onClick(View v) {
		if (R.id.Button02 == v.getId()) {
			Intent intent = new Intent();
			int sdkInt = Build.VERSION.SDK_INT;
			if (sdkInt > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				intent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
				ComponentName componentName = new ComponentName(MyWallpaperService.class.getPackage().getName()
						, MyWallpaperService.class.getCanonicalName());
				intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, componentName);
			}
			else {
				intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
			}

			startActivityForResult(intent, 0);
			intersitialAd.show();
		}
		
		else if (R.id.Button01 == v.getId()) {
			viewAppInMarket(this, "com.zhenby.blink");
		}
	}
    
    public static void viewAppInMarket(Context context, String packageName){
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        try{
            context.startActivity(intent);
        }catch(ActivityNotFoundException e){
            e.printStackTrace();
        }
    } 
}
