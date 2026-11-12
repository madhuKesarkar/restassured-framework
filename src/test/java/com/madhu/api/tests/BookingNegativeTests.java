package com.madhu.api.tests;

import com.madhu.api.base.BaseTest;
import com.madhu.api.data.BookingFactory;
import com.madhu.api.specs.SpecBuilder;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BookingNegativeTests extends BaseTest {

    @DataProvider
    public Object[][] invalidIds() {
        return new Object[][] { {999999999}, {0}, {-1}, {"abc"} };
    }

    @Test(dataProvider = "invalidIds", groups = "regression")
    public void getWithInvalidIdReturns404(Object id) {
        bookingClient.get(id).then().statusCode(404);
    }

    @Test(groups = "regression")
    public void updateWithoutTokenIsForbidden() {
        int id = createBookingAndGetId();
        bookingClient.updateWithoutToken(id, BookingFactory.validBooking())
                .then().statusCode(403);
    }

    @Test(groups = "regression")
    public void deleteWithoutTokenIsForbidden() {
        int id = createBookingAndGetId();
        bookingClient.deleteWithoutToken(id).then().statusCode(403);
        bookingClient.get(id).then().statusCode(200);   // and nothing was deleted
    }

    @Test(groups = "regression")
    public void loginWithWrongPasswordGivesNoToken() {
        given().spec(SpecBuilder.requestSpec())
                .body(Map.of("username", "admin", "password", "wrong"))
                .post("/auth")
                .then()
                .body("reason", equalTo("Bad credentials"))
                .body("token", org.hamcrest.Matchers.nullValue());
    }

    // ---- Expected behaviour per good API design. Restful-Booker fails these (real bugs to report). ----

    @Test(groups = "knownBug")
    public void missingFirstnameShouldReturn400() {
        Map<String, Object> body = new HashMap<>(Map.of(
                "lastname", "K", "totalprice", 100, "depositpaid", true,
                "bookingdates", Map.of("checkin", "2026-12-01", "checkout", "2026-12-05")));
        bookingClient.create(body).then().statusCode(400);
    }

    @Test(groups = "regression")
    public void malformedJsonShouldReturn400() {
        bookingClient.create("{ \"firstname\": \"Broken\", ").then().statusCode(400);
    }
}