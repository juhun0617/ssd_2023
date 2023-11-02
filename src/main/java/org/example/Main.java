package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Main extends JFrame {

    // 멤버 변수
    private Image background;
    private float alpha = 1.0f; //투명도 관리 변수
    private final JPanel panel;

    public Main() {
        // 이미지 로딩 및 리사이징
        loadAndResizeBackgroundImage();
        // 프레임 설정
        initFrame();
        //피널 초기화
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //배경 설정 및 투명도 적용
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2d.drawImage(background, 0, 0, this);
                g2d.dispose();
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);
        setContentPane(panel);
        //마우스 클릭 리스너
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fadeOutBackground();

                panel.removeMouseListener(this);
            }
        });
    }

    public static void main(String[] args) {

        BackgroundMusic bgMusic = new BackgroundMusic();
        bgMusic.startMusic("/Music.wav");

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    /**
     * 이미지 아이콘의 크기를 조절합니다.
     *
     * @param icon   원본 ImageIcon 객체
     * @param width  리사이징 될 너비
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

    private void fadeOutBackground() {
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha -= 0.1f;
                panel.repaint();
                if (alpha <= 0.2f) {
                    alpha = 0.2f;
                    ((Timer) e.getSource()).stop();

                    // 배경 흐려짐이 완료된 후 캐릭터 선택 창을 띄우도록 합니다.
                    showCharacterSelection();
                }
                panel.repaint();
            }
        });
        timer.start();
    }

    private void showCharacterSelection() {

        CharacterSelectionUI selectionUI = new CharacterSelectionUI(panel);
        selectionUI.updateUI();
    }
}
