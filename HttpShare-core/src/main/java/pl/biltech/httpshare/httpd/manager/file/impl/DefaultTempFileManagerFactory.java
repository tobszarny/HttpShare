package pl.biltech.httpshare.httpd.manager.file.impl;

import pl.biltech.httpshare.httpd.manager.file.TempFileManager;
import pl.biltech.httpshare.httpd.manager.file.TempFileManagerFactory;

/**
 * Default strategy for creating and cleaning up temporary files.
 */
public class DefaultTempFileManagerFactory implements TempFileManagerFactory {

    @Override
    public TempFileManager create() {
        return new DefaultTempFileManager();
    }
}
