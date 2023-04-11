package com.github.winter4666.bpofea.testsupport.MySQL;

import com.github.winter4666.bpofea.testsupport.SpringBeanRetriever;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.winter4666.bpofea.testsupport.MySQL.MySQLContainerHolder.MY_SQL_CONTAINER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public interface MySQLTestable {

    @BeforeEach
    default void init() {
        Flyway flyway = SpringBeanRetriever.getBean(Flyway.class);
        flyway.clean();
        flyway.migrate();
    }

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }

}
