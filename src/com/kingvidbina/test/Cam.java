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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.InflateException;

// temp
import android.graphics.Point;

import com.kingvidbina.test.ScopePreview;
import com.kingvidbina.test.ImageProcessor;
import com.kingvidbina.test.HorizonView;
import com.kingvidbina.test.ProcessView;
import com.kingvidbina.test.ValueView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;

// TODO: rework all try - catch blocks to remove catching generic exceptions
// TODO: handle orientation changes properly (rotation possibilities start at API Level 11). Orientation has been hardwired to landscape (see manifest)
/**
 * Activity responsible for displaying the preview and appropriate
 * markers and invoking the image processing methods.

 * @author David Asabina
 * @version 0.1, May 12, 2011
 */
public class Cam extends Activity implements Camera.PreviewCallback
{
    /* don't ever forget to add uses-features
     * and uses-permission tags before the application
     * tags in the manifest. This cause me a lot of trouble
     * and cost me much precious time.
     */
    public static final String TAG = "Test";
    private static final String CLASS = "Cam:";
    
    // the camera object
    private Camera mCamera;
    // the holder for the surface being rendered upon
    private SurfaceHolder mHolder;
    private Context mContext;
    // the View on which the preview is projected
    private ScopePreview mScope;
    private Parameters mParams;
    // the view to depict the horizon level
    private HorizonView mHorizonView;
    private LayoutParams mHorizonParams;
    // the view to depict text data
    private ValueView mValuePixel1;
    private ValueView mValuePixel2;
    private LayoutParams mValueParams;
    // the view used to render extra drawables
    private ProcessView mProcessView;
    private LayoutParams mProcessParams;
    private OrientationEventListener mOrientation;
    // editorial bitmap
    //private Bitmap mBitmap;
    private ImageProcessor mProcessor = new ImageProcessor();
    private int mStep;
    private int mY;
    private int mU;
    private int mV;
    private int mError;

    @Override
    public void onCreate(Bundle icicle){
	Log.v(TAG, CLASS + "onCreate()");
	super.onCreate(icicle);

	// create objects for this activity
	mHorizonView = new HorizonView(this);
	mProcessView = new ProcessView(this);
	mValuePixel1 = new ValueView(this);
	mValuePixel2 = new ValueView(this);

	/**
	 * TODO: having problems inflating curstom views from xml
	 * this problem might have something to do with the chosen
	 * API level.
	 */

	// create and configure parameter objects
	mHorizonParams = new LayoutParams(50, 50);
	mProcessParams = new LayoutParams(LayoutParams.FILL_PARENT, 
					  LayoutParams.FILL_PARENT);
	mValueParams = new LayoutParams(LayoutParams.FILL_PARENT, 
					LayoutParams.FILL_PARENT);
	// TODO: check whether all nested methods throw Exceptions
	try{
	    // inflate xml layout
	    Log.v(TAG, CLASS + "inflate layouts");
	    setContentView(R.layout.cam);
	    Log.v(TAG, CLASS + "layout inflated");
	    Point p = new Point(20, 50);
	    mValuePixel2.setPosition(p);
	    // add views to the current activity
	    addContentView(mValuePixel1, mValueParams);
	    addContentView(mValuePixel2, mValueParams);
	    addContentView(mHorizonView, mHorizonParams);
	    addContentView(mProcessView, mProcessParams);
	    // handle changes in orientation
	    setOrientationListener();
	}catch(InflateException e){
	    // TODO: remove generic exception
	    Log.v(TAG, CLASS + "onCreate(): InflateExeption: " + e.toString());
	}catch(Exception e){
	    // catches everything else
	    Log.v(TAG, CLASS + "onCreate(): Exception: " + e.toString());
	}
	Log.v(TAG, CLASS + "values and shit set :)");
	// set the view in which to display the Camera stream and corresponding data
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

	// disconnect camera stream from preview surface
	mScope.haltPreview();

	try{
	    // stop cam if existent
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
	// disable the orientationlistener
	mOrientation.disable();
	super.onPause();
    }
    
    @Override
    public void onStop(){
	Log.v(TAG, CLASS + "onStop()");
	super.onStop();
    }

    /**
     * start camera preview
     */
    public void onReady(){
	Log.v(TAG, CLASS + "onReady()");
	mCamera.setPreviewCallback(this);
	mScope.showPreview();
	mOrientation.enable();
	Log.v(TAG, CLASS + "onReady(): done");
    }
    
    /**
     * receiver of the PreviewCallback
     * routes the relevant information to the appropriate handling classes
     * @param data the image data as specified by the PreviewFormat of the camera object
     * @param camera the camera object souring the imagedata
     */
    public void onPreviewFrame(byte[] data, Camera camera){
	int len = Array.getLength(data);
	mValuePixel1.dump(String.valueOf("len:" + len));

	try{
	    List<Point> list = mProcessor.processData(data, 0, 153600, 320, 480, 10);
	    Log.v(TAG, CLASS + "list.size: " + list.size());
	    mValuePixel2.dump(String.valueOf("pts: " + list.size()));
	    // 
	    Point[] p = new Point[list.size()];
	    p = list.toArray(p);
	    // convert list to array
	    /*Point[] p = new Point[list.size()];
	    for(int i = 0; i < list.size(); i++){
		p[i] = 
		}*/
	    if(list.size() != 0){
		mProcessView.setPoints(p);
	    }else{
		mProcessView.clearPoints();
	    }

	}catch(Exception e){
	    Log.v(TAG, CLASS + "hmmmmmm :S - " + e.toString());
	}
    }

    /** 
     * sets the View after camera has been initialized
     * ???: allow user to set any view as renderable object for preview
     */
    private void setView(){
	Log.v(TAG, CLASS + "setView()");
	// set the view if it hasn't been set yet
	// thus, the view is only settable once
	Log.v(TAG, CLASS + "*****************");
	if(mScope == null){
	    Log.v(TAG, CLASS + "inflating previews");
	    mScope = (ScopePreview) this.findViewById(R.id.scope);
	}
    }
    
    /**
     * handles changes on orientation changes
     * the mOrientation property is set as the listener
     */
    private void setOrientationListener(){
	mOrientation = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_UI){
		public void onOrientationChanged(int orientation){
		    // set the HorizonView angle
		    mHorizonView.setAngle(orientation);
		}
	    };
    }

