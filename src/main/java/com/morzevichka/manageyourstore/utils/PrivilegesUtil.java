package com.morzevichka.manageyourstore.utils;

import com.morzevichka.manageyourstore.dto.UserSesssion;
import com.morzevichka.manageyourstore.entity.Role;
import com.morzevichka.manageyourstore.entity.Worker;

import java.util.function.Consumer;

public final class PrivilegesUtil {
    private PrivilegesUtil() {}

    public static boolean checkAuthentification(UserSesssion userSesssion, Consumer<String> errorHandler) {
        if (!isUserAuthenticated(userSesssion)) {
            errorHandler.accept("Вы должны авторизоваться");
            return false;
        }
        return true;
    }

    public static boolean checkAdmin(Worker worker, Consumer<String> errorHandler) {
        if (!isUserAdmin(worker)) {
            errorHandler.accept("Недостаточно прав для этого действия");
            return false;
        }
        return true;
    }

    public static boolean isUserAdmin(Worker worker) {
        return worker.getRole().equals(Role.ADMIN);
    }

    public static boolean isUserAuthenticated(UserSesssion session) {
        return session.getWorker() != null;
    }
}
