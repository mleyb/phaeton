package com.bluezero.phaeton;

public class License {
	public static String getPublicKey() {
		return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgDMJGyFbRsftfwRMbbw3CImtmgxW7JyrbNS47cxyiv3xrRod2mg5dq/GWEMWB84TkmQC6Ye6mEFH7GWq9x1C9V6EwA3DiQw1+3Kv4uIGomNLhbdASXqKoe1s5Mas/Z7OGGv6nl2BE+Aps6mdqo747OcOqVuJsZkpdoOsoNiSlCDYWpIrDVQqC1eYOX68IzAx2kng8lFuRevamdpkR1jjgTP/r43dQgNvbE4j/j0iihekJFJA7FRHi0LDZcLAF9CRHQ/PQAjACIJnTFIc2xAhgjzeh+L0PkAOL5wNMlEOI9ZbrMmODZHlVDgetDm1wSR7r22ojy3fFnqRnMhWVSUM+QIDAQAB";
	}
	
	public static byte[] getSalt() {
		return new byte[] { -46, 63, 30, -128, -103, -57, 74, -64, 51, 88, -95, -45, 77, -117, -36, -113, -11, 32, -64, 89 };
	}
}
