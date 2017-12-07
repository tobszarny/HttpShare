package pl.biltech.httpshare.repository.model;

import java.util.UUID;

public class FileItem {
    private final UUID id;
    private boolean removable;
    private boolean persistentDownload;
    private String url;

    public FileItem() {
        id = UUID.randomUUID();
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public boolean isPersistentDownload() {
        return persistentDownload;
    }

    public void setPersistentDownload(boolean persistentDownload) {
        this.persistentDownload = persistentDownload;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UUID getId() {
        return id;
    }
}
