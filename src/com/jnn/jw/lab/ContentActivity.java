package com.jnn.jw.lab;

import com.jnn.jw.mid.TreeMenuAdapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ContentActivity extends Activity {
	String currentURL=StaticResources.CONTENT_ROOT;
	WebView wView = null; 
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		if (wView == null)
		{
			wView=new WebView(this);
		}
		
		//wView.getSettings().setJavaScriptEnabled(true);
		wView.loadUrl(currentURL);
		//wView.invalidate();
		
		wView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		// Add the ImageView to the layout and set the layout as the content
		// view
		//wView.setVisibility(View.VISIBLE);
		wView.setWebViewClient(new myWebViewClient());
		
		setContentView(wView); 

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{    if ((keyCode == KeyEvent.KEYCODE_BACK) && wView.canGoBack())
	{        wView.goBack(); 
	return true;    } 
	return super.onKeyDown(keyCode, event);
	
	
	}
	
	
	private class myWebViewClient extends WebViewClient {    
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{        view.loadUrl(url);  
					return true;   
			}
			}

}
