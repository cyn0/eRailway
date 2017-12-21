package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import datamodel.PNRStatus;
import datamodel.PNRStatus.Passenger;
import in.co.erailway.erailway.Constants;
import in.co.erailway.erailway.MainActivity;

import static android.R.id.message;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by paln on 23/4/2017.
 */

public class AppUtils {
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	public static void shareText(final Activity activity, final String msg) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
		sendIntent.setType("text/plain");
		sendIntent.putExtra("exit_on_sent", true);
		activity.startActivityForResult(Intent.createChooser(sendIntent, "Share to.."), 1); //(Intent.createChooser(sendIntent, mContext.getResources().getText(R.string.share_with)));

	}

	public static String getPnrShareText(final PNRStatus pnrStatus) {
		String message = "PNR: " + pnrStatus.pnr + "\n" +
				"(" + pnrStatus.train_number + ") " + pnrStatus.train_name + "\n" +
				pnrStatus.boarding_point + " to " + pnrStatus.reservation_upto + "\n";
		ArrayList<Passenger> passengers = pnrStatus.passengers;
		for(Passenger passenger : passengers) {
			String row = passenger.number + ": " + passenger.booking_status + " -> " + passenger.current_status + "\n";
			message += row;
		}
		message += "More info at: " + Constants.HOST + Constants.URI_PNR_STATUS + "/" + pnrStatus.pnr;

		return message;
	}
}
