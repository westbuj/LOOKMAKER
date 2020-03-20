package com.jnn.jw.cam;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;

import com.jnn.util.ANR;
import com.jnn.util.SimpleDOMParser;
import com.jnn.util.SimpleElement;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ShapeDesigner extends Activity {
	//public static Bitmap workingImage=null;
	public static String currentProjectName="";
	
	public static ArrayList<Point> refPoints = new ArrayList<Point>();
	public static int pointBeingSet=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        refPoints = new ArrayList<Point>();
        
        setContentView(R.layout.shape_design);
        FloatCanvas fc=new FloatCanvas(this);
        addContentView(fc,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
         
        String sourceFile=getIntent().getExtras().getString("PROJECT");
        ShapeDesigner.currentProjectName=sourceFile.substring(0, sourceFile.lastIndexOf('.')); 

        
        String fContent=ANR.getFileContentsAsString(sourceFile);
        
    	// DocumentBuilder db= new DocumentBuilder();
		SimpleDOMParser dP = new SimpleDOMParser();

		SimpleElement cDoc = null;
		//StringReader sR = new StringReader(fContent);
		try{
		cDoc = dP.parse(new StringReader(fContent));
		}catch (Exception e){}

		Object[] e = cDoc.getChildElements();// .getDocumentElement();//.getFirstChild();

		for (int j = 0; j < e.length; j++) {
			SimpleElement n = (SimpleElement) e[j];

			if (n.getTagName().equals("modelimage")) {
				String bmpURI=n.getAttribute("src");
				//ShapeDesigner.workingImage=BitmapFactory.decodeFile(ANR.appStoragePath+"/"+bmpURI);
				fc.iBMP=BitmapFactory.decodeFile(ANR.appStoragePath+"/"+bmpURI);
				}
			
			if (n.getTagName().equals("poly")) {
				
				AdjPoly newPoly = new AdjPoly(n.getAttribute("name"));
				Object[] pts = n.getChildElements();
				for (int k = 0; k < pts.length; k++) {
					SimpleElement pt = (SimpleElement) pts[k];
					if (pt.getTagName().equals("point"))
					{
						newPoly.addPoint(new Float(pt.getAttribute("x")).floatValue(), new Float(pt.getAttribute("y")).floatValue());
						
					}
				}
				PointEdit.polys.add(newPoly);

				
			}
			
			if (n.getTagName().equals("refpoint")) {
				
				Point newP = new Point(n.getAttribute("name"),n.getAttribute("prompt"),new Float(n.getAttribute("x")).floatValue(),new Float(n.getAttribute("y")).floatValue());
				ShapeDesigner.refPoints.add(newP);
			}
		}
		
		// ImageView wi=(ImageView)findViewById(R.id.designImage);
		// wi.setImageBitmap(ShapeDesigner.workingImage);
		// View ll=(View)findViewById(R.layout.point_edit);
		 addContentView(new PointEdit(this),new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		 
   
    }
	
	public static Point getRefPointByName(String name)
	{
		for (int j=0;j<ShapeDesigner.refPoints.size();j++)
		{
			if (ShapeDesigner.refPoints.get(j).name.equals(name))
				return ShapeDesigner.refPoints.get(j); 
		}
		
		return null;
	}

}
