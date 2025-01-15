package courier;

import base.BaseHttpClient;
import io.restassured.response.ValidatableResponse;

public class CourierApi extends BaseHttpClient {

    private final String courierCreateApi = "/api/v1/courier/";
    private final String courierLoginApi = "/api/v1/courier/login/";
    private final String courierDeleteApi = "/api/v1/courier/";

    public ValidatableResponse create(Object body) {
        return doPostRequest(courierCreateApi, body);
    }

    public ValidatableResponse login(Object body) {
        return doPostRequest(courierLoginApi, body);
    }

    public ValidatableResponse delete(Integer idCourier) {
        CourierDataDelete body = new CourierDataDelete( String.valueOf(idCourier));
        return doDeleteRequest(courierDeleteApi + idCourier, body);
    }

}
