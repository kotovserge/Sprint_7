import courier.CourierApi;
import courier.CourierData;
import courier.CourierDataLogin;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.apache.commons.lang3.RandomStringUtils;

import java.net.http.HttpClient;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateCourierNegativeTest {

    private CourierApi courierApi;
    private CourierData courierData;
    private CourierDataLogin courierDataLogin;
    private String login;
    private String password;
    private String firsName;
    private Integer courierId;

    @Before
    public void prepare() {
        courierApi = new CourierApi();
    }

    public CreateCourierNegativeTest(String login,
                                     String password,
                                     String firsName) {
        this.login = login;
        this.password = password;
        this.firsName = firsName;
    }

    @Parameterized.Parameters
    public static Object[][] setParams() {
        return  new Object[][] {
                {"", RandomStringUtils.randomAlphabetic(8), RandomStringUtils.randomAlphabetic(10)},
                {RandomStringUtils.randomAlphabetic(5), "", RandomStringUtils.randomAlphabetic(10)},
        };
    }

    @Test
    @DisplayName("Create courier negative")
    @Description("Cоздании курьера без логина и пароля")
    public void createCourierTest() {

        courierData = new CourierData(login, password, firsName);

        ValidatableResponse response = courierApi.create(courierData);

        assertEquals("Неверный Статус КОД при создани курьера без обязательных данных",
                HttpStatus.SC_BAD_REQUEST, response.extract().statusCode());
        assertEquals("Неверное сообщение об ошибке при создани курьера без обязательных данных",
                "Недостаточно данных для создания учетной записи",
                response.extract().path("message"));
    }

    @After
    public void tearDown() {
        courierDataLogin = new CourierDataLogin(courierData.getLogin(), courierData.getPassword());
        ValidatableResponse responseLogin = courierApi.login(courierDataLogin);
        if (responseLogin.extract().statusCode() == HttpStatus.SC_OK ) {
            courierId = responseLogin.extract().path("id");
            courierApi.delete(courierId);
        }
    }

}

