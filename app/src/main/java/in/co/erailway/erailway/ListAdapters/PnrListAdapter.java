package in.co.erailway.erailway.ListAdapters;

/**
 * Created by paln on 9/4/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import datamodel.PNRStatus;
import in.co.erailway.erailway.R;

import static in.co.erailway.erailway.R.id.doj;

public class PnrListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<PNRStatus> data;

	private static LayoutInflater inflater=null;
//	public ImageLoader imageLoader;

	public PnrListAdapter(Activity a, ArrayList<PNRStatus> d) {
		activity = a;
		data=d;
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
			convertView = inflater.inflate(R.layout.list_row_pnr, null);
		}

		TextView firstLine = (TextView)convertView.findViewById(R.id.first_line_header);
		TextView secondLine = (TextView)convertView.findViewById(R.id.second_line);
		TextView thirdLine = (TextView)convertView.findViewById(R.id.third_line);
//		ImageView thumb_image=(ImageView)convertView.findViewById(R.id.list_image);

		PNRStatus pnrStatus = data.get(position);
		convertView.setTag(pnrStatus);

		String fLine = pnrStatus.train_name + " (" + pnrStatus.train_number + ")";
		String sLine = pnrStatus.getDOJTimeString(null);
		String tLine = pnrStatus.boarding_point + " to " + pnrStatus.to;

		firstLine.setText(fLine);
		secondLine.setText(sLine);
		thirdLine.setText(tLine);

		return convertView;
	}
}