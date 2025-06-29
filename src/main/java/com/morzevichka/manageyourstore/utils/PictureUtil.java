package com.morzevichka.manageyourstore.utils;

import com.morzevichka.manageyourstore.entity.Worker;
import com.morzevichka.manageyourstore.services.WorkerServiceImpl;
import com.morzevichka.manageyourstore.services.impl.WorkerService;

import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class PictureUtil {
   private static final String DEFAULT_IMAGE_FORMAT = "jpg";

   private PictureUtil() {}

   public static Image loadImageFromFileChooser(Window window, Long id) throws IOException {
      FileChooser fileChooser = new FileChooser();
      fileChooser.getExtensionFilters().add(
              new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png")
      );
      File selectedFile = fileChooser.showOpenDialog(window);

      if (selectedFile == null) {
         throw new IllegalArgumentException("Файл не был выбран");
      }
      saveImage(selectedFile, id);
       return loadImage(id);
   }

   public static Image loadImage(Long id) throws IOException {
      WorkerService workerService = new WorkerServiceImpl();
      Worker worker = workerService.findWorker(id);

      if (worker.getProfilePicture() == null) {
         throw new IllegalStateException("У пользователя не загружена фотография");
      }

      BufferedImage bufferedImage = convertFromByteArrayToImage(worker.getProfilePicture());
      return toFXImage(bufferedImage);
   }

   public static void saveImage(File file, Long id) throws IOException {
      BufferedImage image = ImageIO.read(file);
      byte[] byteImage = convertFromImageToByteArray(image, DEFAULT_IMAGE_FORMAT);

      WorkerService workerService = new WorkerServiceImpl();
      Worker worker = workerService.findWorker(id);

      worker.setProfilePicture(byteImage);
      workerService.updateWorker(worker);
   }

   public static byte[] convertFromImageToByteArray(BufferedImage image, String format) throws IOException {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ImageIO.write(image, format, byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();
   }

   public static BufferedImage convertFromByteArrayToImage(byte[] byteImage) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteImage);
      BufferedImage image = ImageIO.read(byteArrayInputStream);

      if (image == null) {
         throw new IOException("Не удалось декодировать изображение");
      }
      return image;
   }

   public static BufferedImage toAWTImage(Image FXImage) {
      return SwingFXUtils.fromFXImage(FXImage, null);
   }

   public static Image toFXImage(BufferedImage AWTImage) {
      return SwingFXUtils.toFXImage(AWTImage, null);
   }
}
