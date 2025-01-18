import courier.CourierApi;
import courier.CourierData;
import courier.CourierDataLogin;
import courier.CourierRandom;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoginCourierTest {

    private CourierApi courierApi;
    private CourierData courierData;
    private Integer courierId;
    private ValidatableResponse response;

    @Before
    public void prepare() {
        courierApi = new CourierApi();
        courierData = new CourierRandom().generateCourierData() ;
        courierApi.create(courierData);
    }

    @Test
    @DisplayName("Login courier")
    @Description("Позитивня проверка авторизации курьера")
    public void loginCourierPozitivTest() {
        CourierDataLogin courierDataLogin = new CourierDataLogin(
                courierData.getLogin(), courierData.getPassword());
        loginCourier(courierDataLogin);
        checkLoginCourierPositiv();
    }

    @Step("Авторизируем курьера")
    private void loginCourier(CourierDataLogin courierDataLogin) {

        this.response = courierApi.login(courierDataLogin);
    }

    @Step("Проверяем авторизацию курьера")
    private void checkLoginCourierPositiv() {

        assertThat("Неверный статус код при авторизации курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("Неверное  сообщение при позитивной авторизации курьера",
                response.extract().path("id"), instanceOf(Integer.class));
    }

    @Test
    @DisplayName("Login courier with wrong login")
    @Description("Проверка авторизации курьера с неверным логином")
    public void loginCourierNegativLoginTest() {
        CourierDataLogin courierDataLogin = new CourierDataLogin(
                RandomStringUtils.randomAlphabetic(5),
                courierData.getPassword());
        loginCourier(courierDataLogin);
        checkLoginCourierNegativ();
    }


    @Test
    @DisplayName("Login courier with wrong password")
    @Description("Проверка авторизации курьера с неверным паролем")
    public void loginCourierNegativPaswordTest() {
        CourierDataLogin courierDataLogin = new CourierDataLogin(
                courierData.getLogin(),
                RandomStringUtils.randomAlphabetic(8));
        loginCourier(courierDataLogin);
        checkLoginCourierNegativ();
    }

    @Step("Проверяем авторизации курьера с неверным логин или паролем")
    private void checkLoginCourierNegativ() {
        assertThat("Неверный статус код при авторизации курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Неверное сообщение при авторизации курьера с неверным логином",
                response.extract().path("message"), equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Login courier without login")
    @Description("Проверка авторизации курьера без логина")
    public void loginCourierWithoutLoginTest() {
        CourierDataLogin courierDataLogin = new CourierDataLogin(
                "", courierData.getPassword());

        loginCourier(courierDataLogin);
        checkLoginWithoutLoginPassword();
    }

    @Test
    @DisplayName("Login courier without password")
    @Description("Проверка авторизации курьера без паролем")
    public void oginCouriervWithoutPaswordTest() {
        CourierDataLogin courierDataLogin = new CourierDataLogin(
                courierData.getLogin(), "");
        loginCourier(courierDataLogin);
        checkLoginWithoutLoginPassword();
    }

    @Step("Проверяем авторизацию без логина или пароля")
    private void checkLoginWithoutLoginPassword() {
        assertThat("Неверный статус код при авторизации курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("Неверное сообщение при авторизации курьера без логином",
                response.extract().path("message"), equalTo("Недостаточно данных для входа"));
    }

    @After
    public void tearDown() {
        CourierDataLogin courierDataLogin = new CourierDataLogin(courierData.getLogin(), courierData.getPassword());
        ValidatableResponse responseLogin = courierApi.login(courierDataLogin);
        courierId = responseLogin.extract().path("id");
        courierApi.delete(courierId);
    }
}
