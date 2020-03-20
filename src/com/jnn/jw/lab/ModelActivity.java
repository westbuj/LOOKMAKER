package com.jnn.jw.lab;

/**
 * 
 */



import com.jnn.jw.mid.CachedViewed;
import com.jnn.jw.mid.ModelEntry;
import com.jnn.jw.mid.ModelAdapter;
import com.jnn.jw.mid.ModelDirectory;
import com.jnn.util.ANR;
import com.jnn.util.SimpleDOMParser;
import com.jnn.util.SimpleElement;
import com.jnn.util.TaskQueue;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Dad
 * 
 */
public class ModelActivity extends ListActivity {
	
	
	public static ModelAdapter mModelAdapter = null;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		 
		
		
		if (!MainActivity.modelsLoaded) {
			DownloadDirectory.loadModelDirectory("INT:int_dir");
			
		}

		if (ModelActivity.mModelAdapter == null)
			ModelActivity.mModelAdapter = new ModelAdapter(this);
		// Display it
		setListAdapter(ModelActivity.mModelAdapter);

		//setContentView(l);
		

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		CachedViewed thisModelTest=(CachedViewed)MainActivity.modelBrowse.get(position);	
		
		
		if (thisModelTest.getClass().getName().contains("ModelDirectory"))
		{
			ModelDirectory thisMD=(ModelDirectory)thisModelTest;
			if (!thisMD.isLoaded && !thisMD.isLoading)
			{DownloadDirectory d = new DownloadDirectory(thisMD);
			 d.md.isLoading=true;
			 thisModelTest.spoilView();
			 MainActivity.updateModels.sendEmptyMessage(0);
			 MainActivity.mQueue.addTask(d);
			}
			
			
		}
		if (thisModelTest.getClass().getName().contains("ModelEntry"))
		{
		
		
		ModelEntry me =(ModelEntry) ModelActivity.mModelAdapter.getItem(position);
		MainActivity.mQueue.addTask(new LoadModelThread(me));
		
		this.finish();	
		
		
		}

	}
	
	
}
