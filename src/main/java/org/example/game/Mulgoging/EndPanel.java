package org.example.game.Mulgoging;

import org.example.ui.DamaUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class EndPanel extends JPanel {

    private JLabel scoreLabel; // 점수 레이블
    private Mulgoging mainClass; // 메인 클래스 인스턴스
    private Image backgroundImage; // 배경 이미지
    private JFrame frame; // 프레임 인스턴스

    public EndPanel(Mulgoging mainClass, JFrame frame, int score) {
        this.mainClass = mainClass;
        this.frame = frame;

        setLayout(null); // 레이아웃 매니저 비활성화

        loadImage("/Mulgoging/Image/점수창.png"); // 배경 이미지 로드
        setupScoreLabel(score); // 점수 레이블 설정
        addButtons(); // 버튼 추가
        setOpaque(false); // 배경 투명
    }

    /**
     * 점수 레이블을 설정합니다.
     *
     * @param score 점수
     */
    private void setupScoreLabel(int score) {
        scoreLabel = new JLabel(String.valueOf(score)); // 점수 레이블 생성
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28)); // 점수 레이블 폰트 설정
        scoreLabel.setHorizontalAlignment(JLabel.CENTER); // 점수 레이블 가운데 정렬
        scoreLabel.setBounds(410, 330, 80, 30); // 점수 레이블 위치 및 크기 설정
        add(scoreLabel); // 점수 레이블 추가
    }

    /**
     * 배경 이미지를 로드합니다.
     *
     * @param imagePath 배경 이미지 경로
     */
    private void loadImage(String imagePath) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
        backgroundImage = icon.getImage();
    }

    /**
     * 버튼을 추가합니다.
     */
    private void addButtons() {
        JButton restartButton = createImageButton("/Mulgoging/Image/restart.png", e -> mainClass.restartGame());
        JButton exitButton = createImageButton("/Mulgoging/Image/exit.png", e -> {
            frame.dispose();
        });

        // 버튼의 위치와 크기를 설정합니다.
        restartButton.setBounds(210, 348, 200, 100); // 재시작 버튼의 위치와 크기
        exitButton.setBounds(396, 348, 200, 100); // 종료 버튼의 위치와 크기

        add(restartButton);
        add(exitButton);
    }

    /**
     * 이미지 버튼을 생성합니다.
     *
     * @param imagePath      이미지 경로
     * @param actionListener 액션 리스너
     * @return 이미지 버튼
     */
    private JButton createImageButton(String imagePath, ActionListener actionListener) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
        JButton button = new JButton(icon);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.addActionListener(actionListener);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * 점수를 업데이트합니다.
     *
     * @param score 새로운 점수
     */
    public void updateScore(int score) {
        scoreLabel.setText(String.valueOf(score));
    }
}