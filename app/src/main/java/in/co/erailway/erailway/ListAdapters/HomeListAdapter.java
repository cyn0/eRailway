package in.co.erailway.erailway.ListAdapters;

/**
 * Created by paln on 23/4/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.erailway.erailway.Animation.MyBounceAnimation;
import in.co.erailway.erailway.Constants.Titles;
import in.co.erailway.erailway.Fragments.PnrListFragment;
import in.co.erailway.erailway.Fragments.SearchTrainBetweenFragment;
import in.co.erailway.erailway.Fragments.SearchTrainFragment;
import in.co.erailway.erailway.R;

import static in.co.erailway.erailway.R.id.fab;

public class HomeListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<Titles> data;
	private FragmentManager fragmentManager;
	private static LayoutInflater inflater=null;
	private boolean animate;
//	public ImageLoader imageLoader;


	public HomeListAdapter(Activity a, ArrayList<Titles> d, FragmentManager fragmentManager, boolean animate) {
		activity = a;
		data=d;
		this.fragmentManager = fragmentManager;
		this.animate = animate;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		imageLoader=new ImageLoader(activity.getApplicationContext());

	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_home, null);
		}

		final Titles title = data.get(position);
		TextView textView = (TextView)convertView.findViewById(R.id.title);
		textView.setText(title.titleText);

		convertView.findViewById(R.id.cardSearchTrain).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Fragment fragment = null;
				switch (title) {
					case PNR:
						fragment =  PnrListFragment.newInstance();
						break;

					case LIVE_STATUS:
						fragment = SearchTrainFragment.newInstance(true);
						break;

					case TRAIN_ROUTE:
						fragment = SearchTrainFragment.newInstance(false);
						break;

					case TRAINS_BETWEEN:
						fragment = SearchTrainBetweenFragment.newInstance();
						break;
				}
				fragmentManager
						.beginTransaction()
						.replace(R.id.container, fragment)
						.addToBackStack(null)
						.commit();
			}
		});
		convertView.setTag(title);

		convertView.clearAnimation();
		if(animate) {
			final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.bounce_home);

			// Use bounce interpolator with amplitude 0.2 and frequency 20
			MyBounceAnimation interpolator = new MyBounceAnimation(0.2, 20);
			myAnim.setInterpolator(interpolator);
			convertView.startAnimation(myAnim);
		}
		return convertView;
	}
}
