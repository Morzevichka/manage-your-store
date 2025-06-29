package com.morzevichka.manageyourstore.utils;

import com.morzevichka.manageyourstore.App;
import com.morzevichka.manageyourstore.dto.UserSesssion;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.function.Consumer;

public final class LoadStagesUtil {
    private LoadStagesUtil() {}

    public static void loadStage(FXMLLoader fxmlLoader, String title) throws IOException {
        loadStage(fxmlLoader, title, null);
    }

    public static void loadStage(FXMLLoader fxmlLoader, String title, Runnable onClose) throws IOException {
        Parent parent = fxmlLoader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        stage.setOnHidden(event -> {
            if (onClose != null) onClose.run();
        });

        stage.show();
    }

    public static FXMLLoader loadFXML(String view) throws IOException {
        if (view == null) {
            throw new IllegalArgumentException("View cannot be null");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(view + ".fxml"));
        if (fxmlLoader.getLocation() == null) {
            throw new IOException("FXML file not found: " + view);
        }
        return fxmlLoader;
    }

    public static FXMLLoader loadFXMLLoaderWithController(
            String view,
            Class<?> targetControllerClass,
            Object... services
    ) throws IOException {
        return loadFXMLLoaderWithController(true, view, targetControllerClass, services);
    }

    public static FXMLLoader loadFXMLLoaderWithController(
            boolean requireCheckAuth,
            String view,
            Class<?> targetControllerClass,
            Object... services
    ) throws IOException {

        Consumer<String> errorHandler = message -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Ошибка доступа");
            errorAlert.setContentText(message);
            errorAlert.showAndWait();
        };

        if (requireCheckAuth && !PrivilegesUtil.checkAuthentification(UserSesssion.getInstance(), errorHandler)) {
            return null;
        }

        FXMLLoader fxmlLoader = loadFXML(view);

        fxmlLoader.setControllerFactory(clazz -> {

            if (clazz == targetControllerClass) {
                try {
                    for (Constructor<?> constructor:  clazz.getConstructors()) {
                        if (constructor.getParameterCount() == services.length) {
                            Class<?>[] paramTypes = constructor.getParameterTypes();
                            boolean match = true;
                            for (int i = 0; i < paramTypes.length; i++) {
                                if (!paramTypes[i].isAssignableFrom(services[i].getClass())) {
                                    match = false;
                                    break;
                                }
                            }
                            if (match) {
                                return constructor.newInstance(services);
                            }
                        }
                    }
                    throw new RuntimeException("No suitable constructor found for controller: " + clazz.getName());
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    return clazz.getConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException("Failed to instantiate default controller: " + clazz.getName(), e);
                }
            }
        });
        return fxmlLoader;
    }
}
