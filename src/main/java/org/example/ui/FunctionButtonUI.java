package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class FunctionButtonUI extends JPanel {

    JButton closetButton;
    JButton doorButton;
    JButton gameButton;
    JButton shopButton;

    public FunctionButtonUI(){

    }

    public void setFunctionButton(){
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        setClosetButton();
        setDoorButton();
        setGameButton();
        setShopButton();

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
    }

}
