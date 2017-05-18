package in.co.erailway.erailway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tomer.fadingtextview.FadingTextView;

import in.co.erailway.erailway.Animation.MyBounceAnimation;
import in.co.erailway.erailway.AppVersion.AppVersionCheck;
import in.co.erailway.erailway.DBUtil.SQLDatabaseHandler;
import in.co.erailway.erailway.Fragments.HomeFragment;
import in.co.erailway.erailway.Fragments.HomeFragment1;
import in.co.erailway.erailway.Fragments.PnrFragment;
import in.co.erailway.erailway.Fragments.PnrListFragment;
import in.co.erailway.erailway.Fragments.SearchTrainBetweenFragment;
import in.co.erailway.erailway.Fragments.SearchTrainFragment;
import in.co.erailway.erailway.Fragments.WebviewFragment;
import utils.AppUtils;
import utils.HttpHandler;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private Context mContext;

	private boolean hasInternet = true;
	private FragmentManager mFragmentManager;

	private String TAG = "MainActivity";
	boolean doubleBackToExitPressedOnce = false;

	private RelativeLayout mProgressLayout;
	private FrameLayout mFrameLayout;
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = getApplicationContext();
		mFragmentManager = getSupportFragmentManager();
		mProgressLayout = (RelativeLayout) findViewById(R.id.progress_overlay);
		mFrameLayout = (FrameLayout) findViewById(R.id.container);

		if(getFirstTimeStatus()) {

			AsyncTask.execute(new Runnable() {
				@Override
				public void run() {
					SQLDatabaseHandler.getSharedInstance(getApplicationContext()).addStation(getApplicationContext());
				}
			});

			setFirstTimeStatus(false);
			Intent i = new Intent(mContext, IntroActivity.class);
			startActivity(i);
			finish();
		}

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//		toolbar.setTitle("eRailway");
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		navigationView.setOnDragListener(new OnDragListener(){
			@Override
			public boolean onDrag(View view, DragEvent dragEvent) {
				AppUtils.hideSoftKeyboard(MainActivity.this);
				return false;
			}
		});

		///my code
		if(!HttpHandler.hasWorkingInternet(mContext)) {
			hasInternet = false;
			showHideWebview(false);
			return;
		}

		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.
				Builder()
//				.addTestDevice("ED322D8FB650DDE8C4DFBCCE86AE84EB")
				.build();
		mAdView.loadAd(adRequest);


		//version
		AppVersionCheck.doAppVersionCheck(MainActivity.this);

		String uri = null;
		if(getIntent().getExtras() != null) {
			uri = (String) getIntent().getExtras().get(Constants.APP_URL);
			Log.d(TAG, "Got url " + uri);
			loadWebviewWithURI(uri);
		} else {
			goHome();
		}

	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		Fragment fragment = mFragmentManager.findFragmentById(R.id.container);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
			return;
		}

		if(fragment instanceof WebviewFragment){
			WebviewFragment webviewFragment = (WebviewFragment) fragment;
			if(webviewFragment.canGoBack()){
				webviewFragment.goBack();
			} else {
				goToPreviousScreen();
			}

		} else if (fragment instanceof PnrFragment) {
			goToPreviousScreen();

		} else if (fragment instanceof HomeFragment1) {
			if (doubleBackToExitPressedOnce) {
				//wtf?!
				//super.onBackPressed();
				finish();
				return;
			}

			this.doubleBackToExitPressedOnce = true;
			Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					doubleBackToExitPressedOnce=false;
				}
			}, 2000);
		} else {
			goHome();

		}


