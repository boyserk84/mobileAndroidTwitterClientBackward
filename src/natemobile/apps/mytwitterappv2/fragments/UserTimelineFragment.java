package natemobile.apps.mytwitterappv2.fragments;

import natemobile.apps.mytwitterappv2.MyTwitterApp;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;

/**
 * UserTimeline fragment
 * @author nkemavaha
 *
 */
public class UserTimelineFragment extends TweetsListFragment {

	/**
	 * Flag indicate if this is the first time we show this since Activity will feed data directly to this fragment only for the first time.
	 */
	private boolean isFirstLoadDone = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void requestTwitterData(int count, long lastId) {	
		if ( isFirstLoadDone == true ) {
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

			// Prepare a request
			RequestParams request = new RequestParams("count", count);

			boolean isSubsequentLoad = (lastId != -1);

			if ( isSubsequentLoad ) {
				//since_id
				request.put("max_id", Long.toString(lastId) );
				// save the id
				lastTweetId = lastId;
			}

			MyTwitterApp.getRestClient().getUserTimeline(handler, request);
		}
	}
	
	/**
	 * Populate Data from raw JSONArray Twitter data
	 * @param data
	 */
	public void populateData(Object data) {
		if ( data instanceof JSONArray ) {
			JSONArray rawJSONTweets = (JSONArray) data;
			processTweetsData( rawJSONTweets );
			isFirstLoadDone = true;	// indicate that we don't need to load the first chunk of data
		}
	}
	
}
