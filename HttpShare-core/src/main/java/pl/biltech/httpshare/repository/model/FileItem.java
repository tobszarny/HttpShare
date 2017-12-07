package pl.biltech.httpshare.repository.model;

public class FileItem {
    private static long idSequence = 1L;
    private final long id;
    private boolean removable;
    private boolean persistentDownload;
    private String url;

    public FileItem() {
        id = idSequence++;
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

    public long getId() {
        return id;
    }
}
