package in.co.erailway.erailway.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import in.co.erailway.erailway.R;

public class SearchListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private ArrayList<String> trains;
	private static LayoutInflater inflater=null;

	public SearchListAdapter(Activity a, ArrayList<String> trains1){ //ArrayList<HashMap<String, String>> d) {
		activity = a;
//		data=d;
		trains = trains1;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return trains.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.list_row, null);

		TextView title = (TextView)vi.findViewById(R.id.title); // title

		HashMap<String, String> song = new HashMap<String, String>();
//		song = data.get(position);

		// Setting all values in listview
		title.setText(trains.get(position));
		vi.setTag(trains.get(position));
		return vi;
	}
}