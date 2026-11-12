package com.madhu.api.specs;

import com.madhu.api.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.lessThan;

public final class SpecBuilder {
    private SpecBuilder() {}

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.get("base.url"))
                .setContentType(ContentType.JSON)
                .setAccept("application/json")
                .log(LogDetail.URI)
                .build();
    }

    public static ResponseSpecification okJson() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(5000L))
                .build();
    }
}