package pl.biltech.httpshare.httpd.manager.file;

/**
 * Temp file manager.
 * <p/>
 * <p>
 * Temp file managers are created 1-to-1 with incoming requests, to create
 * and cleanup temporary files created as a result of handling the request.
 * </p>
 */
public interface TempFileManager {

    void clear();

    public TempFile createTempFile(String filename_hint) throws Exception;
}
