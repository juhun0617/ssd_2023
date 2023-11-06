package org.example.etc;

import javax.swing.*;
import java.awt.*;

public class ImageTextOverlayLabel extends JLabel {
    private ImageIcon backgroundIcon;

    public ImageTextOverlayLabel(ImageIcon icon) {
        this.backgroundIcon = icon;
        setHorizontalAlignment(CENTER); // 텍스트 가운데 정렬
        setVerticalAlignment(CENTER); // 텍스트 수직 가운데 정렬
        setHorizontalTextPosition(CENTER); // 아이콘에 대한 텍스트 위치 가운데 정렬
        setVerticalTextPosition(CENTER); // 아이콘에 대한 텍스트 위치 수직 가운데 정렬
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundIcon != null) {
            g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}
