package natemobile.apps.mytwitterappv2.fragments;

import natemobile.apps.mytwitterappv2.MyTwitterApp;
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
public class UserTimelineFragment extends TweetsListFragment {
	
	/** Twitter screen name */
	private String m_screenName;
	
	public void setScreenName(String screenName) {
		this.m_screenName = screenName;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void callAPI(JsonHttpResponseHandler handler, RequestParams params) {
		MyTwitterApp.getRestClient().getUserTimeline(handler, params);
	}
	
	@Override
	protected void executeRequest(int count, long lastId ) {
		if ( listener.checkNetworkConnect() ) {
			// Setup handle Rest Client response
			JsonHttpResponseHandler handler = createTweetResponseHandler();

			// Prepare a request
			RequestParams request = createAPIRequestParameters(count, lastId);
			
			request.put("screen_name", m_screenName);

			// Need to check if screen is empty here, preventing empty request
			if ( m_screenName != null && m_screenName.isEmpty() == false ) {
				// Call to MyTwitterApp singleton
				callAPI( handler, request);	
			}
		} else {
			// Make sure PUll-to-refresh is back to its normal state.
			lvTweets.onRefreshComplete();
		}
	}
	
}
