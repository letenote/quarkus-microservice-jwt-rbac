package com.letenote.controllers;


import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(ProductController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {

    @Test
    @Order(1)
    void getProductsIsEmpty(){
        when()
                .get()
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", CoreMatchers.is(0));
    }

    @Test
    @Order(2)
    void addProductNoAuthorizationToken() {
        JsonObject jsonItem = Json
                .createObjectBuilder()
                .add("id", "3")
                .add("name", "itemName3")
                .add("price", "188000")
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonItem.toString())
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    @Order(3)
    @TestSecurity(authorizationEnabled = false)
    void addProductIfAuthorizationIsDisabled(){
        String idExpected = "product-01";
        String nameExpected = "Polo Shirt";
        int priceExpected = 188000;
        JsonObject newProduct = Json.createObjectBuilder()
                .add("id", idExpected)
                .add("name", nameExpected)
                .add("price", priceExpected)
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newProduct.toString())
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", CoreMatchers.equalTo(3))
                .body("id", CoreMatchers.equalTo(idExpected))
                .body("name", CoreMatchers.equalTo(nameExpected))
                .body("price", CoreMatchers.equalTo(priceExpected));

        when()
                .get()
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", CoreMatchers.is(1));
    }

    @Test
    @Order(4)
    @TestSecurity(user = "lemariindah", roles = {"merchant"})
    void addProductWithAuthorization() {
        String idExpected = "product-02";
        String nameExpected = "Polo Shirt Black";
        int priceExpected = 200000;
        JsonObject newProduct = Json.createObjectBuilder()
                .add("id", idExpected)
                .add("name", nameExpected)
                .add("price", priceExpected)
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newProduct.toString())
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", CoreMatchers.equalTo(3))
                .body("id", CoreMatchers.equalTo(idExpected))
                .body("name", CoreMatchers.equalTo(nameExpected))
                .body("price", CoreMatchers.equalTo(priceExpected));

        when()
                .get()
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", CoreMatchers.is(2));
    }

    @Test
    @Order(5)
    @TestSecurity(user = "wrongUser", roles = {"viewer"})
    void addProductWithWrongAuthorization() {
        JsonObject jsonItem = Json
                .createObjectBuilder()
                .add("id", "product-03")
                .add("name", "Polo Shirt Grey")
                .add("price", 13)
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonItem.toString())
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @Order(6)
    @TestSecurity(authorizationEnabled = false)
    void deleteProductIfAuthorizationIsDisabled() {
        given()
                .when().delete("/product-01")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @Order(7)
    void deleteProductNoAuthorization() {
        given()
                .when().delete("/product-02")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    @Order(7)
    @TestSecurity(user = "testUser", roles = {"admin", "merchant"})
    void deleteItemWithAuthorization() {
        given()
                .when().delete("/product-02")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        when()
                .get()
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", CoreMatchers.is(0));
    }
}