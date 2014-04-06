package natemobile.apps.mytwitterappv2.fragments;

import com.nostra13.universalimageloader.core.ImageLoader;

import natemobile.apps.mytwitterappv2.R;
import natemobile.apps.mytwitterappv2.interfaces.OnTweetItemSelected;
import natemobile.apps.mytwitterappv2.interfaces.ResultDataAPIListener;
import natemobile.apps.mytwitterappv2.models.User;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ProfileFragment
 * @author nkemavaha
 *
 */
public class ProfileFragment extends Fragment {
	
	//////////////////
	// UI Elements
	/////////////////
	
	private TextView tvProfileName;
	
	private TextView tvProfileTagline;
	
	private TextView tvFollowers;
	
	private TextView tvFollowings;
	
	private ImageView ivProfileImage;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_profile, container, false);			
		setupViews( view );
		return view;
	}
	
	/**
	 * Setup views of this fragment 
	 * @param view
	 */
	private void setupViews(View view) {
		tvProfileName = (TextView) view.findViewById( R.id.tvProfileName );
		tvProfileTagline = (TextView) view.findViewById( R.id.tvProfileBody );
		tvFollowings = (TextView) view.findViewById( R.id.tvFollowings );
		tvFollowers = (TextView) view.findViewById( R.id.tvFollowers );
		ivProfileImage = (ImageView) view.findViewById( R.id.ivProfileImage );	
	}
	
	public void populateData(Object data ) {
		if ( data instanceof User) {
			User userData = (User) data;
			tvProfileName.setText( "@" + userData.getScreenName() );	
			tvProfileTagline.setText( userData.getTagline() );
			tvFollowers.setText( userData.getFollowersCount() + " followers" );
			tvFollowings.setText( userData.getFriendsCount() + " followings" );
			ImageLoader.getInstance().displayImage( userData.getProfileImage(), ivProfileImage );
		}
	}
}
