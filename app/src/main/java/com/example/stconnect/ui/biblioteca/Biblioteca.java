package com.example.stconnect.ui.biblioteca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stconnect.R;

public class Biblioteca extends Fragment {

    public Biblioteca() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_biblioteca, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView webView = view.findViewById(R.id.webViewCertificados);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://descubridor.santotomas.cl:1701/primo_library/libweb/action/search.do?fctN=facet_domain&dscnt=0&scp.scps=scope:(cst_aleph),scope:(cst_digitool)&fctV=08101&vl(87563938UI0)=sub&tab=cst_tab&dstmp=1745945373213&ct=facet&mode=Advanced&indx=1&fromLogin=true&vl(1UIStartWith0)=exact&vl(freeText0)=%20Libros%20en%20l%C3%ADnea&vl(107694436UI1)=all_items&fn=search&vid=CST&vid=CST&dstmp=1762407657067&vid=CST&prefLang=en_US&fn=change_lang&fromLogin=true&fromLogin=true&backFromPreferences=true");
    }
}
