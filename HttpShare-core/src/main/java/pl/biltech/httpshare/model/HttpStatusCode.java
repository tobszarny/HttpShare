package pl.biltech.httpshare.model;

/**
 * Based on a public available information on Wiki
 * @author tomek
 *
 */
public enum HttpStatusCode {
	// 2XX
	OK(200, "OK"),
	Created(201, "Created"),
	Accepted(202, "Accepted"),
	NonAuthoritativeInformation(203, "Non-Authoritative Information (since HTTP/1.1)"),
	NoContent(204, "No Content"),
	ResetContent(205, "Reset Content"),
	PartialContent(206, "Partial Content"),
	MultiStatus(207, "Multi-Status (WebDAV; RFC 4918)"),
	AlreadyReported(208, "Already Reported (WebDAV; RFC 5842)"),
	IMUsed(226, "IM Used (RFC 3229)"),
	
	
	//3XX
	MultipleChoices(300, "Multiple Choices"),
	MovedPermanently(301, "Moved Permanently"),
	Found(302, "Found"),
	SeeOther(303, "See Other (since HTTP/1.1)"),
	NotModified(304, "Not Modified"),
	UseProxy(305, "Use Proxy (since HTTP/1.1)"),
	SwitchProxy(306, "Switch Proxy"),
	TemporaryRedirectHTTP1_1(307, "Temporary Redirect (since HTTP/1.1)"),
	TemporaryRedirectRFC(308, "Permanent Redirect (approved as experimental RFC)");
	
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
