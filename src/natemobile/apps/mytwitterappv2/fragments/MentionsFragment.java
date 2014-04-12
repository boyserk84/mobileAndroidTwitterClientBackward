package natemobile.apps.mytwitterappv2.fragments;

import natemobile.apps.mytwitterappv2.MyTwitterApp;
import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * MentionsFragment
 * 
 * Showing tweets message of this user being mentioned
 * @author nkemavaha
 *
 */
public class MentionsFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void callAPI(JsonHttpResponseHandler handler, RequestParams params) {
		MyTwitterApp.getRestClient().getMentionsTimeline( handler, params );
	}
	
	public static MentionsFragment newInstance(int page) {
		MentionsFragment fragmentFirst = new MentionsFragment();
        return fragmentFirst;	
	}
}
