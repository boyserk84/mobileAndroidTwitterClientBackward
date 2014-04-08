package natemobile.apps.mytwitterappv2.interfaces;

import natemobile.apps.mytwitterappv2.models.Tweet;

/**
 * OnTweetItemSelected Interface
 * 
 * Listener interface for listen for tweet item being clicked.
 * @author nkemavaha
 *
 */
public interface OnTweetItemSelected {

	/**
	 * Callback when tweet item is being selected
	 * @param tweet		Tweet Object
	 */
	public void onTweetItemSelect(Tweet tweet);
}
