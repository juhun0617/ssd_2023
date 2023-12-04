package org.example.etc;

import javax.swing.*;
import java.awt.*;


/**
 * @author juhun_park
 * 배경 이미지와 함께 텍스트를 오버레이하는 레이블을 구현한 클래스.
 * 이 클래스는 이미지 아이콘을 배경으로 사용하고, 그 위에 텍스트를 중앙에 오버레이합니다.
 */
public class ImageTextOverlayLabel extends JLabel {
    private ImageIcon backgroundIcon;

    /**
     * ImageTextOverlayLabel의 생성자.
     *
     * @param icon 이 레이블의 배경으로 사용할 ImageIcon
     */
    public ImageTextOverlayLabel(ImageIcon icon) {
        this.backgroundIcon = icon;
        setHorizontalAlignment(CENTER); // 텍스트 가운데 정렬
        setVerticalAlignment(CENTER); // 텍스트 수직 가운데 정렬
        setHorizontalTextPosition(CENTER); // 아이콘에 대한 텍스트 위치 가운데 정렬
        setVerticalTextPosition(CENTER); // 아이콘에 대한 텍스트 위치 수직 가운데 정렬
    }


    /**
     * 레이블의 컴포넌트를 그리는 메서드.
     * 이 메서드는 배경 이미지와 텍스트를 레이블에 그립니다.
     *
     * @param g 이 컴포넌트의 그래픽스 컨텍스트
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundIcon != null) {
            g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}
