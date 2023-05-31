package com.example.musicx;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

public class WebViewYoutube extends AppCompatActivity {
private WebView webView;
private ProgressBar progressBar;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_youtube);
        if ( savedInstanceState != null) ( (WebView)findViewById(R.id.webView)).restoreState(savedInstanceState);
        webView= findViewById(R.id.webView);
        progressBar = findViewById(R. id. progress);
        Toolbar toolbar = findViewById(R.id.toolbarWebView);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setPadding(0,0,0,0);
        Objects.requireNonNull(getSupportActionBar()).setTitle(" Youtube ");

        webView.setWebViewClient( new WebViewClient());

        webView. setWebChromeClient( new WebChromeClient(){
            public void onProgressChanged( WebView webView, int progress ){
                progressBar.setProgress(progress);
                if(progress ==100 ){
                    progressBar. setVisibility(View.GONE);
                }else{
                    progressBar. setVisibility(View.VISIBLE);
                }
            }
        });
  String search = getIntent().getStringExtra("url");
        webView.loadUrl("http://www.youtube.com/results?search_query="+search );
        WebSettings configuracionWeb = webView.getSettings();
        configuracionWeb. setJavaScriptEnabled(true);

        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition , String mimetype, long contentLength) {

                String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);

                try {
                    String address = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                            + Environment.DIRECTORY_DOWNLOADS + "/" +
                            fileName;
                    File file = new File(address);
                    boolean a = file.createNewFile();

                    URL link = new URL(url);

                    downloadFile(link, address);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext (), "Error ", Toast.LENGTH_LONG ). show ();
                }
            }
        });



    }




    @Override
    public boolean  onKeyDown(int keycode, KeyEvent event){
        if((keycode == KeyEvent.KEYCODE_BACK) &&  webView.canGoBack()){
            webView.goBack();
            return false ;
        }
        return super.onKeyDown(keycode,event);
    }





    public void downloadFile(URL url, String outputFileName)  {

        try (InputStream in = url.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }catch (Exception e ){
            Toast.makeText(getApplicationContext (), "Error 2", Toast.LENGTH_LONG ). show ();
        }

    }

}