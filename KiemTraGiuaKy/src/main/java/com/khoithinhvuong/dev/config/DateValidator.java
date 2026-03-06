package com.khoithinhvuong.dev.config;

import java.time.DateTimeException;
import java.time.LocalDate;

public class DateValidator {

    public static boolean isValidDate(int day, int month, int year) {
        try {
            LocalDate.of(year, month, day);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

}
