package natemobile.apps.mytwitterappv2;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class MyTwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "he1jodVpwHuQLQEoLawRQ";       // Change this
    public static final String REST_CONSUMER_SECRET = "aakbAIrbsdYj8Oh3EymCgc1esn8AtikSSHV9qBDfxg"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://mytwitterapp"; // Change this (here and in manifest)
    
    
    public static final String TWITTER_DATETIME_FORMAT = "EEE MMM d HH:mm:ss Z yyyy";
    
    public MyTwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here
    public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }
    
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
    
    private int count = 0;
    
    /**
     * Gets Home Timeline tweets API
     * @param handler		callback when response is received.
     * @param params		Request parameters object to pass in
     */
    public void getHomeTimeline(AsyncHttpResponseHandler handler, RequestParams params) {
    	String url = getApiUrl("/statuses/home_timeline.json");
    	
    	if ( count > 10 ) {
    		Log.d("DEBUG", "Exceed limit of " + count);
    	}
    	
    	client.get( url , params, handler);
    	count++;

    }
    
    public void getMentionsTimeline(AsyncHttpResponseHandler handler, RequestParams params) {
    	String url = getApiUrl("/statuses/mentions_timeline.json");
    	client.get( url , params, handler);
    }
    
    /**
     * Gets a current session user's verified credentials (i.e. this will fetch basic account info as well)
     * @param handler	callback when response is received
     */
    public void getCurrentUserVerifiedCredentials(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("/account/verify_credentials.json");
    	client.get( url , null, handler);
    }
    
    /**
     * Gets a given user's Twitter timeline data 
     * @param handler	callback when response is received
     * @param params	Request parameters object to pass in
     */
    public void getUserTimeline(AsyncHttpResponseHandler handler, RequestParams params) {
    	String url = getApiUrl("/statuses/user_timeline.json");
    	client.get( url , params, handler);
    }
    
    /**
     * Post an update to tweet (post a new tweet)
     * @param handler		Callback when response is received.
     * @param message		Updated/Tweet message
     */
    public void postStatusUpdate(AsyncHttpResponseHandler handler, String message) {
    	String url = getApiUrl("/statuses/update.json");
    	RequestParams params = new RequestParams("status", message);
    	client.post( url, params, handler);
    }
}