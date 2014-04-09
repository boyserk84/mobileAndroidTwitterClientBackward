package natemobile.apps.mytwitterappv2;

import natemobile.apps.mytwitterappv2.interfaces.OnTweetItemSelected;
import natemobile.apps.mytwitterappv2.interfaces.ResultDataAPIListener;
import natemobile.apps.mytwitterappv2.utils.NetworkUtils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * AbstractTwitterFragmentActivity
 * 
 * Contains common functionality that can be used across multiple FragmentActivity
 * 
 * @author nkemavaha
 *
 */
public abstract class AbstractTwitterFragmentActivity extends FragmentActivity implements ResultDataAPIListener, OnTweetItemSelected {

	// When Internet connection is lost, notifying user.
	public boolean checkNetworkConnect() {
		boolean result = false;
		if ( NetworkUtils.isNetworkAvailable( (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE) ) ) {
			result = true;
		} else {
			notifyOnToast("Error: No Internet connection!");
			result = false;
		}
		return result;
	}
	
	/**
	 * Helper function to display a toast message
	 * @param msg		Message to display
	 */
	protected void notifyOnToast(String msg) {
		Toast.makeText( this, msg, Toast.LENGTH_SHORT).show();	
	}
}
