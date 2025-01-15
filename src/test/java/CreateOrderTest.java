import io.restassured.response.ValidatableResponse;
import order.OrderApi;
import order.OrderData;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private OrderApi orderApi;
    private OrderData orderData;
    private Integer track;

    @Before
    public void prepare() {
        orderApi = new OrderApi();
    }

    public CreateOrderTest( String firstName,
                            String lastName,
                            String address,
                            String metroStation,
                            String phone,
                            Integer renTime,
                            String deliveryDate,
                            String comment,
                            List<String> colour ) {

        orderData = new OrderData( firstName, lastName, address, metroStation,
                phone, renTime, deliveryDate, comment, colour);
    }

    @Parameterized.Parameters
    public static Object[][] setParams() {
        return new Object[][] {
                {"Иван","Иванов","Москва, ул.Каланчевская", "Красные ворота", "+79871234567", 3, "2025-01-20", "Позвонить", List.of("BLACK")},
                {"Иван","Иванов","Москва, ул.Каланчевская", "Красные ворота", "+79871234567", 3, "2025-01-20", "Позвонить", List.of("GREY")},
                {"Иван","Иванов","Москва, ул.Каланчевская", "Красные ворота", "+79871234567", 3, "2025-01-20", "Позвонить", List.of("GRAY","BLACK")},
                {"Иван","Иванов","Москва, ул.Каланчевская", "Красные ворота", "+79871234567", 3, "2025-01-20", "Позвонить", List.of()},
        };
    }

    @Test
    public void orderTest() {
        ValidatableResponse response = orderApi.create(orderData);
        track = response.extract().path("track");
        System.out.println(track);
        assertEquals(HttpStatus.SC_CREATED, response.extract().statusCode());
        assertEquals(Integer.class, response.extract().path("track").getClass() );
    }

    @After
    public  void tearDown() {
        orderApi.cancel(track);
    }
}
