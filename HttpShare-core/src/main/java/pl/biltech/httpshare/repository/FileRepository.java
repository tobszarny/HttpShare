package pl.biltech.httpshare.repository;

import pl.biltech.httpshare.repository.model.FileItem;

import java.util.List;
import java.util.UUID;

public interface FileRepository {
    FileItem get(String id);

    List<FileItem> getAll();

    void add(FileItem fileItem);

    void addAll(List<FileItem> fileItems);

    boolean remove(UUID id);

    boolean removeAll(List<UUID> ids);

    void clear();
}
