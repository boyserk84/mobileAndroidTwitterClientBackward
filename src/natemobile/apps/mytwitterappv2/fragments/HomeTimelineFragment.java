package natemobile.apps.mytwitterappv2.fragments;

import natemobile.apps.mytwitterappv2.MyTwitterApp;
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
	public void callAPI(JsonHttpResponseHandler handler, RequestParams params) {
		MyTwitterApp.getRestClient().getHomeTimeline( handler, params );	
	}
	
	public static HomeTimelineFragment newInstance(int page) {
		HomeTimelineFragment fragmentFirst = new HomeTimelineFragment();
        return fragmentFirst;	
	}
	
	
}
