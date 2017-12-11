package pl.biltech.httpshare.httpd.manager.file;

import java.io.OutputStream;

/**
 * A temp file.
 * <p/>
 * <p>
 * Temp files are responsible for managing the actual temporary storage and
 * cleaning themselves up when no longer needed.
 * </p>
 */
public interface TempFile {

    public void delete() throws Exception;

    public String getName();

    public OutputStream open() throws Exception;
}
