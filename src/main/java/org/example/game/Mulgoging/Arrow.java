

package org.example.game.Mulgoging;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

public class Arrow {
    private double arrowPosX;
    private double arrowPosY;
    private double arrowSpeed;
    private double arrowAngle;
    private Image arrowImage; // 화살 이미지
    private boolean isFlying = false;
    private double gravity = 20.8; // 중력 가속도
    private double arrowVelX; // 화살의 x축 속도
    private double arrowVelY; // 화살의 y축 속도

    private Timer flightTimer; // 화살의 운동을 관리하는 타이머


    public Arrow(double posX, double posY,  String imagePath) {
        this.arrowPosX = posX;
        this.arrowPosY = posY;

        this.arrowImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))).getImage(); // 이미지 경로 설정



    }// Image 타입으로 수정

    public void updateAngle() {
        if (!isFlying) return;

        // 현재의 속도 방향에 따라 각도를 계산
        this.arrowAngle = Math.atan2(arrowVelY, arrowVelX);
    }
    public void updatePosition(double deltaTime) {
        if (!isFlying) return;

        arrowPosX += arrowVelX * deltaTime;


        arrowVelY += gravity * deltaTime;

        arrowPosY += arrowVelY * deltaTime;

        // 화살의 방향을 올바르게 계산
        updateAngle();



    }

    public double getX() {
        return arrowPosX;
    }

    public double getY() {
        return arrowPosY;
    }

    public Image getImage() {
        return arrowImage;
    }
    public double getAngle() {
        return this.arrowAngle;

    }

    public void setSpeed(double speed) {
        this.arrowSpeed = speed;
        updateVelocity();
    }

    public void launch() {
        // 화살 발사 로직
        isFlying = true;
        updateVelocity();
        if (flightTimer == null) {
            flightTimer = new Timer(1, e -> updatePosition(0.02));
        }
        flightTimer.start();
    }


    private void updateVelocity() {
        //this.arrowVelX = Math.cos(Math.toRadians(arrowAngle)) * arrowSpeed;
        //this.arrowVelY = Math.sin(Math.toRadians(arrowAngle)) * arrowSpeed;
        this.arrowVelX = Math.cos(arrowAngle) * arrowSpeed;
        this.arrowVelY = Math.sin(arrowAngle) * arrowSpeed;

    }
    public void setAngle(double angle) {
        this.arrowAngle = angle;
        updateVelocity();
    }
    public boolean isMoving() {
        return isFlying;
    }

    // 화살 멈추는 메서드
    public void stop() {
        isFlying = false;
        if (flightTimer != null) {
            flightTimer.stop();
        }
        arrowVelX = 0;
        arrowVelY = 0;
    }
    // 화살의 위치를 초기화하는 메서드
    public void resetPosition(double posX, double posY) {
        this.arrowPosX = posX;
        this.arrowPosY = posY;
    }
    public void draw(Graphics2D g2d) {
        AffineTransform transform = new AffineTransform();
        int imageWidth = arrowImage.getWidth(null);
        int imageHeight = arrowImage.getHeight(null);

        transform.translate(arrowPosX, arrowPosY);

        if (isFlying) {
            // 화살이 날아가는 동안 회전
            transform.rotate(arrowAngle, imageWidth / 3.0, imageHeight / 2.0);
        } else {
            // 화살이 아직 날아가지 않은 경우
            transform.rotate(arrowAngle, imageWidth / 2.0, imageHeight / 2.0);
        }

        g2d.drawImage(arrowImage, transform, null);
    }



    // Getter 및 Setter 메서드
}