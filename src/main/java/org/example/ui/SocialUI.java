package org.example.ui;

import org.example.Entity.Character;
import org.example.draw.BackGroundPanel;
import org.example.etc.CustomFont;
import org.example.etc.ShadowButton;
import org.example.etc.SocialString;
import org.example.service.CharacterService;
import org.example.service.Character_DecoService;
import org.example.service.DecoService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;

public class SocialUI {

    private static String BACKGROUND_PATH = "/Image/shopBack.png";


    EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    EntityManager em = emf.createEntityManager();
    DecoService decoService = new DecoService(em);
    CharacterService characterService = new CharacterService(emf);
    Character_DecoService characterDecoService = new Character_DecoService(emf);
    private JPanel panel;
    private BackGroundPanel backPanel;
    private Character character;


    private final socialUICallback callback;
    public SocialUI(JPanel panel,Character character,socialUICallback callback){
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
        setGetKeyButton();
        setInputKeyButton();
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
    private void setGetKeyButton(){
        ShadowButton button = new ShadowButton("내 토큰 내보내기","/Image/Button/backButton.png");
        button.setFont(CustomFont.loadCustomFont(18f));
        button.setPreferredSize(new Dimension(250,80));
        button.addActionListener( e -> {
            ImageIcon temp = new ImageIcon(getClass().getResource("/Image/social.png"));
            Image image = temp.getImage();
            Image resizedImage = image.getScaledInstance(50,50,Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(resizedImage);
            String myString;
            SocialString socialString = new SocialString();
            try {
                myString = socialString.getString(character);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            JTextArea textArea = new JTextArea(myString);
            textArea.setEditable(false);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setCaretPosition(0);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(250, 150));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            JOptionPane.showMessageDialog(
                    backPanel,
                    scrollPane,
                    "내 토큰",
                    JOptionPane.INFORMATION_MESSAGE,
                    imageIcon
            );
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(600, 100, 0, 0);

        backPanel.add(button,gbc);
    }


    private void setInputKeyButton(){
        ShadowButton button = new ShadowButton("친구 토큰 가져오기", "/Image/Button/backButton.png");
        button.setFont(CustomFont.loadCustomFont(18f));
        button.setPreferredSize(new Dimension(250, 80));
        button.addActionListener(e -> {
            // 토큰 입력 받기
            String inputToken = JOptionPane.showInputDialog(backPanel, "토큰을 입력하세요:", "토큰 입력", JOptionPane.PLAIN_MESSAGE);

            // 입력된 토큰이 null, "null", 또는 빈 문자열인지 체크
            if (inputToken != null && !inputToken.trim().isEmpty() && !"null".equals(inputToken)) {
                try {
                    Character character = SocialString.createCharacterFromEncryptedString(inputToken);
                    SocialDamaUI socialDamaUI = new SocialDamaUI(panel, character,()->{
                        DamaUI damaUI = new DamaUI(panel,this.character.getName());
                        damaUI.updateUi();
                    });
                    socialDamaUI.updateUi();

                } catch (Exception ex) {
                    // 오류 처리
                    JOptionPane.showMessageDialog(backPanel, "토큰 처리 중 오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // 유효하지 않은 입력에 대한 처리
                JOptionPane.showMessageDialog(backPanel, "유효한 토큰을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(600, 0, 0, 100);

        backPanel.add(button, gbc);
    }



    public interface socialUICallback{
        void backButton();
    }
}
