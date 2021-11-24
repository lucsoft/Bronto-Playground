package dev.remod.remodlauncher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class BrontoActivity extends Activity {

    class CustomWV extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String hostname = "google.com";

            Uri uri = Uri.parse(url);
            if (url.startsWith("file:") || uri.getHost() != null && uri.getHost().endsWith(hostname)) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
    }

    private WebView wv;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        wv = findViewById(R.id.activity_main_webview);
        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        wv.setWebViewClient(new CustomWV());

        class Brontodroid {
            Context ctx;

            // Bronto's Interface starts here - LePichu
            Brontodroid(Context c) {
                ctx = c;
            }

            @JavascriptInterface
            public void showToast(String toast, int length) {
                int lengthVal = length == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
                Toast.makeText(ctx, toast, lengthVal).show();
            }

            @JavascriptInterface
            public void changeStatusBarColor(int r, int g, int b) {
                Window window = getWindow();
                window.setStatusBarColor(Color.rgb(r, g, b));
            }

            @JavascriptInterface
            public boolean hideStatusBar(boolean value) {
                if (value) {

                    this.ctx.getApplicationContext().startActivity(getIntent());
                    return true;
                } else return false;
            }
        }

        // LOCAL RESOURCE
        wv.loadUrl("file:///android_asset/index.html");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(new Brontodroid(this), "Brontodroid");
        wv.addJavascriptInterface(new Deno.fs(this), "Deno");
    }



    @Override
    public void onBackPressed() {
        if (wv.canGoBack()) {
            wv.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
