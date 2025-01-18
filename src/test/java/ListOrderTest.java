import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.OrderApi;
import order.OrderData;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.SocketTimeoutException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ListOrderTest {

    private OrderApi orderApi;
    private OrderData orderData;
    private Integer track;
    private ValidatableResponse response;

    @Before
    public void prepare() {
        orderApi = new OrderApi();
        orderData = new OrderData("Иван","Иванов",
                "Москва, ул.Каланчевская", "Красные ворота",
                "+79871234567", 3, "2025-01-20",
                "Позвонить", List.of("BLACK"));
        ValidatableResponse response = orderApi.create(orderData);
        track = response.extract().path("track");
    }

    @Test
    @DisplayName("Get list order")
    @Description("Проверка получения списка заказов")
    public void getListOrderTest() {
            ValidatableResponse response = orderApi.getList();
            assertThat("Неверный статус код при получении списка заказов",
                    response.extract().statusCode(), equalTo(HttpStatus.SC_OK));
            assertThat("Список заказов пустой",
                    response.extract().path("orders"), notNullValue());
            System.out.println(response.extract().statusCode());

    }

    @After
    public  void tearDown() {
        orderApi.cancel(track);
    }

}
