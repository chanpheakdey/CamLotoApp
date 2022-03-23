package com.example.camloto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class home_activity extends AppCompatActivity {
    private WebView mWebView;
    private String android_id;
    private boolean gInternetAvailable = false;

    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        Intent intent = getIntent();
        String qrcode = intent.getStringExtra("qrcode");

        try{
            checkInternet();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        LoadWebview(qrcode);

    }

    public  void LoadWebview(String qrcode){
        android_id = Settings.Secure.getString(this.getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        mWebView = (WebView) findViewById(R.id.webview1);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String url = "";
        if (qrcode !="" && qrcode!=null){
            url = "https://camloto.azurewebsites.net/scanresult?qrcode=" + qrcode + "&token=" + android_id;
        }else{
            url = "https://camloto.azurewebsites.net/login?token=" + android_id;
        }
        Toast.makeText(getApplicationContext(), android_id, Toast.LENGTH_LONG).show();

        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient(){

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                //pd.show();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                //pd.dismiss();
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(!gInternetAvailable){
                    Toast.makeText(home_activity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
                    return true;
                }else{
                    String url = request.getUrl().toString();
                    if (url.contains("scanner=1")) {

                        Intent myIntent = new Intent(home_activity.this, scan_activity.class);
                        //myIntent.putExtra("key", value); //Optional parameters
                        home_activity.this.startActivity(myIntent);

                        return true;
                    }
//                        url += "&AppVersion=" + APPVersion;
//                        url += "&PlayerID=" + playerId;

                    view.loadUrl(url);
                    return false;
                }
            }
        });
    }

    public void checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        cm.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                gInternetAvailable = true;

            }

            @Override
            public void onUnavailable() {
                gInternetAvailable = false;
            }

            @Override
            public void onLost(@NonNull Network network) {
                gInternetAvailable = false;
            }
        });
    }
}
