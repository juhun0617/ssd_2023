package org.example.ui;

import org.example.Animal.Animal;
import org.example.Entity.Character;
import org.example.service.CharacterService;
import org.hibernate.type.CharacterType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;

public class FunctionButtonUI extends JPanel {
    JPanel panel;
    Character character;
    Animal animal;
    JButton closetButton;
    JButton doorButton;
    JButton gameButton;
    JButton shopButton;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    EntityManager em = emf.createEntityManager();
    CharacterService characterService = new CharacterService(emf);

    public FunctionButtonUI(){

    }

    public void setFunctionButton(Character character, JPanel panel, Animal animal){
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

        this.add(closetButton,gbc);

        gbc.gridx++;
        this.add(doorButton,gbc);

        gbc.gridx++;
        this.add(gameButton,gbc);

        gbc.gridx++;
        this.add(shopButton,gbc);
    }

    private void setClosetButton(){
        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/Button/closet.png"));
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        closetButton = new JButton(icon);
        closetButton.setBorderPainted(false);
        closetButton.setContentAreaFilled(false);
        closetButton.setFocusPainted(false);
        closetButton.addActionListener(e -> {
            StorageUI storageUI = new StorageUI(panel,character,() -> {
                DamaUI damaUI = new DamaUI(panel, character.getName());
                damaUI.updateUi();
            });
            animal.stopAllTimers();
            characterService.saveCharacter(character);
            storageUI.updateUI();
        });


    }
    private void setDoorButton(){
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
    private void setGameButton(){
        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/Button/game.png"));
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        gameButton = new JButton(icon);
        gameButton.setBorderPainted(false);
        gameButton.setContentAreaFilled(false);
        gameButton.setFocusPainted(false);
        gameButton.addActionListener(e -> {
            GameSelectUI gameSelectUI = new GameSelectUI(panel,character,()-> {
                DamaUI damaUI = new DamaUI(panel, character.getName());
                damaUI.updateUi();
            });
            animal.stopAllTimers();
            characterService.saveCharacter(character);
            gameSelectUI.updateUI();
        });
    }

    private void setShopButton(){
        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/Button/shop.png"));
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        shopButton = new JButton(icon);
        shopButton.setBorderPainted(false);
        shopButton.setContentAreaFilled(false);
        shopButton.setFocusPainted(false);
        shopButton.addActionListener(e -> {
            ShopUI shopUI = new ShopUI(panel,character,() -> {
                DamaUI damaUI = new DamaUI(panel, character.getName());
                damaUI.updateUi();
            });
            animal.stopAllTimers();
            characterService.saveCharacter(character);
            shopUI.updateUI();

        });
    }

}
