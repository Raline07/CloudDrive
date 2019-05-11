package com.example.clouddrivetest.Zip;

import java.util.HashSet;

public abstract class AbstractPool<T> {

    private HashSet<T> available = new HashSet<>();
    private HashSet<T> inUse = new HashSet<>();

    public void add(T t) {
        available.add(t);
    }

    public synchronized T get() {
        if (available.size() <= 0) return null;
        T t = available.iterator().next();
        available.remove(t);
        inUse.add(t);
        return t;
    }

    public synchronized void put(T t) {
        inUse.remove(t);
        available.add(t);
    }
}
