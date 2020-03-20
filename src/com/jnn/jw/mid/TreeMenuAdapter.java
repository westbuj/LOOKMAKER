package com.jnn.jw.mid;

import java.util.ArrayList;

import com.jnn.jw.lab.MainActivity;
import com.jnn.jw.cam.R;
import com.jnn.jw.lab.StaticResources;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TreeMenuAdapter extends BaseAdapter {
	public static ArrayList<Integer> state = new ArrayList<Integer>();
	public static Context mContext;
//	public ArrayList<MenuState> rawTree = null;//new ArrayList<MenuState>();
	
	public void toggleChildren(long position, int targetState)
	{
		long id= position;//((Integer)state.get((int)position)).intValue();
		
		
		for (MenuState ms : MainActivity.rawTree)
		{
			if (ms.mParentID == id)
			{
				if (targetState == StaticResources.AUTO_GEN_CHILD_STATE)
					if (ms.visible)
						targetState= 0; // (THE BOOLEAN NOT) - for !ms.visible
					else
						targetState= 1; // (THE BOOLEAN NOT)
				
				ms.visible = (boolean)(targetState == 1);
				toggleChildren(MainActivity.rawTree.indexOf(ms),targetState);
			}
		}
		
	}
	public void sync()
	{
		ArrayList newState=new ArrayList();
		for (MenuState ms : MainActivity.rawTree)
		{
			if (ms.visible)
				newState.add(new Integer(MainActivity.rawTree.indexOf(ms)));
		}
		state.clear();
		state= newState;
		
		this.notifyDataSetChanged();
	}
	
	public TreeMenuAdapter(Context c)
	{
		TreeMenuAdapter.mContext=c;
		//sync();
  
	}
	public void setRawList(ArrayList<MenuState> iRawTree)
	{
		MainActivity.rawTree=iRawTree;
	}
	int safeAdd(ArrayList iList,Object o)
	{
		int newID = iList.size();
		iList.add(newID,o);
		return newID;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return state.size();
	}

	
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return MainActivity.rawTree.get(((Integer)state.get(arg0)).intValue());
		
		
	}
	public Object getItemNative(int arg0) {
		// TODO Auto-generated method stub
		return MainActivity.rawTree.get(arg0);
		
		
	}
	
	public long getItemId(int position) {
		//int itemId=((Integer)state.get(position)).intValue();
		
		return position;
		
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//LinearLayout l = new LinearLayout(TreeMenuAdapter.mContext);
		MenuState me=MainActivity.rawTree.get(state.get(position));		
		String cName=me.mObject.getClass().getName();
		if (cName.indexOf("KitEntry") > 0)
		{
			KitEntry kit = (KitEntry)me.mObject;	
			if (kit.mView == null)
			{
				LinearLayout l = new LinearLayout(TreeMenuAdapter.mContext);
				l.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout l2 = new LinearLayout(TreeMenuAdapter.mContext);
				l2.setOrientation(LinearLayout.VERTICAL);
			    ViewHolder holder = new ViewHolder();
			    holder.currentPigmentName=new TextView(TreeMenuAdapter.mContext);
			    
			    holder.currentPigmentIcon = new ImageView(TreeMenuAdapter.mContext);
			    l.setTag(holder);
				
			    l.addView(holder.currentPigmentIcon);
			    
			    
				l.setBackgroundDrawable(MainActivity.transparent);
				
				TextView nameField=new TextView(TreeMenuAdapter.mContext);
				nameField.setText(kit.mName);
				nameField.setTextSize(18);				

				l2.addView(nameField);
				
				
				holder.currentPigmentName.setText("");
				holder.currentPigmentName.setTextSize(14);				
				
				
				
				l2.addView(holder.currentPigmentName);
				
				l.addView(l2);
				
				//chipView.setImageBitmap(pig.chip);
				
				
				kit.mView=l;
			}
			return kit.mView;
		}
		if (cName.indexOf("Palette") > 0)
		{
			Palette pal = (Palette)me.mObject;
			if (pal.mView == null)
			{
				LinearLayout l = new LinearLayout(TreeMenuAdapter.mContext);
				l.setBackgroundDrawable(MainActivity.transparent);
				l.setOrientation(LinearLayout.HORIZONTAL);
				TextView nameField=new TextView(mContext);
				nameField.setText(pal.mName);
				nameField.setTextSize(14);
				//nameField.setBackgroundDrawable(MainActivity.transparent);
				l.addView(nameField);
				pal.mView=l;
			}
			return pal.mView;
		}
		if (cName.indexOf("Pigment") > 0)
		{
			Pigment pig = (Pigment)me.mObject;
			if (pig.mView == null)
			{
				LinearLayout l = new LinearLayout(TreeMenuAdapter.mContext);
				l.setBackgroundDrawable(MainActivity.transparent);
				l.setOrientation(LinearLayout.HORIZONTAL);
				ImageView chipView=new ImageView(mContext);
				chipView.setImageBitmap(pig.chip);
				l.addView(chipView);
				TextView nameField=new TextView(mContext);
				nameField.setText(pig.mName);
				nameField.setTextSize(12);
				//nameField.setBackgroundDrawable(MainActivity.transparent);
				l.addView(nameField);
				pig.mView=l;
			}
			return pig.mView;
			
		}
		//l.setBackgroundColor(Color.TRANSPARENT);
		return null;
		
		
	}

}
