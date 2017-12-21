package in.co.erailway.erailway.Fragments;

/**
 * Created by paln on 23/4/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import in.co.erailway.erailway.Constants;
import in.co.erailway.erailway.Constants.Titles;
import in.co.erailway.erailway.ListAdapters.HomeListAdapter;
import in.co.erailway.erailway.R;

public class HomeFragment extends BaseFragment {

	private ListView mList;
	private FragmentManager mFragmentManager;

	private static boolean animate = true;
	/*
	I used this same fragment for gettting train number as input for both Live status and train route :P
	 */
	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getContext();
		mFragmentManager = getFragmentManager();

		fragmentTitle = "eRailway";
		TAG = "HomeFragment";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		mList = (ListView) view.findViewById(R.id.list);


		final ArrayList<Titles> arrayList = new ArrayList<Titles>();
		for(Titles title : Titles.values()) {
			arrayList.add(title);
		}
		HomeListAdapter adapter = new HomeListAdapter(getActivity(), arrayList, mFragmentManager, animate);
		mList.setAdapter(adapter);
		animate = false;
//		view.startAnimation(AnimationUtils.loadAnimation(
//				mContext, android.R.anim.fade_out));
		return view;
	}

}