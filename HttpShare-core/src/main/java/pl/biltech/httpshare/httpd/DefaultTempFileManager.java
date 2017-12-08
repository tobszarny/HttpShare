package pl.biltech.httpshare.httpd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Default strategy for creating and cleaning up temporary files.
 * <p/>
 * <p>
 * This class stores its files in the standard location (that is, wherever
 * <code>java.io.tmpdir</code> points to). Files are added to an internal
 * list, and deleted when no longer needed (that is, when
 * <code>clear()</code> is invoked at the end of processing a request).
 * </p>
 */
public class DefaultTempFileManager implements TempFileManager {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTempFileManager.class);

    private final File tmpdir;

    private final List<TempFile> tempFiles;

    public DefaultTempFileManager() {
        this.tmpdir = new File(System.getProperty("java.io.tmpdir"));
        if (!tmpdir.exists()) {
            tmpdir.mkdirs();
        }
        this.tempFiles = new ArrayList<TempFile>();
    }

    @Override
    public void clear() {
        for (TempFile file : this.tempFiles) {
            try {
                file.delete();
            } catch (Exception ignored) {
                LOG.warn("could not delete file ", ignored);
            }
        }
        this.tempFiles.clear();
    }

    @Override
    public TempFile createTempFile(String filename_hint) throws Exception {
        DefaultTempFile tempFile = new DefaultTempFile(this.tmpdir);
        this.tempFiles.add(tempFile);
        return tempFile;
    }
}
