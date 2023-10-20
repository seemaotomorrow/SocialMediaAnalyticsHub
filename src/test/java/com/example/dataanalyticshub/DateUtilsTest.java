package com.example.dataanalyticshub;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void parseDateString_validDateTime() {
            String input = "20/10/2023 15:30";
            LocalDateTime expectedDateTime = LocalDateTime.of(2023, 10, 20, 15, 30);

            LocalDateTime parsedDateTime = DateUtils.parseDateString(input);

            assertEquals(expectedDateTime, parsedDateTime);
    }


}
