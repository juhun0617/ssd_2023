package org.example.game.Mulgoging;

import org.example.ui.DamaUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EndPanel extends JPanel {

    private JLabel scoreLabel;
    private Mulgoging mainClass;
    private Image backgroundImage;
    private JFrame frame;

    public EndPanel(Mulgoging mainClass,JFrame frame, int score) {
        this.mainClass = mainClass;
        this.frame = frame;
        setLayout(null); // 레이아웃 매니저를 비활성화합니다.

        loadImage("src/Image/점수창.png");
        setupScoreLabel(score);
        addButtons();
        setOpaque(false);
    }

    private void setupScoreLabel(int score) {
        scoreLabel = new JLabel(String.valueOf(score));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setBounds(410, 298, 80, 30); // 점수 레이블의 위치와 크기를 지정합니다.
        add(scoreLabel);
    }

    private void loadImage(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        backgroundImage = icon.getImage();
    }

    private void addButtons() {
        JButton restartButton = createImageButton("src/Image/restart.png", e -> mainClass.restartGame());
        JButton exitButton = createImageButton("src/Image/exit.png", e ->{
            frame.dispose();
        });

        // 버튼의 위치와 크기를 설정합니다.
        restartButton.setBounds(210, 330, 200, 100); // 재시작 버튼의 위치와 크기
        exitButton.setBounds(396, 330, 200, 100); // 종료 버튼의 위치와 크기

        add(restartButton);
        add(exitButton);
    }

    private JButton createImageButton(String imagePath, ActionListener actionListener) {
        ImageIcon icon = new ImageIcon(imagePath);
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

    public void updateScore(int score) {
        scoreLabel.setText(String.valueOf(score));
    }

}
