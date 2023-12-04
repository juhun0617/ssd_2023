package org.example.ui;

import org.example.Animal.Animal;
import org.example.Entity.Character;
import org.example.etc.BackgroundMusic;
import org.example.service.CharacterService;
import org.hibernate.type.CharacterType;

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
 * 사용자 인터페이스 내의 기능 버튼을 관리하는 클래스.
 * 이 클래스는 다양한 기능을 제공하는 버튼들을 초기화하고, 이벤트 리스너를 추가합니다.
 */
public class FunctionButtonUI extends JPanel {
    JPanel panel;
    Character character;
    Animal animal;
    JButton closetButton;
    JButton doorButton;
    JButton gameButton;
    JButton shopButton;
    EntityManagerFactory emf;
    EntityManager em;
    CharacterService characterService;
    private BackgroundMusic backgroundMusic;

    /**
     * 기본 생성자.
     * 데이터베이스 연결을 설정하고, 캐릭터 서비스를 초기화합니다.
     */
    public FunctionButtonUI() {
        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db")
                .toString();
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        em = emf.createEntityManager();
        characterService = new CharacterService(emf);
    }

    /**
     * 패널에 기능 버튼을 설정합니다.
     *
     * @param character 현재 캐릭터 객체
     * @param panel     UI가 표시될 패널
     * @param animal    캐릭터의 동물 객체
     * @param bgMusic   배경 음악 객체
     */
    public void setFunctionButton(Character character, JPanel panel, Animal animal, BackgroundMusic bgMusic) {
        this.backgroundMusic = bgMusic;
        this.panel = panel;
        this.animal = animal;
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        setClosetButton();
        setDoorButton();
        setGameButton();
        setShopButton();
        this.character = character;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1; // Column 0
        gbc.gridy = 0; // Start with Row 0
        gbc.weightx = 1; // Use weighty for vertical fill
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill vertically
        gbc.anchor = GridBagConstraints.WEST; // Anchor to the left
        gbc.insets = new Insets(0, 30, 0, 30);

        this.add(closetButton, gbc);

        gbc.gridx++;
        this.add(doorButton, gbc);

        gbc.gridx++;
        this.add(gameButton, gbc);

        gbc.gridx++;
        this.add(shopButton, gbc);
    }

    /**
     * 옷장 버튼을 초기화하고, 이벤트 리스너를 추가합니다.
     */
    private void setClosetButton() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/Button/closet.png"));
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        closetButton = new JButton(icon);
        closetButton.setBorderPainted(false);
        closetButton.setContentAreaFilled(false);
        closetButton.setFocusPainted(false);
        closetButton.addActionListener(e -> {
            StorageUI storageUI = new StorageUI(panel, character, () -> {
                DamaUI damaUI = new DamaUI(panel, character.getName());
                damaUI.updateUi();
            });
            animal.stopAllTimers();
            characterService.saveCharacter(character);
            storageUI.updateUI();
            backgroundMusic.stopMusic();
        });


    }

    /**
     * 출구 버튼을 초기화하고, 이벤트 리스너를 추가합니다.
     * 이 버튼은 애플리케이션을 종료하는 기능을 수행합니다.
     */
    private void setDoorButton() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/Button/door.png"));
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        doorButton = new JButton(icon);
        doorButton.setBorderPainted(false);
        doorButton.setContentAreaFilled(false);
        doorButton.setFocusPainted(false);
        doorButton.addActionListener(e -> {
            characterService.saveCharacter(character);
            System.exit(0);
        });

    }

    /**
     * 게임 선택 버튼을 초기화하고, 이벤트 리스너를 추가합니다.
     */
    private void setGameButton() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/Button/game.png"));
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        gameButton = new JButton(icon);
        gameButton.setBorderPainted(false);
        gameButton.setContentAreaFilled(false);
        gameButton.setFocusPainted(false);
        gameButton.addActionListener(e -> {
            GameSelectUI gameSelectUI = new GameSelectUI(panel, character, () -> {
                DamaUI damaUI = new DamaUI(panel, character.getName());
                damaUI.updateUi();
            });
            animal.stopAllTimers();
            characterService.saveCharacter(character);
            gameSelectUI.updateUI();
            backgroundMusic.stopMusic();
        });
    }

    /**
     * 상점 버튼을 초기화하고, 이벤트 리스너를 추가합니다.
     */
    private void setShopButton() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/Button/shop.png"));
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        shopButton = new JButton(icon);
        shopButton.setBorderPainted(false);
        shopButton.setContentAreaFilled(false);
        shopButton.setFocusPainted(false);
        shopButton.addActionListener(e -> {
            ShopUI shopUI = new ShopUI(panel, character, () -> {
                DamaUI damaUI = new DamaUI(panel, character.getName());
                damaUI.updateUi();
            });
            animal.stopAllTimers();
            characterService.saveCharacter(character);
            shopUI.updateUI();
            backgroundMusic.stopMusic();
        });
    }

}
