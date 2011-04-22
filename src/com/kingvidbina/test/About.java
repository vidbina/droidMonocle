package com.kingvidbina.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class About extends Activity
{
    public static final String TAG = "Test";

    @Override
    public void onCreate(Bundle icicle){
	Log.v(TAG, "onCreate About");
	super.onCreate(icicle);
	setContentView(R.layout.about);
    }
}