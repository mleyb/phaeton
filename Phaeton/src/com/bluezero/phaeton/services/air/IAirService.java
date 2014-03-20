package com.bluezero.phaeton.services.air;

import org.json.JSONException;
import org.json.JSONObject;

public interface IAirService {
	String httpGetString(String token, String uri) throws HttpAuthenticationException, JSONException;

	JSONObject httpGetJSONObject(String token, String uri) throws HttpAuthenticationException, JSONException;
	
	<T> T httpGetObject(String token, String uri, Class<T> classOfT) throws HttpAuthenticationException, JSONException;	
	
	void httpPost(String token, String uri) throws HttpAuthenticationException, RuntimeException;
	
	String httpPostString(String token, String uri, String requestBodyString) throws HttpAuthenticationException, JSONException;
	
	JSONObject httpPostJSONObject(String token, String uri, JSONObject requestBody) throws HttpAuthenticationException, JSONException;

	<T> T httpPostObject(String token, String uri, Object requestBody,  Class<T> responseClassOfT) throws HttpAuthenticationException, JSONException;
	
	void httpDelete(String token, String uri) throws HttpAuthenticationException;
}
