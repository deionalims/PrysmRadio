package com.prysmradio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.prysmradio.R;

/**
 * Created by fx.oxeda on 31/03/2014.
 */
public class PrysmContentFragment extends PrysmFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final WebView webView = new WebView(getActivity());

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(getString(R.string.prysm_url));

        return webView;
    }

}
