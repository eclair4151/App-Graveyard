package com.shemeshapps.drexeloncampuschallenge.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.shemeshapps.drexeloncampuschallenge.Networking.RequestUtil;
import com.shemeshapps.drexeloncampuschallenge.R;

public class CampusWebview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_webview);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie("http://www.oncampuschallenge.org", "PHPSESSID=" + RequestUtil.getInstance(this).sessionId + ";");
        CookieSyncManager.getInstance().sync();

        WebView w = (WebView)findViewById(R.id.campus_webview);
        WebSettings webSettings = w.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        w.setWebViewClient(new WebViewClient());
        final ProgressBar p = (ProgressBar)findViewById(R.id.progress_bar);
        w.setWebChromeClient(new WebChromeClient() {
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
        w.loadUrl("http://www.oncampuschallenge.org/users/account");
    }
}
