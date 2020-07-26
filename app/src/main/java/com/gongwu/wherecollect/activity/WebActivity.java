package com.gongwu.wherecollect.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.view.Loading;

import butterknife.BindView;
import butterknife.OnClick;

public class WebActivity extends BaseActivity {
    private static final String TAG = "WebActivity";

    @BindView(R.id.web_View)
    WebView webView;
    @BindView(R.id.title_tv)
    TextView mTitleView;
    @BindView(R.id.back_btn)
    ImageButton mBackView;

    private Loading loading;
    private String url;


    @Override
    protected void initViews() {
        mBackView.setVisibility(View.VISIBLE);
        loading = Loading.show(loading, this, "");
        mTitleView.setText(getIntent().getStringExtra("title"));
        url = getIntent().getStringExtra("url");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                if (loading != null) {
                    loading.dismiss();
                }
            }
        });
        webView.loadUrl(url);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void initPresenter() {
    }

    private void back() {
        try {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            //加载null内容
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //清除历史记录
            webView.clearHistory();
            //移除WebView
            ((ViewGroup) webView.getParent()).removeView(webView);
            //销毁VebView
            webView.destroy();
            //WebView置为null
            webView = null;
        }
        super.onDestroy();
    }
}
