package ru.practicum.practicum.steps;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.practicum.practicum.model.User;
import static io.restassured.RestAssured.given;

public class UserSteps {
    @Step
    public ValidatableResponse createUser(User user) {
        return given()
                .body(user)
                .when()
                .post("/api/auth/register")
                .then();
    }


    @Step
    public static ValidatableResponse login(User user) {
        return given()
                .body(user)
                .when()
                .post("/api/auth/login")
                .then();
    }

    @Step
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .auth().oauth2(accessToken)
                .when()
                .delete("/api/auth/user")
                .then();
    }
}

