package com.zenip.camerawallpaper;

import java.lang.reflect.Method;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

@TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
public class MyWallpaperService extends WallpaperService {

	static final String TAG = "MyWallpaperService";
	
	@Override
	public Engine onCreateEngine() {
		Log.d(TAG, "----->onCreateEngine");
		return new MoveLiveWallpaperEngine();
	}
	
	@SuppressLint("NewApi")
	public class MoveLiveWallpaperEngine extends Engine {

		SurfaceHolder holder;
		Camera mCamera;

		public MoveLiveWallpaperEngine() {

		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			holder = new VideoSurfaceHolder(surfaceHolder);
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		@Override
		public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {

		}

		@Override
		public void onDestroy() {
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.setPreviewCallback(null);
				mCamera.release();
				mCamera = null;
			}

			super.onDestroy();
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if (visible) {
				startVideo();
			} else {
				stopVideo();
			}
		}

		/*
		 * 设置相机的预览角度,在2.2以上可以直接使用setDisplayOrientation
		 * 
		 * @param orientation 相机的预览角度
		 */
		private void setDisplayOrientation(Camera camera, int orientation) {
			Method setCameraDisplayOrientation;
			try {
				// 通过反射，获取相应的方法
				setCameraDisplayOrientation = mCamera.getClass().getMethod(
						"setDisplayOrientation", new Class[] { int.class });
				if (setCameraDisplayOrientation != null) {
					setCameraDisplayOrientation.invoke(mCamera,
							new Object[] { orientation });
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void startVideo() {
			Log.d(TAG, "----->startVideo");
			
			Resources resources = getResources();
			int orientation = resources.getConfiguration().orientation;

			try {
				
				boolean frontSuccess = false;
				try{
					if (Camera.getNumberOfCameras() > 1 && LiveWallpaperSettings.getUseFrontCamera(getApplicationContext())) {
						mCamera = Camera.open(1);
						frontSuccess = true;
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				if (!frontSuccess) {
					mCamera = Camera.open();
				}
				
				if (orientation == Configuration.ORIENTATION_PORTRAIT) {
					setDisplayOrientation(mCamera, 90);
				} else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
					setDisplayOrientation(mCamera, 0);
				} 
				CameraUtil.setPreviewSize(mCamera);
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public void stopVideo() {
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.setPreviewCallback(null);
				mCamera.release();
				mCamera = null;
			}
		}
	}

	public class VideoSurfaceHolder implements SurfaceHolder {

		private SurfaceHolder surfaceHolder;

		public VideoSurfaceHolder(SurfaceHolder holder) {
			this.surfaceHolder = holder;
		}

		@Override
		public void addCallback(Callback callback) {
			surfaceHolder.addCallback(callback);
		}

		@Override
		public void removeCallback(Callback callback) {
			surfaceHolder.removeCallback(callback);
		}

		@Override
		public boolean isCreating() {
			return surfaceHolder.isCreating();
		}

		@Override
		public void setType(int type) {
			surfaceHolder.setType(type);
		}

		@Override
		public void setFixedSize(int width, int height) {
			surfaceHolder.setFixedSize(width, height);
		}

		@Override
		public void setSizeFromLayout() {
			surfaceHolder.setSizeFromLayout();
		}

		@Override
		public void setFormat(int format) {
			surfaceHolder.setFormat(format);
		}

		@Override
		public void setKeepScreenOn(boolean screenOn) {
			surfaceHolder.setKeepScreenOn(screenOn);
		}

		@Override
		public Canvas lockCanvas() {

			return surfaceHolder.lockCanvas();
		}

		@Override
		public Canvas lockCanvas(Rect dirty) {
			// TODO Auto-generated method stub
			return surfaceHolder.lockCanvas(dirty);
		}

		@Override
		public void unlockCanvasAndPost(Canvas canvas) {
			surfaceHolder.unlockCanvasAndPost(canvas);
		}

		@Override
		public Rect getSurfaceFrame() {
			return surfaceHolder.getSurfaceFrame();
		}

		@Override
		public Surface getSurface() {
			return surfaceHolder.getSurface();
		}

	}
}
