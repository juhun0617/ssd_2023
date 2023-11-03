package org.example.etc;

import javax.swing.*;
import java.awt.*;

public class CustomRoundButton extends JButton {
    private final int shadowOffset = 2;
    private final int shadowAlpha = 128; // 그림자의 알파 값 (0~255)
    private final int cornerRadius = 10; // 모서리 둥근 정도

    public CustomRoundButton(String text) {
        super(text);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false); // 버튼의 내용 영역 배경을 그리지 않도록 설정
    }

    @Override
    protected void paintComponent(Graphics g) {
        final Graphics2D g2d = (Graphics2D) g.create();

        // 그림자 효과
        g2d.setColor(new Color(0, 0, 0, shadowAlpha)); // 진한 그림자를 위해 알파 값 설정
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 안티 앨리어싱 적용
        g2d.fillRoundRect(shadowOffset, shadowOffset, getWidth() - shadowOffset * 4, getHeight() - shadowOffset * 4, cornerRadius, cornerRadius);

        // 버튼 배경
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth() - shadowOffset * 4, getHeight() - shadowOffset * 4, cornerRadius, cornerRadius);

        g2d.dispose(); // 사용한 그래픽스 객체를 해제

        super.paintComponent(g);
    }
}
