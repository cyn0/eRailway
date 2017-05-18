package in.co.erailway.erailway.Fragments;

/**
 * Created by paln on 27/4/2017.
 */

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchAdapter.OnItemClickListener;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;
import in.co.erailway.erailway.Constants;
import in.co.erailway.erailway.DBUtil.SQLDatabaseHandler;
import in.co.erailway.erailway.MainActivity;
import in.co.erailway.erailway.R;

import static com.google.android.gms.internal.zzs.TAG;

public class SearchTrainBetweenFragment extends BaseFragment {

	private SearchView mSourceSearchView, mDestinationSearchView;
	private MaterialSpinner quotaSpinner;
	private EditText dateEdittext;
	private Button searchTrainButton;

	String myFormat = "dd - MMM - yyyy";
	SimpleDateFormat sdf = new SimpleDateFormat(myFormat,java.util.Locale.getDefault());
	final Calendar myCalendar = Calendar.getInstance();

	public static SearchTrainBetweenFragment newInstance() {
		SearchTrainBetweenFragment fragment = new SearchTrainBetweenFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fragmentTitle = "Search Trains Between";
		TAG = "SearchTrainBetween";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_train_between, container, false);
		mSourceSearchView = (SearchView) view.findViewById(R.id.sourceSearchView);
		mDestinationSearchView = (SearchView) view.findViewById(R.id.destinationSearchView);

		mSourceSearchView.setNavigationIcon(null);
		mSourceSearchView.setVoice(false);
		mSourceSearchView.setHint("Source");

		mDestinationSearchView.setNavigationIcon(null);
		mDestinationSearchView.setVoice(false);
		mDestinationSearchView.setHint("Destination");

//		List<SearchItem> suggestionsList = new ArrayList<>();
//
//		List<String> suggestionsListString = SQLDatabaseHandler.getSharedInstance(getContext()).getAllStations();
//		for(String station : suggestionsListString) {
//			suggestionsList.add(new SearchItem(station));
//		}

