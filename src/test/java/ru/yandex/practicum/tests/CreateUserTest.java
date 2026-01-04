package ru.yandex.practicum.tests;

import io.qameta.allure.junit4.DisplayName;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.practicum.steps.UserSteps;
import ru.practicum.practicum.model.User;

import static org.hamcrest.CoreMatchers.*;

public class CreateUserTest  extends BaseTest {

    private UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp() {

        user = new User();
        user
                //.setEmail(RandomStringUtils.randomAlphabetic(8)+"test-data@qwertyv.ru")
                .setEmail("test-data4756@qwertyv.ru")
                .setPassword("password")
                .setName("Username");
    }

    @Test
    @DisplayName("Создание нового уникального пользователя.")
    public void createUniqueUserTest() {
       userSteps
                .createUser(user)
                .statusCode(200)
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue())
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован.")
    public void createRegisteredUserTest() {
        user
                .setEmail("test-data@yandex.ru");
        userSteps
                .createUser(user)
                .statusCode(403)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создать пользователя без email")
    public void createUserWithoutLoginTest() {
        user
                .setEmail("");
        userSteps
                .createUser(user)
                .statusCode(403)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создать пользователя без password")
    public void createUserWithoutPasswordTest() {
        user
                .setPassword("");
        userSteps
                .createUser(user)
                .statusCode(403)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создать пользователя без Name")
    public void createUserWithoutNameTest() {
        user
                .setName("");
        userSteps
                .createUser(user)
                .statusCode(403)
                .body("success", equalTo(false));

    }
    @After
    public void tearDown() {
        if (!user.getEmail().isEmpty() && !user.getPassword().isEmpty()) {
            String accessTokenWithBearer = UserSteps
                    .login(user)
                    .extract().body().path("accessToken");
            if (accessTokenWithBearer != null) {
                String accessToken = accessTokenWithBearer.replace("Bearer ", "");
               System.out.println(accessToken);
                userSteps.deleteUser(accessToken);
            }

        }
    }
}
