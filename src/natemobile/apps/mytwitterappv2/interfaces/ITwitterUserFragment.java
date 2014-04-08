package natemobile.apps.mytwitterappv2.interfaces;

/**
 * ITwitterUserFragment
 * 
 * Interface for TwitterUserFragment
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
