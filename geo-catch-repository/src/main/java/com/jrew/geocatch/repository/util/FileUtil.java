package com.jrew.geocatch.repository.util;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class FileUtil {

    /**
     *
     * @param file
     * @param data
     * @param extension
     * @throws IOException
     */
    public static void writeImageToFile(File file, byte[] data, String extension) throws IOException {

        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Uploaded image file binary data is empty.");
        }

        if (Base64.isArrayByteBase64(data)) {
            data = Base64.decodeBase64(data);
        }

        BufferedImage src = ImageIO.read(new ByteArrayInputStream(data));
        ImageIO.write(src, extension, file);
    }

    /**
     *
     * @param path
     * @return
     */
    public static File createFile(String path) {

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        return file;
    }

    /**
     *
     * @param file
     */
    public static void deleteFile(File file) {

        if (file.exists()) {
            file.delete();
        }
    }

}
