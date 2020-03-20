package com.jnn.jw.mid;

public class MenuState {
	public static final int TYPE_CANCEL = 0;
	public static final int TYPE_TOGGLE_CHILDREN = 1;
	public static final int TYPE_APPLY = 2;
	public static final int TYPE_NO_ACTION = 3;
	
	public Object mObject;
	public boolean visible;
	public int mType;
	public long mParentID;
	
	public MenuState(Object iObj, boolean iVis,int iType, long parentID)
	{
		mObject=iObj;
		visible=iVis;
		mType=iType;
		mParentID=parentID;		
	}
	

}