    public boolean onCreateOptionsMenu(Menu menu){
	super.onCreateOptionsMenu(menu);
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.scope, menu);
	return(true);
    }
    
    public boolean onOptionsItemSelected(MenuItem choice){
	switch(choice.getItemId()){
	case R.id.scopesettings:
	    Log.v(TAG, CLASS + "OPTION: " + choice.toString());
	    return(true);
	}
	return(false);
    }


    private class InitCamera extends AsyncTask <Integer, Void, Camera>{
	/* assigning values to the Integer values doesn't work.
	 * the Camera.open(int) method is only available starting at
	 * API level 9. This app is developed for android devices
	 * using API level 4.
	 */
	
	/**
	 * runs the initialization of the Camera in a seperate thread
	 * thus keeping the device UI free for regular use without forming
	 * the potential thread of invoking Force Closes.
	 * @param id NOT USED IN CURRENT VERSION (Only API level > 9)
	 * @return the Camera object, either fully initialized or NULL
	 */
	protected Camera doInBackground(Integer... id){
	    Log.v(TAG, CLASS + "AsyncTask:InitCamera");
	    try{
		/* NOTE: API level 4 has no support for multiple cam
		 * API level > 9 supports multiple cameras which can 
		 * be returned by using Camera.open(CAMERA_NUMBER)
		 */
		mCamera = Camera.open();

		Log.d(TAG, CLASS + "AsyncTask:InitCamera: doInBackground(): Camera opened");

		//set camera parameters
		mParams = mCamera.getParameters();
		mParams.setPreviewSize(480, 320);
		/*Log.v(TAG, CLASS + "picsize: " + String.valueOf(mParams.getPictureSize()));
		Log.v(TAG, CLASS + "picformat: " + String.valueOf(mParams.getPictureFormat()));
		Log.v(TAG, CLASS + "pvsize: " + String.valueOf(mParams.getPreviewSize()));
		Log.v(TAG, CLASS + "pvformat: " + String.valueOf(mParams.getPreviewFormat()));
		Log.v(TAG, CLASS + "pvfps: " + String.valueOf(mParams.getPreviewFrameRate()));*/
		//mParams.setPreviewFormat(PixelFormat.NV21);
		mCamera.setParameters(mParams);
		mScope.setCamera(mCamera);
		mProcessView.setImageSize(480, 320);
		return mCamera;
	    }catch(RuntimeException e){
		// TODO: remove generic exception
		Log.e(TAG, CLASS + "AsyncTask:InitCamera: doInBackground(): RuntimeExceptions: " + e.toString());
	    }catch(Exception e){
		Log.e(TAG, CLASS + "AsyncTask:InitCamera: doInBackground(): Exception: " + e.toString());
	    }
	    // return empty camera upon fail
	    return((Camera) null);
	}
	
	/**
	 * Connect the camera to a viewable object for previewing.
	 * This method is automatically called after the initialization
	 * of the Camera has been executed.

	 * @param x the camera object returned by the doInBackground() method
	 */
	protected void onPostExecute(Camera x){
	    Log.v(TAG, CLASS + "AsyncTask:InitCamera: onPostExecute()");

	    // abort method if camera is not set
	    if(mCamera == (Camera) null){ 
		return; 
	    }

	    // connect the camera to the frame and start application
	    mScope.linkCamera();
	    onReady();
	}
    }
}