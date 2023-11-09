package org.example.ui;

import org.example.Entity.Character;
import org.example.etc.FancyProgressBar;

import javax.swing.*;
import java.awt.*;

public class StatusBarUI extends JPanel {

    private final Character character;
    JPanel healthPanel = new JPanel(new BorderLayout());
    JPanel hungryPanel = new JPanel(new BorderLayout());
    JPanel thirstyPanel = new JPanel(new BorderLayout());
    JPanel funPanel = new JPanel(new BorderLayout());
    JProgressBar hungryBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
    JProgressBar healthBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
    JProgressBar thirstyBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
    JProgressBar funBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);


    public StatusBarUI(Character character) {
        this.character = character;
    }

    public void setStatusHud() {
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        setHealthPanel();
        setHungryPanel();
        setThirstyPanel();
        setFunPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Column 0
        gbc.gridy = 1; // Start with Row 0
        gbc.weighty = 1; // Use weighty for vertical fill
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill vertically
        gbc.anchor = GridBagConstraints.WEST; // Anchor to the left
        gbc.insets = new Insets(5, 10, 5, 10); // Provide some padding

        this.add(healthPanel, gbc);

        gbc.gridy++;
        this.add(hungryPanel, gbc);

        gbc.gridy++; // Move to the next column
        this.add(thirstyPanel, gbc);

        gbc.gridy++;
        this.add(funPanel, gbc);
    }


    private void setHealthPanel() {
        healthPanel.setOpaque(false);

        JLabel healthLabel = new JLabel();

        FancyProgressBar ui = new FancyProgressBar();
        healthBar.setUI(ui);
        ui.addHoverListener(healthBar);
        healthBar.setStringPainted(true);
        healthBar.setString(character.getHealth() + "%");
        healthBar.setBackground(new Color(82, 71, 39));
        healthBar.setForeground(new Color(249, 80, 87));
        healthBar.setValue(character.getHealth());


// Load and set the icon for the health label
        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/Icon/heartIcon.png"));
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        healthLabel.setIcon(icon);

// Add the health bar to the center of the health panel
        healthPanel.add(healthBar, BorderLayout.CENTER);

// Add the health label (with icon) to the west of the health panel, which will position it to the left of the health bar
        healthPanel.add(healthLabel, BorderLayout.WEST);
    }

    private void setHungryPanel() {
        hungryPanel.setOpaque(false);

        JLabel hungryLabel = new JLabel();

        FancyProgressBar ui = new FancyProgressBar();
        hungryBar.setUI(ui);
        ui.addHoverListener(hungryBar);
        hungryBar.setStringPainted(true);
        hungryBar.setString(character.getHungry() + "%");
        hungryBar.setBackground(new Color(82, 71, 39));
        hungryBar.setForeground(new Color(249, 150, 80));

        hungryBar.setValue(character.getHungry());

// Load and set the icon for the health label
        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/Icon/hungryIcon.png"));
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        hungryLabel.setIcon(icon);

// Add the health bar to the center of the health panel
        hungryPanel.add(hungryBar, BorderLayout.CENTER);

// Add the health label (with icon) to the west of the health panel, which will position it to the left of the health bar
        hungryPanel.add(hungryLabel, BorderLayout.WEST);
    }

    private void setThirstyPanel() {
        thirstyPanel.setOpaque(false);

        JLabel thirstyLabel = new JLabel();

        FancyProgressBar ui = new FancyProgressBar();
        thirstyBar.setUI(ui);
        ui.addHoverListener(thirstyBar);
        thirstyBar.setStringPainted(true);
        thirstyBar.setString(character.getHungry() + "%");
        thirstyBar.setBackground(new Color(82, 71, 39));
        thirstyBar.setForeground(new Color(80, 165, 249));
        thirstyBar.setValue(character.getThirst());

// Load and set the icon for the health label
        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/Icon/thirstyIcon.png"));
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        thirstyLabel.setIcon(icon);

// Add the health bar to the center of the health panel
        thirstyPanel.add(thirstyBar, BorderLayout.CENTER);

// Add the health label (with icon) to the west of the health panel, which will position it to the left of the health bar
        thirstyPanel.add(thirstyLabel, BorderLayout.WEST);
    }

    private void setFunPanel() {
        funPanel.setOpaque(false);

        JLabel funLabel = new JLabel();

        FancyProgressBar ui = new FancyProgressBar();
        funBar.setUI(ui);
        ui.addHoverListener(funBar);
        funBar.setStringPainted(true);
        funBar.setString(character.getFun() + "%");
        funBar.setBackground(new Color(82, 71, 39));
        funBar.setForeground(new Color(243, 249, 80));
        funBar.setValue(character.getFun());

// Load and set the icon for the health label
        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/Icon/funIcon.png"));
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        funLabel.setIcon(icon);

// Add the health bar to the center of the health panel
        funPanel.add(funBar, BorderLayout.CENTER);

// Add the health label (with icon) to the west of the health panel, which will position it to the left of the health bar
        funPanel.add(funLabel, BorderLayout.WEST);
    }

    public void statusUpdateTimer() {
        int delay = 1000; // 1초마다 업데이트
        new Timer(delay, e -> HudUpdate()).start();
    }

    private void HudUpdate() {

        healthBar.setValue(character.getHealth());
        healthBar.setString(character.getHealth() + "%");

        hungryBar.setValue(character.getHungry());
        hungryBar.setString(character.getHungry() + "%");

        thirstyBar.setValue(character.getThirst());
        thirstyBar.setString(character.getThirst() + "%");

        funBar.setValue(character.getFun());
        funBar.setString(character.getFun() + "%");
        this.revalidate();
        this.repaint();
    }
}
