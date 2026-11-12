package com.madhu.api.clients;

import com.madhu.api.auth.TokenManager;
import com.madhu.api.specs.SpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookingClient {

    private RequestSpecification request() {
        return given().spec(SpecBuilder.requestSpec());
    }

    private RequestSpecification authorisedRequest() {
        return request().cookie("token", TokenManager.getToken());
    }

    public Response getAllIds(Map<String, ?> queryParams) {
        return request().queryParams(queryParams).get("/booking");
    }

    public Response get(Object id) {
        return request().get("/booking/{id}", id);
    }

    public Response create(Object body) {
        return request().body(body).post("/booking");
    }

    public Response update(int id, Object body) {
        return authorisedRequest().body(body).put("/booking/{id}", id);
    }

    public Response updateWithoutToken(int id, Object body) {
        return request().body(body).put("/booking/{id}", id);
    }

    public Response partialUpdate(int id, Map<String, Object> fields) {
        return authorisedRequest().body(fields).patch("/booking/{id}", id);
    }

    public Response delete(int id) {
        return authorisedRequest().delete("/booking/{id}", id);
    }

    public Response deleteWithoutToken(int id) {
        return request().delete("/booking/{id}", id);
    }
}