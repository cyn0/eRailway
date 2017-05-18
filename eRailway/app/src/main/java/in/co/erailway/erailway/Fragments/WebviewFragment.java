package in.co.erailway.erailway.Fragments;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import in.co.erailway.erailway.Constants;
import in.co.erailway.erailway.R;
import utils.HttpHandler;

import static in.co.erailway.erailway.R.id.webview;

/**
 * Created by paln on 17/4/2017.
 */

public class WebviewFragment extends BaseFragment {

	private ProgressBar mProgressBar;
	private WebView mWebView;

	private static String mUrl;

	public static WebviewFragment newInstance(final String url) {
		WebviewFragment fragment = new WebviewFragment();

		mUrl = url;

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fragmentTitle = "eRailway";
		TAG = "WebviewFragment";
	}

	@Override
	public void onStop() {
		mWebView.stopLoading();
		super.onStop();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_webview, container, false);

		mWebView = (WebView) view.findViewById(webview);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setUserAgentString(webSettings.getUserAgentString() + " batman");
		webSettings.setJavaScriptEnabled(true);
		mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		} else {
			mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				Log.d(TAG, progress + "");
				mProgressBar.setProgress(progress);
				if (progress == 100) {
					mProgressBar.setVisibility(View.INVISIBLE);
				}
			}
		});


		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				if(url.contains("pnr")){
					PnrListFragment pnrListFragment = PnrListFragment.newInstance();
					mFragmentManager
							.beginTransaction()
							.replace(R.id.container, pnrListFragment)
							.commit();
					return  true;
				}
				mUrl = url;
				Log.d(TAG, "Loading " + url);

				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				Log.d(TAG, "started");
				mProgressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Log.d(TAG, "finished");
				mProgressBar.setVisibility(View.GONE);

				setFragmentTitle(view.getTitle());
			}
		});

//		mWebView.setOnScrollChangeListener(new OnScrollChangeListener() {
//			@Override
//			public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//
//			}
//		});

//		addOnScrollListener(new RecyclerView.OnScrollListener(){
//			@Override
//			public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//				if (dy > 0)
//					fabAddNew.hide();
//				else if (dy < 0)
//					fabAddNew.show();
//			}
//		});
		loadURL(mUrl);
		return view;
	}

	public void loadURL(final String uri) {
		mUrl = uri;
		if(!HttpHandler.hasWorkingInternet(mContext)) {
			return;
		}
		if(mUrl == null || TextUtils.isEmpty(mUrl)) {
			mUrl = "/m";
		}

		String url = Constants.HOST + mUrl;
		Log.d(TAG, "Loading " + url);
		mWebView.loadUrl(url);
	}

	private void setFragmentTitle(String title) {
		if (!isVisible()) {
			return;
		}

		if(title != null || !TextUtils.isEmpty(title)) {
			//shorten big title
			int start = title.indexOf(" |");
			if(start > 1) {
				title = title.substring(0, start);
			}

			AppCompatActivity activity = (AppCompatActivity) getActivity();
			if(activity != null && activity.getSupportActionBar() != null) {
				activity.getSupportActionBar().setTitle(title);
			}

		}
	}

	public boolean canGoBack(){
		return mWebView.canGoBack();
	}

	public void goBack(){
		mWebView.goBack();
	}

	public void reload(){
		mWebView.reload();
	}

	public void scrollToElement(final String elementId) {
		mWebView.getSettings().setJavaScriptEnabled(true);
//		mWebView.loadUrl("javascript:document.getElementById('current_position').scrollIntoView();");
		mWebView.loadUrl("javascript:document.getElementById('"+ elementId +"').scrollIntoView({ behavior: 'smooth' });");
//		mWebView.scrollBy(0,-50);
		ObjectAnimator anim = ObjectAnimator.ofInt(mWebView, "scrollY",0 , -50);
		anim.setDuration(500).start();
	}


}


