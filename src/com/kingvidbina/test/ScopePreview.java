package com.kingvidbina.test;

import android.content.Context;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.util.AttributeSet;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import java.io.IOException;
//import java.lang.Exception;
//import java.lang.RuntimeException;

public class ScopePreview extends SurfaceView implements SurfaceHolder.Callback
{
    public static final String TAG = "Test";
    private static final String CLASS = "ScopePreview:";
    private SurfaceHolder mHolder;
    private Context mContext;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Camera mCamera;
    private Parameters mParams;
    private Boolean mSurfaceReady = false;
    private Boolean mCameraPreview = false;

    public ScopePreview(Context context){
	super(context);
	mContext = context;
	setupHolder();
    }
    
    public ScopePreview(Context context, AttributeSet attrs){
	super(context, attrs);
	mContext = context;
	setupHolder();
    }
    public ScopePreview(Context context, AttributeSet attrs, int defStyle){
	super(context, attrs, defStyle);
	mContext = context;
	setupHolder();
    }

    private void setupCanvas(){
	mBitmap = Bitmap.createBitmap(mParams.getPreviewSize().width, mParams.getPreviewSize().height, Config.ARGB_8888);
	mCanvas = new Canvas(mBitmap);
    }

    /** prepares the holder */
    private void setupHolder(){
	Log.v(TAG, CLASS + "setupHolder()");
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
	    Log.v(TAG, CLASS + "setupHolder(): Exception: " + e.toString());
	}
    }

    public void setCamera(Camera camera){
	Log.v(TAG, CLASS + "setCamera()");
	mCamera = camera;
	if(mCamera != null){
	    try{
		mParams = mCamera.getParameters();
	    }catch(Exception e){
		Log.v(TAG, CLASS + "setCamera(): Exception: " + e.toString());
	    }
	}
    }

    public void linkCamera(){
	Log.v(TAG, CLASS + "linkCamera()");
	if(mSurfaceReady == true && mCamera != null){
	    try{
		mCamera.setPreviewDisplay(mHolder);
		Log.d(TAG, CLASS + "linkCamera(): linked");
		mCamera.startPreview();
		mCameraPreview = true;
	    }catch(IOException e){
		// TODO: surface invalid
		Log.v(TAG, CLASS + "linkCamera(): IOException: " + e.toString());
	    }catch(Exception e){
		Log.v(TAG, CLASS + "linkCamera(): Exception: " + e.toString());
	    }
	}else{
	    //Exception e = new Exception("surface and cam not ready");
	    //throw e;
	    // TODO: throw a exception at this point
	    Log.d(TAG, CLASS + "linkCamera(): surface or camera not ready");
	}
    }

    public void showPreview(){
	Log.v(TAG, CLASS + "showPreview()");
	if(mSurfaceReady == true && mCameraPreview == false){
	    mCamera.startPreview();
	    mCameraPreview = true;
	}
    }
    
    public void haltPreview(){
	if(mCameraPreview == true && mCamera != null){
	    mCamera.stopPreview();
	    mCameraPreview = false;
	    Log.v(TAG, CLASS + "haltPreview(): done");
	}else{
	    Log.v(TAG, CLASS + "haltPreview(): preview or camera not valid");
	}
    }

    public void draw(Canvas canvas){
	super.draw(canvas);
    }

    public void surfaceCreated(SurfaceHolder holder){
	mSurfaceReady = true;
	Log.v(TAG, CLASS + "surfaceCreated(): " + holder.toString());
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
	// change camera preview surface if camera exists
	if(mCamera != null){
	    try{
		//mParams.setPreviewSize(width, height);
		//mCamera.setParameters(mParams);
		Log.v(TAG, CLASS + "surfaceChanged(): params updated");
	    }catch(RuntimeException e){
		Log.e(TAG, CLASS + "surfaceChanged(): RuntimeException: " + e.getStackTrace().toString());
	    }
      	}
    }
    
    public void surfaceDestroyed(SurfaceHolder holder){
	Log.v(TAG, CLASS + "surfaceDestroyed(): " + holder.toString());
	mSurfaceReady = false;
	// stop preview if camera exists and is in previewmode
	if(mCamera != null && mCameraPreview == true){
	    mCamera.stopPreview();
	    Log.v(TAG, CLASS + "surfaceDestroyed(): stopping preview");
	    mCameraPreview = false;
	}
    }
}
