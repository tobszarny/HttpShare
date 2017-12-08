package pl.biltech.httpshare.httpd;

/**
 * Factory to create temp file managers.
 */
public interface TempFileManagerFactory {

    public TempFileManager create();
}
