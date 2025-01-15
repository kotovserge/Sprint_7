package order;

import base.BaseHttpClient;
import courier.CourierDataDelete;
import io.restassured.response.ValidatableResponse;

public class OrderApi extends BaseHttpClient{

    private final String orderCreateApi = "/api/v1/orders/";
    private final String orderListApi = "/api/v1/orders";
    private final String orderCancelApi = "/api/v1/orders/cancel/";

    public ValidatableResponse create(Object body) {
        return doPostRequest(orderCreateApi, body);
    }

    public ValidatableResponse getList() {
        return doGetRequest(orderListApi);
    }

    public ValidatableResponse cancel(int track) {
        return doPutRequest(orderCancelApi+"?track="+track);
    }
}
