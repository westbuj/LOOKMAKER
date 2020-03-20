package com.jnn.jw.cam;

import java.io.File;
import java.io.FileFilter;

import com.jnn.jw.lab.ColorPaint;
import com.jnn.jw.lab.MainActivity;
import com.jnn.jw.paint.FingerPaint;
import com.jnn.util.ANR;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Launch extends Activity implements OnClickListener, OnItemSelectedListener{
    /** Called when the activity is first created. */
	public static String selectedProject=null; 
    @Override
    public void onCreate(Bundle savedInstanceState) {   
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ANR.setupStorage("jwMUCam");
        
        File appRoot = new File(ANR.appStoragePath);
        //FileFilter filter = new FileFilter();
        File[] files= appRoot.listFiles();  
    
        Spinner flist=(Spinner)findViewById(R.id.filelisting); 
        
        
        
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
         

        for (int j=0;j<files.length;j++) 
        {
        	if (files[j].isFile() && files[j].getName().endsWith(".lab"))
        		adapter.add(files[j].getName());
        }
        flist.setAdapter(adapter);
        
        flist.setOnItemSelectedListener(this); 

        Button button = (Button)findViewById(R.id.cambutton);
        button.setTag("CAMERA");
        button.setOnClickListener(this);
        
        Button dbutton = (Button)findViewById(R.id.design);
        dbutton.setTag("DESIGN");
        dbutton.setOnClickListener(this);
        
        dbutton = (Button)findViewById(R.id.launch_studio);
        dbutton.setTag("STUDIO");
        dbutton.setOnClickListener(this);
        
        dbutton = (Button)findViewById(R.id.launch_new_studio);
        dbutton.setTag("DSTUDIO");
        dbutton.setOnClickListener(this);
        
        
        
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getTag().equals("CAMERA"))
		{Intent myIntent = new Intent(this, PhotoStudio.class);
			this.startActivity(myIntent);
		}
		
		if (v.getTag().equals("DESIGN"))
		{Intent myIntent = new Intent(this, ShapeDesigner.class);
		 myIntent.putExtra("PROJECT",Launch.selectedProject);
		 
			this.startActivity(myIntent);
		}
		if (v.getTag().equals("STUDIO"))
		{Intent myIntent = new Intent(this, MainActivity.class);
		 myIntent.putExtra("PROJECT",Launch.selectedProject);
		 
			this.startActivity(myIntent);
		}
		
		if (v.getTag().equals("DSTUDIO"))
		{Intent myIntent = new Intent(this, FingerPaint.class);
		 myIntent.putExtra("PROJECT",Launch.selectedProject);
		 
			this.startActivity(myIntent);
		}


	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		Launch.selectedProject=arg0.getSelectedItem().toString();  

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}