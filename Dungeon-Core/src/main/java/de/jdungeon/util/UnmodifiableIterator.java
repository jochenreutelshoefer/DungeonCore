package de.jdungeon.util;

import java.util.Iterator;

public final class UnmodifiableIterator<E> implements Iterator<E> {

    /**
     * iterator, The base iterator.
     */
    private final Iterator<? extends E> iterator;

    private UnmodifiableIterator(final Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    public static <E> Iterator<E> create(final Iterator<? extends E> iterator) {
        if (iterator == null) {
            throw new NullPointerException("The iterator can not be null.");
        }
        return new UnmodifiableIterator<>(iterator);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public E next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Iterator.remove() is disabled.");
    }

}
