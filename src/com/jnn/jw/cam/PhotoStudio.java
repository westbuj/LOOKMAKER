package com.jnn.jw.cam;

import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.jnn.util.ANR;

import android.app.Activity;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class PhotoStudio extends Activity implements OnClickListener, Camera.ShutterCallback {
public static int targetW=320;
public static int targetH=480;
public static float targetRatio=(float)((float)targetH/(float)targetW);


public static Camera.Parameters cParam=null;
public static RectF overlayClip=new RectF();
public static Rect previewSize=new Rect();
public iPreview mPreview=null;

public static String saveABitmap(Bitmap sBMP, String name)
{
	String retval="";
	FileOutputStream outStream = null;
	try {
		// write to local sandbox file system
		// outStream =
		// CameraDemo.this.openFileOutput(String.format("%d.jpg",
		// System.currentTimeMillis()), 0);
		// Or write to sdcard
		File root = new File(ANR.appStoragePath);
		if (root.canWrite()){
			File gpxfile = new File(root,name);
			retval=gpxfile.toURI().toASCIIString();
			outStream = new FileOutputStream(gpxfile);
			Log.d("CAM_FILE", " Compress");
			sBMP.compress(Bitmap.CompressFormat.JPEG, 90, outStream); 
			outStream.flush();
			
			//outStream.write(bmOverlay.data);
			outStream.close();
			Log.d("CAM_FILE", "BMP SAVED -  " + name);
		}				
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
	}
	return retval;
}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.mPreview = new iPreview(this);
        
        
        
       DrawOnTop mDraw = new DrawOnTop(this);
       ShutterButton b= new ShutterButton(this);
       b.setOnClickListener(this);
             

        setContentView(mPreview);
        addContentView(mDraw, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addContentView(b, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        

    }


	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.mPreview.takePicture();
	}


	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		
		
		
	}


	public void onShutter() {
		// TODO Auto-generated method stub
		
	}
	
	public static int getWidth(int w, int h)
	{
		
		if (w>h)
			return h;
		else return w;	
		
		
	}
	public static int getHeight(int w, int h)
	{
		if (w>h)
			return w;
		else return h;	
		
		
	}
	public static RectF getCrop(int imageRealWidth, int imageRealHeight)
	{
		int w=imageRealWidth;
		int h=imageRealHeight;
		boolean flipAxis=false;
		if (w>h)
		{			
			h=imageRealWidth;
			w=imageRealHeight;
			flipAxis=true;
		}
		
		float middleW=w/2;
		float middleH=h/2;
		float left=middleW - (w / 3);
		float top =middleH - ( 2 * (h/5)) ;
		float right=middleW + (w /3 );
		float bottom=top + (right - left) * PhotoStudio.targetRatio;
		
		if (flipAxis)
		{
			float holder=left;
			left=top;
			top=holder;
			holder=bottom;
			bottom=right;
			right=holder;
			
		}
		
		return new RectF(left,top,right, bottom);
		
		
		
	}
public static RectF getFramingOval(RectF cropR)
{
	
	RectF oval = new RectF();
    oval.left=  cropR.left + (cropR.right - cropR.left)/6;
    oval.right=  cropR.right - (cropR.right - cropR.left)/6;
    oval.top= cropR.top + (cropR.bottom-cropR.top)/10;
    oval.bottom=oval.top + (float)(1.25*(oval.right-oval.left));
    return oval;
}
}
class DrawOnTop extends View {
		public float lastX=0;
		public int direction=10;
        public DrawOnTop(Context context) {
                super(context);
                // TODO Auto-generated constructor stub
                
             //   LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             //  View view=layoutInflater.inflate(R.layout.cam_overlay,this); 
                Button b=new Button(context);
                b.setText("test");
                //this.addView(b);
                
        }
        
        
        public static AdjPoly centerLips(Point c, float scale)
        {
        	AdjPoly lips = new AdjPoly("Lips");
        	lips.addPoint(c.x-(scale * 15),c.y + (scale * 1));
        	lips.addPoint(c.x-(scale * 5),c.y -(scale * 2 ));
        	lips.addPoint(c.x,c.y);
        	lips.addPoint(c.x+(scale * 5),c.y-(scale * 2));
        	lips.addPoint(c.x+(scale * 15),c.y+(scale * 1));
        	lips.addPoint(c.x+(scale * 5),c.y+(scale * 7));
        	lips.addPoint(c.x-(scale * 5),c.y+(scale * 7));
        	return lips;
        }
        
