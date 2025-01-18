import courier.CourierApi;
import courier.CourierData;
import courier.CourierDataLogin;
import courier.CourierRandom;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
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
    private  ValidatableResponse response;


    @Before
    public void prepare() {
        courierApi = new CourierApi();
        courierData = new CourierRandom().generateCourierData() ;
    }

    @Test
    @Description("Позитивная проверка создания курьера")
    public void createOneCourierTest() {
        createOneCourier();
        checkCreateOneCourier();
    }

    @Step("Создаем курьера")
    private void createOneCourier() {
        response = (ValidatableResponse) courierApi.create(courierData);
    }

    @Step("Проверяем созданного курьера")
    private void checkCreateOneCourier() {
        assertEquals("Неверный код статуса при создании курьера"
                , HttpStatus.SC_CREATED, response.extract().statusCode());
        assertEquals("Неверное сообщение при успешном создании курьера"
                , true, response.extract().path("ok"));
    }

    @Test
    @Description("Проверка создания двух  курьеров с одинаковыми логинами")
    public void createTwoCourierTest() {
        createOneCourier();
        checkCreateTwoCourier();

    }

    @Step("Создаем дубликат курьера")
    private void checkCreateTwoCourier() {
        assertEquals("Неверный код статуса при создании курьера"
                , HttpStatus.SC_CREATED, response.extract().statusCode());
        assertEquals("Неверное сообщение при успешном создании курьера"
                , true, response.extract().path("ok"));
    }

    @After
    public void tearDown() {
        courierDataLogin = new CourierDataLogin(courierData.getLogin(), courierData.getPassword());
        ValidatableResponse responseLogin = courierApi.login(courierDataLogin);
        courierId = responseLogin.extract().path("id");
        courierApi.delete(courierId);
    }
}
