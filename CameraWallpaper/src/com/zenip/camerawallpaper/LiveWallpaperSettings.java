package com.zenip.camerawallpaper;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest.Builder;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.zenip.common.UmengBaseActivity;
@SuppressLint("NewApi")
public class LiveWallpaperSettings extends UmengBaseActivity implements OnClickListener, OnCheckedChangeListener{

	private InterstitialAd intersitialAd;
	
	private StartAppAd startapp = new StartAppAd(this);
	
	private CheckBox mCheckBox;
	
	private static final String PREF_KEY_FRONT_CAMERA = "front_camera";
	
	private boolean mFrontCamera;
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		setUseFrontCamera(this, isChecked);
	} 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG) {
			StartAppSDK.init(this, "DeveloperID", "ApplicationID");
		} else  {
			StartAppSDK.init(this, "106343400", "206781441");
		}
		StartAppAd.showSplash(this, null);
		
		setContentView(R.layout.settings);
		
		
		findViewById(R.id.Button01).setOnClickListener(this);
		findViewById(R.id.Button02).setOnClickListener(this);
		
		int cameraCount = Camera.getNumberOfCameras();
		
		mCheckBox = (CheckBox) findViewById(R.id.camera2);
		mCheckBox.setOnCheckedChangeListener(this);
		if (cameraCount == 1) {
			mCheckBox.setVisibility(View.GONE);
		}

		intersitialAd = GoogleAdmob.requestIntersitionAd(this);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.adlayout);
		layout.addView(GoogleAdmob.createLayoutWithAd(this));
		
		startapp.loadAd();
		
		mFrontCamera = getUseFrontCamera(this);
		mCheckBox.setChecked(mFrontCamera);
		
		
	    
	}
	
	public static SharedPreferences getCameraPref(Context context) {
		return context.getSharedPreferences("camera_pref", Context.MODE_PRIVATE);
	}
	
	public static void setUseFrontCamera(Context context, boolean useFrontCamera) {
		getCameraPref(context).edit().putBoolean(PREF_KEY_FRONT_CAMERA, useFrontCamera).commit();
	}
	
	public static boolean getUseFrontCamera(Context context) {
		return getCameraPref(context).getBoolean(PREF_KEY_FRONT_CAMERA, false);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		startapp.onResume();
	};
	
	@Override
	protected void onPause() {
		super.onPause();
		startapp.onPause();
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		startapp.onBackPressed();
	}
	
	public void showIntersitialAd() {
		if(this.cb.hasCachedInterstitial()) {
			 // Show an interstitial. This and other interstital/MoreApps cache/show calls should be used after onStart().
		    this.cb.showInterstitial();
		}
		
//		else if (intersitialAd.isLoaded()) {
//			intersitialAd.setAdListener(new AdListener() {
//				@Override
//				public void onAdOpened() {
//				}
//				@Override
//				public void onAdClosed() {
//					intersitialAd.setAdListener(this);
//					intersitialAd.loadAd(new Builder().build());
//				}
//			});
//			intersitialAd.show();
//		} else {
//			startapp.onBackPressed();
//		}
	}
	
	@Override
	public void onClick(View v) {
		if (R.id.Button02 == v.getId()) {
			Intent intent = new Intent();
			int sdkInt = Build.VERSION.SDK_INT;
			if (sdkInt > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				try { 
					intent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
					ComponentName componentName = new ComponentName(MyWallpaperService.class.getPackage().getName()
							, MyWallpaperService.class.getCanonicalName());
					intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, componentName);
					startActivityForResult(intent, 0);
				} catch (Exception e) {
					intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
					startActivityForResult(intent, 0);
				} 
			}
			else {
				intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
				startActivityForResult(intent, 0);
			}

			showIntersitialAd();
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
