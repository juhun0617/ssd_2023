package org.example.ui;

import org.example.Animal.*;
import org.example.Entity.Character;
import org.example.Entity.Deco;
import org.example.draw.BackGroundPanel;
import org.example.etc.CustomFont;
import org.example.etc.ImageTextOverlayLabel;
import org.example.etc.ShadowButton;
import org.example.service.CharacterService;
import org.example.service.DecoService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class SocialDamaUI {

    private final int ANIMATION_DELAY = 100; // 애니메이션 속도
    private final int ANIMATION_DURATION = 500; // 애니메이션 지속 시간
    private final double SCALE_FACTOR = 1.1; // 확대 비율
    private final String LEVEL_BAR_PATH = "/Image/coinAndLevelBar.png";


    private final JPanel panel;
    private JPanel backPanel;
    private Character character;
    private Animal animal;
    private JLabel characterLabel;
    private ImageTextOverlayLabel levelBar;
    private ImageIcon levelBarIcon;
    private ImageIcon characterIcon;
    private ImageTextOverlayLabel coinBar;
    private Timer animationTimer;
    private final SocialDamaUICallback callback;


    EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    EntityManager em = emf.createEntityManager();
    CharacterService characterService = new CharacterService(emf);
    DecoService decoService = new DecoService(em);

    public SocialDamaUI(JPanel panel,Character character,SocialDamaUICallback callback){
        this.panel = panel;
        this.character = character;
        this.callback = callback;
    }
    public void updateUi(){
        initializeBackPanel();
        makeAnimalObject();
        drawCharacter();
        setLevelBar();
        setCoinBar();
        setStatusBar();
        setTabel();
        setChair();
        setSocial();
        setBackButton();

        panel.revalidate();
        panel.repaint();


    }

    private void initializeBackPanel(){
        panel.removeAll();
        if (character.getBackId() !=0){
            String path = decoService.findDecoById(character.getBackId()).getDecoImagePath();
            backPanel = new BackGroundPanel(path);
        } else {
            backPanel = new JPanel();
            backPanel.setBackground(new Color(190, 190, 190));
            backPanel.setPreferredSize(new Dimension(800,800));
        }
        panel.add(backPanel,BorderLayout.CENTER);

    }


    private void setSocial(){
        ImageIcon temp = new ImageIcon(getClass().getResource("/Image/social.png"));
        Image image = temp.getImage();
        Image resizedImage = image.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(resizedImage);
        JButton button = new JButton(imageIcon);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(e -> {
            SocialUI2 socialUI = new SocialUI2(panel,character,()->{
                SocialDamaUI socialDamaUI = new SocialDamaUI(panel,character,callback);
                socialDamaUI.updateUi();
            });
            socialUI.updateUI();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(100, 0, 0, 60);
        backPanel.add(button, gbc);

    }
    private void makeAnimalObject(){
        if(Objects.equals(character.getAnimal(),"cat")){
            animal = new Cat(character);
        } else if (Objects.equals(character.getAnimal(), "rabbit")) {
            animal = new Rabbit(character);
        } else if (Objects.equals(character.getAnimal(),"goat")){
            animal = new Goat(character);
        } else if (Objects.equals(character.getAnimal(),"duck")){
            animal = new Duck(character);
        }
    }

    private void setTabel(){
        if (character.getTableId() != -1){
            System.out.println("-----------------------------");
            Deco deco = decoService.findDecoById(character.getTableId());
            ImageIcon temp = new ImageIcon(getClass().getResource(deco.getDecoImagePath()));
            Image image = temp.getImage();
            Image resizedImage = image.getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            ImageIcon table = new ImageIcon(resizedImage);
            JLabel label = new JLabel(table);


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; // X 축 위치
            gbc.gridy = 0; // Y 축 위치
            gbc.anchor = GridBagConstraints.SOUTHWEST;
            gbc.insets = new Insets(0, 70, 280, 0); // 위쪽 여백 10px

            backPanel.add(label, gbc);

            // 패널 업데이트
            backPanel.revalidate();
            backPanel.repaint();

        }
    }
    private void setChair(){
        if (character.getChairId() != -1){
            Deco deco = decoService.findDecoById(character.getChairId());
            ImageIcon temp = new ImageIcon(getClass().getResource(deco.getDecoImagePath()));
            Image image = temp.getImage();
            Image resizedImage = image.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            ImageIcon table = new ImageIcon(resizedImage);
            JLabel label = new JLabel(table);


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; // X 축 위치
            gbc.gridy = 0; // Y 축 위치
            gbc.anchor = GridBagConstraints.SOUTHEAST;
            gbc.insets = new Insets(0, 0, 300, 70); // 위쪽 여백 10px

            backPanel.add(label, gbc);

            // 패널 업데이트
            backPanel.revalidate();
            backPanel.repaint();

        }
    }


    private void drawCharacter(){

        characterIcon = new ImageIcon(getClass().getResource(animal.getPATH()));
        Image image = characterIcon.getImage();
        Image resizedImage = image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
        characterIcon = new ImageIcon(resizedImage);
        characterLabel = new JLabel(characterIcon);
        characterLabel.setSize(new Dimension(200,200));

        // 클릭 이벤트 리스너 설정
        characterLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (animationTimer != null && animationTimer.isRunning()) {
                    return; // 이미 애니메이션이 실행 중이라면 다시 시작하지 않음
                }
                animateCharacter();

                int tmpFun = character.getFun();
                tmpFun++;
                character.setFun(tmpFun);

                int tmpHungry = character.getHungry();
                tmpHungry--;
                character.setHungry(tmpHungry);

            }
        });


        // 캐릭터 레이블을 패널에 추가
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // X 축 위치
        gbc.gridy = 0; // Y 축 위치
        gbc.anchor = GridBagConstraints.SOUTH; // 상단에 고정
        gbc.insets = new Insets(0, 0, 250, 0); // 위쪽 여백 10px

        backPanel.setLayout(new GridBagLayout());
        backPanel.add(characterLabel, gbc); // 레이블을 패널에 추가

        // 패널 업데이트
        backPanel.revalidate();
        backPanel.repaint();

    }

    private void animateCharacter() {
        final long startTime = System.currentTimeMillis();

        // 애니메이션 타이머 생성 및 시작
        animationTimer = new Timer(ANIMATION_DELAY, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            double progress = (double) elapsed / ANIMATION_DURATION;
            if (progress > 1) {
                progress = 1;
                animationTimer.stop(); // 애니메이션 종료
            }

            // 캐릭터 크기를 계산하여 설정
            double scaleFactor = 1 + (SCALE_FACTOR - 1) * (1 - progress);
            resizeCharacter(scaleFactor);
        });
        animationTimer.start();
    }

    private void resizeCharacter(double scaleFactor) {
        int width = (int) (characterIcon.getIconWidth() * scaleFactor);
        int height = (int) (characterIcon.getIconHeight() * scaleFactor);
        Image scaledImage = characterIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        characterLabel.setIcon(new ImageIcon(scaledImage));
        panel.revalidate();
        panel.repaint();
    }


    private void setLevelBar() {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(LEVEL_BAR_PATH));
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(150, 50, Image.SCALE_SMOOTH);
        levelBarIcon = new ImageIcon(resizedImage);
        levelBar = new ImageTextOverlayLabel(levelBarIcon);

        levelBar.setText("Level: " + character.getLevel());
        levelBar.setFont(new CustomFont().loadCustomFont(25f));
        levelBar.setForeground(Color.white);
        levelBar.setPreferredSize(new Dimension(150,50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(30, 30, 0, 0);
        backPanel.add(levelBar, gbc);


    }


    private void setCoinBar() {
        coinBar = new ImageTextOverlayLabel(levelBarIcon);

        coinBar.setText("Coin: "+ character.getMoney());
        coinBar.setFont(new CustomFont().loadCustomFont(25f));
        coinBar.setForeground(Color.white);
        coinBar.setPreferredSize(new Dimension(150,50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(30, 0, 0, 30);
        backPanel.add(coinBar, gbc);


    }
    private void setStatusBar(){

        StatusBarUI hud = new StatusBarUI(character);
        hud.setStatusHud();
        hud.statusUpdateTimer();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(30, 0, 0, 0);
        backPanel.add(hud,gbc);
    }
    private void setBackButton(){
        ShadowButton button = new ShadowButton("돌아가기","/Image/Button/backButton.png");
        button.setFont(CustomFont.loadCustomFont(30f));
        button.setForeground(Color.white);
        button.setPreferredSize(new Dimension(200,70));
        button.addActionListener(e -> {
            callback.backButton();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 60, 70, 0);

        backPanel.add(button, gbc);

    }

    public interface SocialDamaUICallback{
        void backButton();
    }

}
