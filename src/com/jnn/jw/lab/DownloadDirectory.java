package com.jnn.jw.lab;

import java.io.StringReader;

import com.jnn.jw.mid.ModelDirectory;
import com.jnn.jw.mid.ModelEntry;
import com.jnn.util.ANR;
import com.jnn.util.SimpleDOMParser;
import com.jnn.util.SimpleElement;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public class DownloadDirectory extends Thread implements Runnable {

	
	ModelDirectory md;
	@Override
	public void run() {
		loadModelDirectory(md.path);
		md.isLoaded=true;
		MainActivity.modelBrowse.remove(md);
		
		MainActivity.updateModels.sendEmptyMessage(0);
		
		
	}
	
	public DownloadDirectory(ModelDirectory imd)
	{
		md=imd;
	}
	
	
	public static void loadModelDirectory(String url)
	{
		
		String XML = ANR.getFile(MainActivity.mContext.getResources(),url);
		try {

			//wANR.alert(this,XML);
			StringReader sR = new StringReader(XML);
			
			SimpleDOMParser dP = new SimpleDOMParser();

			SimpleElement cDoc;
			try {
				cDoc = dP.parse(sR);
			} catch (Exception e) {
				Log.e("com.jnn.jw", "load model dir failed XML Parse", e);
				return;
			}
			Object[] modelEntry = cDoc.getChildElements();// .
			for (int j = 0; j < modelEntry.length; j++) {
				SimpleElement n = (SimpleElement) modelEntry[j];

				if (n.getTagName().equals("modelimage")) {
					ModelEntry me = new ModelEntry(n.getAttribute("name"), n.getText(),
							"None", n.getAttribute("src"), n
									.getAttribute("sessiondata"));
					//force load of bitmap
					me.getThumbImage(MainActivity.mContext);
					MainActivity.modelBrowse.add(me);
				}
				if (n.getTagName().equals("directory")) {
					ModelDirectory md = new ModelDirectory(n.getAttribute("path"), n.getText());
					
					MainActivity.modelBrowse.add(md);
				}

			}
			MainActivity.modelsLoaded = true;
		} catch (Exception e) {
			MainActivity.modelsLoaded = false;
			Log.e("com.jnn.jw", "load model dir failed", e);
		}


	}

	
}
