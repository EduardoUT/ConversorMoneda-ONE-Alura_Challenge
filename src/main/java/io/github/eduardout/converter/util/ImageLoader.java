package io.github.eduardout.converter.util;

import javax.swing.*;
import java.awt.*;

public class JLabelImageLoader extends JLabel {
    private JLabelImageLoader() {
        throw new UnsupportedOperationException("Utility class.");
    }

    @Override
    public void paint(Graphics g, String fileName) {
        Image image = new ImageIcon(fileName)
        super.paint(g);
    }
}
