package natemobile.apps.mytwitterappv2.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	
    /**
     * Helper function to help detect if there is internet available
     * Reference & credits: http://stackoverflow.com/questions/4238921/android-detect-whether-there-is-an-internet-connection-available
     * @param	manager		Android's ConnectivityManager
     * @return True if connect to the network. Otherwise, false is returned;
     */
    public static boolean isNetworkAvailable(ConnectivityManager manager) {
        ConnectivityManager connectivityManager  = manager;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
