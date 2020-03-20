package com.jnn.jw.cam;

import java.util.ArrayList;

import com.jnn.jw.lab.MainActivity;
import com.jnn.util.ANR;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class PointEdit extends LinearLayout implements OnClickListener {
	
		public static Point cPoint=null;
		public static int cPoly=0;
		public static ArrayList<AdjPoly> polys=new ArrayList<AdjPoly>();
		public String currentProjectName="";
		
	   public PointEdit(Context context) { 
	        super(context); 
	        this.initComponent(context); 
	    } 
	 
	    public PointEdit(Context context, AttributeSet attrs) { 
	        super(context, attrs); 
	        this.initComponent(context); 
	    } 
	 
	 
	    private void initComponent(Context context) { 
	 
	         LayoutInflater inflater = LayoutInflater.from(context); 
	         View v = inflater.inflate(R.layout.point_edit, null,false); 
	         this.addView(v);    
	         //Get the buttons
	         Button navButton=(Button)v.findViewById(R.id.left_button);
	         navButton.setTag("LEFT");
	         navButton.setOnClickListener(this);
	         
	         navButton=(Button)v.findViewById(R.id.up_button);
	         navButton.setTag("UP");
	         navButton.setOnClickListener(this);
	         
	         navButton=(Button)v.findViewById(R.id.down_button);
	         navButton.setTag("DOWN");
	         navButton.setOnClickListener(this);
	         
	         navButton=(Button)v.findViewById(R.id.right_button);
	         navButton.setTag("RIGHT");
	         navButton.setOnClickListener(this);
	         
	         navButton=(Button)v.findViewById(R.id.next_point_button);
	         navButton.setTag("NEXT");
	         navButton.setOnClickListener(this);
	         
	         navButton=(Button)v.findViewById(R.id.make_mask_button);
	         navButton.setTag("MAKEMASK");
	         navButton.setOnClickListener(this);
	         
	         navButton=(Button)v.findViewById(R.id.next_poly_button);
	         navButton.setTag("NEXTPOLY");
	         navButton.setOnClickListener(this);
	         
	         navButton=(Button)v.findViewById(R.id.next_ref_point);
	         navButton.setTag("NEXT_R_POINT");
	         navButton.setOnClickListener(this);
	          
	         //cPoly=DrawOnTop.centerLips(new Point(160,215), (float)1.5);
	         
	         
	 
	    } 
	 
	    

	
	

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getTag().equals("UP"))
			ShapeDesigner.refPoints.get(ShapeDesigner.pointBeingSet).y -= 1;			
		
		if (v.getTag().equals("DOWN"))
			ShapeDesigner.refPoints.get(ShapeDesigner.pointBeingSet).y += 1;
		
		if (v.getTag().equals("LEFT"))
			ShapeDesigner.refPoints.get(ShapeDesigner.pointBeingSet).x -= 1;
		
		if (v.getTag().equals("RIGHT"))
			ShapeDesigner.refPoints.get(ShapeDesigner.pointBeingSet).x += 1;
	
		if (v.getTag().equals("NEXT"))
			ShapeDesigner.pointBeingSet += 1;
		{
			if (ShapeDesigner.pointBeingSet >= ShapeDesigner.refPoints.size())
				ShapeDesigner.pointBeingSet =0;
		}
		
		if (v.getTag().equals("NEXTPOLY"))
			PointEdit.cPoly += 1;
		
		
		if (v.getTag().equals("MAKEMASK"))
			makeStudioProject();
		
		
		if (v.getTag().equals("NEXT_R_POINT"))
		{
			ShapeDesigner.pointBeingSet += 1;
			if (ShapeDesigner.pointBeingSet >= ShapeDesigner.refPoints.size())
				ShapeDesigner.pointBeingSet =0;
		}
		
		this.invalidate();
	}
	
	public void makeStudioProject()
	{//Make Polygons and add to pointedit polys
		
		PointEdit.polys.clear();
		
		AdjPoly nPoly = new AdjPoly("Lips");
		
		//getRefPointByName(String name)
		
		//Start at left corner of lips
		nPoly.addPoint(ShapeDesigner.getRefPointByName("MOUTHLEFT").x,ShapeDesigner.getRefPointByName("MOUTHLEFT").y);
		Point lipCenter = ShapeDesigner.getRefPointByName("MOUTHCENTER");
		
		//start of bow on left lip top
		nPoly.addPoint(lipCenter.x - 10, lipCenter.y - 25);
		
		// center bottom of bow
		nPoly.addPoint(lipCenter.x, lipCenter.y - 15);
		
		//up to right top of bow
		nPoly.addPoint(lipCenter.x + 10, lipCenter.y - 25);
		
		//Down to right edge
		nPoly.addPoint(ShapeDesigner.getRefPointByName("MOUTHRIGHT").x,ShapeDesigner.getRefPointByName("MOUTHRIGHT").y);
		
		//Down to right bottom
		nPoly.addPoint(lipCenter.x +25, lipCenter.y + 15);

		//left bottom
		nPoly.addPoint(lipCenter.x - 25, lipCenter.y + 15);
		
		PointEdit.polys.add(nPoly);
		
		PointEdit.polys.add(createPolyForEyeShadow());
		
		
		
		
		String projFile="<Config>";
		projFile += "<modelimage src='"+ "p"+ShapeDesigner.currentProjectName +  ".jpg'></modelimage>";
		for (int j=0;j<PointEdit.polys.size();j++)
		{ 
			String layerFile="l_"+ShapeDesigner.currentProjectName+"_"+new Integer(j).toString();
			ANR.saveABitmap(this.makeMaskFromPoly(PointEdit.polys.get(j)),layerFile+".jpg");
			projFile +="<layer order='"+new Integer(j).toString()+"' name='" + PointEdit.polys.get(j).name + "' mask='" + layerFile + ".jpg'>";
			projFile +="<externalPalette src='INT:kit_lips' />";
			projFile +="</layer>";
			
			projFile +="<poly order='"+new Integer(j).toString()+"' name='" + PointEdit.polys.get(j).name + "' mask='" + layerFile + ".jpg'>";
			for (int k=0; k < PointEdit.polys.get(j).points.size();k++)
			{
				projFile += "<point id='"+new Integer(j).toString()+"' x='"+new Float(PointEdit.polys.get(j).points.get(k).x).toString()+"' y='"+new Float(PointEdit.polys.get(j).points.get(k).y).toString()+"'/>";
			}
			
			projFile +="</poly>";
				
		}
		
		for (int k=0; k < ShapeDesigner.refPoints.size();k++)
		{
			Point rPt = ShapeDesigner.refPoints.get(k);
			
			projFile += "<refpoint name='"+rPt.name+"' prompt='"+rPt.details+"' x='"+new Float(rPt.x).toString()+"' y='"+new Float(rPt.y).toString()+"'/>";
		}
		projFile +="</Config>";
		
	    ANR.saveXMLFile(projFile,ShapeDesigner.currentProjectName+".lab");
	}

	public Bitmap makeMaskFromPoly(AdjPoly poly)
	{
		Bitmap rmap= Bitmap.createBitmap(PhotoStudio.targetW,PhotoStudio.targetH, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(rmap);
		Path p =new Path();
		p.setLastPoint(poly.points.get(0).x ,poly.points.get(0).y );
		for (int j=1;j<poly.points.size();j++)
		{
			p.lineTo(poly.points.get(j).x, poly.points.get(j).y);
		}
		
		p.lineTo(poly.points.get(0).x ,poly.points.get(0).y );
		
		
		
		c.drawColor(Color.BLACK);
		
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.WHITE);
		
		c.drawPath(p, paint);
		
		
		rmap =  gaussianBlur(rmap);
		rmap =  gaussianBlur(rmap);
		return  gaussianBlur(rmap);
		
	}
	
	public static Bitmap gaussianBlur(Bitmap iBmp)
	{
		//ANR.saveABitmap(iBmp,"preBlur");
		int height = iBmp.getHeight();
		int width = iBmp.getWidth();
		int maskColor;
		int totalColor=0;
		// Bitmap newImage=MainActivity.model.copy(Bitmap.Config.RGB_565 ,
		// true);// Bitmap.createBitmap(width, height,Bitmap.Config.RGB_565 );
		int[] newImage = ANR.getBitArray(iBmp);
		for (int x = 4; x < height-4; x++) {
			for (int y = 4; y < width-4; y++)

			{
				totalColor=0;
				maskColor = Color.red(newImage[(x * width) + y]);
				if (maskColor>5)
				{
				totalColor += Color.red(newImage[((x-1) * width) + y]);
				totalColor += Color.red(newImage[((x-1) * width) + y-1]);
				totalColor += Color.red(newImage[(x * width) + y-1]);
				totalColor += Color.red(newImage[((x+1) * width) + y-1]);
				
				totalColor += Color.red(newImage[((x+1) * width) + y]);
				totalColor += Color.red(newImage[((x+1) * width) + y+1]);
				totalColor += Color.red(newImage[(x * width) + y+1]);
				totalColor += Color.red(newImage[((x-1) * width) + y+1]);
				totalColor += maskColor;
				
				int newGray=(int)totalColor/9;
				newImage[(x * width) + y]=Color.rgb(newGray,newGray,newGray);
				}
	
				

			}
		}
		return Bitmap.createBitmap(newImage, width, height,Bitmap.Config.RGB_565);
	}
	
	public static AdjPoly createPolyForEyeShadow()
	{
	
		
		AdjPoly nPoly = new AdjPoly("Shadowbase");
		
		Point rEye =ShapeDesigner.getRefPointByName("RIGHTEYE");
		Point ISRBROW =ShapeDesigner.getRefPointByName("ISRBROW");
		Point OSRBROW =ShapeDesigner.getRefPointByName("OSRBROW");
		
		float brX=ISRBROW.x + (2*(OSRBROW.x - ISRBROW.x)/3);
		float brY=ISRBROW.y - 12;
		
		
		
		Point browArch=new Point(brX,brY);
		
		//top of eyeball
		brX=rEye.x;
		brY=rEye.y - 7;
		
		Point topEyeball=new Point(brX,brY);
		
		//is eyeball
		brX=rEye.x -10;
		brY=rEye.y;
		
		Point isEye=new Point(brX,brY);
		
		//os eyeball
		brX=rEye.x + 10;
		brY=rEye.y;
		
		Point osEye=new Point(brX,brY);
		
		
		nPoly.addPoint(osEye.x, osEye.y);
		nPoly.addPoint(OSRBROW.x, OSRBROW.y);
		nPoly.addPoint(browArch.x, browArch.y);
		nPoly.addPoint(ISRBROW.x, ISRBROW.y);
		nPoly.addPoint(isEye.x, isEye.y);
		nPoly.addPoint(topEyeball.x, topEyeball.y);
		
		
		return nPoly;
	}

}
