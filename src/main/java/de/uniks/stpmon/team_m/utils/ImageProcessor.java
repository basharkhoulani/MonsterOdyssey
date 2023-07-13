package de.uniks.stpmon.team_m.utils;


import de.uniks.stpmon.team_m.App;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import okhttp3.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.*;

public class ImageProcessor {
    public static String toBase64(String inputImagePath) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(inputImagePath));
            return getBase64(originalImage);
        } catch (IOException e) {
            return IMAGE_PROCESSING_ERROR;
        }
    }

    public static javafx.scene.image.Image fromBase64ToFXImage(String avatar) {
        javafx.scene.image.Image image;
        if (avatar == null) {
            image = new javafx.scene.image.Image(Objects.requireNonNull(App.class.getResource(AVATAR_1)).toString());
            return image;
        }
        if (avatar.startsWith("data:image/png;base64, ")) {
            byte[] imageBytes = Base64.getDecoder().decode(avatar.replaceFirst("data:image/png;base64, ", ""));
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            image = new javafx.scene.image.Image(bis);
        } else if (avatar.startsWith("data:image/jpg;base64, ")) {
            byte[] imageBytes = Base64.getDecoder().decode(avatar.replaceFirst("data:image/jpg;base64, ", ""));
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            image = new javafx.scene.image.Image(bis);
        } else {
            image = new javafx.scene.image.Image(Objects.requireNonNull(App.class.getResource(AVATAR_2)).toString());
        }
        return image;
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

        java.awt.Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, java.awt.Image.SCALE_SMOOTH);

        BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return bufferedImage;
    }

    /**
     * This method is used to crop the
     *
     * @param trainerChunk into 6 Images, that displays the player character moving in some direction
     * @param direction    : either 0 [right], 1 [up], 2 [left], or 3 [down] are possible directions
     * @param isWalking    : determine if the character is moving or not
     */
    public static Image[] cropTrainerImages(Image trainerChunk, int direction, Boolean isWalking) {
        Image[] array = new Image[6];
        int x, y;
        if (isWalking) {
            y = 69;
        } else {
            y = 37;
        }
        switch (direction) {
            case 0 -> x = 0;
            case 1 -> x = 96;
            case 2 -> x = 192;
            default -> x = 288;
        }
        for (int i = 0; i < 6; i++) {
            array[i] = getSubImage(trainerChunk, x, y, 16, 27);
            x += 16;
        }
        return array;
    }

    public static Image getSubImage(Image img, int x, int y, int w, int h) {
        if (img != null) {
            PixelReader reader = img.getPixelReader();
            return new WritableImage(reader, x, y, w, h);
        }
        return null;
    }

    /**
     * This method is used to crop the
     *
     * @param premadeCharacter for instance "Premade_Character_01.png", also found in Constants
     * @param x                coordinate of upper right corner of image
     * @param y                coordinate of upper right corner of image
     * @param w                width of image
     * @param h                height of image
     */
    public static WritableImage showScaledCharacter(String premadeCharacter, int x, int y, int w, int h) {
        try {
            BufferedImage bufferedImage = ImageIO.read(Objects.requireNonNull(App.class.getResource("charactermodels/" + premadeCharacter)));
            BufferedImage bufferedImageFrontView = bufferedImage.getSubimage(x, y, w, h);
            BufferedImage scaledBufferedImage = ImageProcessor.scaleImage(bufferedImageFrontView, 6);
            javafx.scene.image.WritableImage writableImage = new javafx.scene.image.WritableImage(scaledBufferedImage.getWidth(), scaledBufferedImage.getHeight());
            javafx.scene.image.PixelWriter pixelWriter = writableImage.getPixelWriter();
            for (int y1 = 0; y1 < scaledBufferedImage.getHeight(); y1++) {
                for (int x1 = 0; x1 < scaledBufferedImage.getWidth(); x1++) {
                    pixelWriter.setArgb(x1, y1, scaledBufferedImage.getRGB(x1, y1));
                }
            }
            return writableImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public static Image resonseBodyToJavaFXImage(ResponseBody responseBody) throws IOException {
        responseBody.source();
        try (InputStream inputStream = responseBody.byteStream()) {
            byte[] imageData = toByteArray(inputStream);
            return new Image(new ByteArrayInputStream(imageData));
        }
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }
}

