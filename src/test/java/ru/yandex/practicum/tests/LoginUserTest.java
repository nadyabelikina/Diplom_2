package ru.yandex.practicum.tests;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.practicum.steps.UserSteps;
import ru.practicum.practicum.model.User;

import static org.hamcrest.CoreMatchers.*;

/**
 * 2. Логин пользователя:
 * * вход под существующим пользователем;
 * * вход с неверным логином и паролем.
 */
public class LoginUserTest extends BaseTest  {

    private UserSteps userSteps = new UserSteps();
    private User user;


    @Before
    public void setUp() {
        user = new User();
        userSteps = new UserSteps();
        user
                .setEmail("test-data@yandex.ru1123")
                .setPassword("password")
                .setName("Username");
        userSteps.createUser(user)
                .statusCode(200)
                .body("success", equalTo(true));
        String accessTokenWithBearer = UserSteps
                .login(user)
                .extract().body().path("accessToken");
        String accessToken = accessTokenWithBearer.replace("Bearer ", "");
        user.setAccessToken(accessToken);
        }

    @Test
    @DisplayName("Логин под существующим пользователем. Ответ 200")
    @Description("Post запрос на ручку /api/auth/login")
    @Step("Login user")

    public void loginWithUserTrueTest() {
        UserSteps
               .login(user)
                .statusCode(200)
                .and()
                .body("refreshToken", notNullValue())
                .extract().body().path("accessToken");

    }

    @Test
    @DisplayName("Логин под неверным именем почты. Ответ 401")
    @Description("Post запрос на ручку /api/auth/login")
    @Step("Login user")
    public void loginWithUserFalseEmailTest() {

        user
                .setEmail("test-data781246@yndeax.ru");
          UserSteps
                .login(user)
                .statusCode(401);

    }

    @Test
    @DisplayName("Логин под неверным паролем. Ответ 401")
    @Description("Post запрос на ручку /api/auth/login")
    @Step("Login user")
    public void loginWithUserFalsePasswordTest() {

        user
             .setPassword("passwordqwerty");
        UserSteps
                .login(user)
                .statusCode(401);

    }

    @After
    public void tearDown() {
        //System.out.println(user.getAccessToken());
        if (!user.getEmail().isEmpty() && !user.getPassword().isEmpty()) {

            if (user.getAccessToken() != null) {

                userSteps.deleteUser(user.getAccessToken());
            }

        }
    }
}
