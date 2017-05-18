package in.co.erailway.erailway.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import datamodel.PNRStatus;
import datamodel.PNRStatus.Passenger;
import in.co.erailway.erailway.DBUtil.SQLDatabaseHandler;
import in.co.erailway.erailway.MainActivity;
import in.co.erailway.erailway.R;
import utils.AppUtils;
import utils.HttpHandler;

/**
 * Created by paln on 17/4/2017.
 */

public class PnrFragment extends BaseFragment {
	private View mResultView;
	private View mFragmentView;
	private static String mPNR;
	private PNRStatus mPnrStatus;
	private int retryCount = 0;

	public static PnrFragment newInstance(final String pnr) {
		PnrFragment fragment = new PnrFragment();
		mPNR = pnr;

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fragmentTitle = "PNR Status";
		TAG = "PnrFragment";

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem menuItem = menu.add("Share");
		menuItem.setIcon(R.drawable.ic_share_white_48dp);
		menuItem.setTitle("Share");
		menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		MenuItem menuItem1 = menu.add("Delete");
		menuItem1.setIcon(R.drawable.ic_delete_white_48dp);
		menuItem1.setTitle("Delete");
		menuItem1.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View fragmentView = inflater.inflate(R.layout.fragment_pnr_status, container, false);
		mFragmentView = fragmentView;

		mResultView = mFragmentView.findViewById(R.id.result_layout);
		mResultView.setVisibility(View.GONE);

		MainActivity activity = (MainActivity) getActivity();
		if(activity != null) {
			activity.showHideProgressOverlay(true, R.array.pnr_error_texts);
		}
		fetchPnrStatus();

		return fragmentView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "menu clicked");
		if(mPnrStatus == null) {
			((MainActivity)getActivity()).showSnackBar("PNR status not available. Try again later");
			return super.onOptionsItemSelected(item);
		}
		if(item.getTitle().equals("Share")) {
			final String message = AppUtils.getPnrShareText(mPnrStatus);

			AppUtils.shareText(getActivity(), message);

		} else if(item.getTitle().equals("Delete")) {
			handleDeletePnrClicked();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStop() {
		MainActivity activity = (MainActivity) getActivity();
		if(activity != null) {
			activity.showHideProgressOverlay(false, R.array.pnr_error_texts);
		}
		super.onStop();
	}

	private void fetchPnrStatus() {
//		((MainActivity)getActivity()).showHideProgressOverlay(true,  R.array.pnr_error_texts);
//		View result_view = mFragmentView.findViewById(R.id.result_layout);
//		result_view.setVisibility(View.GONE);
//
//		View error_view = mFragmentView.findViewById(R.id.error_layout);
//		error_view.setVisibility(View.VISIBLE);
//
//		View table = mFragmentView.findViewById(R.id.passenger_table);
//		table.setVisibility(View.GONE);

		HttpHandler.getSharedInstance().getPNRStatus(mPNR, new HttpHandler.HttpDataListener() {
			@Override
			public void onDataAvailable(String response) {
				mPnrStatus = PNRStatus.fromString(response);
				populateDetails(mPnrStatus);
				persistPNR();

				MainActivity activity = (MainActivity) getActivity();
				if(activity != null) {
					activity.showHideProgressOverlay(false, R.array.pnr_error_texts);
				}
				mResultView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onError(Exception e) {
				if(retryCount < 3) {
					retryCount++;
					fetchPnrStatus();
				} else {
					((MainActivity)getActivity()).showHideProgressOverlay(false, R.array.pnr_error_texts);
					mResultView.setVisibility(View.GONE);

					View error_view = mFragmentView.findViewById(R.id.error_layout);
					error_view.setVisibility(View.VISIBLE);

//					View table = mFragmentView.findViewById(R.id.passenger_table);
//					table.setVisibility(View.GONE);
				}
			}
		});
	}

	private void populateDetails(final PNRStatus pnrStatus) {
		TextView textView;

		textView = (TextView) mFragmentView.findViewById(R.id.header);
		String header = "(" + pnrStatus.train_number + ") " + pnrStatus.train_name;
		textView.setText(header);

		textView = (TextView) mFragmentView.findViewById(R.id.pnr_no);
		textView.setText(pnrStatus.pnr);

		textView = (TextView) mFragmentView.findViewById(R.id.doj);
		textView.setText(pnrStatus.getDOJTimeString(null));

		textView = (TextView) mFragmentView.findViewById(R.id.boarding_station);
		textView.setText(pnrStatus.boarding_point);

//		textView = (TextView) mFragmentView.findViewById(R.id.reservation_upto);
//		textView.setText(pnrStatus.reservation_upto);

		textView = (TextView) mFragmentView.findViewById(R.id.train_class);
		textView.setText(pnrStatus.train_class);

		textView = (TextView) mFragmentView.findViewById(R.id.status);
		textView.setText(pnrStatus.status);

		textView = (TextView) mFragmentView.findViewById(R.id.origin);
		textView.setText(pnrStatus.from);

		textView = (TextView) mFragmentView.findViewById(R.id.destination);
		textView.setText(pnrStatus.to);

		addPassengerDetails(pnrStatus);
	}

	private void addPassengerDetails(final PNRStatus pnrStatus) {
		LinearLayout passengerTable = (LinearLayout) mFragmentView.findViewById(R.id.passenger_table);
		ArrayList<Passenger> passengers = pnrStatus.passengers;

		for(Passenger passenger: passengers) {
			LinearLayout row = new LinearLayout(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(10, 20, 10, 20);
			row.setLayoutParams(params);
			row.setBackgroundResource(R.drawable.textlines);

			TextView number = new TextView(mContext);
			params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
			number.setText(passenger.number);
			number.setLayoutParams(params);
			row.addView(number);

			TextView bookingStatus = new TextView(mContext);
			params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3.0f);
			bookingStatus.setText(passenger.booking_status);
			bookingStatus.setLayoutParams(params);
			row.addView(bookingStatus);

			TextView currentStatus = new TextView(mContext);
			params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3.0f);
			currentStatus.setText(passenger.current_status);
			currentStatus.setLayoutParams(params);
			row.addView(currentStatus);

			TextView seatPosition = new TextView(mContext);
			params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3.0f);
			seatPosition.setText(passenger.seat_position);
			seatPosition.setLayoutParams(params);
			row.addView(seatPosition);

			passengerTable.addView(row);
		}

	}

	private void persistPNR() {
		SQLDatabaseHandler.getSharedInstance(mContext).addPnr(mPnrStatus);
	}

	private void handleDeletePnrClicked() {
		((MainActivity)getActivity()).showHideProgressOverlay(true, R.array.delete_pnr_texts);
		SQLDatabaseHandler.getSharedInstance(mContext).deletePNR(mPnrStatus);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				((MainActivity)getActivity()).showHideProgressOverlay(false, R.array.delete_pnr_texts);
				mFragmentManager
					.beginTransaction()
					.replace(R.id.container, PnrListFragment.newInstance())
					.addToBackStack(null)
					.commit();
			}
		}, 2000);


	}
}



