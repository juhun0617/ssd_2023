package org.example.etc;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;


/**
 * @author juhun_park
 * 사용자 정의 버튼 클래스.
 * 이 클래스는 그림자 효과와 둥근 모서리를 가진 버튼을 생성하며, 이미지와 텍스트를 함께 표시할 수 있습니다.
 */
public class ShadowButton extends JButton {
    private static final int SHADOW_SIZE = 5;
    private int SHADOW_OFFSET = 2;
    private int CORNER_SIZE = 10;

    private ImageIcon buttonIcon;
    private boolean mouseOver = false;
    private String imagePath;


    /**
     * ShadowButton의 생성자.
     *
     * @param text      버튼에 표시할 텍스트
     * @param imagePath 버튼의 배경 이미지 경로
     */
    public ShadowButton(String text, String imagePath) {
        super(text);
        this.imagePath = imagePath;
        loadImageIcon(imagePath);
        initializeButton();
    }

    /**
     * 버튼의 둥근 모서리 크기를 설정합니다.
     *
     * @param size 둥근 모서리의 크기
     */
    public void setCORNER_SIZE(int size) {
        this.CORNER_SIZE = size;
    }

    /**
     * 버튼의 그림자 오프셋을 설정합니다.
     *
     * @param size 그림자 오프셋 크기
     */
    public void setSHADOW_OFFSET(int size) {
        this.SHADOW_OFFSET = size;
    }

    /**
     * 버튼의 초기 설정을 수행합니다.
     */
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


    /**
     * 지정된 경로에서 버튼의 이미지 아이콘을 로드합니다.
     *
     * @param path 이미지 아이콘의 경로
     */
    private void loadImageIcon(String path) {
        if (path != null && !path.trim()
                .isEmpty()) {
            buttonIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
        } else {
            buttonIcon = null;
        }
    }


    /**
     * 버튼의 이미지 경로를 설정하고, 이미지를 다시 로드합니다.
     *
     * @param imagePath 새로운 이미지 경로
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        if (imagePath != null && !imagePath.trim()
                .isEmpty()) {
            buttonIcon = new ImageIcon(getClass().getResource(imagePath));
        } else {
            buttonIcon = null;
        }
        repaint(); // 버튼 다시 그리기
    }


    /**
     * 버튼 컴포넌트를 그립니다.
     * 이 메서드는 그림자 효과, 이미지, 텍스트를 포함하여 버튼을 사용자 정의 방식으로 그립니다.
     *
     * @param g 그래픽스 컨텍스트
     */
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
