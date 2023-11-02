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
        //bgMusic.startMusic("/Music.wav");

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }


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
                    newOrSaved();
                }
                panel.repaint();
            }
        });
        timer.start();
    }

    public void newOrSaved() {
        Object[] options = {"새 게임", "저장된 게임"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "새 게임을 시작하시겠습니까, 아니면 저장된 게임을 계속하시겠습니까?",
                "게임 선택",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        } else if (choice == JOptionPane.NO_OPTION) {
            loadSavedGame();
        }
    }

    private void startNewGame(){
        CharacterSelectionUI selectionUI = new CharacterSelectionUI(panel);
        selectionUI.updateUI();
    }

    private void loadSavedGame(){
    }
}
