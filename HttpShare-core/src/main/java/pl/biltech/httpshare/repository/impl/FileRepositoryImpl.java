package pl.biltech.httpshare.repository.impl;

import pl.biltech.httpshare.repository.FileRepository;
import pl.biltech.httpshare.repository.model.FileItem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileRepositoryImpl implements FileRepository {
    private final Map<Long, FileItem> repositoryStore = new LinkedHashMap<>();

    @Override
    public void add(FileItem fileItem) {
        repositoryStore.put(fileItem.getId(), fileItem);
    }

    @Override
    public void addAll(List<FileItem> fileItems) {
        fileItems.forEach(f -> add(f));
    }

    @Override
    public boolean remove(long id) {
        if (repositoryStore.containsKey(id)) {
            repositoryStore.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(List<Long> ids) {
        if (ids.isEmpty()) {
            return false;
        }
        boolean allRemoved = true;
        for (Long id : ids) {
            allRemoved &= remove(id);
        }
        return allRemoved;
    }

    @Override
    public void clear() {
        repositoryStore.clear();
    }
}
