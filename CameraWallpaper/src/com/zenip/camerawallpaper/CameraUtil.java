package com.zenip.camerawallpaper;

import java.util.List;
import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;

public class CameraUtil {

	static final String TAG = "CameraUtil";

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public static void setPreviewSize(Camera camera) {
		Log.d(TAG, "----->setPreviewSize()");
		if(camera == null ) {
			return;
		}
		// set optimal preview size
    	Camera.Parameters parameters = camera.getParameters();
    	Log.d(TAG, "current preview size: " + parameters.getPreviewSize().width + ", " + parameters.getPreviewSize().height);
    	
    	Camera.Size current_size = parameters.getPictureSize();
    	
    	Log.d(TAG, "current size: " + current_size.width + ", " + current_size.height);
        
    	List<Camera.Size> preview_sizes = parameters.getSupportedPreviewSizes();
        if( preview_sizes.size() > 0 ) {
	        Camera.Size best_size = preview_sizes.get(0);
	        for(Camera.Size size : preview_sizes) {
	    			Log.d(TAG, "    supported preview size: " + size.width + ", " + size.height);
	        	if( size.width*size.height > best_size.width*best_size.height ) {
	        		best_size = size;
	        	}
	        }
            parameters.setPreviewSize(best_size.width, best_size.height);
    			Log.d(TAG, "new preview size: " + parameters.getPreviewSize().width + ", " + parameters.getPreviewSize().height);

    		/*List<int []> fps_ranges = parameters.getSupportedPreviewFpsRange();
    		if( MyDebug.LOG ) {
		        for(int [] fps_range : fps_ranges) {
	    			Log.d(TAG, "    supported fps range: " + fps_range[Camera.Parameters.PREVIEW_FPS_MIN_INDEX] + " to " + fps_range[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]);
		        }
    		}
    		int [] fps_range = fps_ranges.get(fps_ranges.size()-1);
	        parameters.setPreviewFpsRange(fps_range[Camera.Parameters.PREVIEW_FPS_MIN_INDEX], fps_range[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]);*/
    		camera.setParameters(parameters);
        }
	}
}
