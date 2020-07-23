package com.piatek.rafal.account_service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;

public class AdultValidator implements ConstraintValidator<Adult, String> {

    @Override
    public void initialize(Adult adult) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true;

        try {
            return Period.between(
                    getDateOfBirthFromPesel(value),
                    LocalDate.now()
            ).getYears() > 18;
        } catch (DateTimeException exc) {
            return false;
        }
    }

    private LocalDate getDateOfBirthFromPesel(String pesel) {
        int[] peselAsIntegerArray = pesel
                .chars()
                .map(aaa -> Character.getNumericValue(aaa))
                .toArray();

        var year = 1900 + peselAsIntegerArray[0] * 10 + peselAsIntegerArray[1];
        if (peselAsIntegerArray[2] >= 2 && peselAsIntegerArray[2] < 8)
            year += Math.floor(peselAsIntegerArray[2] / 2) * 100;
        if (peselAsIntegerArray[2] >= 8)
            year -= 100;

        var month = (peselAsIntegerArray[2] % 2) * 10 + peselAsIntegerArray[3];
        var dayOfMonth = peselAsIntegerArray[4] * 10 + peselAsIntegerArray[5];

        return LocalDate.of(year, month, dayOfMonth);
    }

}
