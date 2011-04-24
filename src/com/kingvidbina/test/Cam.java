package com.kingvidbina.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.AsyncTask;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.view.OrientationEventListener;
import android.hardware.SensorManager;

import com.kingvidbina.test.ScopeView;
import com.kingvidbina.test.ImageProcessor;

import java.io.IOException;
import java.lang.reflect.Array;

// TODO: rework all try - catch blocks to remove catching generic exceptions
public class Cam extends Activity implements Camera.PreviewCallback
{
    /* don't ever forget to add uses-features
     * and uses-permission tags before the application
     * tags in the manifest. This cause me a lot of trouble
     * and cost me muck precious time.
     */
    public static final String TAG = "Test";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private Context mContext;
    private ScopeView mScope;
    private Parameters mParams;
    private Scanner mScanner;

    @Override
    public void onCreate(Bundle icicle){
	Log.v(TAG, "onCreate()");
	super.onCreate(icicle);
	try{
	    setContentView(R.layout.cam);
	}catch(Exception e){
	    // TODpO: remove generic exception
	    Log.v(TAG, "activity:Cam - inflate exception: " + e.toString());
	}
	// start thread to load cam
	Log.v(TAG, this.toString());
	// set listener for orientation events
	// set the SurfaceView
	setView();
	// init cam in seperate thread
	if(mCamera == null){
	    new InitCamera().execute();
	}
	Log.d(TAG, "create done");
    }

    @Override
    public void onPause(){
	Log.v(TAG, "onPause()");
	mScope.haltPreview();
	// stop cam if existent
	try{
	    if(mCamera != null){ 
		mCamera.setPreviewCallback(null);
		mCamera.release(); 
		Log.v(TAG, "camera released");
	    }
	}catch(Exception e){
	    // TODO: remove generic exception
	    Log.e(TAG, "onPause exception: " + e.toString());
	}
	/* painfull lesson learned invoke super method
	 * spend a few hours troubleshooting for the source
	 * of that dreaded Force Close m*f*er
	 */
	Log.v(TAG, "<<");
	super.onPause();
	Log.v(TAG, ">>");
    }
    
    @Override
    public void onStop(){
	Log.v(TAG, "onStop()");
	super.onStop();
    }

    public void onReady(){
	Log.v(TAG, "onReady()");
	mCamera.setPreviewCallback(this);
	mScope.showPreview();
	Log.v(TAG, "preview started");
	Log.v(TAG, "preview callback set");
    }
    
    /**
     * receives and pushes frames to the image processing objects
     */
    public void onPreviewFrame(byte[] data, Camera camera){
	int len = Array.getLength(data);
    }

    /** sets the View after camera has been initialized */
    private void setView(){
	Log.v(TAG, "setView()");
	if(mScope == null){
	    mScope = (ScopeView) this.findViewById(R.id.scope);
       	}
    }

    private class InitCamera extends AsyncTask <Integer, Void, Camera>{
	/* assigning values to the Integer values doesn't work.
	 * the Camera.open(int) method is only available starting at
	 * API level 9. This app is developed for android devices
	 * using API level 4.
	 */

	protected Camera doInBackground(Integer... id){
	    Log.v(TAG, "AsyncTask:InitCamera started");
	    try{
		mCamera = Camera.open();
		Log.d(TAG, "Camera opened");
		mParams = mCamera.getParameters();
		mParams.setPreviewSize(480, 320);
		//mParams.setPreviewFormat(PixelFormat.NV21);
		mCamera.setParameters(mParams);
		mScope.setCamera(mCamera);
		return mCamera;
	    }catch(RuntimeException e){
		// TODO: remove generic exception
		Log.e(TAG, "invalid params: " + e.toString());
	    }catch(Exception e){
		Log.e(TAG, "doInBackground: " + e.toString());
	    }
	    return((Camera) null);
	}
	
	protected void onPostExecute(Camera x){
	    Log.v(TAG, "post AsyncTask:InitCamera");
	    if(mCamera == null){ 
		Log.e(TAG, "failed to setup a camera");
		return; 
	    }

	    try{
		mScope.linkCamera();
		onReady();
	    }catch(Exception e){
		// TODO: remove generic exception
		Log.v(TAG, "exception in onPostExecute: " + e.toString());
	    }
	}
    }
}