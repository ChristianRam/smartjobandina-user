package com.smartjobandina.user.util;

import java.util.regex.Pattern;

public class RegexValidator {

    public static boolean patternMatches(String value, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(value)
                .matches();
    }
}
