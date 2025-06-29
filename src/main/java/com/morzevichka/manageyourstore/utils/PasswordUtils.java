package com.morzevichka.manageyourstore.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public final class PasswordUtils {
    private static final int WORKLOAD = 12;
    private static final String PEPPER;

    static {
        PEPPER = Optional.ofNullable(
                Dotenv.configure()
                        .directory("src/main/assets")
                        .filename("env")
                        .load()
                        .get("SECRET_KEY")
                ).orElseThrow(() -> new IllegalStateException("Pepper was not found in env file"));
    }

    private PasswordUtils() {}

    public static String encrypt(String valueToEncrypt) {
        String combined = valueToEncrypt + PEPPER;
        return BCrypt.hashpw(combined, BCrypt.gensalt(WORKLOAD));
    }

    public static boolean checkPassword(String valueToCheck, String storedHash) {
        String combined = valueToCheck + PEPPER;
        return BCrypt.checkpw(combined, storedHash);
    }
}
