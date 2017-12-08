package pl.biltech.httpshare.httpd;

/**
 * Default strategy for creating and cleaning up temporary files.
 */
class DefaultTempFileManagerFactory implements TempFileManagerFactory {

    @Override
    public TempFileManager create() {
        return new DefaultTempFileManager();
    }
}
