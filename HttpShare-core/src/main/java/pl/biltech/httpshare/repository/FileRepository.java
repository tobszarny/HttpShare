package pl.biltech.httpshare.repository;

import pl.biltech.httpshare.repository.model.FileItem;

import java.util.List;

public interface FileRepository {
    void add(FileItem fileItem);

    void addAll(List<FileItem> fileItems);

    boolean remove(long id);

    boolean removeAll(List<Long> ids);

    void clear();
}
