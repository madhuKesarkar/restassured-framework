package com.madhu.api.tests;

import com.madhu.api.base.BaseTest;
import com.madhu.api.data.BookingFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class BookingBoundaryTests extends BaseTest {

    // Practice requirement: totalprice must be between 1 and 10000

    @DataProvider
    public Object[][] validPriceBoundaries() {
        return new Object[][] { {1}, {2}, {9999}, {10000} };   // min, min+1, max-1, max
    }

    @Test(dataProvider = "validPriceBoundaries", groups = "regression")
    public void validPriceBoundariesAreAccepted(int price) {
        bookingClient.create(BookingFactory.validBooking().withTotalprice(price))
                .then().statusCode(200);
    }

    @DataProvider
    public Object[][] invalidPriceBoundaries() {
        return new Object[][] { {0}, {10001} };                // min-1, max+1
    }

    // Known bug: Restful-Booker has no price range validation
    @Test(dataProvider = "invalidPriceBoundaries", groups = "knownBug")
    public void invalidPriceBoundariesShouldBeRejected(int price) {
        bookingClient.create(BookingFactory.validBooking().withTotalprice(price))
                .then().statusCode(400);
    }

    // Edge cases: odd but legal input must be stored unchanged

    @DataProvider
    public Object[][] firstnameEdgeCases() {
        return new Object[][] {
                {"A"},                  // single character
                {"मधु"},                // unicode
                {"O'Neil-Smith"},       // special characters
                {"x".repeat(255)},      // long string
        };
    }

    @Test(dataProvider = "firstnameEdgeCases", groups = "regression")
    public void firstnameEdgeCasesAreStoredUnchanged(String name) {
        int id = bookingClient.create(BookingFactory.validBooking().withFirstname(name))
                .then().statusCode(200)
                .extract().path("bookingid");

        bookingClient.get(id)
                .then().statusCode(200)
                .body("firstname", equalTo(name));
    }
}