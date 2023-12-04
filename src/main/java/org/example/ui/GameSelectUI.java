package org.example.ui;

import org.example.Entity.Character;
import org.example.draw.BackGroundPanel;
import org.example.etc.CustomFont;
import org.example.etc.ShadowButton;
import org.example.game.LodeRunner.SimpleLodeRunner;
import org.example.game.Mulgoging.Mulgoging;
import org.example.game.Pacman.Pacman;
import org.example.game.Snake.SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author juhun_park
 * 사용자에게 게임 선택 옵션을 제공하는 클래스.
 * 이 클래스는 다양한 게임을 사용자에게 보여주고, 선택할 수 있도록 합니다.
 */
public class GameSelectUI {

    private static String BACKGROUND_PATH = "/Image/shopBack.png";
    private static List<String> gameList = new ArrayList<String>();
    private final gameSelectUICallBack callback;
    private JPanel panel;
    private BackGroundPanel backPanel;
    private org.example.Entity.Character character;
    private JPanel gamePanel;

    /**
     * 생성자.
     *
     * @param panel                메인 패널
     * @param character            현재 캐릭터 객체
     * @param gameSelectUICallBack 게임 선택 UI 콜백 인터페이스
     */
    public GameSelectUI(JPanel panel, Character character, gameSelectUICallBack gameSelectUICallBack) {
        this.panel = panel;
        this.callback = gameSelectUICallBack;
        this.character = character;
    }

    /**
     * UI를 업데이트하는 메서드.
     * 백그라운드 패널을 초기화하고, 게임 선택 패널을 설정합니다.
     */
    public void updateUI() {
        initializeBackPanel();


        panel.revalidate();
        panel.repaint();
    }

    /**
     * 백그라운드 패널을 초기화하는 메서드.
     * 뒤로 가기 버튼과 게임 선택 패널을 설정합니다.
     */
    private void initializeBackPanel() {
        panel.removeAll();
        backPanel = new BackGroundPanel(BACKGROUND_PATH);
        backPanel.setLayout(new GridBagLayout());
        panel.add(backPanel, BorderLayout.CENTER);
        backProgress();
        setGamePanel();
    }

    /**
     * 뒤로 가기 버튼을 초기화하고, 이벤트 리스너를 추가하는 메서드.
     */
    private void backProgress() {
        ShadowButton backButton = new ShadowButton("Back", "/Image/Button/ShopBackButton.png");
        Font customFont = CustomFont.loadCustomFont(30f);
        backButton.setFont(customFont);
        backButton.setForeground(Color.white);
        backButton.setPreferredSize(new Dimension(160, 70));
        backButton.addActionListener(e -> callback.backButton());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(60, 60, 0, 0);

        backPanel.add(backButton, gbc);

    }

    /**
     * 게임 목록을 초기화하는 메서드.
     * 게임 목록에 게임 이름을 추가합니다.
     */
    private void setGameList() {
        gameList.add("로드러너");
        gameList.add("스네이크");
        gameList.add("물고깅");
        gameList.add("팩맨");
    }

    /**
     * 게임 선택 패널을 설정하는 메서드.
     * 각 게임에 대한 버튼을 생성하고 이벤트 리스너를 추가합니다.
     */

    private void setGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(4, 1));
        gamePanel.setBackground(new Color(255, 225, 159));
        if (gameList.isEmpty()) {
            setGameList();
        }
        for (String gameName : gameList) {
            ShadowButton button = new ShadowButton(gameName, "/Image/Button/gameButton.png");
            button.setFont(CustomFont.loadCustomFont(50f));
            button.setForeground(Color.BLACK);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(500, 100));
            switch (gameName) {
                case "물고깅":
                    button.addActionListener(e -> {
                        Mulgoging windows = new Mulgoging(character);
                        windows.frame.setVisible(true);
                    });
                    break;
                case "팩맨":
                    button.addActionListener(e -> {
                        Pacman pacman = new Pacman(character);
                        pacman.setVisible(true);
                    });
                    break;
                case "로드러너":
                    button.addActionListener(e -> {
                        SimpleLodeRunner simpleLodeRunner = new SimpleLodeRunner(character);

                    });
                    break;
                case "스네이크":
                    button.addActionListener(e -> {
                        SnakeGame snakeGame = new SnakeGame(character);

                    });
                    break;
            }
            gamePanel.add(button);
        }
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.gridwidth = GridBagConstraints.REMAINDER;
        gbc1.anchor = GridBagConstraints.NORTH;
        gbc1.fill = GridBagConstraints.NONE;
        gbc1.weightx = 1;
        gbc1.weighty = 1;
        gbc1.insets = new Insets(200, 0, 0, 0);

        backPanel.add(gamePanel, gbc1);
        panel.revalidate();
        panel.repaint();

    }

    /**
     * 게임 선택 UI 콜백 인터페이스.
     * 이 인터페이스는 사용자가 뒤로 가기 버튼을 클릭할 때 호출됩니다.
     */
    public interface gameSelectUICallBack {
        void backButton();
    }
}
