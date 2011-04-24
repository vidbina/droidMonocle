package com.kingvidbina.test;

import android.content.Context;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.util.AttributeSet;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import java.io.IOException;
//import java.lang.Exception;
//import java.lang.RuntimeException;

public class ScopeView extends SurfaceView implements SurfaceHolder.Callback
{
    public static final String TAG = "Test";
    private SurfaceHolder mHolder;
    private Context mContext;
    private Canvas mCanvas;
    private Camera mCamera;
    private Parameters mParams;
    private Boolean mSurfaceReady = false;
    private Boolean mCameraPreview = false;

    public ScopeView(Context context){
	super(context);
	mContext = context;
	setupHolder();
    }
    
    public ScopeView(Context context, AttributeSet attrs){
	super(context, attrs);
	mContext = context;
	setupHolder();
    }
    public ScopeView(Context context, AttributeSet attrs, int defStyle){
	super(context, attrs, defStyle);
	mContext = context;
	setupHolder();
    }

    /** prepares the holder */
    private void setupHolder(){
	Log.v(TAG, "setupHolder()");
	mHolder = getHolder();
	mHolder.addCallback(this);
	/** 
	 * remember to set the SurfaceHolder type in the future
	 * this was the reason I didn't see crap on the screen
	 * what a bitch. So much for a deprecated method. I guess
	 * this is what happens when developing for older API Levels
	 */
	mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	try{
	    setVisibility(0);
	}catch(Exception e){
	    Log.v(TAG, "setting visibility fail: " + e.toString());
	}
    }

    public void setCamera(Camera camera){
	Log.v(TAG, "setCamera()");
	mCamera = camera;
	if(mCamera != null){
	    try{
		mParams = mCamera.getParameters();
	    }catch(Exception e){
		Log.v(TAG, "camera set exception: " + e.toString());
	    }
	}
    }

    public void linkCamera(){
	Log.v(TAG, "linkCamera()");
	if(mSurfaceReady == true && mCamera != null){
	    try{
		mCamera.setPreviewDisplay(mHolder);
		Log.d(TAG, "camera linked to holder");
		mCamera.startPreview();
		mCameraPreview = true;
	    }catch(IOException e){
		// TODO: surface invalid
		Log.v(TAG, "link exception: " + e.toString());
	    }catch(Exception e){
		Log.v(TAG, "link exception: " + e.toString());
	    }
	}else{
	    //Exception e = new Exception("surface and cam not ready");
	    //throw e;
	    // TODO: throw a exception at this point
	    Log.d(TAG, "problem linking camera");
	}
    }

    public void showPreview(){
	Log.v(TAG, "showPreview()");
	if(mSurfaceReady == true && mCameraPreview == false){
	    mCamera.startPreview();
	    mCameraPreview = true;
	}
    }
    
    public void haltPreview(){
	Log.v(TAG, "haltPreview()");
	if(mCameraPreview == true && mCamera != null){
	    mCamera.stopPreview();
	    mCameraPreview = false;
	    Log.v(TAG, "preview stopped");
	}
    }

    public void draw(Canvas canvas){
	Log.v(TAG, "draw()");
	super.draw(canvas);
    }

    public void surfaceCreated(SurfaceHolder holder){
	mSurfaceReady = true;
	Log.v(TAG, "surfaceCreated: " + holder.toString());
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
	Log.v(TAG, "surfaceChanged: " + holder.toString());
	// change camera preview surface if camera exists
	Log.v(TAG, "w: " + width + "h: " + height);
	if(mCamera != null){
	    try{
		//mParams.setPreviewSize(width, height);
		//mCamera.setParameters(mParams);
		Log.v(TAG, "params updated");
	    }catch(RuntimeException e){
		Log.e(TAG, "invalid camera params " + e.getStackTrace().toString());
	    }
      	}
    }
    
    public void surfaceDestroyed(SurfaceHolder holder){
	Log.v(TAG, "surfaceDestroyed: " + holder.toString());
	mSurfaceReady = false;
	// stop preview if camera exists and is in previewmode
	if(mCamera != null && mCameraPreview == true){
	    mCamera.stopPreview();
	    Log.v(TAG, "stopping preview");
	    mCameraPreview = false;
	}
    }
}
