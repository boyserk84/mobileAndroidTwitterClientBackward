package natemobile.apps.mytwitterappv2.adapters;

import java.util.List;

import natemobile.apps.mytwitterappv2.ProfileActivity;
import natemobile.apps.mytwitterappv2.R;
import natemobile.apps.mytwitterappv2.TimelineActivity;
import natemobile.apps.mytwitterappv2.models.Tweet;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * TweetAdapter
 * 
 * Binding Tweet object data with view.
 * @author nkemavaha
 *
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {
	
	/**
	 * Constructor
	 * @param context
	 * @param tweets
	 */
	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		// If view doesn't exist, create a new one
		if ( view == null ) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}
		
		Tweet tweet = getItem( position );
		
		ImageView ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage( tweet.getUser().getProfileImage(), ivProfile );
		// Save information so that it is being clicked we know what to pass to activity
		ivProfile.setTag( tweet.getUser().getScreenName() );
		
		// TODO: 2nd iteration: Use HTML text so that we click on URL link if any
		
		TextView tvName = (TextView) view.findViewById( R.id.tvName );
		tvName.setText( tweet.getUser().getName() );
		
		TextView tvBody = (TextView) view.findViewById( R.id.tvBody );
		tvBody.setText( tweet.getBody() );
		
		TextView tvTimeStamp = (TextView) view.findViewById( R.id.tvTimeStamp );
		tvTimeStamp.setText( tweet.getRelativeTimeStamp( getContext() ) );
		
		// Listener when click on a profile image
		ivProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
				profileIntent.putExtra(TimelineActivity.USER_SCREEN_NAME_KEY, (String) v.getTag());
				getContext().startActivity( profileIntent );
				
			}
		});
		
		return view;
		
	}
	

}

