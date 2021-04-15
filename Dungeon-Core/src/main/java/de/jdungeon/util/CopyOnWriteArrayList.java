package de.jdungeon.util;

import de.jdungeon.log.Log;

import java.util.*;

public class CopyOnWriteArrayList<T> implements List<T> {

    private ArrayList<T> internalList;

    public CopyOnWriteArrayList() {
        this.internalList = new ArrayList<>();
    }


    @Override
    public int size() {
        return internalList.size();
    }

    @Override
    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return internalList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return UnmodifiableIterator.create(internalList.iterator());
    }

    @Override
    public Object[] toArray() {
        return internalList.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return internalList.toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        ArrayList<T> newList = new ArrayList<>(internalList);
        boolean b = newList.add(t);
        this.internalList = newList;
        return b;
    }

    @Override
    public boolean remove(Object o) {
        ArrayList<T> newList = new ArrayList<>(internalList);
        boolean b = newList.remove(o);
        this.internalList = newList;
        return b;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return internalList.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        ArrayList<T> newList = new ArrayList<>(internalList);
        boolean b = newList.addAll(collection);
        this.internalList = newList;
        return b;
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        ArrayList<T> newList = new ArrayList<>(internalList);
        boolean b = newList.addAll(i, collection);
        this.internalList = newList;
        return b;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        ArrayList<T> newList = new ArrayList<>(internalList);
        boolean b = newList.removeAll(collection);
        this.internalList = newList;
        return b;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        ArrayList<T> newList = new ArrayList<>(internalList);
        boolean b = newList.retainAll(collection);
        this.internalList = newList;
        return b;
    }

    @Override
    public void clear() {
        this.internalList = new ArrayList<>();
    }

    @Override
    public T get(int i) {
        return internalList.get(i);
    }

    @Override
    public T set(int i, T t) {
        ArrayList<T> newList = new ArrayList<>(internalList);
        T o = newList.set(i, t);
        this.internalList = newList;
        return o;
    }

    @Override
    public void add(int i, T t) {
        ArrayList<T> newList = new ArrayList<>(internalList);
        newList.add(i, t);
        this.internalList = newList;
    }

    @Override
    public T remove(int i) {
        ArrayList<T> newList = new ArrayList<>(internalList);
       T t = newList.remove(i);
        this.internalList = newList;
        return t;
    }

    @Override
    public int indexOf(Object o) {
        return this.internalList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.internalList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        Log.severe("ListIterator not implemented here!");
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        Log.severe("ListIterator not implemented here!");
        return null;
    }

    @Override
    public List<T> subList(int i, int i1) {
        Log.severe("ListIterator not implemented here!");
        return null;
    }
}
