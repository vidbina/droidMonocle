package com.kingvidbina.test;

import android.graphics.Canvas;
import android.util.Log;
import android.graphics.Point;

import java.util.List;
import java.util.ArrayList;

public class ImageProcessor {
    public static final String TAG = "Test";
    public static final String CLASS = "ImageProcessor";
    // get image and return a list of values that correspond to respective pixellocales
    // don't do anything graphically in this class, just simple mathematical operations

    //    private Algorithm mAlgorithm;

    public ImageProcessor(){
	Log.v(TAG, "Scanner");
    }

    // TODO: redesign this method to be more flexible
    public List<Point> processData(byte[] data, int start, int end, int h, int w, int step){
	List<Point> points = new ArrayList<Point>();
	// TODO: process every bit
	for(int y = 0; y < h; y+=step){
	    //Log.v(TAG, CLASS + "y:" + y);
	    for(int x = 0; x < w; x+=step){
		//Log.v(TAG, CLASS + "x:" + x);
		if(data[(y*w)+x] < 0){
		    Point p = new Point(x, y);
		    points.add(p);
		    //Log.v(TAG, CLASS + "adding point");
		    //points.add(Point(x,y));
		}
	    }
	}
	return(points);
    }

    public class Algorithm{
	public Algorithm(){
	}
	public void run(){
	    // TODO: run algorithm
	}
    }
}