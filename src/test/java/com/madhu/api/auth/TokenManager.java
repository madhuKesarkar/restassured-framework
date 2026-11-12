package com.madhu.api.auth;

import com.madhu.api.config.ConfigManager;
import com.madhu.api.specs.SpecBuilder;

import java.util.Map;

import static io.restassured.RestAssured.given;

public final class TokenManager {
    private static String token;

    private TokenManager() {}

    public static synchronized String getToken() {
        if (token == null) {
            token = given()
                    .spec(SpecBuilder.requestSpec())
                    .body(Map.of(
                            "username", ConfigManager.get("auth.username"),
                            "password", ConfigManager.get("auth.password")))
                    .when()
                    .post("/auth")
                    .then()
                    .statusCode(200)
                    .extract().path("token");
            if (token == null) throw new IllegalStateException("Login failed: no token returned");
        }
        return token;
    }
}