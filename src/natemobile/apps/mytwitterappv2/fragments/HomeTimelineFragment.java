package natemobile.apps.mytwitterappv2.fragments;

import natemobile.apps.mytwitterappv2.MyTwitterApp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * HomeTimelineFragment
 * 
 * Showing Twitter home timeline
 * @author nkemavaha
 *
 */
public class HomeTimelineFragment extends TweetsListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View parent = super.onCreateView(inflater, container, savedInstanceState);		
		return parent;
	}
	
	@Override
	public void requestTwitterData(int count, long lastId) {
		
		if ( listener.checkNetworkConnect() ) {

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

			// Call to MyTwitterApp singleton
			MyTwitterApp.getRestClient().getHomeTimeline( handler, request );	
		} else {
			lvTweets.onRefreshComplete();
		}
	}
}
