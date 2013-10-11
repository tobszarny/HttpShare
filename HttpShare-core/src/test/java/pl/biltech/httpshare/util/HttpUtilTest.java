package pl.biltech.httpshare.util;

import java.io.InputStream;

import lombok.Cleanup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author tomek, bilu
 * 
 */
public class HttpUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	@Ignore
	// TODO finish me
	public void testGetBoundary() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testGetContentDispositionFilename() throws Exception {
		@Cleanup
		InputStream fileStream = StreamUtil
				.getFileStream("/http/header-content-disposition.part");

		String header = StreamUtil.readAll(fileStream);

		String filename = HttpUtil.getContentDispositionFilename(header);

		Assert.assertEquals("testFile.txt", filename);
	}

}
