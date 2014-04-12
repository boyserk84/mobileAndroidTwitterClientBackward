package natemobile.apps.mytwitterappv2.adapters;

import natemobile.apps.mytwitterappv2.fragments.HomeTimelineFragment;
import natemobile.apps.mytwitterappv2.fragments.MentionsFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * TweetPageAdapter
 * 
 * Adapter for viewpager (swiping between pages)
 * @author nkemavaha
 *
 */
public class TweetPageAdapter extends SmartFragmentStatePagerAdapter  {

	private static int NUM_ITEMS = 2;
	
	/** Constructor */
	public TweetPageAdapter(FragmentManager fm) {
		super(fm);
	}


	@Override
	public Fragment getItem(int position) {
		switch ( position ) {
			case 0:
				return HomeTimelineFragment.newInstance(0);
			case 1:
				return MentionsFragment.newInstance(1);
			default:
				return null;
		}
	}

	@Override
	public int getCount() {
		return NUM_ITEMS;
	}

}
