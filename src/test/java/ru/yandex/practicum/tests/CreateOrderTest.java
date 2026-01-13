package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;

import ru.practicum.practicum.model.Order;
import ru.practicum.practicum.model.User;
import ru.practicum.practicum.steps.UserSteps;
import ru.practicum.practicum.steps.OrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.hamcrest.CoreMatchers.*;

/**
 * 3. Создание заказа:
 * * с авторизацией,
 * * без авторизации,
 * * с ингредиентами,
 * * без ингредиентов,
 * * с неверным хешем ингредиентов.
 */
public class CreateOrderTest extends BaseTest {
    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private User user;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
    }

    @Test
    @DisplayName("Создание заказа без авторизации. Ответ 200")
    @Description("Post запрос на ручку /api/orders")
    @Step("Создание заказа")
    public void createOrderWithoutAuth() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa73");
        ingredients.add("61c0c5a71d1f82001bdaaa70");
        Order order = new Order(ingredients);

        orderSteps
                .orderWithoutAuth(order)
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без авторизации, c неверным хешем. Ответ 500")
    @Description("Post запрос на ручку /api/orders")
    @Step("Создание заказа")
    public void createOrderWithoutAuthErrorHash() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaawe");
        ingredients.add("61c0c5a71d1f82001bdaaa73");
        ingredients.add("61c0c5a71d1f82001bdaaa70");
        Order order = new Order(ingredients);
        orderSteps
                .orderWithoutAuth(order)
                .assertThat()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без авторизации, без ингредиентов. Ответ 400")
    @Description("Post запрос на ручку /api/orders")
    @Step("Создание заказа")
    public void createOrderWithoutAuthNoIngredient() {
        Order order = new Order(null);
        orderSteps
                .orderWithoutAuth(order)
                .assertThat()
                .statusCode(400)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией. Ответ 200")
    @Description("Post запрос на ручку /api/orders")
    @Step("Создание заказа")
    public void createOrderWithAuth() {
        User user = new User();
        user
                //.setEmail(RandomStringUtils.randomAlphabetic(8)+"test-data@qwertyv.ru")
                .setEmail("test-dat345ved46@qwerty.ru")
                .setPassword("password")
                .setName("Username");
        userSteps
                .createUser(user)
                .statusCode(200)
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue())
                .body("success", equalTo(true));
        String accessTokenWithBearer = UserSteps
                .login(user)
                .extract().body().path("accessToken");
        String accessToken = accessTokenWithBearer.replace("Bearer ", "");
        user.setAccessToken(accessToken);
        //System.out.println(accessToken);
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa73");
        ingredients.add("61c0c5a71d1f82001bdaaa70");

        Order order = new Order(ingredients);
        orderSteps
                .orderWithAuth(accessToken, order)
                .assertThat()
                .statusCode(200);

        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, без ингредиентов. Ответ 400")
    @Description("Post запрос на ручку /api/orders")
    @Step("Создание заказа")
    public void createOrderWithAuthNoIngredient() {
        User user = new User();
        user

                .setEmail("test-data4535446@qwerty.ru")
                .setPassword("password")
                .setName("Username");
        userSteps
                .createUser(user)
                .statusCode(200)
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue())
                .body("success", equalTo(true));
        String accessTokenWithBearer = UserSteps
                .login(user)
                .extract().body().path("accessToken");
        String accessToken = accessTokenWithBearer.replace("Bearer ", "");
        user.setAccessToken(accessToken);
        Order order = new Order(null);
        orderSteps
                .orderWithAuth(accessToken, order)
                .assertThat()
                .statusCode(400);

        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией с неверным хешем")
    @Description("Post запрос на ручку /api/orders")
    @Step("Создание заказа")
    public void createOrderWithAuthErrorHash() {
        User user = new User();
        user

                .setEmail("test-data1232346@qwerty.ru")
                .setPassword("password")
                .setName("Username");
        userSteps
                .createUser(user)
                .statusCode(200)
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue())
                .body("success", equalTo(true));
        String accessTokenWithBearer = UserSteps
                .login(user)
                .extract().body().path("accessToken");
        String accessToken = accessTokenWithBearer.replace("Bearer ", "");

        user.setAccessToken(accessToken);ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaerwt6d");
        ingredients.add("61c0c5a71d1f82001bdaaa73");
        ingredients.add("61c0c5a71d1f82001bdaaa70");

        Order order = new Order(ingredients);
        orderSteps
                .orderWithAuth(accessToken, order)
                .assertThat()
                .statusCode(500);

        userSteps.deleteUser(accessToken);
    }


}
