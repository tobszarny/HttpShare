package pl.biltech.httpshare.repository;

import pl.biltech.httpshare.repository.model.FileItem;

import java.util.List;
import java.util.UUID;

public interface FileRepository {
    void add(FileItem fileItem);

    void addAll(List<FileItem> fileItems);

    boolean remove(UUID id);

    boolean removeAll(List<UUID> ids);

    void clear();
}
