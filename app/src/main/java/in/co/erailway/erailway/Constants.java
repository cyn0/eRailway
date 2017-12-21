package in.co.erailway.erailway;

/**
 * Created by paln on 26/12/2016.
 */

public class Constants {
	public static String HOST =  "https://erailway.co.in";
//	public static String HOST =  "http://52.66.118.127";

	public static String URI_TRAIN_BETWEEN = "/trains-between";
	public static String URI_PNR_STATUS = "/pnr-status";
	public static String URI_LIVE_STATUS = "/train-running-status";
	public static String URI_TRAIN_ROUTE = "/trains";

	public static String FIRST_TIME = "first_time";
	public static String APP_BOOT_COUNT = "app_boot_count";
	public static String APP_SETTINGS = "app_settings";
	public static String APP_URL = "url";

	public enum Titles {
		TRAINS_BETWEEN("Trains Between Stations", URI_TRAIN_BETWEEN),
		PNR("PNR Status", URI_PNR_STATUS),
		LIVE_STATUS("Live Status", URI_LIVE_STATUS),
		TRAIN_ROUTE("Train Details", URI_TRAIN_ROUTE);

		public String titleText;
		public String uri;

		Titles(String t, String u) {
			titleText = t;
			uri = u;
		}
	}

	public enum TrainStartDate {
		YESTERDAY,
		TODAY,
		TOMORROW
	}
}
