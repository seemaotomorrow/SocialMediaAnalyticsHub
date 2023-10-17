package com.example.dataanalyticshub;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    static DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static LocalDateTime parseDateString(String input){
        // custom the date format
        // turn input String into DateTime
        // wished customized format
        return LocalDateTime.parse(input, inputFormatter);
    }
}
