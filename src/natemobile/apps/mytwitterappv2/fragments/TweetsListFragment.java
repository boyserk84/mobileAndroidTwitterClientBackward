package natemobile.apps.mytwitterappv2.fragments;

import java.util.ArrayList;

import natemobile.apps.mytwitterappv2.R;
import natemobile.apps.mytwitterappv2.adapters.TweetsAdapter;
import natemobile.apps.mytwitterappv2.interfaces.OnTweetItemSelected;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;


/**
 * Abstract TweestListFragment (Base fragment)
 * @author nkemavaha
 *
 */
public abstract class TweetsListFragment extends Fragment {
	
	public static final int TWEETS_WHEN_LOAD = 25;
	public static final int TWEETS_TO_LOAD_WHEN_SCROLL = 10;
	
	/** Keep track of last tweet Id */
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
	
	/** Listener from activity, using for notifying if the tweet item is being clicked. */
	OnTweetItemSelected itemListener;
	
	///////////////////////
	/// Adapters
	///////////////////////
	/** Adapter */
	TweetsAdapter adapter;
	
	/** ArrayLIst of Tweets data */
	ArrayList<Tweet> tweets;
	
	/** Flag indicate if this is the first time loading data */
	private boolean isFirstLoad = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lastTweetId = -1;	// Reset lastTweetId
		tweets = new ArrayList<Tweet>();	// Init data
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_tweetslist, container, false);	
		if ( savedInstanceState == null ) {
			lvTweets = (PullToRefreshListView) view.findViewById(  R.id.lvTweets );

			// Setup listener for lvViews
			setupViewsListeners();
			

			
			// Signify that refresh has finished
			lvTweets.onRefreshComplete();
		}
		return view;
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
		// NOTE: Don't get view from Activity() overhere, race condition/unsafe operation may occur.
		// Never put a view OR access Activity directly
		super.onActivityCreated(savedInstanceState);
		
		// Setup adapter
		// TODO: Is it right to getActivity() overhere???
		adapter = new TweetsAdapter( getActivity(), tweets);	
		lvTweets.setAdapter( adapter );		
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
				executeRequest( loadCount, lastTweetId);
				
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
				executeRequest( TWEETS_WHEN_LOAD, lastTweetId);
			}
		});
		
		// Setup listener when click on Tweet item
		lvTweets.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Tweet tweet = getAdapter().getItem(position);
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
	 * Helper function to create a generic callback/handler object
	 * @return	callback object for passing to Twitter API.
	 */
	protected JsonHttpResponseHandler createTweetResponseHandler() {
		// Setup handle Rest Client response
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				processTweetsData( jsonTweets );
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorObject) {				
				processFailureResponse( errorObject );
			}
		};
		return handler;
	}
	
	/**
	 * Helper function to create a simple Twitter API request parameter
	 * @param count			How many tweets we would like to retrieve
	 * @param lastId		Last tweet Id (-1 for none), This is used for tracking how far we're down on the list.
	 * @return RequestParams object, containing parameters for passing to Twitter API.
	 */
	protected RequestParams createAPIRequestParameters(int count, long lastId) {
		// Prepare a request
		RequestParams request = new RequestParams("count", count);

		boolean isSubsequentLoad = (lastId != -1);

		if ( isSubsequentLoad ) {
			//since_id
			request.put("max_id", Long.toString(lastId) );
			// save the id
			lastTweetId = lastId;
		}
		
		return request;
	}
	
	/**
	 * Helper function to execute request to Twitter API
	 * @param count			How many tweets we would like to retrieve
	 * @param lastId		Last tweet Id (-1 for none), This is used for tracking how far we're down on the list.
	 */
	protected void executeRequest(int count, long lastId ) {
		if ( listener.checkNetworkConnect() ) {
			// Setup handle Rest Client response
			JsonHttpResponseHandler handler = createTweetResponseHandler();

			// Prepare a request
			RequestParams request = createAPIRequestParameters(count, lastId);

			// Call to MyTwitterApp singleton
			callAPI( handler, request);	
		} else {
			// Make sure PUll-to-refresh is back to its normal state.
			lvTweets.onRefreshComplete();
		}
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
	
	///////////////////////////////////
	/// Required override methods
	//////////////////////////////////
	
	/**
	 * Call API
	 * @param handler		Handler methond when we receive a reponse from a server
	 * @param params		Params to pass in
	 */
	public abstract void callAPI(JsonHttpResponseHandler handler, RequestParams params);
	

}
