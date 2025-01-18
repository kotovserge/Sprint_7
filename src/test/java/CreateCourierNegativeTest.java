import courier.CourierApi;
import courier.CourierData;
import courier.CourierDataLogin;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import java.util.Locale;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateCourierNegativeTest {
    private CourierApi courierApi;
    private CourierData courierData;
    private CourierDataLogin courierDataLogin;
    private String login;
    private String password;
    private String firstName;
    private Boolean statusCreated;
    private Integer courierId;
    private ValidatableResponse response;

    @Before
    public void prepare() {
        courierApi = new CourierApi();
    }

    public CreateCourierNegativeTest(String login,
                                     String password,
                                     String firstName,
                                     Boolean statusCreated) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.statusCreated = statusCreated;
    }

    @Parameterized.Parameters(name = " login ({0}) , password ({1}) , firsName ({2})")
    public static Object[][] setParams() {
        Faker faker = new Faker(new Locale("ru"));
        return new Object[][]{
                {"", faker.internet().password(4, 4), StringUtils.left(faker.name().firstName(), 10), FALSE},
                {StringUtils.left(faker.name().name(), 10), "", StringUtils.left(faker.name().firstName(), 10), FALSE},
                {StringUtils.left(faker.name().name(), 10), faker.internet().password(4, 4), "", TRUE},
        };
    }

    @Test
    @Description("Cоздание курьера без полей данных")
    public void createCourierTest() {
        courierData = new CourierData(login, password, firstName);
        createCourier();
        checkCreateCourier();
    }

    @Step("Создаем курьера без обязательных полей полей данных")
    private void createCourier() {
        this.response = courierApi.create(courierData);
    }

    @Step("Проверяем созданного курьера без полей данных")
    private void checkCreateCourier() {
        if (statusCreated == FALSE) {
            assertEquals("Неверный Статус КОД при создании курьера без обязательных данных",
                    HttpStatus.SC_BAD_REQUEST, response.extract().statusCode());
            assertEquals("Неверное сообщение об ошибке при создании курьера без обязательных данных",
                    "Недостаточно данных для создания учетной записи",
                    response.extract().path("message"));
        } else if (statusCreated == TRUE) {
            assertEquals("Неверный Статус КОД при создании курьера",
                    HttpStatus.SC_CREATED, response.extract().statusCode());
            assertEquals("Неверное сообщение об ошибке при создании курьера без обязательных данных",
                    true, response.extract().path("ok"));
        }
    }


    @After
    public void tearDown () {
        courierDataLogin = new CourierDataLogin(courierData.getLogin(), courierData.getPassword());
        ValidatableResponse responseLogin = courierApi.login(courierDataLogin);
        if (responseLogin.extract().statusCode() == HttpStatus.SC_OK) {
            courierId = responseLogin.extract().path("id");
            courierApi.delete(courierId);
        }
    }
}
