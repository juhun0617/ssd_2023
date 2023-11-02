package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Main extends JFrame {

    // 멤버 변수
    private Image background;

    public Main() {
        // 이미지 로딩 및 리사이징
        loadAndResizeBackgroundImage();
        // 프레임 설정
        initFrame();
        //마우스 클릭 리스너
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showCharacterSelection();
            }
        });
    }

    /**
     * 이미지 아이콘의 크기를 조절합니다.
     * @param icon 원본 ImageIcon 객체
     * @param width 리사이징 될 너비
     * @param height 리사이징 될 높이
     * @return 크기가 조절된 ImageIcon 객체
     */
    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    /**
     * 배경 이미지를 로드하고 리사이징합니다.
     */
    private void loadAndResizeBackgroundImage() {
        ImageIcon backgroundIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/Main.jpeg")));
        background = resizeImageIcon(backgroundIcon, 800, 800).getImage();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
    }

    /**
     * 프레임의 기본 설정을 합니다.
     */
    private void initFrame() {
        setTitle("Main");
        setSize(800, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void showCharacterSelection(){


        this.dispose();
    }

    public static void main(String[] args) {
        new Main();
    }
}
