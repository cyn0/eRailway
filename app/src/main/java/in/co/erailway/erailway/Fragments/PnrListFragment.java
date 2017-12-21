package in.co.erailway.erailway.Fragments;

/**
 * Created by paln on 10/4/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;

import java.util.ArrayList;
import java.util.Calendar;

import datamodel.PNRStatus;
import in.co.erailway.erailway.DBUtil.SQLDatabaseHandler;
import in.co.erailway.erailway.ListAdapters.PnrListAdapter;
import in.co.erailway.erailway.MainActivity;
import in.co.erailway.erailway.R;

/**
 * Created by paln on 9/4/2017.
 */

public class PnrListFragment extends BaseFragment {

	private ListView mListView;

	public static PnrListFragment newInstance() {
		PnrListFragment fragment = new PnrListFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fragmentTitle = "Saved PNR";
		TAG = "PNRListFragment";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pnr_list, container, false);
		mListView = (ListView) view.findViewById(R.id.list);

		final ArrayList<PNRStatus> pnrs = SQLDatabaseHandler.getSharedInstance(mContext).getAllPNRs();

//		Collections.sort(pnrs, new Comparator<PNRStatus>() {
//			@Override
//			public int compare(PNRStatus lhs, PNRStatus rhs) {
//				return PNRStatus.compareDate(lhs, rhs);
//			}
//		});

		ListAdapter adapter = new PnrListAdapter(getActivity(), pnrs);
		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String pnr = pnrs.get(position).pnr;
				searchPnrClicked(pnr);
			}
		});

		if(pnrs.size() < 1 ){
//			((MainActivity)getActivity()).showSnackBar("No Saved PNRs found.");
			((MainActivity)getActivity()).bounceFoatingButton(true, 1000 * 1000, 500);
			(view.findViewById(R.id.no_pnr_layout)).setVisibility(View.VISIBLE);
		}
		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		AppCompatActivity appCompatActivity = ((AppCompatActivity)getActivity());
		if(appCompatActivity != null && appCompatActivity.getSupportActionBar() != null) {
			appCompatActivity.getSupportActionBar().setTitle(fragmentTitle);
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		((MainActivity)getActivity()).showHideFoatingButton(true, R.drawable.ic_search_white_48dp, new OnClickListener() {
			@Override
			public void onClick(View view) {
				handleFloatingIconClicked();

			}
		});

	}

	@Override
	public void onStop() {
		MainActivity activity = (MainActivity) getActivity();
		if(activity != null) {
			activity.showHideFoatingButton(false, 0, null);
			activity.bounceFoatingButton(false, 0, 0);
		}
		super.onStop();
	}

	private void handleFloatingIconClicked() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);

		Log.d("pnrlist", hour + ":" + min);
		if((hour == 0 && min < 30) || (hour == 23 && min > 40)) {
			String title = "PNR status unavailable!";
			String content = "PNR status is not available during maintainence hours - 23:40 to 00:30";
			new MaterialDialog.Builder(mContext)
					.title(title)
					.content(content)
					.positiveText("Okay")
					.onPositive(new SingleButtonCallback() {
						@Override
						public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							dialog.dismiss();
						}
					})
					.widgetColorRes(R.color.colorPrimary)
					.show();
			return;
		}
		new MaterialDialog.Builder(getActivity())
				.title("Search PNR")
				//.content("input_content")
				.inputType(InputType.TYPE_CLASS_NUMBER)
				.input("Enter PNR", "", new MaterialDialog.InputCallback() {
					@Override
					public void onInput(MaterialDialog dialog, CharSequence input) {
						searchPnrClicked(input.toString());
					}
				})
				.inputRange(10, 10, getResources().getColor(R.color.error_color))
				.widgetColorRes(R.color.colorPrimary)
				.show();
	}

	private void searchPnrClicked(final String pnr) {
		PnrFragment pnrStatusFragment = PnrFragment.newInstance(pnr);
		mFragmentManager
				.beginTransaction()
				.replace(R.id.container, pnrStatusFragment)
				.addToBackStack(null)
				.commit();

	}



}

