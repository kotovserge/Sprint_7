package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseHttpClient {

    private RequestSpecification baseRequestSpec = new RequestSpecBuilder()
            .setBaseUri(Url.HOST)
            .addHeader("Content-Type", "application/json")
            .setRelaxedHTTPSValidation()
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .addFilter(new ErrorLoggingFilter())
            .build();

    protected ValidatableResponse doGetRequest(String path) {
        return (ValidatableResponse) given()
                .spec(baseRequestSpec)
                .get(path)
                .thenReturn();
    }

    protected ValidatableResponse doPostRequest(String path, Object body) {
        return (ValidatableResponse) given()
                .spec(baseRequestSpec)
                .body(body)
                .post(path)
                .then();
    }

    protected ValidatableResponse doDeleteRequest(String path, Object body) {
        return given()
                .spec(baseRequestSpec)
                .body(body)
                .delete(path)
                .then();
    }

    protected ValidatableResponse doPutRequest(String path) {
        return given()
                .spec(baseRequestSpec)
                .put(path)
                .then();
    }
}
