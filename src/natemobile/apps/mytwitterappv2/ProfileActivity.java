package natemobile.apps.mytwitterappv2;

import java.util.ArrayList;

import natemobile.apps.mytwitterappv2.fragments.ProfileFragment;
import natemobile.apps.mytwitterappv2.fragments.UserTimelineFragment;
import natemobile.apps.mytwitterappv2.interfaces.OnTweetItemSelected;
import natemobile.apps.mytwitterappv2.interfaces.ResultDataAPIListener;
import natemobile.apps.mytwitterappv2.models.Tweet;
import natemobile.apps.mytwitterappv2.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Profile Activity
 * 
 * To get TweetsListFragment to work, it requires ResultDataAPIListener and OnTweetItemSelected
 * since UserTimelineFragment requires it (extends from TweetsListFragment)
 * @author nkemavaha
 *
 */
public class ProfileActivity extends FragmentActivity implements ResultDataAPIListener, OnTweetItemSelected {

	
	TextView tvUserName;
	TextView tvTagline;
	TextView tvFollowers;
	TextView tvFollowings;
	ImageView ivProfileImg;
	
	///////////////////////
	/// Fragments
	//////////////////////
	
	ProfileFragment profileFragment;
	
	UserTimelineFragment userTimelineFragment;
	
	/////////////////////
	/// Internal data
	/////////////////////
	ArrayList<Tweet> userTweets;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		if ( savedInstanceState == null ) {
			profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentById( R.id.fragmentUserProfile );
			userTimelineFragment = (UserTimelineFragment) getSupportFragmentManager().findFragmentById( R.id.fragmentUserTimeline );
		}

		// retrieve data
		Intent data = getIntent();
		String screenName = data.getStringExtra( TimelineActivity.USER_SCREEN_NAME_KEY );
		
		// Fetch profile data from Twitter API
		initLoadProfile(screenName);
		
	}
	
	/**
	 * Load profile from Twitter API
	 * @param screenName	Twiter screen name
	 */
	private void initLoadProfile(String screenName) {
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonArrayData) {
				userTweets = Tweet.fromJson( jsonArrayData );
				boolean hasData = (userTweets != null && userTweets.size() > 0) ;
				
				if ( hasData ) {
					Tweet tweet= userTweets.get(0);
					populateProfileData( tweet.getUser(), jsonArrayData );
				}
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorObject) {				
				Log.d( "DEBUG", errorObject.toString() );
			}
		};
		
		// Prepare a request
		RequestParams request = new RequestParams("screen_name", screenName);
		MyTwitterApp.getRestClient().getUserTimeline(handler, request);
	}
	
	/**
	 * Helper function to populate data on ProfileActivity view and fragments
	 * @param userData
	 * @param rawJSONTweets
	 */
	private void populateProfileData(User userData, JSONArray rawJSONTweets ) {
		getActionBar().setTitle( "@" + userData.getScreenName() );
		profileFragment.populateData( userData );
		userTimelineFragment.populateData( rawJSONTweets );
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResponseReceived(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTweetItemSelect(Tweet tweet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkNetworkConnect() {
		// TODO Auto-generated method stub
		return false;
	}

}
