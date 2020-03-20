package com.jnn.jw.mid;

import java.util.ArrayList;

import com.jnn.jw.cam.R;
import com.jnn.util.ANR;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class KitEntry {

	public String mName;
	public Bitmap mIcon = null;
	public Bitmap mMask=null;
	public String mMaskURL;
	public int cShade;
	public int renderOrder=0;
	public int id;
	public ArrayList<Palette> mPalettes = new ArrayList<Palette>();
	
	public View mView=null;
	
	public KitEntry(String iName, int iRenderOrd)
	{
		mName = iName;
		renderOrder=iRenderOrd;
	}
	
	public Palette addPalette(String iName,String iDetails,String inURL)
	{
		Palette outPal=new Palette(iName,iDetails,inURL);
		outPal.mParentLayer=this;
		mPalettes.add(outPal);
		return outPal;
	}
	Bitmap getBitmap(Context c)
	{
		
		if (mIcon == null)
			mIcon=BitmapFactory.decodeResource(c.getResources(),R.drawable.generic_kit_icon);
		
	return mIcon;	
			
	}
	
	public Bitmap getMask(Context c)
	{
		
		if (mMask == null)
		{mMask=ANR.getBitmap(c,mMaskURL);			
		}
		return mMask;
	}
	
}
