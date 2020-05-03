package com.mszewczyk.cp.persistance;

import java.util.Collection;

public interface DatabaseConnector<T> extends AutoCloseable {
    void initializeDataStructure();

    void store(T value);

    Collection<T> getAll();
}
