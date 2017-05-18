package datamodel;

import org.json.JSONObject;

import static android.R.attr.versionCode;
import static android.R.id.input;

/**
 * Created by paln on 27/4/2017.
 */

public class VersionCode {
	public String latestVersion;
	public String minVersion;

	final static private String KEY_ANDROID = "android";
	final static private String KEY_MIN_VERSION = "minVersion";
	final static private String KEY_LATEST_VERSION = "latestVersion";

	public static VersionCode fromString(final String response) throws Exception{
		VersionCode versionCode = new VersionCode();
//		try {
			JSONObject root = new JSONObject(response);
			JSONObject android = root.getJSONObject(KEY_ANDROID);

			versionCode.latestVersion = android.getString(KEY_LATEST_VERSION);
			versionCode.minVersion = android.getString(KEY_MIN_VERSION);

//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		return versionCode;
	}
}
