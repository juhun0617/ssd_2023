package org.example.etc;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ShadowButton extends JButton {
    private static final int SHADOW_SIZE = 5;
    private static final int SHADOW_OFFSET = 2;
    private static final int CORNER_SIZE = 10;

    private ImageIcon buttonIcon;
    private boolean mouseOver = false;
    private String imagePath;

    public ShadowButton(String text, String imagePath) {
        super(text);
        this.imagePath = imagePath;
        loadImageIcon(imagePath);
        initializeButton();
    }

    private void initializeButton() {
        setOpaque(false); // 버튼 배경 투명 처리
        setContentAreaFilled(false); // 내용 영역 배경 그리기 안 함
        setBorderPainted(false); // 테두리 그리기 안 함
        setFocusPainted(false); // 포커스 그리기 안 함
        setRolloverEnabled(true); // 마우스 오버 감지 활성화

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mouseOver = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mouseOver = false;
                repaint();
            }
        });
    }

    private void loadImageIcon(String path) {
        if (path != null && !path.trim().isEmpty()) {
            buttonIcon = new ImageIcon(getClass().getResource(path));
        } else {
            buttonIcon = null;
        }
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            buttonIcon = new ImageIcon(getClass().getResource(imagePath));
        } else {
            buttonIcon = null;
        }
        repaint(); // 버튼 다시 그리기
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 그림자 효과
        if (mouseOver && buttonIcon != null) {
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(SHADOW_OFFSET, SHADOW_OFFSET, getWidth() - SHADOW_SIZE, getHeight() - SHADOW_SIZE, CORNER_SIZE, CORNER_SIZE);
        }

        // 버튼 배경 이미지
        if (buttonIcon != null) {
            g2.drawImage(buttonIcon.getImage(), 0, 0, getWidth() - SHADOW_SIZE, getHeight() - SHADOW_SIZE, this);
        }

        // 텍스트
        FontMetrics metrics = g.getFontMetrics(getFont());
        int x = (getWidth() - metrics.stringWidth(getText())) / 2;
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setColor(getForeground());
        g2.drawString(getText(), x, y);

        g2.dispose();
        super.paintComponent(g);
    }
}
