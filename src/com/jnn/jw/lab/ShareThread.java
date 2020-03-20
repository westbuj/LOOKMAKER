package com.jnn.jw.lab;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.jnn.util.ANR;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore.Images;
import android.util.Log;

public class ShareThread extends Thread implements Runnable {

	public Context mContext;

	public ShareThread (Context c){
		mContext=c;
	}
	@Override
	public void run() {
		//MainActivity.showSharing.sendEmptyMessage(0);
		Bitmap sendMap=overlayImage(MainActivity.tempHolder,
				ANR.getBitmapFromURL(mContext,"INT:jw_lab_overlay"));
		
		ContentValues values = new ContentValues();
		values.put(Images.Media.TITLE, "MADE IN JWCOLORLAB");
		values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
		values.put(Images.Media.MIME_TYPE, "image/jpeg");

		Uri url = mContext.getContentResolver().insert(
				Images.Media.EXTERNAL_CONTENT_URI, values);
		try {
			OutputStream outStream = mContext.getContentResolver()
					.openOutputStream(url);
			sendMap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
			outStream.flush();
			outStream.close();
			Log.d("done", "done");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
	
		
		
		Intent i = new Intent(Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_STREAM, url);
		i.setType("image/jpeg");
		// i.putExtra("sms_body", "Look What I made with jwColorLab");
		i.putExtra(Intent.EXTRA_SUBJECT,
				"Look What I made with jwColorLab");
		MainActivity.hideProgress.sendEmptyMessage(0);
		
		mContext.startActivity(Intent.createChooser(i, "Send Image To:"));

		
	}
	
	private Bitmap overlayImage(Bitmap iImage,Bitmap overlay){
		int sourceColor=255;
		int height = iImage.getHeight();
		int width = iImage.getWidth();
		int r,g,b;
		
		int white = Color.rgb(255, 255, 255);
		// if the color is white, skip this layer
		int[] image = MainActivity.getBitArray(iImage);
		int[] over = MainActivity.getBitArray(overlay);			
			
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++)

			{

				sourceColor = over[(x * width) + y];
				//r=Color.red(sourceColor);
				//g=Color.green(sourceColor);
				//b=Color.blue(sourceColor);
				
				if (sourceColor!=white)
				{
					image[(x * width) + y] =over[(x * width) + y];
				}
			}
		}
		return Bitmap.createBitmap(image, width, height,Bitmap.Config.RGB_565);
	}

}
