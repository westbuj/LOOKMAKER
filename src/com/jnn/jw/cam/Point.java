package com.jnn.jw.cam;

public class Point {

	public float x=0;
	public float y=0;
	public String name="";
	public String details="";
	public Point(float ix, float iy)
	{
		x=ix;
		y=iy;
	}
	public Point(String iName, float ix, float iy)
	{
		x=ix;
		y=iy;
		this.name=iName;
	}
	
	public Point(String iName,String Desc, float ix, float iy)
	{
		x=ix;
		y=iy;
		this.name=iName;
		this.details = Desc;
	}
}
