package org.example.game.Mulgoging;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

public class Fish {

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }
    private double x, y;
    private double speed;
    private double angle;
    private Image image;
    private int panelWidth, panelHeight; // 화면 크기
    private double tailSwing = 0; // 꼬리 흔들림 각도
    private static final double SPEED = 1.0; // 이동 속도
    private static final double ANGLE_CHANGE = Math.toRadians(10); // 각도 변화 최대치

    private static final int MIN_X = 400;
    private static final int MAX_X = 800;
    private static final int MIN_Y = 450;
    private static final int MAX_Y = 800;
    public Fish(double x, double y, double speed, String imagePath, int panelWidth, int panelHeight) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = Math.random() * 2 * Math.PI;
        this.image = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))).getImage();
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    public void move(int panelWidth,int panelHeight) {
        // 이전 방향에서 약간의 랜덤 변화를 추가
        angle += Math.random() * ANGLE_CHANGE - ANGLE_CHANGE / 1.2;

        // 이동
        x += SPEED * Math.cos(angle);
        y += SPEED * Math.sin(angle);

        // 화면 경계 처리
        if (x < MIN_X || x > MAX_X - image.getWidth(null)) {
            angle = Math.PI - angle;
        }
        if (y < MIN_Y || y > MAX_Y - image.getHeight(null)) {
            angle = 2 * Math.PI - angle;
        }
        x = Math.min(Math.max(x, MIN_X), MAX_X - image.getWidth(null));
        y = Math.min(Math.max(y, MIN_Y), MAX_Y - image.getHeight(null));


        // 각도 정규화
        if (angle < 0) angle += 2 * Math.PI;
        if (angle > 2 * Math.PI) angle -= 2 * Math.PI;
    }

    public void draw(Graphics2D g2d) {
        // 꼬리 흔들림 각도 업데이트

        AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
        transform.rotate(angle, image.getWidth(null) / 2.0, image.getHeight(null) / 2.0);
        g2d.drawImage(image, transform, null);
    }

    // Getter 및 Setter 필요시 추가
}
