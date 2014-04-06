package natemobile.apps.mytwitterappv2.fragments;

import java.util.ArrayList;

import natemobile.apps.mytwitterappv2.R;
import natemobile.apps.mytwitterappv2.adapters.TweetsAdapter;
import natemobile.apps.mytwitterappv2.interfaces.OnTweetItemSelected;
import natemobile.apps.mytwitterappv2.interfaces.RequestDataAPI;
import natemobile.apps.mytwitterappv2.interfaces.ResultDataAPIListener;
import natemobile.apps.mytwitterappv2.models.Tweet;
import natemobile.apps.mytwitterappv2.views.EndlessScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;


/**
 * TweestListFragment (Base fragment)
 * @author nkemavaha
 *
 */
public class TweetsListFragment extends Fragment implements RequestDataAPI {
	
	public static final int TWEETS_WHEN_LOAD = 25;
	public static final int TWEETS_TO_LOAD_WHEN_SCROLL = 10;
	
	protected long lastTweetId = -1;
	
	/////////////////////
	/// UI fields
	////////////////////
	/** ListView of all tweets*/
	protected PullToRefreshListView lvTweets;
	
	////////////////////////////
	// Listener and adapters
	///////////////////////////
	/** Listener from activity, using for listening for response result in fragment so that we can show it to user. */
	ResultDataAPIListener listener;
	
	OnTweetItemSelected itemListener;
	
	/** Adapter */
	TweetsAdapter adapter;
	
	/** Flag indicate if this is the first time loading data */
	private boolean isFirstLoad = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if ( savedInstanceState == null ) {
			
		}
		return inflater.inflate( R.layout.fragment_tweetslist, container, false);	
	}
	
	
	// Store the listener (activity) that will have events fired once the fragment is attached
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Make sure activity implements ResultDataAPIListner
		if (activity instanceof ResultDataAPIListener) {
			listener = (ResultDataAPIListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement ResultDataAPIListenr.OnResponseReceived() in activity class.");
		}
		
		// Make sure activity implements OnTweetItemSelected
		if (activity instanceof OnTweetItemSelected) {
			itemListener = (OnTweetItemSelected) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTweetItemSelected.onTweetItemSelect() in activity class.");
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		lvTweets = (PullToRefreshListView) getActivity().findViewById( R.id.lvTweets );

		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		
		// Setup adapter
		adapter = new TweetsAdapter( getActivity(), tweets);	
		lvTweets.setAdapter( adapter );		
		
		// Setup listener for lvViews
		setupViewsListeners();
		
		// Signify that refresh has finished
		lvTweets.onRefreshComplete();
		
	}
	
	/**
	 * Setup listeners for views
	 */
	private void setupViewsListeners() {
		
		// Setup endless scrolling
		lvTweets.setOnScrollListener( new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				int loadCount = isFirstLoad? TWEETS_WHEN_LOAD : TWEETS_TO_LOAD_WHEN_SCROLL;
				isFirstLoad = false;
				requestTwitterData( loadCount, lastTweetId);
				
			}
		});
		
		// Setup pull-to-refresh
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list contents
				// Make sure you call listView.onRefreshComplete()
				// once the loading is done. This can be done from here or any
				// place such as when the network request has completed successfully.
				requestTwitterData( TWEETS_TO_LOAD_WHEN_SCROLL, lastTweetId);
			}
		});
		
		// Setup listener when click on tweent's profile image
		lvTweets.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Tweet tweet = getAdapter().getItem(position);
				Log.d("DEBUG", "On Itme Clikc belong to " + tweet.getUser().getScreenName() );
				
				view.setTag(tweet.getUser().getScreenName());
				
				// TODO: Get Child
				
				itemListener.onTweetItemSelect( tweet );
				
			}
		});
	}
	
	/**
	 * Helper function to process Tweets data received upon onSuccess
	 * @param jsonTweets
	 */
	protected void processTweetsData(JSONArray jsonTweets) {
		ArrayList<Tweet> tweets = Tweet.fromJson( jsonTweets);
		// NOTE: Due to "max_id" request, which results in returning ID less than (that is, older than) or equal to the specified ID,
		//			we will always get a duplicate one (overlapped)
		// More info: https://dev.twitter.com/docs/api/1.1/get/statuses/home_timeline

		// Remove the duplicate tweet (the first one we retrieved) 
		Tweet firstDuplicateTweet = (tweets.size() > 0)? tweets.get( 0 ) : null;
		boolean hasTheSameTweetId = false;
		if ( firstDuplicateTweet != null ) {
			hasTheSameTweetId = (firstDuplicateTweet.getId() == lastTweetId);
			if ( hasTheSameTweetId == true) {
				tweets.remove( 0 );
			}
		}

		// Either there are more tweets data OR there is no duplicate id.
		boolean hasNewTweets = (tweets.size() > 0 || hasTheSameTweetId == false);

		// Only update if new tweets are NOT overlapped.
		if ( hasNewTweets == true ) {
			// Subsequent
			adapter.addAll( tweets );
			adapter.notifyDataSetChanged();
			listener.onResponseReceived( tweets.size() + " Tweet(s)");
		}
	

		// Prevent Index Out Of Bound
		if ( tweets.size() > 0 ) {
			lastTweetId = tweets.get( tweets.size() -1 ).getId();
		}
		
		// Notify lvTweets that pull-to-refresh is complete
		lvTweets.onRefreshComplete();
	}
	
	/**
	 * Helper function to process Tweetsdata upon onFailure
	 * @param errorObject
	 */
	protected void processFailureResponse(JSONObject errorObject) {
		try {
			Log.d("DEBUG", errorObject.toString());
			JSONArray errorArray = errorObject.getJSONArray("errors");
			JSONObject errorLog = (JSONObject) errorArray.get(0);
			
			if ( errorLog.getInt("code") == 88 ) {
				listener.onResponseReceived(  errorLog.getString("message") + ". Try again in 15 mins." );
			}
		} catch (JSONException e1) {
			listener.onResponseReceived( e1.getMessage() );
		}
		
		// Notify lvTweets that pull-to-refresh is complete
		lvTweets.onRefreshComplete();
	}
	
	/**
	 * 
	 * @return Get TweetsAdapter Object (binding view and data)
	 */
	public TweetsAdapter getAdapter() {
		return adapter;
	}

	// Override needs in the child class
	@Override
	public void requestTwitterData(int count, long lastId) {}
	

}
