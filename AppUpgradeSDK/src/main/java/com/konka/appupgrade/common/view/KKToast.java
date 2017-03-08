package com.konka.appupgrade.common.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.konka.appupgrade.R;

public class KKToast extends Toast{
   
	public KKToast(Context context){		
		super(context);		
	}
	
	public static KKToast makeText(Context context, CharSequence text) {
		KKToast toast = new KKToast(context);
    	LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(R.layout.kktoast_sdk, null);
		TextView tv = (TextView)v.findViewById(R.id.message);
		tv.setText(text);
		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 72);
		toast.setView(v);
		toast.setDuration(Toast.LENGTH_SHORT);		
		return toast;		
	}

}
