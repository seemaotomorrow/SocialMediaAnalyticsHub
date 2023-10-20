package com.example.dataanalyticshub;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateUserInputTest {

    @Test
    void hasComma_InputHasComma() {
        String input = "hello, world";
        boolean expectValue = true;

        ValidateUserInput validateUserInput = new ValidateUserInput();
        boolean actualValue = validateUserInput.hasComma(input);
        assertEquals(expectValue, actualValue);
    }

    @Test
    void hasComma_InputHasNoComma() {
        String input = "hello world";
        boolean expectValue = false;

        ValidateUserInput validateUserInput = new ValidateUserInput();
        boolean actualValue = validateUserInput.hasComma(input);
        assertEquals(expectValue, actualValue);
    }

    @Test
    void isPositiveInteger__InputIsPositiveInt() {
        String input = "10";
        boolean expectValue = true;

        ValidateUserInput validateUserInput = new ValidateUserInput();
        boolean actualValue = validateUserInput.isPositiveInteger(input);
        assertEquals(expectValue, actualValue);

    }

    @Test
    void isPositiveInteger__InputIsNotPositiveInt() {
        String input = "-3";
        boolean expectValue = false;

        ValidateUserInput validateUserInput = new ValidateUserInput();
        boolean actualValue = validateUserInput.isPositiveInteger(input);
        assertEquals(expectValue, actualValue);

    }
    @Test
    void isPositiveInteger_InputIsNotInt() {
        String input = "hello";
        boolean expectValue = false;

        ValidateUserInput validateUserInput = new ValidateUserInput();
        boolean actualValue = validateUserInput.isPositiveInteger(input);
        assertEquals(expectValue, actualValue);

    }
    @Test
    void validateDateFromUser_CorrectFormatAndValidDate() {
        String date = "22/03/2023 12:12";
        boolean expectValue = true;

        ValidateUserInput validateUserInput = new ValidateUserInput();
        boolean actualValue = validateUserInput.validateDateFromUser(date);
        assertEquals(expectValue, actualValue);
    }
    @Test
    void validateDateFromUser_WrongFormat() {
        String date = "12/3/2022 12";
        boolean expectValue = false;

        ValidateUserInput validateUserInput = new ValidateUserInput();
        boolean actualValue = validateUserInput.validateDateFromUser(date);
        assertEquals(expectValue, actualValue);
    }

    @Test
    void validateDateFromUser_CorrectFormatInvalidDate() {
        String invalidDate = "12/33/2022 44:44";
        boolean expectValue = false;

        ValidateUserInput validateUserInput = new ValidateUserInput();
        boolean actualValue = validateUserInput.validateDateFromUser(invalidDate);
        assertEquals(expectValue, actualValue);
    }


}
