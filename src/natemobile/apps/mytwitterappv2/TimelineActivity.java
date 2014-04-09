package natemobile.apps.mytwitterappv2;

import natemobile.apps.mytwitterappv2.fragments.HomeTimelineFragment;
import natemobile.apps.mytwitterappv2.fragments.MentionsFragment;
import natemobile.apps.mytwitterappv2.models.Tweet;
import natemobile.apps.mytwitterappv2.models.User;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * TimelineActivity
 * 
 * Showing HomeTimeline and Mentioned tweets.
 * 
 * To get TweetsListFragment to work, it requires ResultDataAPIListener
 * 
 * @author nkemavaha
 *
 */
public class TimelineActivity extends AbstractTwitterFragmentActivity implements TabListener {
	public static final int COMPOSE_REQUEST_CODE = 101;
	public static final int COMPOSE_REQUEST_FAIL = -99;
	public static final int USERPROFILE_REQUEST_CODE = 102;
	
	public static final String USER_DATA_KEY = "userData";
	public static final String USER_SCREEN_NAME_KEY = "screen_name";
	
	public static final int INITIAL_TWEETS_TO_LOAD = 25;
	
	
	//////////////////////
	// Fragments
	//////////////////////
	
	/** HomeTimeline fragment */
	private HomeTimelineFragment homeTimelineFragment;
	
	/** Mentions fragment */
	private MentionsFragment mentionsFragment;
	
	///////////////////////
	/// Data fields
	//////////////////////	
	/**
	 * Current session user on this app
	 */
	private User currentSessionUser;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupCurrentSessionUserInfo();
		
		if ( savedInstanceState == null ) {
			homeTimelineFragment= new HomeTimelineFragment();
			mentionsFragment = new MentionsFragment();
		}
		
		setupNavigationTabs();
		
	}
	
	/**
	 * Setup navigation tabs and set fragment to each tab.
	 */
	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );
		actionBar.setDisplayShowTitleEnabled( true );
		Tab tabHome = actionBar.newTab().setText("").setTag( "HomeTimelineFragment" ).setIcon(R.drawable.ic_home)
				.setTabListener( this );
		Tab tabMention = actionBar.newTab().setText("").setTag( "MentionsFragment" ).setIcon( R.drawable.ic_mention )
				.setTabListener( this );
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMention);
		actionBar.selectTab( tabHome );
	}

	/**
	 * Helper function to request a current session User information from Twitter API.
	 */
	private void setupCurrentSessionUserInfo() {
		if ( currentSessionUser == null ) {
			JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject jsonObject ) {
					currentSessionUser = User.fromJson( jsonObject );
				}

				@Override
				public void onFailure(Throwable e, JSONObject errorObject) {				
					showLog( "Failure to get current session user info! " + errorObject.toString() );
				}	
			};

			MyTwitterApp.getRestClient().getCurrentUserVerifiedCredentials(handler);
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		boolean result = super.onOptionsItemSelected(item);
		switch ( item.getItemId() ) {
			// Compose a Tweet message
			case R.id.miCompose:
				openComposeActivity();
				result = true;
				break;
				
			// See a profile
			case R.id.miProfile:
				openProfileActivity( currentSessionUser.getScreenName() );
				result = true;
				break;
			default:
				break;
		}
		
		return result;
		
	}
	
	/**
	 * Helper function to handle open a profile activity.
	 * @param screenName	Screen name
	 */
	private void openProfileActivity(String screenName ) {
		Intent i = new Intent(getBaseContext(), ProfileActivity.class );
		i.putExtra( USER_SCREEN_NAME_KEY, screenName );	
		startActivity( i );
	}
	
	/**
	 * Helper function to handle open a compose activity.
	 * 
	 */
	private void openComposeActivity() {
		startActivityForResult( getIntentForComposeActivity() , COMPOSE_REQUEST_CODE);	
	}
	
	/**
	 * Helper function to get Intent object specifically for ComposeActivity
	 * @return Intent Object with current session user object.
	 */
	private Intent getIntentForComposeActivity() {
		Intent i = new Intent(getBaseContext(), ComposeActivity.class );
		i.putExtra( USER_DATA_KEY, currentSessionUser);	
		return i;
	}
	
	/** Callback when compose activity is returned. */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check if there is any internet connection, before sending request
		if ( checkNetworkConnect() == false ) {
			return;
		}
		
		if ( requestCode == COMPOSE_REQUEST_CODE ) {
			if ( resultCode == RESULT_OK ) {
				String message = data.getStringExtra("twitterMessage");
				
				if ( message.isEmpty() == false ) {
					
					JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
						
							@Override
							public void onSuccess(JSONObject update) {
								homeTimelineFragment.getAdapter().insert( Tweet.fromJson( update ), 0);
								homeTimelineFragment.getAdapter().notifyDataSetChanged();
								notifyOnToast( "Your status has been updated!");
							}
							
							@Override
							public void onFailure(Throwable e, JSONObject errorObject) {
								showLog( "Failed to post " + errorObject.toString() );
								
							}
					};
					
					MyTwitterApp.getRestClient().postStatusUpdate(handler, message);
				} else {
					notifyOnToast("Your message is blank!");	
				}
				// handle failed message
			} else if ( resultCode == COMPOSE_REQUEST_FAIL ){
				notifyOnToast("Your message characters exceeds limit!");
			}
		}
		
	}
	
	//////////////////////////////
	/// Debug Helper functions
	/////////////////////////////

	/** Flag indicate whether we should show a debug log info. */
	private boolean isOnProduction = false;
	
	/**
	 * Helper function for showing debug log so that we can toggle on/off
	 * @param message
	 */
	private void showLog( String message ) {
		if ( isOnProduction == false ) {
			Log.d("DEBUG", message);
		}
	}
	

    
    //////////////////////////////////////
    /// OnTab Selected Listeners
    /////////////////////////////////////

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		
		// use appropriate transaction for backward compatibility
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		
		if ( tab.getTag() == "HomeTimelineFragment") {
			// Set Framelayout container and replace it with HomeTimelineFragment
			fts.replace( R.id.frameContainer , homeTimelineFragment);
		} else {
			// Set Framelayout container and replace it with MentionsFragment
			fts.replace( R.id.frameContainer , mentionsFragment );
		}
		
		// commit and update changes to fragment
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	////////////////////////////////////////////////
	/// Listeners - listening event from fragments
	////////////////////////////////////////////////
	
	// When we receive response event from fragment and we need to show this result to user
	@Override
	public void onResponseReceived(String message) {
		notifyOnToast( message );
	}

	// When we received a response event from fragment and we need to switch to view activity
	@Override
	public void onTweetItemSelect(Tweet tweet) {
		Intent i = new Intent( getBaseContext(), ViewTweetActivity.class);
		i.putExtra( ViewTweetActivity.VIEW_TWEET_KEY, tweet);
		startActivity(i);
	}
	
	public void onImageClick(View v) {
		Log.d("DEBUG", v.getTag().toString());
		openProfileActivity( v.getTag().toString() );
	}


}
