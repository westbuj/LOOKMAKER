package com.jnn.jw.lab;

import com.jnn.jw.cam.R;
import java.io.FileNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Hashtable;

import com.jnn.util.ANR;
import com.jnn.util.SimpleDOMParser;
import com.jnn.util.SimpleElement;
import com.jnn.util.TaskQueue;
import com.jnn.jw.mid.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {

	public static ArrayList<MenuState> rawTree = new ArrayList<MenuState>();
	public static Context mContext = null;
	public static Bitmap currentMask=null;

	public static Bitmap model;
	public static Bitmap tempHolder;
	public static ImageView i; 
	
	public static int touchTarget=0;
		
	public static Drawable transparent;

	public static ArrayList<KitEntry> cKit = null;//new ArrayList<KitEntry>();
	public static ArrayList<Object> modelBrowse = new ArrayList<Object>();
	public static ArrayList<ModelDirectory> modelDirectory = new ArrayList<ModelDirectory>();
	public static boolean modelsLoaded = false;
 
	
	
	// For speed
	static float tHsv[] = new float[4];
	static float sHsv[] = new float[4];
	static float fHsv[] = new float[4];

	// For the rendering thread
	public static RenderThread mRT = null;
	public static boolean isRendering = false;
	public static boolean resetRendering = false;
	
	public static Handler updateImage = new Handler() {

		public void handleMessage(Message msg) {
			MainActivity.updateImage();
		}
	};
	
	public static Handler updateModels = new Handler() {

		public void handleMessage(Message msg) {
			ModelActivity.mModelAdapter.notifyDataSetChanged();
		}
	};
	
	
	public static ProgressDialog mProgress;//=new ProgressDialog(MainActivity.mContext);
	
	public static Handler showProgress = new Handler() {

		public void handleMessage(Message msg) {
			MainActivity.mProgress.setMessage("....downloading....");
			MainActivity.mProgress.show();
		}
	};
	
	public static Handler showSharing = new Handler() {

		public void handleMessage(Message msg) {
			MainActivity.mProgress.setMessage("....preparing....");
			MainActivity.mProgress.show();
		}
	};
	
	
	public static Handler hideProgress = new Handler() {

		public void handleMessage(Message msg) {
			MainActivity.mProgress.hide();  
		}
	};

	public static TaskQueue mQueue = new TaskQueue();
	
	//for start up control
	public static boolean LOAD_DEFAULT=true;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		mProgress=new ProgressDialog(MainActivity.mContext);
		
		 	        
	        
		MainActivity.mQueue.start();
		
		StateObjectWrapper so = (StateObjectWrapper)getLastNonConfigurationInstance();
		if (so!=null)
		{
			MainActivity.cKit=so.cKit;
			MainActivity.model=so.model;
			MainActivity.modelBrowse=so.modelBrowse;
			MainActivity.modelDirectory=so.modelDirectory;
			MainActivity.modelsLoaded=so.modelsLoaded;
			MainActivity.rawTree=so.rawTree;
			MainActivity.tempHolder=so.tempHolder;
			MainActivity.LOAD_DEFAULT=so.LOAD_DEFAULT;
		}
		else
			{LOAD_DEFAULT=true;
				String sourceFile=getIntent().getExtras().getString("PROJECT");
	        	String fContent=ANR.getFileContentsAsString(sourceFile);	
	        	parseModelRules(fContent);
	    		LOAD_DEFAULT=false;
			}
		
		//transparent = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),(R.drawable.transparent)));
		// Create a LinearLayout in which to add the ImageView
		LinearLayout l = new LinearLayout(this);
		l.setOrientation(LinearLayout.VERTICAL);

		// Instantiate an ImageView and define its properties

		//Bitmap splash = BitmapFactory.decodeResource(getResources(),
			//	R.drawable.lab_spl);//

		i = new ImageView(this);
		//LOAD_DEFAULT=true;
		

		i.setAdjustViewBounds(true);
		// set the ImageView bounds to match the Drawable's dimensions
		i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		// Add the ImageView to the layout and set the layout as the content
		// view

		l.addView(i);

		setContentView(l);
		addContentView(new PaintCanvas(this), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));


	}

	public String getRawResource(int id) {
		String oString = "";
		InputStream is = this.getResources().openRawResource(id);
		try {
			int size = is.available();

			// Read the entire resource into a local byte buffer.
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			oString = new String(buffer);
		} catch (Exception e) {

		}

		return oString;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, StaticResources.MENU_MODELS, 0, "Models");
		menu.add(0, StaticResources.MENU_KIT, 0, "Kit");
		menu.add(0, StaticResources.MENU_SHARE, 0, "Share");
		// menu.add(0, StaticResources.MENU_SAVE, 0, "Save");
		menu.add(0, StaticResources.MENU_ABOUT, 0, "Help");
		return true;
	}
	
	
	public Object onRetainNonConfigurationInstance() 
	{   
		
		StateObjectWrapper so = new StateObjectWrapper();
		so.cKit=MainActivity.cKit;
		so.model=MainActivity.model;
		so.modelBrowse=MainActivity.modelBrowse;
		so.modelDirectory=MainActivity.modelDirectory;
		so.modelsLoaded=MainActivity.modelsLoaded;
		so.rawTree=MainActivity.rawTree;
		so.tempHolder=MainActivity.tempHolder;
		so.LOAD_DEFAULT=MainActivity.LOAD_DEFAULT;
		
		return so;
		}

	protected void onResume() {
		super.onResume();
		if (MainActivity.LOAD_DEFAULT)
		{model = BitmapFactory.decodeResource(getResources(),R.drawable.lab_spl);
		tempHolder = model;
		i.setImageBitmap(model);
		i.invalidate();
		String defaultRules = getRawResource(R.raw.angie_b);
		parseModelRules(defaultRules);
		LOAD_DEFAULT=false;
		}
		
		else
				
		{
			if (tempHolder==null)
				tempHolder = model;
				
		i.setImageBitmap(tempHolder);
		i.invalidate();
		}
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item)

	{
		try {
			switch (item.getItemId())

			{
			case StaticResources.MENU_KIT:
				/*
				 * Bitmapsplash=getBitmapFromURL(
				 * "http://www.jwcosmetics.com/vStudio/models/Angie.jpg");
				 * i.setImageBitmap(splash); i.invalidate();
				 */

				Intent kitIntent = new Intent(this, KitActivity.class);
				startActivity(kitIntent);

				/*
				 * Intent mkitIntent = new Intent(this, MakeupKit.class);
				 * startActivity(mkitIntent);
				 */
				return true;

			case StaticResources.MENU_MODELS:

				Intent modelIntent = new Intent(this, ModelActivity.class);
				startActivity(modelIntent);

				return true;
			case StaticResources.MENU_SHARE:
				// to do: check if still rendering
				MainActivity.showSharing.sendEmptyMessage(0);
				MainActivity.mQueue.addTask(new ShareThread(MainActivity.mContext));
				//MainActivity.hideProgress.sendEmptyMessage(0);
				return true;
			case StaticResources.MENU_ABOUT:

				Intent cIntent = new Intent(this, ContentActivity.class);
				startActivity(cIntent);

				return true;
			}
		} catch (Exception e) {

			ANR.alert(this, e.getMessage());
		}
		return false;
	}

	public static void loadmodelrules(ModelEntry m) {
		parseModelRules(ANR.getFile(MainActivity.mContext.getResources(),m.mKitURL));

	}

	public static void parseModelRules(String XML) {
		try {
			ArrayList<KitEntry> newKit = new ArrayList<KitEntry>();

			StringReader sR = new StringReader(XML);

			// DocumentBuilder db= new DocumentBuilder();
			SimpleDOMParser dP = new SimpleDOMParser();

			SimpleElement cDoc = null;

			cDoc = dP.parse(sR);

			Object[] e = cDoc.getChildElements();// .getDocumentElement();//.getFirstChild();

			for (int j = 0; j < e.length; j++) {
				SimpleElement n = (SimpleElement) e[j];

				if (n.getTagName().equals("modelimage")) {

					model = ANR.getBitmap(MainActivity.mContext, n
							.getAttribute("src"));
					tempHolder = model;
					//i.setImageBitmap(model);
					//i.invalidate();

				}

				if (n.getTagName().equals("layer")) {// Load the layer

					KitEntry k = new KitEntry(n.getAttribute("name"),
							new Integer(n.getAttribute("order")).intValue());

					k.mMaskURL = n.getAttribute("mask");

					k.cShade = Color.rgb(255, 255, 255);

					Object[] palNodes = n.getChildElements();
					for (int pal = 0; pal < palNodes.length; pal++) {
						Palette oPal = null;

						SimpleElement palette = (SimpleElement) palNodes[pal];
						if (palette.getTagName().equals("palette")) {
							oPal = k.addPalette(palette.getAttribute("name"),
									palette.getAttribute("details"), palette
											.getAttribute("productURL"));
					//		oPal.addPigment(k,"Remove "+ palette.getAttribute("Name"),255,255,255);
							// Now get the palette Entries
							Object[] palEntryNodes = palette.getChildElements();
							for (int pe = 0; pe < palEntryNodes.length; pe++) {
								SimpleElement pEntry = (SimpleElement) palEntryNodes[pe];

								if (pEntry.getTagName().equals("paletteEntry")) {

									int red = new Integer(pEntry
											.getAttribute("red")).intValue();
									int green = new Integer(pEntry
											.getAttribute("green")).intValue();
									int blue = new Integer(pEntry
											.getAttribute("blue")).intValue();

									oPal.addPigment(k, pEntry
											.getAttribute("name"), red, green,
											blue);

								}
							}

						}
						if (palette.getTagName().equals("externalPalette")) {
							Palette sPal = null;
							String exPallet = ANR.getFile(MainActivity.mContext.getResources(), palette
									.getAttribute("src"));
							StringReader sR2 = new StringReader(exPallet);

							// DocumentBuilder db= new DocumentBuilder();
							SimpleDOMParser dP2 = new SimpleDOMParser();

							SimpleElement cDoc2 = null;

							cDoc2 = dP2.parse(sR2);

							Object[] e2 = cDoc2.getChildElements();// .getDocumentElement();//.getFirstChild();

							for (int j2 = 0; j2 < e2.length; j2++) {

								SimpleElement sPalette = (SimpleElement) e2[j2];
								if (sPalette.getTagName().equals("palette")) {
									sPal = k.addPalette(sPalette
											.getAttribute("name"), sPalette
											.getAttribute("details"), sPalette
											.getAttribute("productURL"));
									// sPal.mParentLayer=k;

									// Now get the palette Entries
									Object[] palEntryNodes = sPalette
											.getChildElements();
									for (int pe = 0; pe < palEntryNodes.length; pe++) {
										SimpleElement pEntry = (SimpleElement) palEntryNodes[pe];

										if (pEntry.getTagName().equals(
												"paletteEntry")) {

											int red = new Integer(pEntry
													.getAttribute("red"))
													.intValue();
											int green = new Integer(pEntry
													.getAttribute("green"))
													.intValue();
											int blue = new Integer(pEntry
													.getAttribute("blue"))
													.intValue();

											sPal.addPigment(k, pEntry
													.getAttribute("name"), red,
													green, blue);

										}
									}
								}
							}
						}

					}

					k.id=newKit.size();
					newKit.add(k.id,k);
					

				}

			}
			MainActivity.cKit = newKit;
			MainActivity.loadKit();

		} catch (Exception e) {
			ANR.alert(MainActivity.mContext, e.getMessage());
			return;
		}
	}

	public static void loadKit() {
		ArrayList<MenuState> newTree = new ArrayList<MenuState>();

		for (KitEntry k : MainActivity.cKit) {
			MenuState m = new MenuState(k, true,
					MenuState.TYPE_TOGGLE_CHILDREN, -1);
			int newKitID = safeAdd(newTree, m);

			for (Palette p : k.mPalettes) {
				m = new MenuState(p, false, MenuState.TYPE_TOGGLE_CHILDREN,
						newKitID);
				int newPaletteID = safeAdd(newTree, m);
				for (Pigment pig : p.mPigments) {
					m = new MenuState(pig, false, MenuState.TYPE_APPLY,
							newPaletteID);
					safeAdd(newTree, m);

				}

			}
		}

		MainActivity.rawTree = null;
		MainActivity.rawTree = newTree;
	}

	static int safeAdd(ArrayList iList, Object o) {
		int newID = iList.size();
		iList.add(newID, o);
		return newID;
	}

	public static void triggerRender() {

		if (mRT != null) {
			MainActivity.mRT.stop();
		}

		mRT = new RenderThread();
		mRT.start();

	}

	public static void renderImage() {
		// if (!MainActivity.isRendering) return;
//	mRT.setPriority(Thread.MAX_PRIORITY);
		// newImage = createImage(250,375);
		// Graphics g =newImage.getGraphics();
		// g.drawImage(i,0,0,this);

		// int[] rImagePixels=grabPixels(i);

		// Process the layers
		// Enumeration keys=renderLayers.getSortedKeys();

		int height = MainActivity.model.getHeight();
		int width = MainActivity.model.getWidth();
		// Bitmap newImage=MainActivity.model.copy(Bitmap.Config.RGB_565 ,
		// true);// Bitmap.createBitmap(width, height,Bitmap.Config.RGB_565 );
		int[] newImage = getBitArray(MainActivity.model);

		for (KitEntry ke : MainActivity.cKit) {
if (ke.id == MainActivity.touchTarget)
{	
			if (MainActivity.resetRendering) {
				MainActivity.resetRendering = false;
				//mRT.setPriority(Thread.NORM_PRIORITY);
				return;
			}

			int targetLayerColor = (int) ke.cShade;
			int white = Color.rgb(255, 255, 255);
			// if the color is white, skip this layer
			if (targetLayerColor != white) {
				// Bitmap mask=ke.getMask(MainActivity.mContext);
				int[] mask = getBitArray(ke.getMask(MainActivity.mContext));
				// newImage=makeAnAlphaCodedImage(targetLayerColor,mask,newImage);
				int maskColor;
				int tempColor;
				int newRed;
				int newBlue;
				int newGreen;
				int fColor = 0;
				int sourceColor;
				float newHue;
				float newSat;
				float trans;

				for (int x = 0; x < height; x++) {
					for (int y = 0; y < width; y++)

					{

						maskColor = Color.red(mask[(x * width) + y]);// alphaMask.getPixel(x,
																		// y);
						sourceColor = newImage[(x * width) + y];

						switch (maskColor) {
						case 0:
							fColor = sourceColor;
							break;

						default:

							trans = (float) maskColor / (float) 255;
							newRed = (int) (Color.red(targetLayerColor) * trans + (1 - trans)
									* Color.red(sourceColor));
							newGreen = (int) (Color.green(targetLayerColor)
									* trans + (1 - trans)
									* Color.green(sourceColor));
							newBlue = (int) (Color.blue(targetLayerColor)
									* trans + (1 - trans)
									* Color.blue(sourceColor));

							// tempColor=Color.rgb(newRed, newGreen, newBlue);
							tempColor = Color.rgb(newRed, newGreen, newBlue);

							Color.colorToHSV(sourceColor, sHsv);
							Color.colorToHSV(tempColor, tHsv);
							tHsv[2] = sHsv[2];

							/*
							 * newHue=tHsv[0] * trans + (1-trans) * sHsv[0];
							 * newSat=tHsv[1] * trans + (1-trans) * sHsv[1];
							 * tHsv[0]=newHue; tHsv[1]=newSat;
							 * 
							 * tHsv[2]=sHsv[2];
							 */
							fColor = Color.HSVToColor(tHsv);// maskColor;//targetLayerColor;

						}
						newImage[(x * width) + y] = fColor;
						if (MainActivity.resetRendering) {
							MainActivity.resetRendering = false;
							//mRT.setPriority(Thread.NORM_PRIORITY);
							return;
						}

					}

				}

			}

		}

		MainActivity.tempHolder = (Bitmap.createBitmap(newImage, width, height,
				Bitmap.Config.RGB_565));
		MainActivity.updateImage.sendEmptyMessage(0);
}
		MainActivity.isRendering = false;

	}
 
	public static void updateImage() {
		MainActivity.i.setImageBitmap(tempHolder);
		MainActivity.i.invalidate();
	}

	public static int[] getBitArray(Bitmap iMap) {
		int[] pix = new int[iMap.getWidth() * iMap.getHeight()];
		iMap.getPixels(pix, 0, iMap.getWidth(), 0, 0, iMap.getWidth(), iMap
				.getHeight());
		return pix;
	}

	public static void NotifyReload() {
		MainActivity.i.setImageBitmap(model);
		MainActivity.i.invalidate();
	}

	

}