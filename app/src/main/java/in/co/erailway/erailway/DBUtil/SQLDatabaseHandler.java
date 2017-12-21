package in.co.erailway.erailway.DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lapism.searchview.SearchItem;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import datamodel.PNRStatus;
import in.co.erailway.erailway.R;

/**
 * Created by paln on 9/4/2017.
 */

public class SQLDatabaseHandler extends SQLiteOpenHelper {
	private static String TAG = "DATABASE";
	private static final int DATABASE_VERSION = 2;

	private static final String DATABASE_NAME = "myDB";
	private static final String TABLE_PNRS = "pnrList";

	final private static String KEY_TRAIN_NUMBER = "train_number";
	final private static String KEY_TRAIN_NAME = "train_name";
	final private static String KEY_DOJ = "doj";
	final private static String KEY_FROM = "train_from";
	final private static String KEY_BOARDING_POINT = "boarding_point";
	final private static String KEY_TO = "train_to";
	final private static String KEY_CLASS = "class";
	final private static String KEY_STATUS = "status";
	final private static String KEY_PASSENGER = "passenger";
	final private static String KEY_UPDATE_TIME = "update_time";
	final private static String KEY_PNR = "pnr";

	private static final String TABLE_STATION = "stationList";
	final private static String KEY_STATION_CODE = "station_code";
	final private static String KEY_STATION_NAME = "station_name";
	final private static String KEY_STATION_TEXT = "actual_text";

	private static SQLDatabaseHandler mDatabaseHandler;

	public SQLDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static SQLDatabaseHandler getSharedInstance(final Context context) {
		if(mDatabaseHandler == null) {
			mDatabaseHandler = new SQLDatabaseHandler(context);
		}
		return  mDatabaseHandler;
	}
//text primary key not null
	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PNRS + "("
				+ KEY_PNR + " TEXT PRIMARY KEY NOT NULL,"
				+ KEY_TRAIN_NAME + " TEXT,"
				+ KEY_TRAIN_NUMBER + " TEXT,"
				+ KEY_CLASS + " TEXT,"
//				+ KEY_DOJ + " TEXT,"
				+ KEY_DOJ + " INT,"
				+ KEY_FROM + " TEXT,"
				+ KEY_TO + " TEXT,"
				+ KEY_BOARDING_POINT + " TEXT,"
				+ KEY_STATUS + " TEXT,"
				+ KEY_PASSENGER + " TEXT,"
				+ KEY_UPDATE_TIME + " INT"
				+
				")";
		db.execSQL(CREATE_CONTACTS_TABLE);

		String CREATE_STATION_TABLE = "CREATE TABLE " + TABLE_STATION + "("
				+ KEY_STATION_CODE + " TEXT PRIMARY KEY NOT NULL,"
				+ KEY_STATION_NAME + " TEXT,"
				+ KEY_STATION_TEXT + " TEXT"
				+
				")";
		db.execSQL(CREATE_STATION_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PNRS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATION);
		// Create tables again
		onCreate(db);
	}

	public void addPnr(final PNRStatus pnrStatus) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PNR, pnrStatus.pnr);
		values.put(KEY_TRAIN_NAME, pnrStatus.train_name);
		values.put(KEY_TRAIN_NUMBER, pnrStatus.train_number);
		values.put(KEY_CLASS, pnrStatus.train_class);
		values.put(KEY_DOJ, pnrStatus.doj);
		values.put(KEY_FROM, pnrStatus.from);
		values.put(KEY_TO, pnrStatus.to);
		values.put(KEY_BOARDING_POINT, pnrStatus.boarding_point);
		values.put(KEY_UPDATE_TIME, System.currentTimeMillis());
		values.put(KEY_PASSENGER, pnrStatus.passengersString);
		values.put(KEY_STATUS, pnrStatus.status);

		// Inserting Row
		db.insert(TABLE_PNRS, null, values);
		db.close(); // Closing database connection

	}

	public ArrayList<PNRStatus> getAllPNRs() {
		ArrayList<PNRStatus> data = new ArrayList<>();

		String selectQuery = "SELECT  * FROM " + TABLE_PNRS + " ORDER BY " + KEY_DOJ + " ASC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				PNRStatus pnrStatus = new PNRStatus();
				pnrStatus.pnr = cursor.getString(0);
				pnrStatus.train_name = cursor.getString(1);
				pnrStatus.train_number = cursor.getString(2);
				pnrStatus.train_class = cursor.getString(3);
				pnrStatus.doj = cursor.getLong(4);
				pnrStatus.from = cursor.getString(5);
				pnrStatus.to = cursor.getString(6);
				pnrStatus.boarding_point = cursor.getString(7);
				pnrStatus.status = cursor.getString(8);
				pnrStatus.passengersString = cursor.getString(9);
				pnrStatus.updateTime = cursor.getInt(10);

				data.add(pnrStatus);

			} while (cursor.moveToNext());
		}

		cursor.close();

//		PNRStatus pnrStatus = PNRStatus.getDummyPNR();
//
//		data.add(pnrStatus);
//		data.add(pnrStatus);
		return data;
	}

	public void deletePNR(final PNRStatus pnrStatus) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PNRS, KEY_PNR + " = ?",
				new String[] { String.valueOf(pnrStatus.pnr) });
		db.close();
	}

	public void addStation(final Context context){
		try {

			InputStream inputStreamReader = context.getResources().openRawResource(R.raw.stations);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStreamReader));
			Log.d(TAG, "Start : insert stations into DB");
			long startTime = System.nanoTime();
			String line;
			SQLiteDatabase db = this.getWritableDatabase();
			db.beginTransaction();
			try {
				while ((line = br.readLine()) != null) {
					//Log.d(TAG, line);
					String[] tokens = line.split("#");
					addStation1(db, tokens[0], tokens[1], tokens[2]);
				}
				db.setTransactionSuccessful();
			} catch (Exception e ) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
			br.close();
			db.close();
			long stopTime = System.nanoTime();
			System.out.println("Took "+(stopTime - startTime) + " ns");
			Log.d(TAG, "Done : insert stations into DB");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addStation(final String stationCode, final String stationName, final String actualText) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_STATION_CODE, stationCode);
		values.put(KEY_STATION_NAME, stationName);
		values.put(KEY_STATION_TEXT, actualText);

		db.insert(TABLE_STATION, null, values);
		db.close();
	}

	private void addStation1(SQLiteDatabase db, final String stationCode, final String stationName, final String actualText) {
//		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_STATION_CODE, stationCode);
		values.put(KEY_STATION_NAME, stationName);
		values.put(KEY_STATION_TEXT, actualText);

		db.insert(TABLE_STATION, null, values);
//		db.close();
	}
	public ArrayList<String> getAllStations() {
		ArrayList<String> stationList = new ArrayList<>();

		String selectQuery = "SELECT  * FROM " + TABLE_STATION;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				String stationCode = cursor.getString(0);
				String stationName = cursor.getString(1);
				String actualText = cursor.getString(2);

				stationList.add(actualText);

			} while (cursor.moveToNext());
		}

		cursor.close();
		return stationList;
	}

	public ArrayList<SearchItem> getAllStationsAsSearchItem() {
		ArrayList<SearchItem> stationList = new ArrayList<>();

		String selectQuery = "SELECT  * FROM " + TABLE_STATION;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				String stationCode = cursor.getString(0);
				String stationName = cursor.getString(1);
				String actualText = cursor.getString(2);

				stationList.add(new SearchItem(actualText));

			} while (cursor.moveToNext());
		}

		cursor.close();
		return stationList;
	}
}