package org.example.ui;

import org.example.Animal.*;
import org.example.Entity.Character;
import org.example.draw.BackGroundPanel;
import org.example.service.CharacterService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class DamaUI {

    private final int[] BACKGROUND = {252,255,217};
    private final int ANIMATION_DELAY = 100; // 애니메이션 속도
    private final int ANIMATION_DURATION = 500; // 애니메이션 지속 시간
    private final double SCALE_FACTOR = 1.1; // 확대 비율


    EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    EntityManager em = emf.createEntityManager();
    CharacterService characterService = new CharacterService(em);




    private final JPanel panel;
    private JPanel backPanel;
    private Character character;
    private Animal animal;
    private JLabel characterLabel;
    private ImageIcon characterIcon;
    private Timer animationTimer;


    public DamaUI(JPanel panel,String name) {
        this.panel = panel;
        this.character = characterService.findCharacterByName(name);
    }

    public void updateUi(){

        initializeBackPanel();
        System.out.println(character.getAnimal() + "-" + character.getName());

        startTimer();
        drawCharacter();
        panel.revalidate();
        panel.repaint();

    }

    private void initializeBackPanel(){
        panel.removeAll();
        backPanel = new JPanel();
        backPanel.setBackground(new Color(BACKGROUND[0],BACKGROUND[1],BACKGROUND[2]));


        panel.add(backPanel,BorderLayout.CENTER);

    }

    private void startTimer(){
        makeAnimalObject();
        animal.startAllTimers();
        Runtime.getRuntime().addShutdownHook(new Thread(animal::stopAllTimers));
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
                // characterLabel의 크기와 클릭된 위치를 기반으로 실제 클릭이 이미지 내부에서 발생했는지 판단합니다.
                // 이를 위해 characterLabel의 위치와 이미지의 크기를 사용하여 클릭이 유효한 범위 내에서 발생했는지 확인합니다.
                Point clickPoint = e.getPoint();
                Rectangle imageBounds = new Rectangle(300, 300, 200, 200);

                if (imageBounds.contains(clickPoint)) {
                    if (animationTimer != null && animationTimer.isRunning()) {
                        return; // 이미 애니메이션이 실행 중이라면 다시 시작하지 않음
                    }
                    animateCharacter();
                }
            }
        });


        // 캐릭터 레이블을 패널에 추가
        backPanel.setLayout(new BorderLayout());
        backPanel.add(characterLabel,BorderLayout.CENTER); // 레이블을 패널에 추가

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
    }



