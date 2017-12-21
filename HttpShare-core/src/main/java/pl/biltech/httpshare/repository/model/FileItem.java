package pl.biltech.httpshare.repository.model;

import java.io.File;
import java.util.UUID;

public class FileItem {
    private final UUID id;
    private String name;
    private boolean removable;
    private boolean persistentDownload;
    private File file;
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

//    public void setUrl(String url) {
//        this.url = url;
//    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileItem withRemovable(boolean removable) {
        this.removable = removable;
        return this;
    }

    public FileItem withPersistentDownload(boolean persistentDownload) {
        this.persistentDownload = persistentDownload;
        return this;
    }

    public FileItem withFile(File file) {
        this.file = file;
        this.name = file.getName();
        return this;
    }

    public FileItem withName(String name) {
        this.name = name;
        return this;
    }


    public FileItem withServerUrl(String url) {
        this.url = url + "/api/file/" + this.getId();
        return this;
    }
}
