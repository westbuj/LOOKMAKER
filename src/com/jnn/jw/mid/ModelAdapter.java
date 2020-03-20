package com.jnn.jw.mid;

import java.util.ArrayList;
import com.jnn.jw.lab.MainActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ModelAdapter extends BaseAdapter {

	/** Remember our context so we can use it when constructing views. */
	private Context mContext;

	public ModelAdapter(Context context) {
		mContext = context;
	}

	public void addItem(ModelEntry it) {
		MainActivity.modelBrowse.add(it);
	}

	public void setListItems(ArrayList<Object> lit) {
		MainActivity.modelBrowse = lit;
	}

	/** @return The number of items in the */
	public int getCount() {
		return MainActivity.modelBrowse.size();
	}

	public Object getItem(int position) {
		return MainActivity.modelBrowse.get(position);
	}

	public boolean areAllItemsSelectable() {
		return true;
	}

	public boolean isSelectable(int position) {
		return true;
	}

	/** Use the array index as a unique id. */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * @param convertView
	 *            The old view to overwrite, if one is passed
	 * @returns a IconifiedTextView that holds wraps around an IconifiedText
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CachedViewed thisModelTest=(CachedViewed)MainActivity.modelBrowse.get(position);
		if (thisModelTest.getView() != null)
			return thisModelTest.getView();
		
		LinearLayout l = new LinearLayout(this.mContext);
		l.setOrientation(LinearLayout.VERTICAL);
		if (thisModelTest.getClass().getName().contains("ModelDirectory"))
		{
			ModelDirectory thisDir = (ModelDirectory)thisModelTest;
			
				
			TextView nameField=new TextView(this.mContext);
			TextView statusPanel=new TextView(this.mContext);
			statusPanel.setGravity(Gravity.CENTER_HORIZONTAL);
			statusPanel.setTextSize(24);
			if (thisDir.isLoading){
				
				
				statusPanel.setText("...Downloading...");			
				nameField.setText(thisDir.details);
				
				
				
			}
			else
			{	statusPanel.setText("Download");	
				nameField.setText(thisDir.details);
			}
			
			if (thisDir.isLoaded){
				statusPanel.setTextSize(6);
				statusPanel.setText("");
				nameField.setText(thisDir.details);
			}
			l.addView(statusPanel);
			l.addView(nameField);
		}
		if (thisModelTest.getClass().getName().contains("ModelEntry"))
		{
			
		ModelEntry thisModel = (ModelEntry)thisModelTest;
		
		l.setOrientation(LinearLayout.HORIZONTAL);
		
		TextView nameField=new TextView(this.mContext);
		nameField.setTextSize(18);
		nameField.setText(thisModel.mName);
		
		
		l.addView(nameField);
		// Instantiate an ImageView and define its properties
		ImageView i = new ImageView(this.mContext);
		
		i.setImageBitmap(thisModel.getThumbImage(this.mContext));
		i.setAdjustViewBounds(true);
		i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		l.addView(i);
		
		
		TextView detailsField=new TextView(this.mContext);
		detailsField.setText(thisModel.mDetails);
		
		l.addView(detailsField);
		}
		
		thisModelTest.setView(l);
		return l;
	}
}