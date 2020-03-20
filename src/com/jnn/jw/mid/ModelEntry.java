package com.jnn.jw.mid;

import com.jnn.util.ANR;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

public class ModelEntry implements CachedViewed{
	public String mName;
	public String mDetails;
	public String mImageURL;
	public String mThumbURL;
	public String mKitURL;
	Bitmap mProdImage=null;
	Bitmap mThumbImage=null;
	private View mView=null;
	
	public ModelEntry(String iName,String iDetails,String iURL,String tURL, String iKitURL)
	{
		mName = iName;
		mDetails = iDetails;
		mImageURL = iURL;
		mThumbURL =tURL;
		mKitURL = iKitURL;
	}
	
	public Bitmap getProductionImage(Context c)
	{
		if (mProdImage == null)
		{
			mProdImage = ANR.getBitmapFromURL(c,mImageURL);
		}
		return mProdImage;
		
	}
	public Bitmap getThumbImage(Context c)
	{
		if (mThumbImage == null)
		{
			mThumbImage = ANR.getBitmapFromURL(c,mThumbURL);
		}
		return mThumbImage;
		
	}

	
	public View getView() {
		// TODO Auto-generated method stub
		return mView;
	}

	
	public void spoilView() {
		// TODO Auto-generated method stub
		mView=null;
	}
	
	public void setView(View v) {
		// TODO Auto-generated method stub
		mView=v;		
	}
	

}
