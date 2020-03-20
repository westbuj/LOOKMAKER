package com.jnn.jw.lab;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.jnn.jw.mid.KitEntry;
import com.jnn.jw.mid.MenuState;
import com.jnn.jw.mid.ModelDirectory;

public class StateObjectWrapper {


	public  ArrayList<MenuState> rawTree = new ArrayList<MenuState>();
	

	public Bitmap model;
	public Bitmap tempHolder;	
	public ArrayList<KitEntry> cKit;
	public ArrayList<Object> modelBrowse;
	public ArrayList<ModelDirectory> modelDirectory;
	public boolean modelsLoaded;
	public boolean LOAD_DEFAULT;


}
