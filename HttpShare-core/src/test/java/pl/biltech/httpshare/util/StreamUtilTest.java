package pl.biltech.httpshare.util;

import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author tomek
 * 
 */
public class StreamUtilTest {

	@Test
	public void testGetFileStream() throws Exception {
		InputStream fileStream = StreamUtil
				.getFileStream("/files/some.properties");

		StringWriter writer = new StringWriter();
		IOUtils.copy(fileStream, writer, "utf-8");
		String theString = writer.toString();

		Assert.assertEquals("greetings=hello", theString);
	}

}
