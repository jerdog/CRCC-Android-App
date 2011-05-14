/** Developed by Jeremy Meiss, JerdogDev - jerdog76@gmail.com				*/
/** Application Name: CRCC Android App										*/
/** Purpose: To provide an application for CRCC and to allow				*/
/**		for viewing the church's website and linking directly				*/
/**		to information, sermons, etc.										*/
/**																			*/
/**	Changelog:																*/
/**	2011-01-25	jmm:	initial application (v0.1)							*/
/** 2011-01-25	jmm:	added multitouch and zoom support (v0.11)			*/
/** 2011-01-25	jmm:	properly formatted the webViewClient				*/
/** 2011-01-25	jmm:	added parsing of geo:, mailto:, tel: links	(v0.12)	*/
//	2011-01-26	jmm:	fixed the parsing issue with webview (v0.13)
//	2011-01-29	jmm:	added menu actions (v0.2)
//	2011-01-29	jmm:	added progress bar for loading functions (v0.2)
//	2011-01-29	jmm:	added functions for menu for About and Exit app (v0.2.1)
//	2011-01-30	jmm:	split out tel: from url handling (v0.2.2)
//	2011-01-31	jmm:	added link in menu to rss feed (v0.2.2)
//	2011-01-31	jmm:	enabled cache, geolocation, plugins, multiple windows, allow js to open windows (v0.2.2)
// 	2011-01-31	jmm:	added Messages menu which loads rss page (v0.2.2)


package com.apps.jerdog.crcc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

@SuppressWarnings("unused")
public class mainActivity extends Activity {
    /** Called when the activity is first created. */
    /**@Override */
    WebView webview;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);		// set progress feature for the webview
        setContentView(R.layout.main);
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);		// makes progress bar visible
        webview = (WebView) findViewById(R.id.webview);

        // begin the code for the progress bar
        final Activity MyActivity = this;
        webview.setWebChromeClient(new WebChromeClient() {					// sets the Chrome Client and defines the 
        	public void onProgressChanged(WebView view, int progress)		// onProgressChanged - making the progress bar update
        	{
        		MyActivity.setTitle(R.string.loading_);				// text to display during the loading
        		MyActivity.setProgress(progress * 100);			// make the bar display upon loading
        		
        		if(progress == 100)								// return the app name after finished loading
        			MyActivity.setTitle(R.string.app_name);
           	}
        });
        
        // set the settings for the webview
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setInitialScale(25);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setAppCacheMaxSize(128);
        webview.getSettings().setGeolocationEnabled(true);
        webview.getSettings().setPluginsEnabled(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.setWebViewClient(new myWebViewClient());
        webview.loadUrl(getString(R.string.http_www_cedarridge_cc));
        }
 
    // begin the code for the application menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
      super.onCreateOptionsMenu(menu);
     
      menu.add(0, 0, 0, R.string.app_about);
      menu.add(0,1,1,R.string.messages_feed);
//      menu.add("Facility Calendar");
      menu.add(0,4,4,R.string.str_exit);
      return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //System.out.println("Selected menu item: " +item.getTitle());
    	super.onOptionsItemSelected(item);
    	
    	switch(item.getItemId())
    	{
    	case 0:
    		openOptionsDialog();
    		break;
    	case 1:
//    		webview.loadUrl("http://www.churchmp3.com/public/churches/9/messages.xml");
//    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.churchmp3.com/public/churches/9/messages.xml")); 
    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.crcc_rss_feed))); 
    			startActivity(intent);
    		break;
    	case 4:
    		exitOptionsDialog();
    		break;
    	}
        return true;
    }
    
    private void openOptionsDialog()
    {
    	new AlertDialog.Builder(this)
	    	.setTitle(R.string.app_about)
	    	.setMessage(R.string.app_about_message)
	    	.setPositiveButton(R.string.str_ok,
    	new DialogInterface.OnClickListener()
    	{
    	public void onClick(DialogInterface dialoginterface, int i)
    	{
    	}
    	})
    	.show();
    }
    
    private void exitOptionsDialog()
    {
	    new AlertDialog.Builder(this)
		    .setTitle(R.string.app_exit)
		    .setMessage(R.string.app_exit_message)
		    .setNegativeButton(R.string.str_no,
		    new DialogInterface.OnClickListener()
	    {
	    public void onClick(DialogInterface dialoginterface, int i)
	    {}
	    })
	    .setPositiveButton(R.string.str_ok,
	    new DialogInterface.OnClickListener()
	    {
	    public void onClick(DialogInterface dialoginterface, int i)
	    {
	     finish();
    }
    })
    .show();
    }
    
    // begin the code to intercept the back key and prohibit it from exiting the application
    // instead allowing it to go back in the webview if possible, then exit if not
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    // begin the code for handling specific urls in the webview, telling the app to view them with their respective apps
    private class myWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) 
        {
            if (url.startsWith("mailto:")) {
            	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            		startActivity(intent);
            		return true;
            } else if (url.startsWith("geo:") || url.startsWith("feed:")) { 
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url)); 
                    startActivity(intent); 
                    return true;
            } else if (url.startsWith("tel:")) {
            	Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            		startActivity(intent);
            		return true;
            	}
            
            view.loadUrl(url);
            return true;
        }
        
//        public Intent newEmailIntent(Context context, String subject) {
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Message sent from CRCC Android App");
//            intent.setType("message/rfc822");
//            return intent;
//        }
    }}