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

/**
 * @author juhun_park
 * SocialDamaUI에서 호출되는 클래스로, 게임의 최고점수를 디스플레이 합니다.
 */
public class SocialUI2 {

    private static String BACKGROUND_PATH = "/Image/shopBack.png";
    private final socialUI2Callback callback;
    EntityManagerFactory emf;
    EntityManager em;
    private JPanel panel;
    private BackGroundPanel backPanel;
    private Character character;

    /**
     * SocialUI2의 생성자.
     * UI 컴포넌트를 초기화하고 필요한 서비스를 설정합니다.
     *
     * @param panel     UI를 위한 메인 패널.
     * @param character 표시 및 상호 작용을 위한 캐릭터 엔티티.
     * @param callback  UI 이벤트를 처리하기 위한 콜백 인터페이스.
     */
    public SocialUI2(JPanel panel, Character character, socialUI2Callback callback) {

        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db")
                .toString();
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        em = emf.createEntityManager();

        this.panel = panel;
        this.character = character;
        this.callback = callback;
    }

    /**
     * UI 컴포넌트를 업데이트하고 새로고침합니다.
     * 이 메서드는 패널을 초기화하고, 소셜 상호작용 관련 컴포넌트를 설정합니다.
     */
    public void updateUI() {
        initializeBackPanel();


        panel.revalidate();
        panel.repaint();
    }

    /**
     * 배경패널을 초기화 합니다.
     */
    private void initializeBackPanel() {
        panel.removeAll();
        backPanel = new BackGroundPanel(BACKGROUND_PATH);
        backPanel.setLayout(new GridBagLayout());
        panel.add(backPanel, BorderLayout.CENTER);
        backProgress();
        setScoreLabel();

    }

    /**
     * 뒤로가기 버튼을 설정합니다.
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
     * 게임별 최고점수를 디스플레이 합니다.
     */
    private void setScoreLabel() {
        JLabel label1 = new JLabel("로드러너 : " + character.getMax_score_1() + "점");
        JLabel label2 = new JLabel("스네이크 : " + character.getMax_score_2() + "점");
        JLabel label3 = new JLabel("물고깅   : " + character.getMax_score_3() + "점");
        JLabel label4 = new JLabel("팩맨     : " + character.getMax_score_4() + "점");
        label1.setFont(CustomFont.loadCustomFont(50f));
        label2.setFont(CustomFont.loadCustomFont(50f));
        label3.setFont(CustomFont.loadCustomFont(50f));
        label4.setFont(CustomFont.loadCustomFont(50f));

        JPanel jPanel = new JPanel(new GridLayout(4, 1));
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
        backPanel.add(jPanel, gbc);
        JLabel title = new JLabel("최고점수");
        title.setFont(CustomFont.loadCustomFont(50f));
        gbc.insets = new Insets(200, 200, 0, 0);
        backPanel.add(title, gbc);

    }

    /**
     * SocialUI2 클래스에서의 콜백 메서드를 위한 인터페이스.
     * 뒤로 가기 버튼 기능을 구현하기 위한 구조를 제공합니다.
     */
    public interface socialUI2Callback {
        void backButton();
    }
}
