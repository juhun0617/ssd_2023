package org.example.game.Mulgoging;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

/**
 * 게임 내에서 물고기를 표현하는 클래스입니다.
 * 물고기의 위치, 속도, 각도 및 이미지를 관리하며, 물고기의 움직임 및 상태를 제어합니다.
 */
public class Fish {
    private static final double SPEED = 1.0; // 이동 속도 상수
    private static final double ANGLE_CHANGE = Math.toRadians(10); // 각도 변화 최대치 상수
    private static final int MIN_X = 400; // 화면의 최소 X 좌표
    private static final int MAX_X = 800; // 화면의 최대 X 좌표
    private static final int MIN_Y = 450; // 화면의 최소 Y 좌표
    private static final int MAX_Y = 800; // 화면의 최대 Y 좌표
    private final double speed; // 물고기의 속도
    private final Image image; // 물고기 이미지
    private final int panelWidth;
    private final int panelHeight; // 화면 크기
    private final double tailSwing = 0; // 꼬리 흔들림 각도
    private double x, y; // 물고기의 현재 위치
    private double angle; // 물고기의 현재 방향 각도

    /**
     * 물고기 객체를 초기화합니다.
     *
     * @param x           물고기의 초기 X 좌표
     * @param y           물고기의 초기 Y 좌표
     * @param speed       물고기의 속도
     * @param imagePath   물고기 이미지의 파일 경로
     * @param panelWidth  화면의 너비
     * @param panelHeight 화면의 높이
     */
    public Fish(double x, double y, double speed, String imagePath, int panelWidth, int panelHeight) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = Math.random() * 2 * Math.PI;
        this.image = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))).getImage();
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    /**
     * 물고기의 현재 X 좌표를 반환합니다.
     *
     * @return 물고기의 X 좌표
     */
    public double getX() {
        return x;
    }

    /**
     * 물고기의 현재 Y 좌표를 반환합니다.
     *
     * @return 물고기의 Y 좌표
     */
    public double getY() {
        return y;
    }

    /**
     * 물고기의 이미지를 반환합니다.
     *
     * @return 물고기 이미지
     */
    public Image getImage() {
        return image;
    }

    /**
     * 물고기의 움직임을 관리합니다.
     * 이 메소드는 물고기의 방향을 약간 랜덤하게 조절하고, 새 위치로 이동시킵니다.
     * 화면 경계에서의 충돌 처리도 포함합니다.
     *
     * @param panelWidth  화면의 너비
     * @param panelHeight 화면의 높이
     */
    public void move(int panelWidth, int panelHeight) {
        angle += Math.random() * ANGLE_CHANGE - ANGLE_CHANGE / 1.2;
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

    /**
     * 물고기를 화면에 그립니다.
     * 이 메소드는 물고기의 위치와 각도에 따라 이미지를 회전시키고 화면에 표시합니다.
     *
     * @param g2d 그래픽스 2D 컨텍스트
     */
    public void draw(Graphics2D g2d) {
        AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
        transform.rotate(angle, image.getWidth(null) / 2.0, image.getHeight(null) / 2.0);
        g2d.drawImage(image, transform, null);
    }


}