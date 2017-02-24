package io.nearby.android.ui.help;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import io.nearby.android.R;

public class HelpActivity extends AppCompatActivity{

    private static final String HELP_URL = "https://nearbyapp.github.io/#support";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);

        WebView webView = (WebView)findViewById(R.id.webview);
        webView.loadUrl(HELP_URL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