//		if(mFragmentManager.getBackStackEntryCount() > 0){
//			mFragmentManager.popBackStackImmediate();
//
//		} else {
//
//			if (doubleBackToExitPressedOnce) {
//				super.onBackPressed();
//				return;
//			}
//
//			this.doubleBackToExitPressedOnce = true;
//			Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//			new Handler().postDelayed(new Runnable() {
//
//				@Override
//				public void run() {
//					doubleBackToExitPressedOnce=false;
//				}
//			}, 2000);
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);

		if(!HttpHandler.hasWorkingInternet(mContext)) {
			return true;
		}

		Fragment fragment = null;
		int id = item.getItemId();
		if(id == R.id.nav_home){
			fragment = HomeFragment1.newInstance();

		} else if (id == R.id.nav_search_train) {
			fragment = SearchTrainBetweenFragment.newInstance();

		} else if (id == R.id.nav_pnr) {
			fragment = PnrListFragment.newInstance();

		} else if (id == R.id.nav_live_status) {
			fragment = SearchTrainFragment.newInstance(true);

		} else if (id == R.id.nav_train_route) {
			fragment = SearchTrainFragment.newInstance(false);

		} else if (id == R.id.nav_share) {
			String message =  "Hey! I found this awesome rail info App - eRailway. It gives Train live status, PNR status and much more. Get it here : https://tinyurl.com/erailway";

			AppUtils.shareText(this, message);

			return true;
		} else if (id == R.id.nav_send) {
			Intent email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL, new String[]{"team@erailway.co.in"});
			email.putExtra(Intent.EXTRA_SUBJECT, "Hey! I liked the app");
			email.putExtra(Intent.EXTRA_TEXT, "");
			email.setType("message/rfc822");
			startActivity(Intent.createChooser(email, "Choose an Email client :"));

			return true;
		}

		mFragmentManager
				.beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack(null)
				.commit();
		return true;
	}

	private void loadWebviewWithURI(String uri) {
		WebviewFragment paymentFragment = WebviewFragment.newInstance(uri);
		mFragmentManager
				.beginTransaction()
				.replace(R.id.container, paymentFragment)
				.commit();
	}

//	private void stopLoadingWebView() {
//		if(hasInternet) {
//			mWebView.stopLoading();
//		}
//	}
	public void setFirstTimeStatus(boolean status){
		Context context = getApplicationContext();
		SharedPreferences.Editor editor = context.getSharedPreferences(Constants.APP_SETTINGS, context.MODE_PRIVATE).edit();
		editor.putBoolean(Constants.FIRST_TIME, status);
		editor.commit();
	}

	private void showHideWebview(final boolean show) {
		View content_view = findViewById(R.id.content);
		View no_internet_view = findViewById(R.id.no_internet_layout);

		if(show) {
			content_view.setVisibility(View.VISIBLE);
			no_internet_view.setVisibility(View.GONE);

		} else {
			content_view.setVisibility(View.GONE);
			no_internet_view.setVisibility(View.VISIBLE);
		}

	}

	private boolean getFirstTimeStatus(){
		Context context = getApplicationContext();

		return context.getSharedPreferences(Constants.APP_SETTINGS, context.MODE_PRIVATE).getBoolean(Constants.FIRST_TIME, true);
	}

	public void showHideFoatingButton(final boolean show, final int imageResourceId, final OnClickListener onClickListener) {
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

		if(show) {
			fab.setVisibility(View.VISIBLE);
			fab.setOnClickListener(onClickListener);
			fab.setImageResource(imageResourceId);

		} else {
			fab.setVisibility(View.GONE);
		}
	}

	public void bounceFoatingButton(final boolean start, final int duration, final int count) {
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

		if(start) {
//			new BounceAnimation(fab)
//					.setBounceDistance(40)
//					.setDuration(duration)
//					.setNumOfBounces(count)
//					.animate();

			fab.clearAnimation();
			final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

			// Use bounce interpolator with amplitude 0.2 and frequency 20
			MyBounceAnimation interpolator = new MyBounceAnimation(0.2, 20);
			myAnim.setInterpolator(interpolator);

			fab.startAnimation(myAnim);
		} else {

			fab.clearAnimation();
		}
	}

	public void showHideFoatingButtonTop(final boolean show, final int imageResourceId, final OnClickListener onClickListener) {
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);

		if(show) {
			fab.setVisibility(View.VISIBLE);
			fab.setOnClickListener(onClickListener);
//			fab.setImageResource(imageResourceId);

		} else {
			fab.setVisibility(View.GONE);
		}
	}

	public void showHideProgressOverlay(final boolean show, final int resourceArray) {
		mProgressLayout.setVisibility(show ? View.VISIBLE : View.GONE);

		FadingTextView fadingTextView = (FadingTextView) findViewById(R.id.fading_text_view);
		fadingTextView.setTexts(resourceArray);
		fadingTextView.requestLayout();
	}

	public void showSnackBar(final String msg) {
		Snackbar.make(mFrameLayout, msg, Snackbar.LENGTH_LONG)
				.setAction("Action", null).show();
	}

	public void showHideAd(final boolean show) {
		mAdView.setVisibility(show ? View.VISIBLE : View.GONE);
//		mAdView.setVisibility(View.GONE);
	}

	public void goHome() {
		HomeFragment homeFragment = HomeFragment.newInstance();
		mFragmentManager
				.beginTransaction()
				.replace(R.id.container, homeFragment)
				.commit();
	}

	public void goToPreviousScreen() {
		if(mFragmentManager.getBackStackEntryCount() > 0){
			mFragmentManager.popBackStackImmediate();

		} else {
			goHome();

		}
	}
}
