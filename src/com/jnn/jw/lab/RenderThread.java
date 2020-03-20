package com.jnn.jw.lab;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class RenderThread extends Thread implements Runnable {

	public static boolean runRenderThread=true;
	static boolean renderingStopped=false;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		MainActivity.renderImage();
		//MainActivity.mRT=null;		

		//RenderThread.renderingStopped=true;
	}
	
	
	
	/*public void stopRender(){
		RenderThread.runRenderThread = false;
		   while (!renderingStopped)
			   SystemClock.sleep(500);
		
	}
	
	
	
	*/
	
	
	
	/*
	 * 
	public void run() {
	
		// TODO Auto-generated method stub
		MainActivity.renderImage();
		handler.sendEmptyMessage(0);
	}

	private Handler handler = new Handler() {
		                
		               public void handleMessage(Message msg) {
		                        MainActivity.updateImage();
		               }
		       };
*/

}
