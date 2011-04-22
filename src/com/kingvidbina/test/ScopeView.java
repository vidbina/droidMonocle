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
import java.lang.Exception;

public class ScopeView extends SurfaceView implements SurfaceHolder.Callback
{
    public static final String TAG = "Test";
    public SurfaceHolder Holder;
    private Canvas mCanvas;
    private Camera mCamera;
    private Parameters mParams;
    private Boolean mSurfaceReady;

    public ScopeView(Context context){
	super(context);
	confHolder();
    }
    
    public ScopeView(Context context, AttributeSet attrs){
	super(context, attrs);
	confHolder();
    }
    public ScopeView(Context context, AttributeSet attrs, int defStyle){
	super(context, attrs, defStyle);
	confHolder();
    }

    /** prepares the holder */
    private void confHolder(){
	mCamera = null;
	Holder = getHolder();
	Holder.addCallback(this);
	/** 
	 * remember to set the SurfaceHolder type in the future
	 * this was the reason I didn't see crap on the screen
	 * what a bitch. So much for a deprecated method. I guess
	 * this is what happens when developing for older API Levels
	 */
	Holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	try{
	    setVisibility(0);
	}catch(Exception e){
	    Log.v(TAG, "setting visibility fail: " + e.toString());
	}
    }

    public void setCamera(Camera camera){
	try{
	mCamera = camera;
	mParams = mCamera.getParameters();
	}catch(Exception e){
	    Log.v(TAG, "HERE " + e.toString());
	}
    }
    public void linkCamera(){
	if(mSurfaceReady == true && mCamera != null){
	    try{
		mCamera.setPreviewDisplay(Holder);
		mCamera.startPreview();
	    }catch(IOException e){
		Log.v(TAG, "LINK1: " + e.toString());
	    }catch(Exception e){
		Log.v(TAG, "LINK2: " + e.toString());
	    }
	    
	}else{
	    //Exception e = new Exception("surface and cam not ready");
	    //throw e;
	    // TODO: throw a exception at this point
	    Log.v(TAG, "problem");
	}
    }

    public void draw(Canvas canvas){
	super.draw(canvas);
    }

    public void surfaceCreated(SurfaceHolder holder){
	mSurfaceReady = true;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
	// TODO: handle surfacechanges
	Log.v(TAG, "surfaceChanged");
    }
    
    public void surfaceDestroyed(SurfaceHolder holder){
	mSurfaceReady = false;
	// TODO: stoppreview
    }
}