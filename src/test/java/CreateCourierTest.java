import courier.CourierApi;
import courier.CourierData;
import courier.CourierDataLogin;
import courier.CourierRandom;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateCourierTest {

    private CourierApi courierApi;
    private CourierData courierData;
    private CourierDataLogin courierDataLogin;
    private Integer courierId;

    @Before
    public void prepare() {
        courierApi = new CourierApi();
        courierData = CourierRandom.generateCourierData();
    }

    @Test
    @DisplayName("Create courier")
    @Description("Позитивная проверка создания курьера")
    public void createOneCourierTest() {
        ValidatableResponse response = (ValidatableResponse) courierApi.create(courierData);
        assertEquals("Неверный код статуса при создании курьера"
                , HttpStatus.SC_CREATED, response.extract().statusCode());
        assertEquals("Неверное сообщение при успешном создании курьера"
                , true, response.extract().path("ok"));
    }

    @Test
    @DisplayName("Create two courier")
    @Description("Проверка создания двух  курьеров с одинаковыми логинами")
    public void createTwoCourierTest() {
        ValidatableResponse responseOne = (ValidatableResponse) courierApi.create(courierData);
        assertEquals(HttpStatus.SC_CREATED, responseOne.extract().statusCode());
        assertEquals(true, responseOne.extract().path("ok"));

        ValidatableResponse responseTwo = (ValidatableResponse) courierApi.create(courierData);
        assertEquals(HttpStatus.SC_CONFLICT, responseTwo.extract().statusCode());
        assertEquals("Этот логин уже используется. Попробуйте другой.", responseTwo.extract().path("message"));

    }

    @After
    public void tearDown() {
        courierDataLogin = new CourierDataLogin(courierData.getLogin(), courierData.getPassword());
        ValidatableResponse responseLogin = courierApi.login(courierDataLogin);
        courierId = responseLogin.extract().path("id");
        courierApi.delete(courierId);
    }
}
