package order;

import base.BaseHttpClient;
import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Step;

import constants.Url;

public class OrderApi extends BaseHttpClient{

    @Step("Send post order create")
    public ValidatableResponse create(Object body) {
        return doPostRequest(Url.ORDER_CREATE_API, body);
    }

    @Step("Send post order list")
    public ValidatableResponse getList() {
        return doGetRequest(Url.ORDER_LIST_API);
    }

    @Step("Send post order cancel")
    public ValidatableResponse cancel(int track) {
        return doPutRequest(Url.ORDER_CANCEL_API + "?track="+track);
    }
}
