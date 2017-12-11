package pl.biltech.httpshare.httpd.manager.file;

/**
 * Factory to create temp file managers.
 */
public interface TempFileManagerFactory {

    public TempFileManager create();
}
