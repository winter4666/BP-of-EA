package com.github.winter4666.bpofea.testsupport;

import com.github.winter4666.bpofea.testsupport.MySQL.MySQLTestable;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("com.github.winter4666.bpofea.*.dao")
@Import(SpringBeanRetriever.class)
public abstract class RdbDaoTest implements MySQLTestable {
}
