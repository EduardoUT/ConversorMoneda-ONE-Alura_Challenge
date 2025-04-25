package io.github.eduardout.converter.util;

import java.awt.Toolkit;
import java.awt.Image;
import java.util.Objects;

public class ImageLoader {

    private ImageLoader() {
        throw new UnsupportedOperationException("Utility class.");
    }

    public static Image getImage(String fileName) {
        if (Objects.isNull(fileName) || fileName.isEmpty()) {
            throw new IllegalArgumentException("The given filepath is null or empty.");
        }
        return Toolkit.getDefaultToolkit()
                .getImage(ClassLoader.getSystemResource(fileName));
    }
}
