
package com.madhu.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)      // null fields are left out of the JSON
@JsonIgnoreProperties(ignoreUnknown = true)
public record Booking(
        String firstname,
        String lastname,
        Integer totalprice,
        Boolean depositpaid,
        BookingDates bookingdates,
        String additionalneeds) {

    public Booking withTotalprice(Integer price) {
        return new Booking(firstname, lastname, price, depositpaid, bookingdates, additionalneeds);
    }

    public Booking withFirstname(String name) {
        return new Booking(name, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
    }
}