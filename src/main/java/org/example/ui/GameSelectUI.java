package org.example.ui;

import org.example.Entity.Character;
import org.example.draw.BackGroundPanel;
import org.example.etc.CustomFont;
import org.example.etc.ShadowButton;
import org.example.game.Mulgoging.Mulgoging;
import org.example.game.Pacman.Pacman;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameSelectUI {

    private static String BACKGROUND_PATH = "/Image/shopBack.png";
    private static List<String> gameList = new ArrayList<String>();


    private JPanel panel;
    private BackGroundPanel backPanel;
    private org.example.Entity.Character character;
    private JPanel gamePanel;
    private final gameSelectUICallBack callback;

    public GameSelectUI(JPanel panel, Character character, gameSelectUICallBack gameSelectUICallBack){
        this.panel = panel;
        this.callback = gameSelectUICallBack;
        this.character = character;
    }

    public void updateUI(){
        initializeBackPanel();


        panel.revalidate();
        panel.repaint();
    }

    private void initializeBackPanel() {
        panel.removeAll();
        backPanel = new BackGroundPanel(BACKGROUND_PATH);
        backPanel.setLayout(new GridBagLayout());
        panel.add(backPanel, BorderLayout.CENTER);
        backProgress();
        setGamePanel();
    }


    private void backProgress(){
        ShadowButton backButton = new ShadowButton("Back", "/Image/Button/shopBAckButton.png");
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
    private void setGameList(){
        gameList.add("로드러너");
        gameList.add("스네이크");
        gameList.add("물고깅");
        gameList.add("팩맨");
    }

    private void setGamePanel(){
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(4,1));
       gamePanel.setBackground(new Color(255,225,159));
        if (gameList.isEmpty()){
            setGameList();
        }
        for (String gameName : gameList){
            ShadowButton button = new ShadowButton(gameName,"/Image/Button/gameButton.png");
            button.setFont(CustomFont.loadCustomFont(50f));
            button.setForeground(Color.BLACK);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(500,100));
            switch (gameName){
                case "물고깅":
                    button.addActionListener(e -> {
                        Mulgoging windows = new Mulgoging(character);
                        windows.frame.setVisible(true);
                    });
                    break;
                case "팩맨" :
                    button.addActionListener(e -> {
                        Pacman pacman = new Pacman(character);
                        pacman.setVisible(true);
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


    public interface gameSelectUICallBack{
        void backButton();
    }
}
