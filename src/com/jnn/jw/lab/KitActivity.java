package com.jnn.jw.lab;

import android.app.ListActivity;
import com.jnn.util.TaskQueue;

/**
 * 
 */




import com.jnn.jw.mid.MenuState;

import com.jnn.jw.mid.Palette;
import com.jnn.jw.mid.Pigment;
import com.jnn.jw.mid.TreeMenuAdapter;
import com.jnn.jw.mid.ViewHolder;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


/**
 * @author Dad
 * 
 */
public class KitActivity extends ListActivity {
	/** Called when the activity is first created. */
	
	//public static Thread thread;
	
	public static TreeMenuAdapter menuState = null;
	@Override	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//this.setTheme(resid);
		//this.setVisible(false);
		
		//setTheme(R.style.Theme_Transparent);
		this.getListView().setCacheColorHint(Color.TRANSPARENT);
		

		 TreeMenuAdapter itla = new TreeMenuAdapter(this.getBaseContext());
		 
		 itla.sync();
				 
		 setListAdapter(itla);
		 menuState=itla;

		//setContentView(l); 

	}
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, StaticResources.MENU_EXPAND_KIT, 0, "Expand");
		menu.add(0, StaticResources.MENU_COLLAPSE_KIT, 0, "Collapse");
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)

	{

			switch (item.getItemId())
			{
			case StaticResources.MENU_EXPAND_KIT:
				for (MenuState ms : MainActivity.rawTree)
				{
					ms.visible=true;
					
					
				}
				
				break;
			case StaticResources.MENU_COLLAPSE_KIT:
				for (MenuState ms : MainActivity.rawTree)
				{
					if (ms.mParentID == -1)
					{
						menuState.toggleChildren(MainActivity.rawTree.indexOf(ms),0);
						
					}
				}
				break;				

			}
	menuState.sync();
	return true;
	}

	
	
	protected void onListItemClick(ListView l, View v, int position,
	long id) {
	super.onListItemClick(l, v, position, id);
	
	TreeMenuAdapter ta = (TreeMenuAdapter)this.getListAdapter();
	MenuState me=(MenuState)ta.getItem((int)id);
	
	if(me.mType == MenuState.TYPE_TOGGLE_CHILDREN)
	{
		ta.toggleChildren(((Integer)TreeMenuAdapter.state.get((int)id)).intValue(),StaticResources.AUTO_GEN_CHILD_STATE);
		ta.sync();
	}
	
	if(me.mType == MenuState.TYPE_APPLY)
	{
		//changeTheLayers targetShade and redraw
		
		Pigment pig=(Pigment)me.mObject;
		pig.mParentLayer.cShade=pig.mColor;
		
		ViewHolder holder=(ViewHolder)pig.mParentLayer.mView.getTag();
		holder.currentPigmentIcon.setImageBitmap(pig.chip);
		holder.currentPigmentName.setText(pig.mName);
		
		MenuState parentM = (MenuState)ta.getItemNative((int)me.mParentID);
		Palette parentPal = (Palette)parentM.mObject;
		
		holder.currentPigmentName.setText(pig.mName +" - "+parentPal.mName);
		
		
		pig.mParentLayer.mView.invalidate();
		MainActivity.touchTarget=pig.mParentLayer.id;
		Bitmap layMask=pig.mParentLayer.getMask(this);
		MainActivity.currentMask=Bitmap.createBitmap(layMask.getWidth(),layMask.getHeight(),Bitmap.Config.RGB_565);
		Canvas c=new Canvas(MainActivity.currentMask);
		c.drawBitmap(layMask,0,0, null);
		
		
		
		
		//MainActivity.triggerRender();
		
		MainActivity.mQueue.addTask(new RenderThread());
		
		

	}
	
	
	}
		

}