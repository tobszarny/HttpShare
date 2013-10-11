package pl.biltech.httpshare.util;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.Before;
import org.junit.Test;

/**
 * @author tomek, bilu
 * 
 */
public class NetworkUtilTest {

	private NetworkUtil networkUtil;

	@Before
	public void setUp() {
		networkUtil = spy(new NetworkUtil());
	}
	
	@Test
	public void shouldReturnSelectedPortIfItsEmpty() throws IOException {
		// given
		int startFrom = 80;
		ServerSocket servletSocket = mock(ServerSocket.class);
		when(networkUtil.getServerSocket(startFrom)).thenReturn(servletSocket);

		// when
		int port = networkUtil.findFirstFreePort(startFrom);

		// then
		assertThat(port).isEqualTo(startFrom);
	}

	@Test
	public void shouldReturnCorrectPortAfter3UnsuccessfulAttempts() throws IOException {
		// given
		NetworkUtil networkUtil = spy(new NetworkUtil());
		int startFrom = 90;
		for (int i = 0; i < 3; i++) {
			when(networkUtil.getServerSocket(startFrom + i)).thenThrow(new IOException());
		}

		// when
		int port = networkUtil.findFirstFreePort(startFrom);

		// then
		assertThat(port).isEqualTo(startFrom + 3);
	}
}
