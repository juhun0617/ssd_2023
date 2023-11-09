package org.example.etc;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class FancyProgressBar extends BasicProgressBarUI {
    private boolean isHovered = false;

    // 마우스 리스너를 추가하는 생성자
    public void addHoverListener(JProgressBar progressBar) {
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                progressBar.repaint(); // 호버 상태 변경 시 재그리기
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                progressBar.repaint();
            }
        });
    }

    @Override
    protected Dimension getPreferredInnerVertical() {
        return new Dimension(20, 146);
    }

    @Override
    protected Dimension getPreferredInnerHorizontal() {
        return new Dimension(300, 20);
    }



    @Override
    protected void paintDeterminate(Graphics g, JComponent c) {

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int iStrokWidth = 3;
        g2d.setStroke(new BasicStroke(iStrokWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(progressBar.getBackground());
        g2d.setBackground(progressBar.getBackground());

        int width = progressBar.getWidth();
        int height = progressBar.getHeight();

        // 라운딩을 제거하기 위해 둥근 모서리의 반경을 0으로 설정합니다.
        RoundRectangle2D outline = new RoundRectangle2D.Double((iStrokWidth / 2), (iStrokWidth / 2),
                width - iStrokWidth, height - iStrokWidth,
                0, 0);

        g2d.draw(outline);

        int iInnerHeight = height - (iStrokWidth * 4);
        int iInnerWidth = width - (iStrokWidth * 4);

        double dProgress = progressBar.getPercentComplete();
        if (dProgress < 0) {
            dProgress = 0;
        } else if (dProgress > 1) {
            dProgress = 1;
        }

        iInnerWidth = (int) Math.round(iInnerWidth * dProgress);

        int x = iStrokWidth * 2;
        int y = iStrokWidth * 2;

        // 그림자 효과를 제거하기 위해 LinearGradientPaint 대신 단색을 설정합니다.
        g2d.setPaint(progressBar.getForeground());

        RoundRectangle2D fill = new RoundRectangle2D.Double(iStrokWidth * 2, iStrokWidth * 2,
                iInnerWidth, iInnerHeight, 0, 0); // 라운딩을 제거합니다.

        g2d.fill(fill);

        if (isHovered) {
            String valueString = String.format("%d%%", progressBar.getValue());
            drawValueString(g2d, valueString, x, y, iInnerWidth, iInnerHeight);
        }

        g2d.dispose();
    }


    protected void drawValueString(Graphics2D g2d, String valueString, int x, int y, int width, int height) {
        FontMetrics fm = progressBar.getFontMetrics(progressBar.getFont());
        int stringWidth = fm.stringWidth(valueString);
        int stringHeight = fm.getAscent();

        // 값의 위치를 중앙으로 설정
        int drawX = x + (width - stringWidth) / 2;
        int drawY = y + ((height - stringHeight) / 2) + fm.getAscent();

        Font customFont = CustomFont.loadCustomFont(18f);
        g2d.setFont(customFont);

        g2d.setColor(Color.BLACK); // 값의 색상
        g2d.drawString(valueString, drawX, drawY);
    }

    @Override
    protected void paintIndeterminate(Graphics g, JComponent c) {
        super.paintIndeterminate(g, c); //To change body of generated methods, choose Tools | Templates.
    }

}
