package com.kingvidbina.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.AsyncTask;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;

import com.kingvidbina.test.ScopeView;

import java.io.IOException;

// TODO: rework all try - catch blocks to remove catching generic exceptions
public class Cam extends Activity 
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

    @Override
    public void onCreate(Bundle icicle){
	super.onCreate(icicle);
	try{
	    setContentView(R.layout.cam);
	}catch(Exception e){
	    // TODO: remove generic exception
	    Log.v(TAG, "activity:Cam - inflate exception: " + e.toString());
	}
	// start thread to load cam
	Log.v(TAG, this.toString());
	// set the SurfaceView
	setView();
	// init cam in seperate thread
	new InitCamera().execute();
    }

    @Override
    public void onPause(){
	/* painfull lesson learned invoke super method
	 * spend a few hours troubleshooting for the source
	 * of that dreaded Force Close m*f*er
	 */
	super.onPause();
	// stop cam if existent
	try{
	    if(mCamera != null){ mCamera.release(); }
	}catch(Exception e){
	    // TODO: remove generic exception
	    Log.e(TAG, "onPause exception: " + e.toString());
	}
    }
    
    @Override
    public void onStop(){
	super.onStop();
    }

    /*
    public void onPreviewFrame(byte[] data, Camera camera){
	Log.v(TAG, "." + data.);
	}*/
    
    /** sets the View after camera has been initialized */
    private void setView(){
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
	    // abort upon empty cam
	    if(mCamera == null){ return; }

	    try{
		mScope.linkCamera();
	    }catch(Exception e){
		// TODO: remove generic exception
		Log.v(TAG, "exception in onPostExecute: " + e.toString());
	    }
	}
    }
}