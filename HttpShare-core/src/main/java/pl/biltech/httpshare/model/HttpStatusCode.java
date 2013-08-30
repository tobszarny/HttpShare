package pl.biltech.httpshare.model;

/**
 * Based on a public available information on Wiki
 * @author tomek
 *
 */
public enum HttpStatusCode {
	// 2XX
	OK(200, "OK"),
	CREATED(201, "Created"),
	ACCEPTED(202, "Accepted"),
	NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information (since HTTP/1.1)"),
	NO_CONTENT(204, "No Content"),
	RESET_CONTENT(205, "Reset Content"),
	PARTIAL_CONTENT(206, "Partial Content"),
	MULTI_STATUS(207, "Multi-Status (WebDAV; RFC 4918)"),
	ALREADY_REPORTED(208, "Already Reported (WebDAV; RFC 5842)"),
	IM_USED(226, "IM Used (RFC 3229)"),
	
	
	//3XX
	MULTIPLE_CHOICES(300, "Multiple Choices"),
	MOVED_PERMANENTLY(301, "Moved Permanently"),
	FOUND(302, "Found"),
	SEE_OTHER(303, "See Other (since HTTP/1.1)"),
	NOT_MODIFIED(304, "Not Modified"),
	USE_PROXY(305, "Use Proxy (since HTTP/1.1)"),
	SWITCH_PROXY(306, "Switch Proxy"),
	TEMPORARY_REDIRECT_HTTP_1_1(307, "Temporary Redirect (since HTTP/1.1)"),
	TEMPORARY_REDIRECT_RFC(308, "Permanent Redirect (approved as experimental RFC)");
	
	private String nameDescription;
	private int code;
	
	private HttpStatusCode(int code, String nameDescription) {
		this.nameDescription = nameDescription;
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

	public String getNameDescription() {
		return nameDescription;
	}
	
	
	
}
