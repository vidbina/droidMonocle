package com.kingvidbina.test;

import android.view.ViewGroup;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

// TODO: determine correct View subclass for HUD, consider making this a ViewDroup with seperate gauge
public class ScopeHUD extends ViewGroup{
    public static final String TAG = "Test";
    private Context mContext;

    ScopeHUD(Context context){
	super(context);
        boo();
    }
    ScopeHUD(Context context, AttributeSet attrs){
	super(context, attrs);
	boo();
    }
    ScopeHUD(Context context, AttributeSet attrs, int defStyle){
	super(context, attrs, defStyle);
	boo();
    }

    public void onLayout(boolean changed, int l, int t, int r, int b){
	Log.v(TAG, "ScopeHUD.onLayout()");
    }
    
    private void boo(){
	Log.v(TAG, "boo");
    }
}