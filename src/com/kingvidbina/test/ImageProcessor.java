package com.kingvidbina.test;

import android.graphics.Canvas;
import android.util.Log;

public class ImageProcessor {
    public static final String TAG = "Test";
    // get image and return a list of values that correspond to respective pixellocales
    // don't do anything graphically in this class, just simple mathematical operations

    //    private Algorithm mAlgorithm;

    public ImageProcessor(){
	Log.v(TAG, "Scanner");
    }

    public void processData(byte[] data){
	// TODO: process every bit
	Log.v(TAG, "process");
    }

    public class Algorithm{
	public Algorithm(){
	}
	public void run(){
	    // TODO: run algorithm
	}
    }
}