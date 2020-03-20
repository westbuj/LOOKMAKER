package com.jnn.jw.mid;

import android.view.View;

public class ModelDirectory  implements CachedViewed{
	public String path=null;
	public String details=null;
	public boolean isLoaded=false;
	public boolean isLoading=false;
	public View mView;
	
	
	public ModelDirectory(String iPath, String iDetails)
	{
		path=iPath;
		details=iDetails;
	}


	public View getView() {
		// TODO Auto-generated method stub
		return mView;
	}


	public void spoilView() {
		// TODO Auto-generated method stub
		mView=null;		
	}


	public void setView(View v) {
		// TODO Auto-generated method stub
		mView=v;		
	}

}
