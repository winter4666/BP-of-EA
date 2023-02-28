package com.github.winter4666.bpofea.testbase;

import org.testcontainers.containers.MySQLContainer;

public class MySQLContainerHolder {

    public static MySQLContainer<?> MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.32");
        MY_SQL_CONTAINER.start();
    }
}
