package com.bluezero.phaeton.services.air;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.bluezero.phaeton.Constants;
import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;

public class AirService extends Service implements IAirService {	
	private static final String TAG = AirService.class.getSimpleName();
	
	private static final int ConnectionTimeoutMs = (20 * 1000);
	
	private final IBinder _binder = new AirServiceBinder();
	
	private HttpClient _httpClient;
	
	public class AirServiceBinder extends Binder {
		public IAirService getService() {
            return AirService.this;
        }
    }
	
    @Override
    public IBinder onBind(Intent arg0) {
          return _binder;
    }
    
    @Override
    public void onCreate() {
          super.onCreate();
          
          _httpClient = createHttpClient();
          
          Log.v(TAG, "Service created");
    }
    
    @Override
    public void onDestroy() {
          super.onDestroy();
          
          _httpClient.getConnectionManager().shutdown();
          
          Log.v(TAG, "Service destroyed");
    }                 
    
	public String httpGetString(String token, String uri) throws HttpAuthenticationException, JSONException {
		String responseBodyString = null;		    

        try {
    		HttpGet httpGet = new HttpGet(uri);
    		
    		httpGet.setHeader(new BasicHeader("Authorization", "Bearer " + token));
    		httpGet.setHeader("Accept-Encoding", "gzip");    		
    		    		
    		// send the request and receive the response
    		HttpResponse response = getResponseForRequest(_httpClient, httpGet);
    		         
    		checkResponse(response);
    		            
            HttpEntity entity = response.getEntity();
            
            if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
				
				Header contentEncoding = response.getFirstHeader("Content-Encoding");
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				    instream = new GZIPInputStream(instream);
				}
				
				// convert content stream to a String
				String resultString = convertStreamToString(instream);								
				
				instream.close();
				
				if (resultString.length() > 0) {				
					responseBodyString = resultString;
					
					// raw DEBUG output of our received JSON
					Log.i(TAG, "Received response: " + responseBodyString);
				}
				else {
					Log.e(TAG, "No response content received");
				}
			}                                 
        }        
        catch (IOException e) {
        	Log.e(TAG, "There was an IO stream related error", e);
        	BugSenseHandler.sendException(e);
        	throw new RuntimeException(e);
		}
        
        return responseBodyString;
	}	

	public JSONObject httpGetJSONObject(String token, String uri) throws HttpAuthenticationException, JSONException {
		String responseBodyString = httpGetString(token, uri);
		
		if (responseBodyString != null) {
			return new JSONObject(responseBodyString);
		}
		else {
			return null;
		}			
	}
	
	public <T> T httpGetObject(String token, String uri, Class<T> classOfT) throws HttpAuthenticationException, JSONException {
		String responseBodyString = httpGetString(token, uri);
		
		if (responseBodyString != null) {		
			return new Gson().fromJson(responseBodyString, classOfT);
		}
		else { 
			return null; 
		}
	}
	
	public void httpPost(String token, String uri) throws HttpAuthenticationException, RuntimeException {
		try {
			httpPostString(token, uri, null);
		} 
		catch (JSONException e) {
			// do nothing
		} 
	}
	
	public String httpPostString(String token, String uri, String requestBodyString) throws HttpAuthenticationException, JSONException, RuntimeException {
		String responseBodyString = null;		
				
		try {
			HttpPost httpPost = new HttpPost(uri);

			httpPost.setHeader(new BasicHeader("Authorization", "Bearer " + token));
			
			// Set HTTP parameters
			if (requestBodyString != null) {
				httpPost.setEntity(new StringEntity(requestBodyString));
			}
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-Type", "application/json");						
			
			// send the request and receive the response
    		HttpResponse response = getResponseForRequest(_httpClient, httpPost);
			
    		checkResponse(response);
			
			// get hold of the response entity (-> the data):
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
													
				Header contentEncoding = response.getFirstHeader("Content-Encoding");
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				    instream = new GZIPInputStream(instream);
				}						
				
				// convert content stream to a String
				String resultString = convertStreamToString(instream);				
				
				instream.close();
				
				if (resultString.length() > 0) {				
					responseBodyString = resultString;
					
					// raw DEBUG output of our received JSON object:
					Log.i(TAG, responseBodyString);
				}
				else {
					Log.i(TAG, "No response content received");
				}
			} 
		}
		catch (IOException e)	{
			Log.e(TAG, "There was an IO stream related error", e);	
			BugSenseHandler.sendException(e);
			throw new RuntimeException(e);
		}
		
		return responseBodyString;
	}
	
	public JSONObject httpPostJSONObject(String token, String uri, JSONObject requestBody) throws HttpAuthenticationException, JSONException {
		String responseBodyString = httpPostString(token, uri, requestBody.toString());
		
		if (responseBodyString != null) {
			return new JSONObject(responseBodyString);
		}
		else {
			return null;
		}			
	}

	public <T> T httpPostObject(String token, String uri, Object requestBody,  Class<T> responseClassOfT) throws HttpAuthenticationException, JSONException {				
		String requestBodyString = new Gson().toJson(requestBody);		
		
		String responseBodyString = httpPostString(token, uri, requestBodyString);			
		
		if (responseBodyString != null) {							
			T t = new Gson().fromJson(responseBodyString, responseClassOfT);
		
			return t;
		}
		else { 
			return null; 
		}
	}

	public void httpDelete(String token, String uri) throws HttpAuthenticationException {
		HttpDelete httpDelete = new HttpDelete(uri);			
		
		httpDelete.setHeader(new BasicHeader("Authorization", "Bearer " + token));		
		
		try {							
			HttpResponse response = _httpClient.execute(httpDelete);
			
			checkResponse(response);
		}
		catch (IOException e)	{
			Log.e(TAG, "There was an IO stream related error", e);		
			BugSenseHandler.sendException(e);
			throw new RuntimeException(e);
		}
	}
		
	private DefaultHttpClient createHttpClient() {
		HttpParams httpParams = new BasicHttpParams();		
		HttpConnectionParams.setConnectionTimeout(httpParams, ConnectionTimeoutMs);
		HttpConnectionParams.setSoTimeout(httpParams, ConnectionTimeoutMs);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
			       
        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(5, false) {      
  	        public boolean retryRequest(IOException ex, int execCount, HttpContext context) {
  	            if (!super.retryRequest(ex, execCount, context)) {   
  	               Log.i("HTTP retry-handler", "Won't retry");
  	               return false;
  	            }
  	            try {
  	               Thread.sleep(2000);                     
  	            } 
  	            catch (InterruptedException e) {
  	            	BugSenseHandler.sendException(e);
  	            }
  	            
  	            Log.i("HTTP retry-handler", "Retrying request...");
  	            return true;
  	        }
        };
        
        httpClient.setHttpRequestRetryHandler(retryHandler);
        
        return httpClient;
	}
		
	private HttpResponse getResponseForRequest(HttpClient client, HttpUriRequest request) throws IOException, HttpAuthenticationException {
		HttpResponse response = null;
		
		try {		
			long t = System.currentTimeMillis();
			
			// send the request and receive the response
			response = client.execute(request);
			
			Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis() - t) + "ms]");
			Log.i(TAG, "HTTPResponse status code was " + response.getStatusLine().getStatusCode());
		}
		catch (ClientProtocolException e) {        	
        	Log.e(TAG, "There was a protocol based error", e);
        	BugSenseHandler.sendException(e);
        	
        	// TODO - treat this as a bad authentication until I can figure out why this
        	//		  happens on bad credentials??
			throw new HttpAuthenticationException(Constants.Errors.HTTP_AUTHENTICATION);
        }
		
		return response;
	}
	
	private void checkResponse(HttpResponse response) throws HttpAuthenticationException, ParseException, IOException {
		Log.d(TAG, "Got status code " + response.getStatusLine().getStatusCode() + " for response");
		
    	if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				throw new HttpAuthenticationException(Constants.Errors.HTTP_AUTHENTICATION);
			}
			else {
				HttpEntity entity = response.getEntity();
	            
				String message = null;
				
				try {				
		            if (entity != null) {	           
		            	String responseBodyString = EntityUtils.toString(entity);
		            	
		            	if (responseBodyString.startsWith("[")) {
		            		JSONArray jsonArray = new JSONArray(responseBodyString);
		            		if (jsonArray.length() > 0) {
		            			JSONObject jsonObject = jsonArray.getJSONObject(0);		            			
		            			if (jsonObject.has("Message")) {
			            			message = jsonObject.getString("Message");
			            		}
		            		}
		            	}
		            	else if (responseBodyString.startsWith("{")) {
		            		JSONObject jsonObject = new JSONObject(responseBodyString);
		            		if (jsonObject.has("Message")) {
		            			message = jsonObject.getString("Message");
		            		}
		            	}
		            	else {
		            		message = responseBodyString;
		            	}
		            }
				}
				catch (Exception e) {
					Log.e(TAG, e.toString());
					BugSenseHandler.sendException(e);
				}
				
				throw new RuntimeException((message != null ? message : "An error occurred."));
			}
		}
    }
	
	private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        } 
        finally 
        {
            try {
                is.close();
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return sb.toString();
    }
}