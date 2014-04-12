package natemobile.apps.mytwitterappv2.listeners;

import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.app.Fragment ;
import android.support.v4.app.FragmentActivity;

/**
 * This version supports for API at least 14.
 * 
 * This helps save memoery when switching between tabs.
 * 
 * Reference and credits to 
 * https://gist.github.com/nesquena/8b9f9ec29582afd4d138
 *
 * @param <T>
 */
public class SupportFragmentTabListener<T extends Fragment> implements TabListener {
	private Fragment mFragment;
	private final FragmentActivity mActivity;
	private final String mTag;
	private final Class<T> mClass;
	private final int mfragmentContainerId;
 
	public SupportFragmentTabListener(FragmentActivity activity, String tag, Class<T> clz) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
		mfragmentContainerId = android.R.id.content;
	}
 
	public SupportFragmentTabListener(int fragmentContainerId, FragmentActivity activity, String tag, Class<T> clz) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
		mfragmentContainerId = fragmentContainerId;
	}
 

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@SuppressLint("NewApi")
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction sft) {
		// Check if the fragment is already initialized
		if (mFragment == null) {
			// If not, instantiate and add it to the activity
			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			sft.add( mfragmentContainerId, mFragment, mTag);
		} else {
			// If it exists, simply attach it in order to show it
			sft.attach( mFragment );
		}
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
