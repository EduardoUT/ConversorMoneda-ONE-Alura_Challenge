package io.github.eduardout.converter.util;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageLoader {

    private List<Image> images;
    private static String fileDirectory;
    private Window window;

    private ImageLoader() {
        throw new UnsupportedOperationException("Utility class.");
    }

    public void addImage(String fileName) {
        images.add(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(fileName)));
    }

    public List<Image> getImagesList() {
        if(images.isEmpty()) {
            throw new IllegalStateException("No images were loaded on the list.");
        }
        return images;
    }

    public static Image getImage(String fileName) {
        if (Objects.isNull(fileName) || fileName.isEmpty()) {
            throw new IllegalArgumentException("The given filepath is null or empty.");
        }
        return Toolkit.getDefaultToolkit()
                .getImage(ClassLoader.getSystemResource(fileName));
    }
}
