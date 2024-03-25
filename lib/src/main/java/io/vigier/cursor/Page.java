package io.vigier.cursor;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class Page<E> implements Iterable<E> {
    private final List<E> content;
    private final Position self;
    private final Position next;

    @Override
    public Iterator<E> iterator() {
        return content.iterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        content.forEach(action);
    }
}
