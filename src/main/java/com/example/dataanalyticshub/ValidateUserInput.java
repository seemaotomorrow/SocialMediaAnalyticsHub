package com.example.dataanalyticshub;

import java.time.DateTimeException;

public class ValidateUserInput {

    // Validate if a string has no comma
    public boolean hasComma(String input){
            if(input.contains(",")){
                return true;
            } else{
                return false;
            }
    }


    // Validate if a string is a positive integer
    public boolean isPositiveInteger(String str) {
        try {
            int num = Integer.parseInt(str);
            return num >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validate if a string is in the correct date format
    public boolean validateDateFromUser (String input){
            String regex = "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}";
            if(input.matches(regex)){
                try{
                    //noinspection ResultOfMethodCallIgnored
                    DateUtils.parseDateString(input);
                    return true;
                } catch (DateTimeException e){
                    System.out.println("Invalid date");
                    return false;
                }
            } else {
                System.out.println("Enter a valid date and time format of DD/MM/YYYY HH:MM: ");
                return false;
            }


    }
}
