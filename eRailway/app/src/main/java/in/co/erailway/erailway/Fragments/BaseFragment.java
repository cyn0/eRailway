package in.co.erailway.erailway.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import in.co.erailway.erailway.MainActivity;
import in.co.erailway.erailway.R;

/**
 * Created by paln on 14/5/2017.
 */

public class BaseFragment extends Fragment {
	protected String fragmentTitle = "eRailway";
	protected String TAG = "BaseFragment";

	protected Context mContext;
	protected FragmentManager mFragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mFragmentManager = getFragmentManager();
		mContext = getContext();
	}

	@Override
	public void onStart() {
		super.onStart();

		MainActivity mainActivity = ((MainActivity) getActivity());
		if (mainActivity == null) {
			return;
		}

		mainActivity.getSupportActionBar().setTitle(fragmentTitle);

		Fragment fragment = this;
		if(fragment instanceof HomeFragment1
				|| fragment instanceof PnrListFragment
				|| fragment instanceof PnrFragment
				|| fragment instanceof SearchTrainBetweenFragment) {
			mainActivity.showHideAd(true);
		} else {
			mainActivity.showHideAd(false);
		}

	}
}
