package com.github.winter4666.bpofea.user;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.RdbDaoTest;
import com.github.winter4666.bpofea.user.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserTest extends RdbDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void should_get_users_successfully() {
        String name = new Faker().name().fullName();
        jdbcTemplate.update("INSERT INTO user (name) VALUES (?)", name);

        get("/users")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("first().name", equalTo(name));
    }

    @Test
    void should_add_user_successfully() {
        String name = new Faker().name().fullName();
        given().formParam("name", name)
                .when().post("/users")
                .then().statusCode(HttpStatus.CREATED.value());

        List<User> users = jdbcTemplate.query("select * from user", (rs, n) -> new User(rs.getInt("id"), rs.getString("name")));

        assertThat(users.size(), equalTo(1));
        assertThat(users.get(0).getName(), equalTo(name));

    }

}
