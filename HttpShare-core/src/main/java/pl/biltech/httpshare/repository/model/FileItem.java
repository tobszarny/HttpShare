package pl.biltech.httpshare.repository.model;

import java.util.UUID;

public class FileItem {
    private final UUID id;
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileItem withRemovable(boolean removable) {
        this.removable = removable;
        return this;
    }

    public FileItem withPersistentDownload(boolean persistentDownload) {
        this.persistentDownload = persistentDownload;
        return this;
    }

    public FileItem withUrl(String url) {
        this.url = url;
        return this;
    }
}
