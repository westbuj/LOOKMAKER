package com.jnn.jw.lab;

import com.jnn.jw.mid.ModelEntry;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class LoadModelThread extends Thread implements Runnable {

	public ModelEntry me;
	
	@Override
	public void run() {
		MainActivity.showProgress.sendEmptyMessage(0);
		MainActivity.loadmodelrules(me);
		MainActivity.updateImage.sendEmptyMessage(0);
		MainActivity.hideProgress.sendEmptyMessage(0);
	}
	public LoadModelThread( ModelEntry ime){
		me=ime;
	}
	
	
	

}
