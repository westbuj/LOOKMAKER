/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jnn.jw.paint;

import com.jnn.jw.lab.MainActivity;
import com.jnn.jw.mid.KitEntry;
import com.jnn.jw.mid.Rules;
import com.jnn.util.ANR;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class FingerPaint extends GraphicsActivity
        implements ColorPickerDialog.OnColorChangedListener {    

	  private Paint       mPaint;
	    private MaskFilter  mEmboss;
	    private MaskFilter  mBlur;
	     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);
        
        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                                       0.4f, 6, 3.5f);

        mBlur = new BlurMaskFilter(4, BlurMaskFilter.Blur.NORMAL);
    }
    
  
    
    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

    public class MyView extends View {
        
        private static final float MINP = 0.25f;
        private static final float MAXP = 0.75f;
        
       
        private Path    mPath;
        private Paint   mBitmapPaint;
        
        private Paint   mStylus;
        
        private Bitmap  maskBitmap;
        private Canvas  maskCanvas;
        private Bitmap  mTarget;
        private Bitmap  mBitmap;
        private Bitmap  maskedTarget;
        private Canvas  mCanvas;
        // For speed
    	float tHsv[] = new float[4];
    	float sHsv[] = new float[4];
    	float fHsv[] = new float[4];
    	
    	
 
        
        private Rules rules = null;
       public int[] getBitArray(Bitmap iMap) {
    		int[] pix = new int[iMap.getWidth() * iMap.getHeight()];
    		iMap.getPixels(pix, 0, iMap.getWidth(), 0, 0, iMap.getWidth(), iMap.getHeight());
    		return pix;
    	}
       
       public Bitmap stripAlpha(Bitmap source)
       {
    	   int[] newImage = getBitArray(source);
    	   int dx=source.getWidth();
    	   int dy=source.getHeight();
    	   for (int x = 0; x < dx*dy; x++)
           {           
           	//trans=Color.red(maskImage[x]);       			
           	newImage[x] = Color.argb(255,Color.red(newImage[x]),Color.green(newImage[x]), Color.blue(newImage[x]));            			
           }
    	   
    	   return Bitmap.createBitmap(newImage, dx,dy,Bitmap.Config.ARGB_8888);
    	   
       }
    	public void applyMask(){   
        	
        
        int[] newImage = getBitArray(this.mTarget);
        int[] maskImage= getBitArray(this.maskBitmap);
             
        int trans=0;
        int dx=this.mTarget.getWidth();
        int dy=this.mTarget.getHeight();
        
        ANR.saveABitmap(stripAlpha(this.mTarget),"pre_mask.jpg");
        
        for (int x = 0; x < dx*dy; x++)
        {           
        	//trans=Color.red(maskImage[x]);       			
        	newImage[x] = Color.argb(Color.red(maskImage[x]),Color.red(newImage[x]),Color.green(newImage[x]), Color.blue(newImage[x]));            			
        }
        
        
        this.maskedTarget = Bitmap.createBitmap(newImage, dx,dy,Bitmap.Config.ARGB_8888);
        ANR.saveABitmap(this.maskBitmap,"new_mask.jpg");
        ANR.saveABitmap(this.mTarget,"new_target.jpg");
        Paint paint = new Paint();
        //paint.setFilterBitmap(false);

        mCanvas.drawBitmap(mBitmap, 0, 0, paint);
       // mCanvas.drawBitmap(maskColor, 0, 0, paint);
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawBitmap(maskedTarget, 0, 0, paint);
        
    }
        public MyView(Context c) {
        	
            super(c);
            
            String sourceFile=getIntent().getExtras().getString("PROJECT");
        	String fContent=ANR.getFileContentsAsString(sourceFile);	
        	this.rules=new Rules(c,fContent);
        	
        	mStylus=new Paint();
        	mStylus.setStyle(Paint.Style.FILL_AND_STROKE);
        	mStylus.setColor(Color.CYAN);
        	
            //mBitmap = this.rules.mainimage;//Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
            mBitmap=Bitmap.createBitmap(this.rules.mainimage.getWidth(),this.rules.mainimage.getHeight(), Bitmap.Config.ARGB_8888);
            maskBitmap=Bitmap.createBitmap(this.rules.kit.get(0).getMask(c).getWidth(),this.rules.kit.get(0).getMask(c).getHeight(), Bitmap.Config.ARGB_8888);
            maskCanvas = new Canvas(maskBitmap);
            maskCanvas.drawBitmap(this.rules.kit.get(0).getMask(c), 0, 0,null);
                        
            
            
            
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawBitmap(this.rules.mainimage, 0, 0,null);
            
            //<paletteEntry name="3" red="188" green="117" blue="86" />
            Color.colorToHSV(Color.rgb(188,117,86), sHsv);
            float targetHue=sHsv[0];
            //Create an Image with the Max COlor (for overlay)
            
            //this.maskBitmap=mBitmap;
            
            int[] newImage = getBitArray(this.rules.mainimage);
            int[] maskImage= getBitArray(this.maskBitmap);
                 
            int dx=this.rules.mainimage.getWidth();
            int dy=this.rules.mainimage.getHeight();
            
            //create the target
            for (int x = 0; x < dx * dy ; x++)
            {  			int trans=Color.red(maskImage[x]);            			
            			Color.colorToHSV(newImage[x], sHsv);
            			sHsv[0]=targetHue;
            			newImage[x] = Color.HSVToColor( sHsv);            			
            	
            }
            
            this.mTarget = (Bitmap.createBitmap(newImage, this.rules.mainimage.getWidth(),this.rules.mainimage.getHeight(),Bitmap.Config.ARGB_8888));
            ANR.saveABitmap(this.mTarget,"target.jpg");
            //Create the masked target
            for (int x = 0; x < dx*dy; x++)
            {
            	          
            			int trans=Color.red(maskImage[x]);
            			
            			newImage[x] = Color.argb(trans, Color.red(newImage[x]), Color.green(newImage[x]),Color.blue(newImage[x]));
            			
            	
            }
            
            this.maskedTarget = (Bitmap.createBitmap(newImage,dx,dy,Bitmap.Config.ARGB_8888));
            
            
            
            
            ANR.saveABitmap(this.maskedTarget,"masked_target.jpg");
            ANR.saveABitmap(this.mBitmap,"source.jpg");
            //mCanvas.drawBitmap(maskBitmap, 0, 0, null);
            
            
            
            Paint paint = new Paint();
            //paint.setFilterBitmap(false);

            mCanvas.drawBitmap(mBitmap, 0, 0, paint);
           // mCanvas.drawBitmap(maskColor, 0, 0, paint);
            //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            mCanvas.drawBitmap(this.maskedTarget, 0, 0, paint);
            
            mPath = new Path();
            mBitmapPaint = new Paint();//Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFAAAAAA);

          //maskCanvas.drawPath(mPath, mPaint);
         // renderImage();
            ANR.saveABitmap(mBitmap,"final_image.jpg");
          canvas.drawBitmap(mBitmap, 0, 0, null);
          canvas.drawCircle(mX, mY, 3, this.mStylus);
          
           
            
        }
        
        

		private float mX, mY;
        private static final float TOUCH_TOLERANCE = 1;
        
        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            //mCanvas.drawPath(mPath, mPaint);
            maskCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw            
            mPath.reset();
            applyMask();
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }
    
    
    private static final int COLOR_MENU_ID = Menu.FIRST;
    private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
    private static final int BLUR_MENU_ID = Menu.FIRST + 2;
    private static final int ERASE_MENU_ID = Menu.FIRST + 3;
    private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
        menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
        menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
        menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
        menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z');

        /****   Is this the mechanism to extend with filter effects?
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(
                              Menu.ALTERNATIVE, 0,
                              new ComponentName(this, NotesList.class),
                              null, intent, 0, null);
        *****/
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
            case COLOR_MENU_ID:
                new ColorPickerDialog(this, this, mPaint.getColor()).show();
                return true;
            case EMBOSS_MENU_ID:
                if (mPaint.getMaskFilter() != mEmboss) {
                    mPaint.setMaskFilter(mEmboss);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case BLUR_MENU_ID:
                if (mPaint.getMaskFilter() != mBlur) {
                    mPaint.setMaskFilter(mBlur);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case ERASE_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(
                                                        PorterDuff.Mode.CLEAR));
                return true;
            case SRCATOP_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(
                                                    PorterDuff.Mode.SRC_ATOP));
                mPaint.setAlpha(0x80);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    

	
}
