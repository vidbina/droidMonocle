package com.kingvidbina.test;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.util.Log;

// TODO: remove the catching of generic exeptions
public class Monocle extends Activity implements OnClickListener
{
    public static final String TAG = "Test";
    private static final String CLASS = "Main:";

    @Override
    public void onCreate(Bundle icicle)
    {
	Log.v(TAG, "===================================");
	Log.v(TAG, CLASS + "onCreate()");
        super.onCreate(icicle);
        setContentView(R.layout.main);

	try{
	    Button btnAbout = (Button) findViewById(R.id.button_about);
	    Log.v(TAG, CLASS + ">>>");
	    btnAbout.setOnClickListener((View.OnClickListener) this);
	    Log.v(TAG, CLASS + "~~~");
	    Log.v(TAG, CLASS + "onCreate(): inflation complete");
	}catch(Exception e){
	    Log.v(TAG, CLASS + "onCreate(): Exception: " + e.toString());
	}
    }

    @Override
    public void onPause(){
	super.onPause();
	Log.v(TAG, CLASS + "onPause()");
    }

    @Override
    public void onDestroy(){
	super.onDestroy();
	Log.v(TAG, CLASS + "onDestroy()");
	Log.v(TAG, "===================================");
    }
    
    public void onClick(View clicked){
	Log.v(TAG, CLASS + "onClick(): " + clicked.toString());
	switch(clicked.getId()){
	case R.id.button_about:
	    // TODO: make a nice dialog (see Android book)
	    Intent i = new Intent(this, About.class);
	    startActivity(i);
	    break;
	default:
	    Log.v(TAG, CLASS + "onClick(): unknown click");
	    break;
	}
    }

    public void startCam(View v){
	Log.v(TAG, CLASS + "startCam()");
	try{
	    startActivity(new Intent(this, Cam.class));
	}catch(Exception e){
	    Log.v(TAG, CLASS + "startCam(): Exception: " + e.toString());
	}
    }
}
