package org.acme;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.GameDto;
import org.acme.repository.GameRepository;
import org.junit.jupiter.api.*;
import org.wildfly.common.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameResourceTest {

    private GameDto gameDto;
    private GameDto gameDto2;
    @BeforeEach
    void setUp() {
        gameDto = new GameDto(null,"magnificent game","magnificent-game","STRATEGY","Most magnificent game in game store service!",12);
        gameDto2 = new GameDto(null,"good game","good-game","SHOOTING","Very good game in this game store service!",7);
    }

    @Test
    @Order(1)
    void testGameEndpointAll() {
        given()
          .when().get("/v1/game/all")
          .then()
             .statusCode(200)
             .body(is("[]"));
    }
    @Test
    @Order(2)
    void testGameEndpointOneNotFound() {
        given()
          .when().get("/v1/game/3")
          .then()
             .statusCode(404)
             .body(is("Can't find gameId=3 in DB , Game not found"));
    }


    @Test
    @Order(3)
    void testGameEndpointOneByNameBadRequest() {
        given()
                .when().get("/v1/game/")
                .then()
                .statusCode(400)
                .body(is("game name must be populated"));
    }

    @Test
    @Order(4)
    void testGameEndpointOneByName() {
        given()
                .when().get("/v1/game?name=not_found_game")
                .then()
                .statusCode(404)
                .body(is("Game not found by shortTitle: not_found_game"));
    }

    @Test
    @Order(5)
    void testGameEndpointCreate() {
        Response response = given()
                .body(gameDto)
                .and()
                .header("Content-type", "application/json")
                .when().post("/v1/game")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals(1, response.jsonPath().getInt("gameId"));

        Response response2 = given()
                .body(gameDto2)
                .and()
                .header("Content-type", "application/json")
                .when().post("/v1/game")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response2.getStatusCode());
        Assertions.assertEquals(2, response2.jsonPath().getInt("gameId"));

    }

    @Test
    @Order(6)
    void testGameEndpointDelete() throws InterruptedException {
        Thread.sleep(2000);
        Response response = given()
                .body(gameDto)
                .and()
                .header("Content-type", "application/json")
                .when().post("/v1/game")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.getStatusCode());

        Response response2 = given()
                .body(gameDto2)
                .and()
                .header("Content-type", "application/json")
                .when().post("/v1/game")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response2.getStatusCode());


        Response response3 = given()
                .pathParam("id",1)
                .when().delete("/v1/game/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response3.getStatusCode());
        Assertions.assertEquals("Successfully Deleted game with id= 1", response3.getBody().asString());
    }


}