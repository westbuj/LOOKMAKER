package com.jnn.jw.cam;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class AdjPoly {

	public ArrayList<Point> points=new ArrayList<Point>();
	public String name="";
	public AdjPoly(String iName)
	{
		this.name=iName;
	}
	
	public void addPoint(float x, float y)
	{
		points.add(new Point(x,y));
	}
	
	public void drawPoly(Canvas canvas)
	{
		
		 Paint paint = new Paint();
         paint.setStyle(Paint.Style.STROKE);
         paint.setColor(Color.RED);
         
		for (int j=0;j<points.size();j++)
		{
			Point p=points.get(j);
			Point p2=null;
			if (j+1>=points.size())
			{
				p2=points.get(0);
			}else
			{
				p2=points.get(j+1);
			}
			
			canvas.drawLine(p.x, p.y,p2.x, p2.y, paint);
		}
	}
}
