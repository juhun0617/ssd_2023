package org.example.game.Mulgoging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 게임의 소개 화면을 표현하는 클래스입니다.
 * 사용자가 화면을 클릭하면 게임이 시작됩니다.
 */
public class IntroPanel extends JPanel {

    private Mulgoging mainClass; // 게임의 메인 클래스 참조
    private Image backgroundImage; // 소개 화면의 배경 이미지

    /**
     * IntroPanel 객체를 생성하고 초기화합니다.
     * 배경 이미지를 로드하고, 마우스 리스너를 추가하여 클릭 이벤트를 처리합니다.
     *
     * @param mainClass 게임의 메인 클래스 인스턴스
     */
    public IntroPanel(Mulgoging mainClass) {
        this.mainClass = mainClass;

        // 배경 이미지 로드
        loadImage();

        // 패널 배경색 설정
        setBackground(Color.WHITE);
        // 마우스 리스너 추가
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainClass.startGame();
            }
        });
    }

    /**
     * 배경 이미지를 로드합니다.
     */
    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Mulgoging/Image/게임규칙.png"));
        backgroundImage = icon.getImage();
    }

    /**
     * 컴포넌트의 그래픽을 그립니다.
     * 이 메소드는 배경 이미지와 필요한 텍스트 또는 기타 요소를 패널에 그립니다.
     *
     * @param g 그래픽스 컨텍스트
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 이미지 그리기
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        // 텍스트 또는 기타 요소 그리기
        // 필요한 추가 그래픽 요소를 여기에 그립니다.
    }


}


