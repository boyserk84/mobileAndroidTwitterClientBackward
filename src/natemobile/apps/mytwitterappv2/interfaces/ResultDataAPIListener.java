package natemobile.apps.mytwitterappv2.interfaces;

/**
 * Interface ResultDataAPIListener
 * 
 * This would help communicate with the main activity about response we receive and network status.
 * @author nkemavaha
 *
 */
public interface ResultDataAPIListener {
	
	void onResponseReceived( String message );
	
	boolean checkNetworkConnect();

}
