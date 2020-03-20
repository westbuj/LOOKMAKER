package com.jnn.jw.cam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class FloatCanvas extends View {
	
	public Bitmap iBMP=null;
	
	public FloatCanvas(Context context) {
		
		
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FloatCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FloatCanvas(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		if (this.iBMP != null)
			canvas.drawBitmap(this.iBMP,0,0,null);
		
		
		 Paint paint = new Paint();
         //paint.setStyle(Paint.Style.STROKE);
         paint.setColor(Color.BLUE);
         
		canvas.drawText("FLOAT CANVAS HERE", canvas.getWidth()/2,canvas.getHeight()/2, paint);
		
		 paint.setColor(Color.WHITE);
		 paint.setStyle(Style.STROKE);
		 
		 //canvas.drawCircle(PointEdit.polys.get(PointEdit.cPoly).points.get(PointEdit.cPoint).x,PointEdit.polys.get(PointEdit.cPoly).points.get(PointEdit.cPoint).y,4, paint);
		 //PointEdit.polys.get(PointEdit.cPoly).drawPoly(canvas);
		 
		 for (int j=0;j<ShapeDesigner.refPoints.size();j++)
		 {
			 if (j == ShapeDesigner.pointBeingSet)
				 paint.setColor(Color.YELLOW);
			 else
				 paint.setColor(Color.GRAY);
			 
			 paint.setStyle(Style.STROKE);
			 canvas.drawCircle(ShapeDesigner.refPoints.get(j).x,ShapeDesigner.refPoints.get(j).y,3, paint);	 
			 
			 
		 }
		 
		 for (int j=0;j<PointEdit.polys.size();j++)
		  PointEdit.polys.get(j).drawPoly(canvas);
		 
	        //paint.setStyle(Paint.Style.STROKE);
	        paint.setColor(Color.YELLOW);
	   	 	paint.setStyle(Style.STROKE);
	        String prompt="Touch the " + ShapeDesigner.refPoints.get(ShapeDesigner.pointBeingSet).details;
			canvas.drawText(prompt, canvas.getWidth()/2,canvas.getHeight()/3, paint);
		
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		Point pt=ShapeDesigner.refPoints.get(ShapeDesigner.pointBeingSet);
		pt.x=event.getX();//RawX();
		pt.y=event.getY();//RawY();
		this.invalidate();
		
		
		return super.onTouchEvent(event);
	}

}