		List<SearchItem> suggestionsList = SQLDatabaseHandler.getSharedInstance(getContext()).getAllStationsAsSearchItem();
		SearchAdapter searchAdapter = new MySearchAdapter(mContext, suggestionsList);
		searchAdapter.addOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
				String query = textView.getText().toString();
				mSourceSearchView.setQuery(query, true);
				mSourceSearchView.close(true);

			}
		});

		SearchAdapter searchAdapter1 = new MySearchAdapter(mContext, suggestionsList);
		searchAdapter1.addOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
				String query = textView.getText().toString();
				mDestinationSearchView.setQuery(query, true);
				mDestinationSearchView.close(true);
			}
		});

		mSourceSearchView.setAdapter(searchAdapter);
		mDestinationSearchView.setAdapter(searchAdapter1);

		String[] ITEMS = {"General Quota", "Tatkal", "Premium Tatkal", "Ladies", "Defence", "Duty pass", "Foreign Tourist",
				"Lower Berth"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ITEMS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		quotaSpinner = (MaterialSpinner) view.findViewById(R.id.spinner1);
		quotaSpinner.setPaddingSafe(0, 0, 0, 0);
		quotaSpinner.setAdapter(adapter);
		quotaSpinner.setSelection(1);

		dateEdittext = (EditText) view.findViewById(R.id.editDatePicker);

		dateEdittext.setText(sdf.format(myCalendar.getTime()));

		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				dateEdittext.setText(sdf.format(myCalendar.getTime()));
			}

		};

		dateEdittext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(getActivity(), date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		searchTrainButton = (Button) view.findViewById(R.id.searchTrain);
		searchTrainButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleSearchTrainClicked();
			}
		});

		return view;
	}

	private void handleSearchTrainClicked() {

		String source = mSourceSearchView.getQuery().toString();
		String destination = mDestinationSearchView.getQuery().toString();
		final String quota = (String) quotaSpinner.getSelectedItem();
		if(TextUtils.isEmpty(source)) {
			showErrorText("Please choose Source");
			return;
		} else if(TextUtils.isEmpty(destination)) {
			showErrorText("Please choose destination");
			return;
		} else if(quota.equals("Quota")) {
			showErrorText("Please select a quota from dropdown");
			return;
		}
		Log.d(TAG, quota);
		//https://erailway.co.in/trains-between/MAS-Chennai-Central-to-BCT-Mumbai-Central?quota=DP&date=2017-05-03


		//processing :( :(
		source = source.replaceAll("[()]","").replaceAll(" ", "-");
		destination = destination.replaceAll("[()]","").replaceAll(" ", "-");
		String quotaNotation = "GN";
		switch (quota) {
			case "General Quota":
				quotaNotation = "GN";
				break;
			case "Tatkal":
				quotaNotation = "CK";
				break;
			case "Premium Tatkal":
				quotaNotation = "PT";
				break;
			case "Ladies":
				quotaNotation = "LD";
				break;
			case "Defence":
				quotaNotation = "DF";
				break;
			case "Duty pass":
				quotaNotation = "DP";
				break;
			case "Foreign Tourist":
				quotaNotation = "FF";
				break;
			case "Lower Berth":
				quotaNotation = "SS";
				break;
		}

//		String myFormat = "yyyy-MM-dd";
//		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		String dateString = sdf.format(myCalendar.getTime());

		String uri = Constants.URI_TRAIN_BETWEEN + "/" + source + "-to-" + destination;
		final Uri builtUri = Uri.parse(uri)
				.buildUpon()
				.appendQueryParameter("quota", quotaNotation)
				.appendQueryParameter("date",dateString)
				.build();

		WebviewFragment paymentFragment = WebviewFragment.newInstance(builtUri.toString());
		mFragmentManager
				.beginTransaction()
				.replace(R.id.container, paymentFragment)
				.addToBackStack(null)
				.commit();
	}

	@Override
	public void onStart() {
		super.onStart();
//		mSourceSearchView.setQuery("", false);

	}
	private void showErrorText(final String msg) {
		((MainActivity)getActivity()).showSnackBar(msg);
	}


	public class MySearchAdapter extends SearchAdapter {

		public MySearchAdapter(Context context) {
			super(context);
		}

		public MySearchAdapter(Context context, List<SearchItem> suggestionsList) {
			super(context, suggestionsList);
		}

		// ---------------------------------------------------------------------------------------------
		@Override
		public Filter getFilter() {
			return new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();

					if (!TextUtils.isEmpty(constraint)) {
						key = constraint.toString().toLowerCase(Locale.getDefault());

						List<SearchItem> results = new ArrayList<>();
						List<SearchItem> history = new ArrayList<>();

						history.addAll(mSuggestionsList);

						for (SearchItem item : history) {
							String string = item.get_text().toString().toLowerCase(Locale.getDefault());
							if (string.contains(key)) {
								results.add(item);
							}
						}

						if (results.size() > 0) {
							filterResults.values = results;
							filterResults.count = results.size();
						}
					} else {
						key = "";
					}

					return filterResults;
				}

				@Override
				protected void publishResults(final CharSequence constraint, FilterResults results) {
					List<SearchItem> dataSet = new ArrayList<>();

					if (results.count > 0) {
						List<?> result = (ArrayList<?>) results.values;
						for (Object object : result) {
							if (object instanceof SearchItem) {
								dataSet.add((SearchItem) object);
							}
						}
					} else {
//						if (key.isEmpty()) {
//							List<SearchItem> allItems = mHistoryDatabase.getAllItems(mDatabaseKey);
//							if (!allItems.isEmpty()) {
//								dataSet = allItems;
//							}
//						}
					}
//					Collections.reverse(dataSet);

					//T0-DO working properly?
					Collections.sort(dataSet, new Comparator<SearchItem>() {
						@Override
						public int compare(SearchItem lhs, SearchItem rhs) {
							// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
							String first = lhs.get_text().toString();
							String sec = rhs.get_text().toString();

							int i1 = first.indexOf(constraint.toString());
							int i2 = sec.indexOf(constraint.toString());

							return (i1 < i2) ? -1 : (i1 == i2) ? 0 : 1;
						}
					});

					setData(dataSet);
				}
			};
		}


	}
}