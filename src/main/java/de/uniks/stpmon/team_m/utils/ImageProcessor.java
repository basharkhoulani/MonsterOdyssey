package de.uniks.stpmon.team_m.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static de.uniks.stpmon.team_m.Constants.IMAGE_PROCESSING_ERROR;
import static de.uniks.stpmon.team_m.Constants.MAX_BASE64_LENGTH;

public class ImageProcessor {
    public static String toBase64(String inputImagePath) {
        try {
            // Originalbild laden
            BufferedImage originalImage = ImageIO.read(new File(inputImagePath));
            return getBase64(originalImage);
        } catch (IOException e) {
            return IMAGE_PROCESSING_ERROR;
        }
    }

    public static javafx.scene.image.Image toFXImage(String base64) {
        byte[] imageBytes = Base64.getDecoder().decode(base64);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        return new javafx.scene.image.Image(bis);
    }

    private static String getBase64(BufferedImage image) throws IOException {
        double scaleFactor = 1.0;
        while (true) {
            BufferedImage scaledImage = scaleImage(image, scaleFactor);
            String base64Image;
            base64Image = convertToBase64(scaledImage);

            if (base64Image.length() <= MAX_BASE64_LENGTH) {
                return base64Image;
            }

            scaleFactor *= 0.9;
        }
    }

    private static BufferedImage scaleImage(BufferedImage image, double scaleFactor) {
        int scaledWidth = (int) (image.getWidth() * scaleFactor);
        int scaledHeight = (int) (image.getHeight() * scaleFactor);

        Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return bufferedImage;
    }

    private static String convertToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();

        return Base64.getEncoder().encodeToString(imageBytes);
    }
}