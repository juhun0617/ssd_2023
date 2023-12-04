package org.example.draw;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author juhun_park
 * 배경 패널을 생성해주는 클래스 입니다
 * 이 클래스는 JPanel의 상속을 받습니다
 */
public class BackGroundPanel extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    public float transparency = 1.0f;


    private Image backgroundImage;

    /**
     * BackGroundPanel 객체의 생성자 입니다
     * 이미지를 로드 후 패널을 투명화 합니다
     *
     * @param imagePath : 배경이미지 경로
     */
    public BackGroundPanel(String imagePath) {
        loadImage(imagePath);
        setOpaque(false);

    }

    /**
     * 이미지를 로드해주는 메서드 입니다
     *
     * @param imagePath : 배경이미지 경로
     */
    private void loadImage(String imagePath) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
        backgroundImage = icon.getImage()
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    }

    /**
     * 요소를 그려주는 메서드를 오버라이드
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2d.drawImage(backgroundImage, 0, 0, this);
        g2d.dispose();
    }


};


