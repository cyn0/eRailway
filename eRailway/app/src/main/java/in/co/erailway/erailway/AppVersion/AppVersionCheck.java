package in.co.erailway.erailway.AppVersion;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;

import datamodel.VersionCode;
import in.co.erailway.erailway.BuildConfig;
import in.co.erailway.erailway.Constants;
import utils.HttpHandler;
import utils.HttpHandler.HttpDataListener;

/**
 * Created by paln on 27/4/2017.
 */

public class AppVersionCheck {
	private static int mVersionCode = BuildConfig.VERSION_CODE;
	private String mVersionName = BuildConfig.VERSION_NAME;

	public static void doAppVersionCheck(final Context context) {

		HttpDataListener httpDataListener = new HttpDataListener() {
			@Override
			public void onDataAvailable(String response) {
				handleVersionCheckResponse(context, response);
			}

			@Override
			public void onError(Exception e) {

			}
		};

		HttpHandler.getSharedInstance().getVersionCode(httpDataListener);
	}

	private static void handleVersionCheckResponse(final Context context, final String response) {
		try {
			VersionCode versionCode = VersionCode.fromString(response);
			int minServer = Integer.parseInt(versionCode.minVersion);
			int latestServer = Integer.parseInt(versionCode.latestVersion);

			if(mVersionCode < minServer) {
				forceUser(context);
			} else if(mVersionCode < latestServer) {
				informUser(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
			forceUser(context);
		}
	}

	private static void informUser(final Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context); //context.getSharedPreferences(Constants.APP_SETTINGS, context.MODE_PRIVATE);
		int appBootCount = sharedPreferences.getInt(Constants.APP_BOOT_COUNT, 0);

		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(Constants.APP_BOOT_COUNT, ++appBootCount);
		editor.commit();

		if(appBootCount % 10 != 0) {
			return;
		}

		String title = "New update available";
		final String content = "Please update the app to enjoy awesome new features and security fixes.";

		try {
			new MaterialDialog.Builder(context)
					.title(title)
					.content(content)
					.positiveText("Update")
					.negativeText("Later")
					.onPositive(new SingleButtonCallback() {
						@Override
						public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							redirectToPlayStore(context);
						}
					})
					.onNegative(new SingleButtonCallback() {
						@Override
						public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							dialog.dismiss();
						}
					})
					.show();
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}

	private static void forceUser(final Context context) {
		final String content = "Current version of App is too old. Please update the app to enjoy new features and security fixes.";
		try {
		new MaterialDialog.Builder(context)
				.title("App too old...")
				.content(content)
				.positiveText("Update")
				.onPositive(new SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						redirectToPlayStore(context);
					}
				})
				.cancelable(false)
				.autoDismiss(false)
				.show();
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}

	private static void redirectToPlayStore(final Context context) {
		try {
			//activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+activity.getPackageName())));
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp")));
		} catch (ActivityNotFoundException e) {

			context.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));

		}
	}
}
