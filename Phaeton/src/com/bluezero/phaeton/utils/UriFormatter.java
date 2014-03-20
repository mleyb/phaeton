package com.bluezero.phaeton.utils;

public class UriFormatter {
	public static String formatUriForResource(String resourceUriBase, int resourceId) {
		return String.format((resourceUriBase + "/%s"), resourceId);
	}
	
	public static String formatUriForResource(String resourceUriBase, int resourceId, String subResource) {
		return String.format((resourceUriBase + "/%s/" + subResource), resourceId);
	}

	public static String formatUriForResource(String resourceUriBase, String resourceId) {
		return String.format((resourceUriBase + "/%s"), resourceId);
	}	
	
	public static String formatNewChildRegistrationUri(String uriBase, String registrationCode) {
		return String.format((uriBase + "/%s"), registrationCode.toString());
	}
}
