package com.mszewczyk.cp.persistance;

public interface DatabaseConfiguration {
    String dataCenter();

    String dbUrl();

    int port();

    String keySpace();
}
