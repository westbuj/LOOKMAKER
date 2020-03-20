package com.jnn.jw.mid;

import java.util.ArrayList;

import com.jnn.jw.lab.MainActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

public class Palette {
	
	public String mName;
	public String mDetails;
	public String mProductURL;
	public ArrayList<Pigment> mPigments=new ArrayList<Pigment>();
	public KitEntry mParentLayer;
	public View mView=null;
	
	Palette(String iName,String iDetails,String inURL)
	{
		mName=iName;
		mDetails=iDetails;
		mProductURL = inURL;
		
		
	}
	
	public Pigment addPigment(KitEntry iParent,String iName,int r, int g, int b)
	{
		Pigment ret= new Pigment(mParentLayer, iName,Color.rgb(r, g, b));
		//mParentLayer=iParent;
		mPigments.add(ret);
		return ret;
		
	}
	
	
	
	

}
