package natemobile.apps.mytwitterappv2;

import com.nostra13.universalimageloader.core.ImageLoader;

import natemobile.apps.mytwitterappv2.models.Tweet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ViewTweetActivity
 * 
 * View a Tweet message activity
 * @author nkemavaha
 *
 */
public class ViewTweetActivity extends Activity {
	
	public static final String VIEW_TWEET_KEY = "view_tweet";

	////////////////////////
	/// UI elements
	////////////////////////
	
	TextView tvScreenName;
	
	TextView tvTimeStamp;
	
	TextView tvMessage;
	
	ImageView ivProfileImage;
	
	private Tweet tweet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_tweet);
		setupViews();
		
		Intent data = getIntent();
		tweet = (Tweet) data.getSerializableExtra( VIEW_TWEET_KEY );
		
		tvScreenName.setText( "@" + tweet.getUser().getScreenName() );
		tvTimeStamp.setText( tweet.getRelativeTimeStamp( this.getBaseContext() ));
		tvMessage.setText( tweet.getBody() );
		
		ImageLoader.getInstance().displayImage( tweet.getUser().getProfileImage() , ivProfileImage );
		
	}
	
	/** Setup views */
	private void setupViews() {
		tvScreenName = (TextView) findViewById(R.id.tvViewTwitterName);
		tvMessage = (TextView) findViewById(R.id.tvViewTweetMessage);
		tvTimeStamp = (TextView) findViewById(R.id.tvViewTwitterDate);
		ivProfileImage = (ImageView) findViewById(R.id.ivViewProfileImage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_tweet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}

}
