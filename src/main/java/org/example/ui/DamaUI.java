package org.example.ui;

import org.example.Animal.*;
import org.example.Entity.Character;
import org.example.Entity.Deco;
import org.example.draw.BackGroundPanel;
import org.example.etc.BackgroundMusic;
import org.example.etc.CustomFont;
import org.example.etc.ImageTextOverlayLabel;
import org.example.service.CharacterService;
import org.example.service.DecoService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author juhun_park
 * 캐릭터와 관련된 인터랙티브 UI를 구현하는 클래스.
 * 캐릭터의 애니메이션, 상태 바, 레벨, 코인 등을 관리합니다.
 */
public class DamaUI {

    private final int ANIMATION_DELAY = 100; // 애니메이션 속도
    private final int ANIMATION_DURATION = 500; // 애니메이션 지속 시간
    private final double SCALE_FACTOR = 1.1; // 확대 비율
    private final String LEVEL_BAR_PATH = "/Image/coinAndLevelBar.png";
    private final JPanel panel;
    EntityManagerFactory emf;
    EntityManager em;
    CharacterService characterService;
    DecoService decoService;
    private JPanel backPanel;
    private Character character;
    private Animal animal;
    private JLabel characterLabel;
    private ImageTextOverlayLabel levelBar;
    private ImageIcon levelBarIcon;
    private ImageIcon characterIcon;
    private Timer animationTimer;
    private ImageTextOverlayLabel coinBar;
    private BackgroundMusic bgMusic;


    // 필드 선언...

    /**
     * DamaUI 클래스의 생성자.
     * SQLite 데이터베이스에 연결하고, 캐릭터 서비스와 데코레이션 서비스를 초기화합니다.
     *
     * @param panel 이 UI 컴포넌트가 추가될 패널
     * @param name  선택된 캐릭터의 이름
     */
    public DamaUI(JPanel panel, String name) {

        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db")
                .toString();
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        em = emf.createEntityManager();
        characterService = new CharacterService(emf);
        decoService = new DecoService(em);


        this.panel = panel;
        this.character = characterService.findCharacterByName(name);
    }

    /**
     * UI 컴포넌트를 초기화하고 캐릭터 관련 화면을 구성합니다.
     */
    public void updateUi() {
        startBackgroundMusic();

        initializeBackPanel();
        System.out.println(character.getAnimal() + "-" + character.getName());

        startTimer();
        drawCharacter();
        setLevelBar();
        setCoinBar();
        statusUpdateTimer();

        setStatusBar();
        setFunctionButton(character, panel, animal, bgMusic);
        setTabel();
        setChair();
        updateXp();
        updateHealth();
        setSocial();


        panel.revalidate();
        panel.repaint();

    }

    /**
     * 배경음악을 리소스 경로에서 불러와 배경음악을 재생합니다.
     */
    private void startBackgroundMusic() {

        // Assuming BackgroundMusic is a runnable that plays music
        bgMusic = new BackgroundMusic();
        bgMusic.startMusic("/Sound/dama.wav");
        new Thread(String.valueOf(bgMusic)).start();
        System.out.println("----------------Music Start");

    }

    /**
     * 캐릭터의 체력을 조회하여 캐릭터의 사망을 처리하고 데이터베이스에서 캐릭터를 삭제합니다
     */
    private void updateHealth() {
        if (character.getHealth() <= 0) {
            ImageIcon temp = new ImageIcon(getClass().getResource(animal.getPATH()));
            Image image = temp.getImage();
            Image resizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(resizedImage);
            JLabel label = new JLabel("캐릭터가 사망하였습니다.");
            label.setFont(CustomFont.loadCustomFont(18f));
            JOptionPane.showMessageDialog(
                    backPanel,
                    label,
                    character.getName() + " 사망",
                    JOptionPane.INFORMATION_MESSAGE,
                    imageIcon
            );
            characterService.deleteCharacter(character);
            System.exit(0);

        }
    }

    /**
     * 캐릭터의 경험치를 조회하여 경험치가 50이상일 때 레벨을 1씩 올립니다.
     */
    private void updateXp() {
        if (character.getXp() >= 50) {
            character.setXp(0);
            character.setLevel(character.getLevel() + 1);
            characterService.saveCharacter(character);
        }
    }

    /**
     * 배경패널을 초기화 합니다.
     */
    private void initializeBackPanel() {
        panel.removeAll();
        if (character.getBackId() != 0) {
            String path = decoService.findDecoById(character.getBackId())
                    .getDecoImagePath();
            backPanel = new BackGroundPanel(path);
        } else {
            backPanel = new JPanel();
            backPanel.setBackground(new Color(190, 190, 190));
            backPanel.setPreferredSize(new Dimension(800, 800));
        }
        panel.add(backPanel, BorderLayout.CENTER);

    }

