package org.example.ui;

import org.example.Entity.Character;
import org.example.draw.BackGroundPanel;
import org.example.etc.CustomFont;
import org.example.etc.ShadowButton;
import org.example.service.CharacterService;
import org.example.service.Character_DecoService;
import org.example.service.DecoService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SocialUI2 {

    private static String BACKGROUND_PATH = "/Image/shopBack.png";


    EntityManagerFactory emf;
    EntityManager em;


    private JPanel panel;
    private BackGroundPanel backPanel;
    private Character character;


    private final socialUI2Callback callback;
    public SocialUI2(JPanel panel, Character character, socialUI2Callback callback){

        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db").toString();
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        em = emf.createEntityManager();

        this.panel = panel;
        this.character = character;
        this.callback = callback;
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
        setScoreLabel();

    }

    private void backProgress(){
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
    private void setScoreLabel(){
        JLabel label1 = new JLabel("로드러너 : " + character.getMax_score_1()+"점");
        JLabel label2 = new JLabel("스네이크 : " + character.getMax_score_2()+"점");
        JLabel label3 = new JLabel("물고깅   : " + character.getMax_score_3()+"점");
        JLabel label4 = new JLabel("팩맨     : " + character.getMax_score_4()+"점");
        label1.setFont(CustomFont.loadCustomFont(50f));
        label2.setFont(CustomFont.loadCustomFont(50f));
        label3.setFont(CustomFont.loadCustomFont(50f));
        label4.setFont(CustomFont.loadCustomFont(50f));

        JPanel jPanel = new JPanel(new GridLayout(4,1));
        jPanel.add(label1);
        jPanel.add(label2);
        jPanel.add(label3);
        jPanel.add(label4);
        jPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(300, 200, 0, 0);
        backPanel.add(jPanel,gbc);
        JLabel title = new JLabel("최고점수");
        title.setFont(CustomFont.loadCustomFont(50f));
        gbc.insets = new Insets(200, 200, 0, 0);
        backPanel.add(title,gbc);

    }

    public interface socialUI2Callback{
        void backButton();
    }
}
