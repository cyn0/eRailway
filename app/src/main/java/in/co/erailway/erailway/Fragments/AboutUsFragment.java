package in.co.erailway.erailway.Fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import in.co.erailway.erailway.ListAdapters.HomeListAdapter;
import in.co.erailway.erailway.R;

import static java.security.AccessController.getContext;

/**
 * Created by paln on 20/5/2017.
 */

public class AboutUsFragment extends BaseFragment{

	public static AboutUsFragment newInstance() {
		AboutUsFragment fragment = new AboutUsFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getContext();
		mFragmentManager = getFragmentManager();

		fragmentTitle = "About Us";
		TAG = "AboutFragment";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about_us, container, false);

		return view;
	}
}
