package com.shemeshapps.drexeloncampuschallenge.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.shemeshapps.drexeloncampuschallenge.Helpers.SessionHelper;
import com.shemeshapps.drexeloncampuschallenge.R;

public class LoginWebviewActivity extends Activity {

    private Context mContext;
    private WebView mWebview;
    private WebView mWebviewPop;
    private ProgressBar p;
    private FrameLayout mContainer;
    boolean facebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_webview);

        mWebview = (WebView)findViewById(R.id.campuswebview);
        p = (ProgressBar)findViewById(R.id.progress_bar);
        mContainer = (FrameLayout) findViewById(R.id.webview_frame);
        mContext = this;
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        CookieManager.getInstance().removeAllCookie();

        if(getIntent().getExtras()!=null)
            facebookLogin = getIntent().getExtras().getBoolean("facebook_login",false);


        if(facebookLogin)
        {
            mWebview.setVisibility(View.GONE);
        }

        mWebview.addJavascriptInterface(LoginWebviewActivity.this, "android");
        mWebview.setWebViewClient(new UriWebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (url.equals("http://www.oncampuschallenge.org/")) {
                    view.loadUrl("javascript:android.testLogin($(\".header-user-link\").get(0).text.toLowerCase() == \"my account\")");
                }
                else if(url.equals("http://www.oncampuschallenge.org/users/facebook-complete/"))
                {
                    facebookLogin = false;
                    mWebview.setVisibility(View.VISIBLE);
                }
                else if(url.equals("http://www.oncampuschallenge.org/users/complete/"))
                {
                    testLogin("true");
                }
                else if(facebookLogin) //auto facebook login
                {
                    view.loadUrl("javascript: $(\".fb-login\").click() ");
                }
            }

        });
        mWebview.setWebChromeClient(new UriChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                if (progress < 100 && p.getVisibility() == ProgressBar.GONE) {
                    p.setVisibility(ProgressBar.VISIBLE);
                }
                p.setProgress(progress);
                if (progress == 100 && !facebookLogin) {
                    p.setVisibility(ProgressBar.GONE);
                }

            }
        });


        mWebview.loadUrl("http://www.oncampuschallenge.org/users/signup");
    }

    @JavascriptInterface
    public void testLogin(String value) {
        if(value.equals("true")) //logged in
        {

            Intent i = new Intent();
            i.putExtra("sessionId", SessionHelper.extractSessionId());
            setResult(RESULT_OK,i);
            finish();
        }
    }





    private class UriWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String host = Uri.parse(url).getHost();
            //Log.d("shouldOverrideUrlLoading", url);
            if (host.equals("www.oncampuschallenge.org"))
            {
                // This is my web site, so do not override; let my WebView load
                // the page
                if(mWebviewPop!=null)
                {
                    mWebviewPop.setVisibility(View.GONE);
                    mContainer.removeView(mWebviewPop);
                    mWebviewPop=null;
                }
                return false;
            }

            if(host.equals("m.facebook.com") || host.equals("www.facebook.com"))
            {
                return false;
            }

            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            Log.d("onReceivedSslError", "onReceivedSslError");
        }
    }

    class UriChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(mContext);
            mWebviewPop.setVerticalScrollBarEnabled(false);
            mWebviewPop.setHorizontalScrollBarEnabled(false);
            mWebviewPop.setWebViewClient(new UriWebViewClient());
            mWebviewPop.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int progress) {

                    if (progress < 100 && p.getVisibility() == ProgressBar.GONE) {
                        p.setVisibility(ProgressBar.VISIBLE);
                    }
                    p.setProgress(progress);
                    if (progress == 100) {
                        p.setVisibility(ProgressBar.GONE);
                    }

                }
            });
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mContainer.addView(mWebviewPop);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();
            p.bringToFront();
            return true;
        }


        @Override
        public void onCloseWindow(WebView window) {
            Log.d("onCloseWindow", "called");
        }

    }
}
