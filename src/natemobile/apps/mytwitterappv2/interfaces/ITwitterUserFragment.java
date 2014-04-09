package natemobile.apps.mytwitterappv2.interfaces;

/**
 * ITwitterUserFragment
 * 
 * Interface for TwitterUserFragment.
 * 
 * This is used for passing data to fragment from activity. 
 * 
 * NOTE:
 * However, this is not useful if fragment needs to static or Async data.
 * @author nkemavaha
 *
 */
public interface ITwitterUserFragment {

	/**
	 * Populate Data from raw JSONArray Twitter data
	 * @param rawJSONData	Data in JSON format, could be Array or Object.
	 */
	public void populateData(Object rawJSONData );
}
