package io.vigier.cursor.repo;

import io.vigier.cursor.Page;
import io.vigier.cursor.PageRequest;

public interface CursorPageRepository<T> {
    Page<T> find(final PageRequest request);

}
