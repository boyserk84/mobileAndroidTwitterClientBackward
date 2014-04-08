package natemobile.apps.mytwitterappv2.fragments;

import natemobile.apps.mytwitterappv2.MyTwitterApp;
import natemobile.apps.mytwitterappv2.interfaces.ITwitterUserFragment;

import org.json.JSONArray;

import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * UserTimeline fragment
 * 
 * Showing a time line of a specific user.
 * @author nkemavaha
 *
 */
public class UserTimelineFragment extends TweetsListFragment implements ITwitterUserFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void callAPI(JsonHttpResponseHandler handler, RequestParams params) {
		MyTwitterApp.getRestClient().getUserTimeline(handler, params);
	}
	
	@Override
	public void populateData(Object data) {
		if ( data instanceof JSONArray ) {
			JSONArray rawJSONTweets = (JSONArray) data;
			processTweetsData( rawJSONTweets );
		}
	}
	
}
