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

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.InflateException;

//temp
import android.graphics.Point;

import com.kingvidbina.test.ScopePreview;
import com.kingvidbina.test.ImageProcessor;
import com.kingvidbina.test.HorizonView;
import com.kingvidbina.test.ProcessView;

import java.io.IOException;
import java.lang.reflect.Array;

// TODO: rework all try - catch blocks to remove catching generic exceptions
// TODO: handle orientation changes properly (rotation possibilities start at API Level 11). Orientation has been hardwired to landscape (see manifest)
public class Cam extends Activity implements Camera.PreviewCallback
{
    /* don't ever forget to add uses-features
     * and uses-permission tags before the application
     * tags in the manifest. This cause me a lot of trouble
     * and cost me much precious time.
     */
    public static final String TAG = "Test";
    private static final String CLASS = "Cam:";

    private Camera mCamera;
    private SurfaceHolder mHolder;
    private Context mContext;
    private ScopePreview mScope;
    private Parameters mParams;
    private HorizonView mTest;
    private ProcessView mProcessView;
    private LayoutParams mTestParams;
    private OrientationEventListener mOrientation;

    @Override
    public void onCreate(Bundle icicle){
	Log.v(TAG, CLASS + "onCreate()");
	super.onCreate(icicle);
	try{
	    // TODO: organize this
	    mTest = new HorizonView(this);
	    mProcessView = new ProcessView(this);
	    setContentView(R.layout.cam);
	    Log.v(TAG, mTest.toString());
	    mTestParams = new LayoutParams(50, 50);
	    Log.v(TAG, mTestParams.toString());
	    addContentView(mTest, mTestParams);
	    addContentView(mProcessView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	    setOrientationListener();
	    Log.v(TAG, CLASS + "onCreate(): contentview set");
	}catch(InflateException e){
	    // TODO: remove generic exception
	    Log.v(TAG, CLASS + "onCreate(): InflateExeption: " + e.toString());
	}catch(Exception e){
	    // catches everything else
	    Log.v(TAG, CLASS + "onCreate(): Exception: " + e.toString());
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
	Log.d(TAG, CLASS + "onCreate(): done");
    }

    @Override
    public void onPause(){
	Log.v(TAG, CLASS + "onPause()");
	mScope.haltPreview();
	// stop cam if existent
	try{
	    if(mCamera != null){ 
		mCamera.setPreviewCallback(null);
		mCamera.release(); 
		Log.v(TAG, CLASS + "onPause(): camera released");
	    }
	}catch(Exception e){
	    // TODO: remove generic exception
	    Log.e(TAG, CLASS + "onPause(): Exception: " + e.toString());
	}
	/* painfull lesson learned invoke super method
	 * spend a few hours troubleshooting for the source
	 * of that dreaded Force Close m*f*er
	 */
	mOrientation.disable();
	super.onPause();
    }
    
    @Override
    public void onStop(){
	Log.v(TAG, CLASS + "onStop()");
	super.onStop();
    }

    public void onReady(){
	Log.v(TAG, CLASS + "onReady()");
	mCamera.setPreviewCallback(this);
	mScope.showPreview();
	mOrientation.enable();
	Log.v(TAG, CLASS + "onReady(): done");
    }
    
    /**
     * receives and pushes frames to the image processing objects
     */
    public void onPreviewFrame(byte[] data, Camera camera){
	int len = Array.getLength(data);
    }

    /** sets the View after camera has been initialized */
    private void setView(){
	Log.v(TAG, CLASS + "setView()");
	if(mScope == null){
	    mScope = (ScopePreview) this.findViewById(R.id.scope);
       	}
    }

    private void setOrientationListener(){
	mOrientation = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_UI){
		public void onOrientationChanged(int orientation){
		    mTest.setAngle(orientation);
		}
	    };
    }

    private class InitCamera extends AsyncTask <Integer, Void, Camera>{
	/* assigning values to the Integer values doesn't work.
	 * the Camera.open(int) method is only available starting at
	 * API level 9. This app is developed for android devices
	 * using API level 4.
	 */

	protected Camera doInBackground(Integer... id){
	    Log.v(TAG, CLASS + "AsyncTask:InitCamera");
	    try{
		mCamera = Camera.open();
		Log.d(TAG, CLASS + "AsyncTask:InitCamera: doInBackground(): Camera opened");
		mParams = mCamera.getParameters();
		mParams.setPreviewSize(480, 320);
		//mParams.setPreviewFormat(PixelFormat.NV21);
		mCamera.setParameters(mParams);
		mScope.setCamera(mCamera);
		return mCamera;
	    }catch(RuntimeException e){
		// TODO: remove generic exception
		Log.e(TAG, CLASS + "AsyncTask:InitCamera: doInBackground(): RuntimeExceptions: " + e.toString());
	    }catch(Exception e){
		Log.e(TAG, CLASS + "AsyncTask:InitCamera: doInBackground(): Exception: " + e.toString());
	    }
	    return((Camera) null);
	}
	
	protected void onPostExecute(Camera x){
	    Log.v(TAG, CLASS + "AsyncTask:InitCamera: onPostExecute()");
	    if(mCamera == null){ 
		Log.e(TAG, CLASS + "AsyncTask:InitCamera: onPostExecute(): failed to setup a camera");
		return; 
	    }

	    try{
		mScope.linkCamera();
		onReady();
	    }catch(Exception e){
		// TODO: remove generic exception
		Log.v(TAG, CLASS + "AsyncTask:InitCamera: onPostExecute(): Exception: " + e.toString());
	    }
	}
    }
}