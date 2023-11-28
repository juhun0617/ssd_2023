package org.example.ui;

import org.example.Entity.Character;
import org.example.draw.BackGroundPanel;
import org.example.etc.CustomFont;
import org.example.etc.ImageTextOverlayLabel;
import org.example.etc.ShadowButton;

import javax.swing.*;
import java.awt.*;

public class ShopUI {
    private static String BACKGROUND_PATH = "/Image/shopBack.png";
    private final JPanel panel;
    private BackGroundPanel backPanel;
    private JButton backButton;
    private Font customFont;
    private ImageTextOverlayLabel coinBar;
    private ImageIcon coinBarIcon;
    private JButton class1Button;
    private JButton class2Button;
    private JButton class3Button;
    private Character character;


    private final shopUICallback callback;
    public ShopUI(JPanel panel, Character character,shopUICallback callback) {
        this.panel = panel;
        this.callback = callback;
        this.character = character;
    }

    public void updateUI(){
        initializeBackPanel();


        panel.revalidate();
        panel.repaint();
    }
    private void initializeBackPanel(){
        panel.removeAll();
        backPanel = new BackGroundPanel(BACKGROUND_PATH);
        backPanel.setLayout(new GridBagLayout());
        panel.add(backPanel,BorderLayout.CENTER);
        backProgress();
        setCoinBar();
        setClassBar();

    }
    private void backProgress(){
        backButton = new ShadowButton("Back","/Image/Button/shopBAckButton.png");
        customFont = CustomFont.loadCustomFont(30f);
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

    private void setCoinBar(){
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Image/coinAndLevelBar.png"));
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(150,50,Image.SCALE_SMOOTH);
        coinBarIcon = new ImageIcon(resizedImage);
        coinBar = new ImageTextOverlayLabel(coinBarIcon);

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
        gbc.insets = new Insets(60, 0, 0, 60);
        backPanel.add(coinBar, gbc);
    }

    private void setClassBar(){
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Image/Button/shopClass.png"));
        class1Button = new JButton("가구",imageIcon);
        class1Button.setHorizontalTextPosition(JButton.CENTER);
        class1Button.setVerticalTextPosition(JButton.CENTER);
        class1Button.setContentAreaFilled(false);
        class1Button.setBorderPainted(false);
        class1Button.setFocusPainted(false);
        class1Button.setPreferredSize(new Dimension(imageIcon.getIconWidth(),imageIcon.getIconHeight()));
        Font font = CustomFont.loadCustomFont(40f);
        class1Button.setFont(font);
        class1Button.setForeground(new Color(86,71,33));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(170, 40, 0, 0);

        backPanel.add(class1Button, gbc);

        class2Button = new JButton("음식",imageIcon);
        class2Button.setHorizontalTextPosition(JButton.CENTER);
        class2Button.setVerticalTextPosition(JButton.CENTER);
        class2Button.setContentAreaFilled(false);
        class2Button.setBorderPainted(false);
        class2Button.setFocusPainted(false);
        class2Button.setPreferredSize(new Dimension(imageIcon.getIconWidth(),imageIcon.getIconHeight()));
        class2Button.setFont(font);
        class2Button.setForeground(new Color(86,71,33));

        gbc.insets = new Insets(170,40+imageIcon.getIconWidth(),0,0);

        backPanel.add(class2Button,gbc);

        class3Button = new JButton("배경",imageIcon);
        class3Button.setHorizontalTextPosition(JButton.CENTER);
        class3Button.setVerticalTextPosition(JButton.CENTER);
        class3Button.setContentAreaFilled(false);
        class3Button.setBorderPainted(false);
        class3Button.setFocusPainted(false);
        class3Button.setPreferredSize(new Dimension(imageIcon.getIconWidth(),imageIcon.getIconHeight()));
        class3Button.setFont(font);
        class3Button.setForeground(new Color(86,71,33));

        gbc.insets = new Insets(170,40+(imageIcon.getIconWidth()*2),0,0);

        backPanel.add(class3Button,gbc);

    }


    public interface shopUICallback {
        void backButton();
    }
}