        public static AdjPoly centerEye(String name,Point c, float scale)
        {
        	
        	AdjPoly eye = new AdjPoly(name);
        	eye.addPoint(c.x-(scale * 14),c.y);
        	eye.addPoint(c.x,c.y-(scale * 8));
        	eye.addPoint(c.x+(scale * 14),c.y);
        	eye.addPoint(c.x,c.y+(scale * 8));
        	
        	
        	return eye;
        }
        
        public static void drawLips(Canvas canvas)
        {
        	AdjPoly lips = centerLips(new Point(160,215), (float)1.5);
        	
        	lips.drawPoly(canvas);
        	
        	
        	
        	//draw eyes
        	AdjPoly leye = DrawOnTop.centerEye("Left Eye",new Point(135,165),(float)1.15);
        	AdjPoly reye = DrawOnTop.centerEye("Right Eye",new Point(180,165),(float)1.15);
        	leye.drawPoly(canvas);
        	reye.drawPoly(canvas);
        	
        	
        }

       
        public static void drawOverlay(Canvas canvas, int height, int width)
        {
        	
        	int cH=canvas.getHeight();
           	int cW=canvas.getWidth();
        
           	 DrawOnTop.drawLips(canvas);
        	 Paint paint = new Paint();
             paint.setStyle(Paint.Style.STROKE);
             paint.setColor(Color.WHITE);
             
             
             //TestCameraOverlay.overlayClip.set(left,top,right,bottom);
             RectF cropR=PhotoStudio.getCrop(canvas.getWidth(), canvas.getHeight());
             canvas.drawRect(cropR, paint);
             RectF oval = PhotoStudio.getFramingOval(cropR);
             paint.setColor(Color.BLACK);
             canvas.drawLine(0, (oval.bottom-oval.top)/2,canvas.getWidth(),(oval.bottom-oval.top)/2, paint);           
             paint.setStyle(Paint.Style.STROKE);
             paint.setColor(Color.CYAN);
             canvas.drawOval(oval,paint);
             
             
             //canvas.drawText("canvas h,w= " + Integer.toString(cH) + "," + Integer.toString(cW)+"  and target is" + Integer.toString(height) + "," + Integer.toString(width), 0, cH-30, paint);
             
        }
        @Override
        protected void dispatchDraw(Canvas canvas) {
                // TODO Auto-generated method stub

        	                 
                
                PhotoStudio.overlayClip=PhotoStudio.getCrop(canvas.getWidth(),canvas.getHeight());
                
                
                Size sP = PhotoStudio.cParam.getPreviewSize();                
                Size s = PhotoStudio.cParam.getPictureSize();
                DrawOnTop.drawOverlay(canvas, sP.height, sP.width);
                
                super.dispatchDraw(canvas);


        }

}

//----------------------------------------------------------------------


class ShutterButton extends Button {
	
    public ShutterButton(Context context) {
            super(context);
            // TODO Auto-generated constructor stub            
            this.setText("Take Photo");
    }

    
    @Override
    protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub

    	   
            super.onDraw(canvas);
    }
  

}

//----------------------------------------------------------------------


