package org.example.game.Mulgoging;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

/**
 * 게임 내에서 화살을 표현하는 클래스입니다.
 * 화살의 위치, 속도, 각도 및 이미지를 관리하며, 화살의 움직임 및 상태를 제어합니다.
 */
public class Arrow {
    private final Image arrowImage; // 화살 이미지
    private final double gravity = 20.8; // 중력 가속도
    private double arrowPosX; // 화살의 X 좌표
    private double arrowPosY; // 화살의 Y 좌표
    private double arrowSpeed; // 화살의 속도
    private double arrowAngle; // 화살의 각도
    private boolean isFlying = false; // 화살이 날아가고 있는지 여부
    private double arrowVelX; // 화살의 x축 속도
    private double arrowVelY; // 화살의 y축 속도

    private Timer flightTimer; // 화살의 운동을 관리하는 타이머

    /**
     * 화살 객체를 초기화합니다.
     *
     * @param posX      화살의 초기 X 좌표
     * @param posY      화살의 초기 Y 좌표
     * @param imagePath 화살 이미지의 파일 경로
     */
    public Arrow(double posX, double posY, String imagePath) {
        this.arrowPosX = posX;
        this.arrowPosY = posY;
        this.arrowImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))).getImage();
    }

    /**
     * 화살의 현재 속도 방향에 따라 각도를 업데이트합니다.
     */
    public void updateAngle() {
        if (!isFlying) return;
        this.arrowAngle = Math.atan2(arrowVelY, arrowVelX);
    }

    /**
     * 주어진 시간 간격에 따라 화살의 위치를 업데이트합니다.
     *
     * @param deltaTime 업데이트 간의 시간 차이(초)
     */
    public void updatePosition(double deltaTime) {
        if (!isFlying) return;
        arrowPosX += arrowVelX * deltaTime;
        arrowVelY += gravity * deltaTime;
        arrowPosY += arrowVelY * deltaTime;
        updateAngle();
    }

    /**
     * 화살의 현재 X 좌표를 반환합니다.
     *
     * @return 화살의 X 좌표
     */
    public double getX() {
        return arrowPosX;
    }

    /**
     * 화살의 현재 Y 좌표를 반환합니다.
     *
     * @return 화살의 Y 좌표
     */
    public double getY() {
        return arrowPosY;
    }

    /**
     * 화살의 이미지를 반환합니다.
     *
     * @return 화살 이미지
     */
    public Image getImage() {
        return arrowImage;
    }

    /**
     * 화살의 현재 각도를 반환합니다.
     *
     * @return 화살의 각도(라디안)
     */
    public double getAngle() {
        return this.arrowAngle;
    }

    /**
     * 화살의 각도를 설정합니다.
     *
     * @param angle 설정할 화살의 각도(라디안)
     */
    public void setAngle(double angle) {
        this.arrowAngle = angle;
        updateVelocity();
    }

    /**
     * 화살의 속도를 설정합니다.
     *
     * @param speed 화살의 속도
     */
    public void setSpeed(double speed) {
        this.arrowSpeed = speed;
        updateVelocity();
    }

    /**
     * 화살을 발사합니다. 이 메소드는 화살의 속도와 타이머를 초기화하고 발사 상태로 설정합니다.
     */
    public void launch() {
        isFlying = true;
        updateVelocity();
        if (flightTimer == null) {
            flightTimer = new Timer(1, e -> updatePosition(0.02));
        }
        flightTimer.start();
    }

    /**
     * 화살의 속도를 현재 각도와 속도를 바탕으로 계산하여 업데이트합니다.
     */
    private void updateVelocity() {
        this.arrowVelX = Math.cos(arrowAngle) * arrowSpeed;
        this.arrowVelY = Math.sin(arrowAngle) * arrowSpeed;
    }

    /**
     * 화살이 움직이고 있는지 여부를 반환합니다.
     *
     * @return 화살이 움직이고 있다면 true, 그렇지 않으면 false
     */
    public boolean isMoving() {
        return isFlying;
    }

    /**
     * 화살의 움직임을 멈춥니다. 이는 타이머를 정지시키고 속도를 0으로 설정합니다.
     */
    public void stop() {
        isFlying = false;
        if (flightTimer != null) {
            flightTimer.stop();
        }
        arrowVelX = 0;
        arrowVelY = 0;
    }

    /**
     * 화살의 위치를 초기화합니다.
     *
     * @param posX 초기화할 X 좌표
     * @param posY 초기화할 Y 좌표
     */
    public void resetPosition(double posX, double posY) {
        this.arrowPosX = posX;
        this.arrowPosY = posY;
    }

    /**
     * 화살을 화면에 그립니다. 이 메소드는 화살의 현재 상태에 따라 화살을 회전시키고 그립니다.
     *
     * @param g2d 그래픽스 2D 컨텍스트
     */
    public void draw(Graphics2D g2d) {
        AffineTransform transform = new AffineTransform();
        int imageWidth = arrowImage.getWidth(null);
        int imageHeight = arrowImage.getHeight(null);

        transform.translate(arrowPosX, arrowPosY);
        if (isFlying) {
            transform.rotate(arrowAngle, imageWidth / 3.0, imageHeight / 2.0);
        } else {
            transform.rotate(arrowAngle, imageWidth / 2.0, imageHeight / 2.0);
        }
        g2d.drawImage(arrowImage, transform, null);
    }


}