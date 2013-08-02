package pl.biltech.httpshare.view.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NetworkUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	// TODO: improve test
	@Test
	public void testFindFirstFreePort() throws Exception {
		int port = NetworkUtil.findFirstFreePort(80);

		Assert.assertTrue(port >= 80);
	}

}
