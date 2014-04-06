package natemobile.apps.mytwitterappv2.interfaces;

import natemobile.apps.mytwitterappv2.models.Tweet;

/**
 * OnTweetItemSelected Interface
 * Listener interface for listen for tweet item being clicked.
 * @author nkemavaha
 *
 */
public interface OnTweetItemSelected {

	public void onTweetItemSelect(Tweet tweet);
}
