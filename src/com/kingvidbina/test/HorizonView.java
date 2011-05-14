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
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

import java.lang.Math;
import java.lang.String;
import java.lang.Integer;

// TODO: move as much drawing data to xml as possible (colors, dimensions, etc.)
// REORGANISE CODE

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
    private Paint mMarkerPaint = new Paint();
    private Paint mTextPaint = new Paint();
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
    // path containing the marker
    private Path mMarker;
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
    private int mAngle;
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
	    mPaint.setAntiAlias(true);

	    mTextPaint.setColor(mRes.getColor(R.color.horizontext));
	    mTextPaint.setTypeface(Typeface.MONOSPACE);
	    mTextPaint.setTextSize(mRes.getDimension(R.dimen.horizontextsize));
	    mTextPaint.setAntiAlias(true);

	    mMarkerPaint.setColor(mRes.getColor(R.color.anglecolor));
	    mMarkerPaint.setStyle(Style.STROKE);
	    mMarkerPaint.setStrokeWidth(mRes.getDimension(R.dimen.anglestroke));
	    mMarkerPaint.setAntiAlias(true);
	    mMarker = new Path();
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
		// draw movable horizonbar
		canvas.drawLine(mStart.x, mStart.y, mEnd.x, mEnd.y, mPaint); 
		// draw anglemeasurement
		canvas.drawPath(mMarker, mMarkerPaint);
		// draw degree text
		canvas.drawText(String.valueOf(mAngle) + " " + (char) 0x00B0,
				mCenter.x + mLength + 5,
				mCenter.y - 20,
				mTextPaint);
		// draw horizon marker
		canvas.drawLine(0, mCenter.y, mCenter.x*2, mCenter.y, mMarkerPaint);
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
	    // calculate necessary values for determining marker pos and dimensions
	    double dOffset = (angle + 90) % 360;
	    double dTemp = 0;
	    if(dOffset > 180){
		dTemp = -(360 - dOffset);
	    }else{
		dTemp = dOffset;
	    }
	    double dAngle = Math.toRadians(mDefaultAngle - angle);
	    double dDeg = Math.toDegrees(dAngle);
	    double finalAngle = (dDeg + 360) % 180;
	    /*if(finalAngle > 180){
		finalAngle = -(finalAngle);
	    }*/
	    float fLen = (float) mLength/2;
	    mAngle = (int) dTemp;
	    
	    // attempt trigonometric operations
	    // TODO: check whether these actually throw exceptions
	    try{
		// set the values for the horizonline
		int o = (int) (Math.sin(dAngle) * fLen);
		int a = (int) (Math.cos(dAngle) * fLen);
		mStart.set((int) mCenter.x - a, (int) mCenter.y - o);
		mEnd.set((int) mCenter.x + a, (int) mCenter.y + o);
		// set the values for the anglemarker
		RectF rectArc = new RectF(mCenter.x - fLen/2,
					mCenter.y - fLen/2,
					mCenter.x + fLen/2,
					mCenter.y + fLen/2);
		// build path for marker
		Path tempPath = new Path();
		// at angle measurement arc to path
		tempPath.addArc(rectArc, (float) 0, (float) - dTemp);
		// prep path for drawing measurement line
		tempPath.moveTo(mCenter.x, mCenter.y);
		
		float offsetx = fLen*2 * (float) Math.cos(dAngle);
		float offsety = fLen*2 * (float) Math.sin(dAngle);
		
		// set endpoint for measurement line		  
		tempPath.lineTo(mCenter.x + offsetx, 
				mCenter.y + offsety);
		// set the marker
		mMarker.set(new Path(tempPath));

		//Log.v(TAG, CLASS + "f: " + finalAngle);
		// add end knob to marker
		float radiusCircle = 3;
		mMarker.addCircle(mCenter.x + offsetx, 
				  mCenter.y + offsety, 
				  radiusCircle, 
				  Direction.CCW);
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