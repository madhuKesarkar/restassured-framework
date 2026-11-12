package com.madhu.api.data;

import com.madhu.api.models.Booking;
import com.madhu.api.models.BookingDates;
import net.datafaker.Faker;

import java.time.LocalDate;

public final class BookingFactory {
    private static final Faker FAKER = new Faker();

    private BookingFactory() {}

    public static Booking validBooking() {
        LocalDate checkin = LocalDate.now().plusDays(FAKER.number().numberBetween(1, 60));
        return new Booking(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.number().numberBetween(50, 1000),
                true,
                new BookingDates(checkin.toString(), checkin.plusDays(3).toString()),
                "Breakfast");
    }
}