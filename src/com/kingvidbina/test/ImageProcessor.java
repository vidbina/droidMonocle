package com.kingvidbina.test;

import android.graphics.Canvas;
import android.util.Log;

public class ImageProcessor {
    public static final String TAG = "Test";

    private Canvas[] mCanvas;
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