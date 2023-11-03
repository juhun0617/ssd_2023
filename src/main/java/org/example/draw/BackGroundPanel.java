package org.example.draw;

import org.example.BackgroundMusic;

import javax.swing.*;
import java.awt.*;
import java.nio.FloatBuffer;
import java.util.Objects;

public class BackGroundPanel extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    public float transparency = 1.0f;


    private Image backgroundImage;

    public BackGroundPanel(String imagePath) {
        loadImage(imagePath);
        setOpaque(false);

    }

    private void loadImage(String imagePath) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
        backgroundImage = icon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2d.drawImage(backgroundImage, 0, 0, this);
        g2d.dispose();
    }


};


