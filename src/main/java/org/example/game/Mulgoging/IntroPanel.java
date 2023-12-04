package org.example.game.Mulgoging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IntroPanel extends JPanel {

    private Mulgoging mainClass;
    private Image backgroundImage;

    public IntroPanel(Mulgoging mainClass) {
        this.mainClass = mainClass;

        // 배경 이미지 로드
        loadImage();

        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainClass.startGame();
            }
        });
    }

    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Mulgoging/Image/게임규칙.png"));
        backgroundImage = icon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 이미지 그리기
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        // 텍스트 또는 기타 요소 그리기



    }
}
