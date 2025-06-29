package com.morzevichka.manageyourstore.utils;

import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+7\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})"
    );

    private ValidationUtil() {}

    public static boolean isValidEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Почта не соответствует формату");
        }
        return true;
    }

    public static boolean isValidPhone(String phone) {
        System.out.println(phone);
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Неправильный формат номера телефона");
        }
        return true;
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 16) {
            throw new IllegalArgumentException("Пароль должен быть от 8 до 16 символов");
        }
        if (!password.matches("[0-9a-zA-Z-_?!]+")) {
            throw new IllegalArgumentException("Пароль должен содеражать \"-_?!\"");
        }
        return true;
    }
}
