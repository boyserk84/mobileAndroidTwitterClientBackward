package natemobile.apps.mytwitterappv2.fragments;

import natemobile.apps.mytwitterappv2.MyTwitterApp;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;

public class UserTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void requestTwitterData(int count, long lastId) {
		// Setup handle Rest Client response
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				//processTweetsData( jsonTweets );
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorObject) {
				processFailureResponse( errorObject );
			}
		};
		
		// Prepare a request
		RequestParams request = new RequestParams("count", count);
		
		MyTwitterApp.getRestClient().getUserTimeline(handler, request);	
	}
}
