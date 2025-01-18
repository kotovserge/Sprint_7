package courier;

import base.BaseHttpClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import constants.Url;

public class CourierApi extends BaseHttpClient {


    @Step ("Send post courier create")
    public ValidatableResponse create(Object body) {
        return doPostRequest(Url.COURIER_CREATE_API, body);
    }
    @Step ("Send post courier login")
    public ValidatableResponse login(Object body) {
        return doPostRequest(Url.COURIER_LOGIN_API, body);
    }

    @Step ("Send post courier delete")
    public ValidatableResponse delete(Integer idCourier) {
        CourierDataDelete body = new CourierDataDelete( String.valueOf(idCourier));
        return doDeleteRequest(Url.COURIER_CREATE_API + idCourier, body);
    }

}
