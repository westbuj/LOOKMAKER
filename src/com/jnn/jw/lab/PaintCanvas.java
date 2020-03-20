package com.jnn.jw.lab;

import com.jnn.jw.mid.KitEntry;

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

public class PaintCanvas extends View {
	
	public Bitmap iBMP=null;
	
	public PaintCanvas(Context context) {
		
		
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PaintCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PaintCanvas(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		if (this.iBMP != null)
			canvas.drawBitmap(this.iBMP,0,0,null);
		
		
		 
		
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		
		
		Canvas c = new Canvas(MainActivity.currentMask);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
   	 	paint.setStyle(Style.FILL);
		c.drawCircle(event.getX(), event.getY(), 5, paint);
		
		
		
		MainActivity.mQueue.addTask(new RenderThread());
		this.invalidate();
		
		
		return super.onTouchEvent(event);
	}

}
