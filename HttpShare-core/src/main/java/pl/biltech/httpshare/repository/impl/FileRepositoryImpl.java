package pl.biltech.httpshare.repository.impl;

import pl.biltech.httpshare.repository.FileRepository;
import pl.biltech.httpshare.repository.model.FileItem;

import java.util.*;

public class FileRepositoryImpl implements FileRepository {
    private final Map<UUID, FileItem> repositoryStore = new LinkedHashMap<>();

    @Override
    public FileItem get(String id) {
        UUID uuid = UUID.fromString(id);
        return repositoryStore.get(uuid);
    }

    @Override
    public List<FileItem> getAll() {
        return new ArrayList<>(repositoryStore.values());
    }

    @Override
    public void add(FileItem fileItem) {
        repositoryStore.put(fileItem.getId(), fileItem);
    }

    @Override
    public void addAll(List<FileItem> fileItems) {
        fileItems.forEach(f -> add(f));
    }

    @Override
    public boolean remove(UUID id) {
        if (repositoryStore.containsKey(id)) {
            repositoryStore.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(List<UUID> ids) {
        if (ids.isEmpty()) {
            return false;
        }
        boolean allRemoved = true;
        for (UUID id : ids) {
            allRemoved &= remove(id);
        }
        return allRemoved;
    }

    @Override
    public void clear() {
        repositoryStore.clear();
    }
}
