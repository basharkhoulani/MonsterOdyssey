package de.uniks.stpmon.team_m.utils;

import javafx.scene.image.Image;
import okhttp3.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageProcessor {
    public static Image resonseBodyToJavaFXImage(ResponseBody responseBody) throws IOException {
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
