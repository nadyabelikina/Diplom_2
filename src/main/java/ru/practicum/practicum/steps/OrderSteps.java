package ru.practicum.practicum.steps;

import io.restassured.response.ValidatableResponse;
import ru.practicum.practicum.model.Order;


import static io.restassured.RestAssured.given;
public class OrderSteps {


    public ValidatableResponse orderWithoutAuth(Order order) {
        return given()
                .body(order)
                .post("/api/orders")
                .then();

    }

    public ValidatableResponse orderWithAuth(String accessToken, Order order) {
        return given()
                .body(order)
                .auth().oauth2(accessToken)
                .post("/api/orders")
                .then();
    }

    public ValidatableResponse getOrderUserAuth(String accessToken) {
        return given()
                .auth().oauth2(accessToken)
                .get("/api/orders")
                .then();
    }

    public ValidatableResponse getOrderUserNotAuth() {
        return given()
                .get("/api/orders")
                .then();
    }
}
