package datamodel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.R.attr.format;

/**
 * Created by paln on 27/12/2016.
 */

public class PNRStatus {
	/*
	{"train_number":"12632","train_name":"NELLAI EXPRESS ","doj":" 2- 1-2017","from":"MDU ","to":"MS",
	"reservation_upto":"MS  ","boarding_point":"MDU ","class":" SL",
	"passengers":[{"number":"1","booking_status":"W/L18,PQWL  ","current_status":"W/L   13"}],
	"pnr":"4745954614","fare":"315","status":" CHART NOT PREPARED "}
	 */

	final private static String KEY_TRAIN_NUMBER = "train_number";
	final private static String KEY_TRAIN_NAME = "train_name";
	final private static String KEY_DOJ = "doj";
	final private static String KEY_FROM = "from";
	final private static String KEY_TO = "to";
	final private static String KEY_RESERVATION_UPTO = "reservation_upto";
	final private static String KEY_BOARDING_POINT = "boarding_point";
	final private static String KEY_CLASS = "class";
	final private static String KEY_PASSENGERS = "passengers";
	final private static String KEY_PNR = "pnr";
	final private static String KEY_FARE = "fare";
	final private static String KEY_STATUS = "chart_prepared";

	public String pnr;
	public String train_number;
	public String train_name;
	public long doj;
	public String from;
	public String to;
	public String reservation_upto;
	public String fare;
	public String status;
	public String train_class;
	public String boarding_point;
	public String passengersString;
	public ArrayList<Passenger> passengers = new ArrayList<>();

	public int updateTime;

	public Map<String, Object> mMap = new HashMap<String, Object>();

	public static PNRStatus fromString(String input){
		try {
			PNRStatus mPnrStatus = new PNRStatus();
			JSONObject root = new JSONObject(input);
			mPnrStatus.mMap = jsonToMap(root);
			/*
	{"train_number":"12632","train_name":"NELLAI EXPRESS ","doj":" 2- 1-2017","from":"MDU ","to":"MS",
	"reservation_upto":"MS  ","boarding_point":"MDU ","class":" SL",
	"passengers":[{"number":"1","booking_status":"W/L18,PQWL  ","current_status":"W/L   13"}],
	"pnr":"4745954614","fare":"315","status":" CHART NOT PREPARED "}
	 */
			mPnrStatus.train_number = root.getString(KEY_TRAIN_NUMBER);
			mPnrStatus.train_name = root.getString(KEY_TRAIN_NAME);
			mPnrStatus.doj = root.getLong(KEY_DOJ); //root.getString(KEY_DOJ).replace(" ", "");
			mPnrStatus.from = root.getString(KEY_FROM);
			mPnrStatus.to = root.getString(KEY_TO);
			mPnrStatus.reservation_upto = root.getString(KEY_RESERVATION_UPTO);
			mPnrStatus.boarding_point = root.getString(KEY_BOARDING_POINT);
			mPnrStatus.train_class = root.getString(KEY_CLASS);
			mPnrStatus.pnr = root.getString(KEY_PNR);
			mPnrStatus.fare = root.getString(KEY_FARE);
			mPnrStatus.status = root.getString(KEY_STATUS);

			mPnrStatus.passengersString = root.getString(KEY_PASSENGERS);
			JSONArray passengersArray = root.getJSONArray(KEY_PASSENGERS);
			for(int i =0; i<passengersArray.length(); i++) {
				JSONObject passenger = (JSONObject) passengersArray.get(i);

				Passenger passen = new Passenger();
				passen.number = passenger.getString("number");
				passen.booking_status = passenger.getString("booking_status");
				passen.current_status = passenger.getString("current_status");
				passen.seat_position = passenger.getString("seat_position");

				mPnrStatus.passengers.add(passen);
			}
			return mPnrStatus;
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
		Map<String, Object> retMap = new HashMap<>();

		if(json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keysItr = object.keys();
		while(keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if(value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if(value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<>();
		for(int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if(value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if(value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	public String getUpdateTimeString(SimpleDateFormat simpleDateFormat) {
		if(simpleDateFormat == null) {
			simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", java.util.Locale.getDefault());
		}
		Date resultdate = new Date(updateTime);
		return simpleDateFormat.format(resultdate);
	}

	public String getDOJTimeString(SimpleDateFormat simpleDateFormat) {
		if(simpleDateFormat == null) {
			simpleDateFormat = new SimpleDateFormat("dd- MMM -yyyy", java.util.Locale.getDefault());
		}
		Date resultdate = new Date(doj);
		return simpleDateFormat.format(resultdate);
	}

	public static class Passenger {
		public String number;
		public String booking_status;
		public String current_status;
		public String seat_position;
	}

	public static PNRStatus getDummyPNR() {
		/*
	{"train_number":"12632","train_name":"NELLAI EXPRESS ","doj":" 2- 1-2017","from":"MDU ","to":"MS",
	"reservation_upto":"MS  ","boarding_point":"MDU ","class":" SL",
	"passengers":[{"number":"1","booking_status":"W/L18,PQWL  ","current_status":"W/L   13"}],
	"pnr":"4745954614","fare":"315","status":" CHART NOT PREPARED "}
	 */
		PNRStatus pnrStatus = new PNRStatus();
		pnrStatus.pnr = "4745954614";
		pnrStatus.status = "CHART NOT PREPARED ";
		pnrStatus.train_number = "12632";
		pnrStatus.train_name = "NELLAI EXPRESS ";
		//pnrStatus.doj = "2-1-2017";
		pnrStatus.from = "MDU";
		pnrStatus.to = "MS";
		pnrStatus.reservation_upto = "MS  ";
		pnrStatus.boarding_point = "MDU ";
		pnrStatus.train_class = "SL";
		pnrStatus.fare = "315";
//		pnrStatus.

		return pnrStatus;
	}

//	public static int compareDate(PNRStatus lhs, PNRStatus rhs) {
//		String lhs_doj = lhs.doj;
//		String rhs_doj = rhs.doj;
//
//		String myFormat = "dd - MMM - yyyy";
//		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//		try {
//			Date lhs_date = sdf.parse(lhs_doj);
//			Date rhs_date = sdf.parse(rhs_doj);
//
//			return  lhs_date.compareTo(rhs_date);
//		} catch (Exception e ){
//			e.printStackTrace();
//		}
//
//		return lhs_doj.compareTo(rhs_doj);
//	}
}
