package pl.biltech.httpshare.view.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.Headers;

/**
 * @author tomek
 * 
 */
@SuppressWarnings("restriction")
public class HttpUtil {

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	private static final Logger logger = LoggerFactory
			.getLogger(HttpUtil.class);
	private static final String CONTENT_TYPE = "Content-type";
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String SQUARE_PARENTHESIS_PATTERN = "[\\[\\]]";

	public static String getBoundary(Headers headers) {

		logger.debug("getBoundary called");
		// Headers requestHeaders = exchange.getRequestHeaders();

		// for (Object key : headers.keySet()) {
		// logger.debug(key.toString() + " = "
		// + headers.get(key).toString());
		// }

		String contentType = headers.get(CONTENT_TYPE).toString()
				.replaceAll(SQUARE_PARENTHESIS_PATTERN, " ").trim();

		String boundary = contentType.split(";")[1].trim();
		boundary = boundary.split("=")[1];

		logger.debug("bondary = " + boundary);

		return boundary;

	}

	public static String getContentDispositionFilename(String header) {
		String[] headerLines = header.split(LINE_SEPARATOR);

		logger.debug("headerLines = " + headerLines.length);

		for (String headerLine : headerLines) {
			if (headerLine.contains(CONTENT_DISPOSITION)) {
				String content = headerLine.split(":")[1];
				String[] contentParts = content.split(";");
				for (String contentPart : contentParts) {
					if (contentPart.contains("filename")) {
						String name = contentPart.split("=")[1];
						name = name.replaceAll("\\\"", " ").trim();
						return name;
					}
				}

			}
		}

		return "";
	}

}
