package org.example.etc;

import java.awt.*;
import java.io.InputStream;

public class CustomFont {
    private static final String FONT_PATH = "/font/neodgm.ttf";
    private static Font customFont;

    public static Font loadCustomFont(float size) {
        if (customFont == null) {
            try (InputStream is = CustomFont.class.getResourceAsStream(FONT_PATH)) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return customFont.deriveFont(size);
    }

}