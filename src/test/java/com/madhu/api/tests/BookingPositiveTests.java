package com.madhu.api.tests;

import com.madhu.api.base.BaseTest;
import com.madhu.api.data.BookingFactory;
import com.madhu.api.models.Booking;
import com.madhu.api.models.BookingResponse;
import com.madhu.api.specs.SpecBuilder;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class BookingPositiveTests extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    public void getAllBookingIdsReturnsList() {
        bookingClient.getAllIds(Map.of())
                .then().spec(SpecBuilder.okJson())
                .body("size()", greaterThan(0));
    }

    @Test(groups = {"smoke", "regression"})
    public void createBookingReturnsIdAndEchoesRequest() {
        Booking request = BookingFactory.validBooking();

        Response response = bookingClient.create(request);

        response.then()
                .spec(SpecBuilder.okJson())
                .body(matchesJsonSchemaInClasspath("schemas/create-booking-response.json"));

        BookingResponse body = response.as(BookingResponse.class);
        assertThat(body.bookingid()).isPositive();
        assertThat(body.booking()).isEqualTo(request);   // what we sent is what was saved
    }

    @Test(groups = "regression")
    public void getBookingReturnsWhatWasCreated() {
        Booking request = BookingFactory.validBooking();
        int id = bookingClient.create(request).then().statusCode(200).extract().path("bookingid");

        Booking fetched = bookingClient.get(id)
                .then().spec(SpecBuilder.okJson())
                .body(matchesJsonSchemaInClasspath("schemas/booking.json"))
                .extract().as(Booking.class);

        assertThat(fetched).isEqualTo(request);
    }

    @Test(groups = "regression")
    public void updateBookingWithTokenReplacesIt() {
        int id = createBookingAndGetId();
        Booking updated = BookingFactory.validBooking();

        Booking result = bookingClient.update(id, updated)
                .then().spec(SpecBuilder.okJson())
                .extract().as(Booking.class);

        assertThat(result).isEqualTo(updated);
    }

    @Test(groups = "regression")
    public void partialUpdateChangesOnlyGivenFields() {
        Booking original = BookingFactory.validBooking();
        int id = bookingClient.create(original).then().statusCode(200).extract().path("bookingid");

        Booking result = bookingClient.partialUpdate(id, Map.of("firstname", "Madhu"))
                .then().spec(SpecBuilder.okJson())
                .extract().as(Booking.class);

        assertThat(result.firstname()).isEqualTo("Madhu");
        assertThat(result.lastname()).isEqualTo(original.lastname());      // untouched
        assertThat(result.totalprice()).isEqualTo(original.totalprice());  // untouched
    }

    @Test(groups = "regression")
    public void deletedBookingIsGone() {
        int id = createBookingAndGetId();

        // Restful-Booker returns 201 for DELETE (a quirk; most APIs return 204)
        bookingClient.delete(id).then().statusCode(201);
        bookingClient.get(id).then().statusCode(404);
    }
}