class iPreview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    public Camera mCamera;
    
 
    iPreview(Context context) {
        super(context);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    public float scaleNumber(float newTotal, float refInput, float refStandard)
    {
    	return newTotal *(refInput/refStandard);
    }
public void takePicture()
{
	
	
	
	ShutterCallback shutterCallback = new ShutterCallback(){public void onShutter(){Log.d("CAM_ACTION", "onShutter'd");}};
	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() {		public void onPictureTaken(byte[] data, Camera camera) {			Log.d("CAM_ACTION", "onPictureTaken - raw");		}	};
	
	/** Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			
			AdjPoly lips=null;
			AdjPoly leye=null;
			AdjPoly reye=null;
			
			Bitmap bmp=null;
			Bitmap rotBitmap=null;
			Bitmap finalBitmap=null;
			Bitmap mutableBMP=null; 
			try{
			Log.d("CAM_SAVE", "onPictureTaken - jpeg _ BEGIN");
			Log.d("CAM_ACTION", " DECODE BYTE ARRAY");
			
			
			bmp=BitmapFactory.decodeByteArray(data,0,data.length);
			//bmp=BitmapFactory.decodeResource(getResources(), R.drawable.test_capture);
			
			//Calculate crop prep size
			
			RectF cropR=PhotoStudio.getCrop(bmp.getWidth(), bmp.getHeight());
			float virtualW=PhotoStudio.getWidth((int)(cropR.right-cropR.left),(int)(cropR.bottom-cropR.top));
			float ratio=virtualW/PhotoStudio.targetW;//.getWidth(bmp.getWidth(), bmp.getHeight());
			
			
			
			float finalW=PhotoStudio.getWidth(bmp.getWidth(), bmp.getHeight())/ratio;
			float finalH= finalW * PhotoStudio.targetRatio;
			
			if(bmp.getWidth() > bmp.getHeight())
			{
				float holder =finalW;
				finalW=finalH;
				finalH=holder;
				
			}
			Bitmap prepmap = Bitmap.createScaledBitmap(bmp, (int)finalW,(int)finalH,false);
			
			//PhotoStudio.saveABitmap(prepmap,"PREMAP");
			
			
			finalBitmap=prepmap;
			if (prepmap.getHeight() < prepmap.getWidth())
	        {
	        	Matrix rotMatrix = new Matrix();
		        
		        //then rotate
		        // rotate the Bitmap
	        	rotMatrix.postRotate(90);
	        	finalBitmap = Bitmap.createBitmap(prepmap, 0, 0, prepmap.getWidth(),prepmap.getHeight(), rotMatrix, true);
	        //	PhotoStudio.saveABitmap(finalBitmap,"ROTATE");
	        	
	        }
			
			
			
			Log.d("CAM_ACTION", " Create overlay");
			//first resize
			
			RectF finalCrop= PhotoStudio.getCrop(finalBitmap.getWidth(),finalBitmap.getHeight());
			Rect intRect=new Rect((int)finalCrop.left,(int)finalCrop.top,(int)finalCrop.right,(int)finalCrop.bottom);
			
			
			
	        mutableBMP = Bitmap.createBitmap(PhotoStudio.targetW, PhotoStudio.targetH,Bitmap.Config.RGB_565);
	        
	        //PhotoStudio.saveABitmap(mutableBMP,"CROP");
	        			 
		     Canvas canvas = new Canvas(mutableBMP);
		     Log.d("CAM_ACTION", " Draw bitmap");
		    
		  //   canvas.drawBitmap(finalBitmap,0,0,null);
		     
		     canvas.drawBitmap (finalBitmap, intRect, new RectF(0,0,PhotoStudio.targetW,PhotoStudio.targetH), null);
		     

		     
		     
		     Log.d("CAM_ACTION", " Draw overlay");
		    // DrawOnTop.drawOverlay(canvas,  TestCameraOverlay.targetH,  TestCameraOverlay.targetW);
		     
		     Log.d("CAM_ACTION", " paint");
		     Paint paint = new Paint();
             paint.setStyle(Paint.Style.STROKE);
             paint.setColor(Color.BLACK);
             canvas.drawText("Orig bmp w,h= " + Integer.toString(bmp.getWidth()) + "," + Integer.toString(bmp.getHeight()), 0, 10, paint);
             
               
            lips = DrawOnTop.centerLips(new Point(160,320), (float)2.5);
       //  	lips.drawPoly(canvas);
         	
         	
         	//draw eyes
         	leye = DrawOnTop.centerEye("Left Eye",new Point(160-60,185),(float)2.15);
         	reye = DrawOnTop.centerEye("Right Eye",new Point(160+60,185),(float)2.15);
         //	leye.drawPoly(canvas);
         //	reye.drawPoly(canvas);
         	
		     
			}
			catch (Exception e) {
				Log.d("CAM_ACTION", e.getMessage());
			}
		     			
			
		     Log.d("CAM_ACTION", " saving");
		     
		     String projName=ANR.getNextProjectName("Look_",".lab");
		     projName=projName.substring(0, projName.lastIndexOf('.'));
		     
		     
		     
		     String imageFileName= String.format("p"+projName+".jpg");
		     PhotoStudio.saveABitmap(mutableBMP,imageFileName);
             
             String xml="<Config>";
             
             xml += "<modelimage src='"+ imageFileName +  "'></modelimage>";
             xml +="<poly name='lips'>";
             
             for (int j=0;j < lips.points.size();j++)
               	 xml += "<point id='"+new Integer(j).toString()+"' x='"+new Float(lips.points.get(j).x).toString()+"' y='"+new Float(lips.points.get(j).y).toString()+"'/>";
                   
             xml +="</poly>";
             
             xml +="<poly name='Left Eye'>";
             
             for (int j=0;j < leye.points.size();j++)
               	 xml += "<point id='"+new Integer(j).toString()+"' x='"+new Float(leye.points.get(j).x).toString()+"' y='"+new Float(leye.points.get(j).y).toString()+"'/>";
                   
             xml +="</poly>";
             
             xml +="<poly name='Right Eye'>";
             
             for (int j=0;j < reye.points.size();j++)
               	 xml += "<point id='"+new Integer(j).toString()+"' x='"+new Float(reye.points.get(j).x).toString()+"' y='"+new Float(reye.points.get(j).y).toString()+"'/>";
                   
             xml +="</poly>";
             
             xml +="<refpoint name='MOUTHCENTER' prompt='Center of Mouth' x='0' y='0' />";
             xml +="<refpoint name='RIGHTEYE' prompt='Right Eye' x='0' y='0' />";
             xml +="<refpoint name='LEFTEYE' prompt='Left Eye' x='0' y='0' />";
             xml +="<refpoint name='MOUTHLEFT' prompt='Left Corner of mouth' x='0' y='0' />";
             xml +="<refpoint name='MOUTHRIGHT' prompt='Right Corner of mouth' x='0' y='0' />";
             
             xml +="<refpoint name='NOSETIP' prompt='Tip of Nose' x='0' y='0' />";
             xml +="<refpoint name='OSRBROW' prompt='Outer End of Right Eyebrow' x='0' y='0' />";
             xml +="<refpoint name='ISRBROW' prompt='Inside Start of Right Eyebrow' x='0' y='0' />";
             xml +="<refpoint name='OSLBROW' prompt='Outer End of Left Eyebrow' x='0' y='0' />";
             xml +="<refpoint name='ISLBROW' prompt='Inside Start of Left Eyebrow' x='0' y='0' />";
             
             
             
             
             
             xml += "</Config>";
		     
		     ANR.saveXMLFile(xml,projName+".lab");
			
			Log.d("CAM_SAVE", "onPictureTaken - jpeg _DONE");
		}
	};  

	
	this.mCamera.takePicture(shutterCallback, rawCallback,jpegCallback);
	//this.mCamera.release();
}
    public void surfaceCreated(SurfaceHolder holder) {
    	 // The Surface has been created, acquire the camera and tell it where
        // to draw.
        this.mCamera = Camera.open();
        PhotoStudio.cParam=mCamera.getParameters();
        try {
        	this.mCamera.setPreviewDisplay(holder);
        } catch (IOException exception) {
        	this.mCamera.release();
        	this.mCamera = null;
            // TODO: add more exception handling logic here
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
    	this.mCamera.stopPreview();
    	this.mCamera.release();
    	this.mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        Camera.Parameters parameters = this.mCamera.getParameters();
 
       // parameters.set("jpeg-quality", 100); 
        parameters.set("orientation", "portrait"); 
      //  parameters.set("picture-size", "320X430"); 
      //  parameters.set("rotation", 0); 
     //   parameters.setPictureFormat(PixelFormat.JPEG); 

        //parameters.setPreviewSize(w, h);
     //  TestCameraOverlay.previewSize.bottom=parameters.getPreviewSize().height;
       // TestCameraOverlay.previewSize.right=parameters.getPreviewSize().width;
        
        //For emulator only
        
        
        
        this.mCamera.setParameters(parameters);
        this.mCamera.startPreview();
        
    }
     
    class ShutterButton extends Button{

		public ShutterButton(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setVisibility(VISIBLE);
			this.setText("CLICK");
		}
    	
    }

}



