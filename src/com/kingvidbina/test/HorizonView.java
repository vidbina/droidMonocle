package com.kingvidbina.test;

import android.view.View;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.shapes.Shape;

import java.lang.Math;

// TODO: move as much drawing data to xml as possible (colors, dimensions, etc.)

/**
 * A view to portray angular offset from the horizon.
 * The angle values set, will be evaluated for validity
 * and drawn to the canvas if appropriate.

 * @author David Asabina
 * @version 0.1, May 12, 2011
 */
public class HorizonView extends View {
    public static final String TAG = "Test";
    private static final String CLASS = "HorizonView:";
    // the paint properties of the marker
    private Paint mPaint = new Paint();
    // context of the view
    private Context mContext;
    // NOT USED
    private int mOffset;
    // length of the marker
    private int mLength;
    // the point at which the marker should be centered
    private Point mCenter;
    // the calculated start point of the marker
    private Point mStart;
    // the calculated end point of the marker
    private Point mEnd;
    // flag to indicate whether object is properly set-up
    private boolean mReady;
    // flag to indicate whether object is ready to be drawn
    // necessary values have been calculated based on angle
    private boolean mDraw;
    // the reference angle
    private int mDefaultAngle;
    // the marker shape
    private Shape mShape;
    // the context resources
    private Resources mRes;
    /**
     * 
     */
    HorizonView(Context context){
	super(context);
	mContext = context;
	onConstruct();
    }

    HorizonView(Context context, AttributeSet attrs){
	super(context, attrs);
	mContext = context;
	onConstruct();
    }

    HorizonView(Context context, AttributeSet attrs, int defStyle){
	super(context, attrs, defStyle);
	mContext = context;
	onConstruct();
    }

    /**
     * Runs through the basic initialization rite for the view.
     */
    private void onConstruct(){
	// check whether function have unecessarily been placed in try-catch block
	mRes = mContext.getResources();
	try{
	    // set graphical default properties
	    mLength = mRes.getDimensionPixelSize(R.dimen.horizonlength);
	    
	    mPaint.setColor(mRes.getColor(R.color.horizoncolor));
	    mPaint.setStrokeWidth(mRes.getDimension(R.dimen.horizonstrokewidth));
	    mPaint.setDither(true);
	}catch(Exception e){
	    Log.v(TAG, CLASS + "onConstruct(): Exception: " + e.toString());
	}
    }

    /**
     * gets the current paint object for the view
     * @return the Paint object of the View
     */
    private Paint getPaint(){
	return mPaint;
    }

    /**
     * sets the current paint object for the view
     * @param paint a fully configurated Paint object
     */
    private void setPaint(Paint paint){
	mPaint = paint;
    }

    /**
     * gets the Shape object used to depict as marker
     * @return the Shape object used as marker
     */
    private Shape getShape(){
	return mShape;
    }

    /**
     * set the Shape object to display as the marker
     * @param shape the shape object containing the marker information
     */
    private void setShape(Shape shape){
	mShape = shape;
    }
    
    /**
     * sets the necessary properties for drawing the angle
     * @param canvas the canvas to be drawn to
     */
    public void setVitals(Canvas canvas){
	// if the the properties are not yet set set to defaults
	if(mReady != true){
	    //mLength = 60;
    	    mCenter = new Point(canvas.getWidth()/2, canvas.getHeight()/2);
	    mStart = new Point(mCenter.x - mLength/2, mCenter.y);
	    mEnd = new Point(mCenter.x + mLength/2, mCenter.y);
	    mDefaultAngle = 270;
	    mReady = true;
	    Log.v(TAG, CLASS + "setVitals(): done");
	}
    }

    /**
     * determines the measured width and height for the object
     * @param measuredWidth the measured width of the parent
     * @param measuredHeight the measured height of the parent

     * @throws IllegalStateException
     */
    protected void onMeasure(int measuredWidth, int measuredHeight){
	// call setMeasuredDimension by invoking method from superclass
	super.onMeasure(measuredWidth, measuredHeight);
	// TODO: getSuggestedMinimumWidth(), getSuggestedMinimumHeight()
	// somehow redundant as call to superclass method already does this
	setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * Draws the horizonmeter in correspondence to the set angle
     * @param canvas the canvas to be drawn to
     */
    protected void onDraw(Canvas canvas){
	super.onDraw(canvas);
	// ensure that vitals are set
	setVitals(canvas);
	// if there is a drawable angle
	if(mDraw == true){ 
	    // attempt drawing to canvas
	    try{
		canvas.drawLine(mStart.x, mStart.y, mEnd.x, mEnd.y, mPaint); 
	    }catch(Exception e){
		// TODO: clean up generic exceptions
		Log.v(TAG, CLASS + "onDraw(): DrawException: " + e.toString());
	    }
	}
    }

    /**
     * Calculates the start- and endpoint for the horizon line
     * @param angle the positive angle in degrees
     */
    protected void setAngle(int angle){
	//
	if(angle != -1){
	    // calculate the marker's start- and endpoint
	    double x = Math.toRadians(mDefaultAngle - angle);
	    float h = (float) mLength/2;
	    
	    // attempt trigonometric operations
	    // TODO: check whether these actually throw exceptions
	    try{
		int o = (int) (Math.sin(x) * h);
		int a = (int) (Math.cos(x) * h);
		mStart.set((int) mCenter.x - a, (int) mCenter.y - o);
		mEnd.set((int) mCenter.x + a, (int) mCenter.y + o);
	    }catch(Exception e){
		// TODO: get rid of generic exceptions
		Log.v(TAG, CLASS + "setAngle(): Exception: " + e.toString());
	    }
	    // necessary properties determined for given angle
	    mDraw = true;
	}else{
	    // necessary properties could not be determined for given angle
	    mDraw = false;
	}
	// allow object to be redrawn
	invalidate();
    }
}