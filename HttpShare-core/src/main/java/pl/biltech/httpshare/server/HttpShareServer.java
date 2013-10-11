package pl.biltech.httpshare.server;

import java.io.File;
import java.io.IOException;

public interface HttpShareServer {

	public void start() throws IOException;

	public void stop();

	public boolean isServerRunning();

	void addFileToDownload(File file) throws IOException;

	public String getFileToDownloadUrl();


}
