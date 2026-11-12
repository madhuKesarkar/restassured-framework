package com.madhu.api.base;

import com.madhu.api.clients.BookingClient;
import com.madhu.api.data.BookingFactory;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {
    protected BookingClient bookingClient;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        bookingClient = new BookingClient();
    }

    protected int createBookingAndGetId() {
        return bookingClient.create(BookingFactory.validBooking())
                .then().statusCode(200)
                .extract().path("bookingid");
    }
}