    /**
     * 캐릭터의 탁상 데이터를 조회하여 패널에 탁상 요소를 배치합니다.
     */
    private void setTabel() {
        if (character.getTableId() != -1) {
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

    /**
     * 캐릭터의 의자 데이터를 조회하여 패널에 의자 요소를 배치합니다.
     */
    private void setChair() {
        if (character.getChairId() != -1) {
            System.out.println("--------------------------------------------");
            System.out.println(character.getChairId());
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

    /**
     * 캐릭터의 상태 타이머를 시작합니다.
     */
    private void startTimer() {
        makeAnimalObject();
        animal.startAllTimers();
        Runtime.getRuntime()
                .addShutdownHook(new Thread(animal::stopAllTimers));
    }

    /**
     * 캐릭터의 동물 종류에 따른 동물객체를 생성합니다.
     */
    private void makeAnimalObject() {
        if (Objects.equals(character.getAnimal(), "cat")) {
            animal = new Cat(character);
        } else if (Objects.equals(character.getAnimal(), "rabbit")) {
            animal = new Rabbit(character);
        } else if (Objects.equals(character.getAnimal(), "goat")) {
            animal = new Goat(character);
        } else if (Objects.equals(character.getAnimal(), "duck")) {
            animal = new Duck(character);
        }
    }

    /**
     * 게시판,놀러가기 버튼을 생성하고, 액션리스너를 구현합니다.
     */
    private void setSocial() {
        ImageIcon temp = new ImageIcon(getClass().getResource("/Image/social.png"));
        Image image = temp.getImage();
        Image resizedImage = image.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(resizedImage);
        JButton button = new JButton(imageIcon);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(e -> {
            SocialUI socialUI = new SocialUI(panel, character, () -> {
                DamaUI damaUI = new DamaUI(panel, character.getName());
                damaUI.updateUi();
            });
            animal.stopAllTimers();
            characterService.saveCharacter(character);
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

    /**
     * 캐릭터 이미지를 패널에 그립니다.
     */
    private void drawCharacter() {

        characterIcon = new ImageIcon(getClass().getResource(animal.getPATH()));
        Image image = characterIcon.getImage();
        Image resizedImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        characterIcon = new ImageIcon(resizedImage);
        characterLabel = new JLabel(characterIcon);
        characterLabel.setSize(new Dimension(200, 200));

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

    /**
     * 캐릭터가 클릭되었을 때 캐릭터의 움직임을 처리합니다.
     */
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

    /**
     * 캐릭터의 크기 애니매이션을 처리합니다.
     *
     * @param scaleFactor : 캐릭터가 얼마나 확대될 지 지정하는 파라메터.
     */
    private void resizeCharacter(double scaleFactor) {
        int width = (int) (characterIcon.getIconWidth() * scaleFactor);
        int height = (int) (characterIcon.getIconHeight() * scaleFactor);
        Image scaledImage = characterIcon.getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH);

        characterLabel.setIcon(new ImageIcon(scaledImage));
        panel.revalidate();
        panel.repaint();
    }


    /**
     * 캐릭터의 레벨을 표시합니다.
     */
    private void setLevelBar() {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(LEVEL_BAR_PATH));
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(150, 50, Image.SCALE_SMOOTH);
        levelBarIcon = new ImageIcon(resizedImage);
        levelBar = new ImageTextOverlayLabel(levelBarIcon);

        levelBar.setText("Level: " + character.getLevel());
        levelBar.setFont(new CustomFont().loadCustomFont(25f));
        levelBar.setForeground(Color.white);
        levelBar.setPreferredSize(new Dimension(150, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(30, 30, 0, 0);
        backPanel.add(levelBar, gbc);


    }

    /**
     * 캐릭터의 보유잔고를 표시합니다.
     */
    private void setCoinBar() {
        coinBar = new ImageTextOverlayLabel(levelBarIcon);

        coinBar.setText("Coin: " + character.getMoney());
        coinBar.setFont(new CustomFont().loadCustomFont(25f));
        coinBar.setForeground(Color.white);
        coinBar.setPreferredSize(new Dimension(150, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(30, 0, 0, 30);
        backPanel.add(coinBar, gbc);


    }

    /**
     * 캐릭터의 레벨과 보유잔고를 업데이트 합니다.
     */
    private void statusUpdate() {
        levelBar.setText("Level: " + character.getLevel());
        coinBar.setText("Coin: " + character.getMoney());


        backPanel.revalidate();
        backPanel.repaint();
    }

    /**
     * 타이머를 이용하여 상태를 주기적으로 업데이트 합니다.
     */
    private void statusUpdateTimer() {
        int delay = 1000; // 1초마다 업데이트
        new Timer(delay, e -> statusUpdate()).start();
    }


    /**
     * 캐릭터의 상태바를 디스플레이 합니다.
     */
    private void setStatusBar() {

        StatusBarUI hud = new StatusBarUI(character);
        hud.setStatusHud();
        hud.statusUpdateTimer();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(30, 0, 0, 0);
        backPanel.add(hud, gbc);
    }

    /**
     * 기능 툴바를 추가하는 메서드입니다.
     *
     * @param character : 현재 캐릭터 객체
     * @param panel     : 현재 사용중인 패널
     * @param animal    : animal 객체
     * @param bgMusic   : 재생되고 있는 음악
     */
    private void setFunctionButton(Character character, JPanel panel, Animal animal, BackgroundMusic bgMusic) {
        FunctionButtonUI functionButton = new FunctionButtonUI();
        functionButton.setFunctionButton(character, panel, animal, bgMusic);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(0, 0, 70, 0);
        backPanel.add(functionButton, gbc);

    }


}



