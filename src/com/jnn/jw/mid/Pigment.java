package com.jnn.jw.mid;

import com.jnn.util.ANR;

import android.graphics.Bitmap;
import android.view.View;

public class Pigment {

	public String mName;
	public int mColor;
	public Bitmap chip;
	public KitEntry mParentLayer;
	public View mView=null;
	
	Pigment(KitEntry iParent,String iName, int iColor){
		mName=iName;
		mColor=iColor;
		//build chip here
		chip=ANR.drawSolidChip(iColor);
		mParentLayer=iParent;
		
	}
	
}
