package com.shemeshapps.drexelregistrationassistant.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shemeshapps.drexelregistrationassistant.Helpers.PreferenceHelper;
import com.shemeshapps.drexelregistrationassistant.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WebView wv = (WebView)findViewById(R.id.loginWebview);
        wv.clearFormData();
        wv.clearCache(true);
        wv.clearHistory();
        PreferenceHelper.clearCookies(this);

        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setDomStorageEnabled(true);
        wv.loadUrl("https://one.drexel.edu/web/university/academics");
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if(url.equals("https://one.drexel.edu/web/university/academics"))
                {
                    String cookies = CookieManager.getInstance().getCookie("https://login.drexel.edu/cas/login");
                    if(cookies!=null && cookies.contains("CASTGC="))
                    {
                        view.stopLoading();
                        PreferenceHelper.setDrexelCookie(LoginActivity.this,cookies);
                        setResult(RESULT_OK);
                        finish();
                    }
                }
                super.onPageStarted(view, url, favicon);
            }

        });
    }
}