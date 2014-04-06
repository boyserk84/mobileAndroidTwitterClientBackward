package natemobile.apps.mytwitterappv2.interfaces;

public interface ResultDataAPIListener {
	
	void onResponseReceived( String message );
	
	boolean checkNetworkConnect();

}
