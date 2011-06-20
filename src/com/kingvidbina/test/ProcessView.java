package com.kingvidbina.test;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.Drawable;
import android.content.res.Resources;

import java.lang.reflect.Array;

public class ProcessView extends View {
    public static final String TAG = "Test";
    private static final String CLASS = "ProcessView:";
    
    private Context mContext;
    private LayerDrawable mLayerDrawable;
    private Drawable mDrawable;
    private Point mTempPoint;
    private boolean mReady;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mImageWidth;
    private int mImageHeight;

    ProcessView(Context context){
	super(context);
	Log.v(TAG, CLASS + "1");
	mContext = context;
    }

    ProcessView(Context context, AttributeSet attrs){
	super(context, attrs);
	Log.v(TAG, CLASS + "2");
	mContext = context;
    }
    
    ProcessView(Context context, AttributeSet attrs, int defStyle){
	super(context, attrs, defStyle);
	Log.v(TAG, CLASS + "3");
	mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// hard-coded values to coincide with the screen dimensions on HTC Desire HD
	// TODO: dynamically acquire the screen dimensions
	mScreenWidth = 590; //getMeasuredWidth();
	mScreenHeight = 315; //getMeasuredHeight();
	Log.v(TAG, CLASS + "screen width:" + mScreenWidth + " height:" + mScreenHeight);
    }

    /*public void setImageSize(Camera.Size size){
	setImageSize(size.width, size.height);
	}*/

    public void setImageSize(int width, int height){
	mImageWidth = width;
	mImageHeight = height;
	Log.v(TAG, CLASS + "setImageSize(" + mImageWidth + "," + mImageHeight + ")");
    }

    /**
     * stretch the 2-d plane to coincide with scaled camera preview
     */
    private Point scale(Point pt){
	Point out = new Point(pt.x*mScreenWidth/mImageWidth, 
			      pt.y*mScreenHeight/mImageHeight);
	//Log.v(TAG, CLASS + "[x:" + pt.x + "->" + out.x + ", y:" + pt.y + "->" + out.y + "]");
	return(out);
    }

    public void clearPoints(){
	invalidate();
    }

    public void setPoints(Point[] points){
	Log.v(TAG, CLASS + "setPoints()");
	prepDrawables(points);
	mReady = true;
	invalidate();
    }
    
    private void prepDrawables(Point[] targets){
	int len = Array.getLength(targets);
	//Log.v(TAG, CLASS + "prepDrawables(): targets: " + len);
	try{
	    Drawable[] drawables;
	    drawables = new Drawable[len];
	    mDrawable = getResources().getDrawable(R.drawable.target);
	    for(int i = 0; i < len; i++){
		Point pt = scale(targets[i]);
		//Point pt = targets[i];
		int x = pt.x;
		int y = pt.y;
		drawables[i] = getResources().getDrawable(R.drawable.target);
		drawables[i].setBounds(x, y, x + drawables[i].getIntrinsicHeight(), y + drawables[i].getIntrinsicWidth());
		//Log.v(TAG, CLASS + "prepDrawables(): setting x:" + x + " y:" + y + " result[" + i + "]:" + drawables[i].toString());
	    }
	    
	    mLayerDrawable = new LayerDrawable(drawables);
	}catch(Exception e){
	    Log.v(TAG, "prepDrawables(): Exception: " + e.toString());
	}
    }

    protected void onDraw(Canvas canvas){
	super.onDraw(canvas);
	try{
	    if(mReady == true){
		mLayerDrawable.draw(canvas);
	    }
	}catch(Exception e){
	    Log.v(TAG, CLASS + "onDraw(): Exception: " + e.toString());
	}
    }
}
    

   
