package in.co.erailway.erailway.Fragments;

/**
 * Created by paln on 23/4/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import in.co.erailway.erailway.R;

public class HomeFragment1 extends BaseFragment {

	public static HomeFragment1 newInstance() {
		HomeFragment1 fragment = new HomeFragment1();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fragmentTitle = "eRailway";
		TAG = "HomeFragment";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home2, container, false);

		CardView searchTrain = (CardView) view.findViewById(R.id.cardSearchTrain);
		CardView pnrStatus = (CardView) view.findViewById(R.id.cardPNRStatus);
		CardView liveStatus = (CardView) view.findViewById(R.id.cardLiveStatus);
		CardView trainDetails = (CardView) view.findViewById(R.id.cardTrainDetails);

		searchTrain.setOnClickListener(clickListener);
		pnrStatus.setOnClickListener(clickListener);
		liveStatus.setOnClickListener(clickListener);
		trainDetails.setOnClickListener(clickListener);

//		ArrayList<Card> cards = new ArrayList<Card>();
//
//		Card card = new Card(getContext());
//		CardHeader header = new CardHeader(getContext());
//		header.setTitle("Search Train");
//		card.addCardHeader(header);
//		card.setTitle("");
//		CardThumbnail thumb = new CardThumbnail(getActivity());
//		thumb.setDrawableResource(R.drawable.search1);
//
//		card.addCardThumbnail(thumb);
//		cards.add(card);
//
//		Card card1 = new Card(getContext());
//		CardHeader header1 = new CardHeader(getContext());
//		header1.setTitle("PNR status");
//		CardThumbnail thumb1 = new CardThumbnail(getActivity());
//		thumb1.setDrawableResource(R.drawable.pnr1);
//		card1.addCardThumbnail(thumb1);
//		card1.addCardHeader(header1);
//
//		Card card2 = new Card(getContext());
//		CardHeader header2 = new CardHeader(getContext());
//		header2.setTitle("Live Status");
//		CardThumbnail thumb2 = new CardThumbnail(getActivity());
//		thumb2.setDrawableResource(R.drawable.live1);
//		card2.addCardThumbnail(thumb2);
//		card2.addCardHeader(header2);
//
//		Card card3 = new Card(getContext());
//		CardHeader header3 = new CardHeader(getContext());
//		header3.setTitle("Train Details");
//		CardThumbnail thumb3 = new CardThumbnail(getActivity());
//		thumb3.setDrawableResource(R.drawable.details1);
//		card3.addCardThumbnail(thumb3);
//		card3.addCardHeader(header3);
//
//		cards.add(card1);
//		cards.add(card2);
//		cards.add(card3);
//
//		CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(mContext,cards);
//
//		CardGridView gridView = (CardGridView) view.findViewById(R.id.carddemo_grid_cursor);
//		gridView.setAdapter(mCardArrayAdapter);


		return view;
	}

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			Fragment fragment = null;
			int id = view.getId();
			if(id==R.id.cardSearchTrain) {
				fragment = SearchTrainBetweenFragment.newInstance();
			} else if(id==R.id.cardPNRStatus) {
				fragment =  PnrListFragment.newInstance();
			} else if(id==R.id.cardLiveStatus) {
				fragment = SearchTrainFragment.newInstance(true);
			} else if(id==R.id.cardTrainDetails) {
				fragment = SearchTrainFragment.newInstance(false);
			}

			mFragmentManager
					.beginTransaction()
					.replace(R.id.container, fragment)
					.addToBackStack(null)
					.commit();
		}
	};
}