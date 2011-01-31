/** Developed by Jeremy Meiss, JerdogDev - jerdog76@gmail.com		*/
/** Application Name: CRCC Android App								*/
/** Purpose: To provide an application for CRCC and to allow		*/
/**		for viewing the church's website and linking directly		*/
/**		to information, sermons, etc.								*/
/**																	*/
/**	Changelog:														*/
/**	2011-01-25	jmm:	initial application (v0.1)					*/
/** 2011-01-25	jmm:	added multitouch and zoom support (v0.11)	*/


package com.apps.jerdog.crcc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressWarnings("unused")
public class CopyOfmainActivity extends Activity {
    /** Called when the activity is first created. */
    /**@Override */
    WebView webview;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("http://www.cedarridge.cc");
        }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public class myWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) 
        {
            if (url.startsWith("mailto:") || url.startsWith("tel:")) { 
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url)); 
                    startActivity(intent); 
                    } 
            view.loadUrl(url);
            return true;
        }
    }}