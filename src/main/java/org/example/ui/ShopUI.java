package org.example.ui;

import org.example.Entity.Character;
import org.example.Entity.Character_Deco;
import org.example.Entity.Deco;
import org.example.draw.BackGroundPanel;
import org.example.etc.CustomFont;
import org.example.etc.ImageTextOverlayLabel;
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
import java.util.*;
import java.util.List;

public class ShopUI {

    EntityManagerFactory emf;
    EntityManager em;
    DecoService decoService ;
    Character_DecoService characterDecoService;
    CharacterService characterService;
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
    private ImageIcon imageIcon;
    private ImageIcon imageIcon2;
    private JPanel itemPanel;


    private final shopUICallback callback;
    public ShopUI(JPanel panel, Character character,shopUICallback callback) {

        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db").toString();
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        em = emf.createEntityManager();
        characterService = new CharacterService(emf);
        characterDecoService = new Character_DecoService(emf);
        decoService = new DecoService(em);

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
        backButton = new ShadowButton("Back","/Image/Button/ShopBackButton.png");
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
        if (coinBar != null){
            backPanel.remove(coinBar);
        }
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
        imageIcon = new ImageIcon(getClass().getResource("/Image/Button/shopClassNP.png"));
        imageIcon2 = new ImageIcon(getClass().getResource("/Image/Button/shopClass.png"));
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
        class1Button.addActionListener(e -> {
            initClassButton("가구");
            setItemList("furniture");
        });

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
        class2Button.addActionListener(e -> {
            initClassButton("음식");
            setItemList("food");
        });

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
        class3Button.addActionListener(e -> {
            initClassButton("배경");
            setItemList("back");
        });

        gbc.insets = new Insets(170,40+(imageIcon.getIconWidth()*2),0,0);

        backPanel.add(class3Button,gbc);
        initClassButton("가구");
        setItemList("furniture");
    }

    private void initClassButton(String itemClass){
        if (Objects.equals(itemClass, "가구")){
            class1Button.setIcon(imageIcon2);
            class2Button.setIcon(imageIcon);
            class3Button.setIcon(imageIcon);
        } else if (Objects.equals(itemClass,"음식")) {
            class2Button.setIcon(imageIcon2);
            class1Button.setIcon(imageIcon);
            class3Button.setIcon(imageIcon);
        } else if (Objects.equals(itemClass,"배경")){
            class3Button.setIcon(imageIcon2);
            class1Button.setIcon(imageIcon);
            class2Button.setIcon(imageIcon);
        }
    }

    private List<Deco> getDecoList(String decoClass){
        List<Deco> decoList = decoService.findDecoByClass(decoClass);
        for (Deco deco : decoList){
            System.out.println(deco.getDecoName());
        };
        return decoList;
    }


    private void setItemList(String decoClass) {
        // 기존 itemPanel 제거
        if (itemPanel != null) {
            backPanel.remove(itemPanel);
        }

        itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayout(2, 3));
        itemPanel.setBackground(new Color(142,118,71));

        List<Deco> decoList = getDecoList(decoClass);

        for (Deco deco : decoList) {
            System.out.println(deco.getDecoImagePath());
            ImageIcon temp = new ImageIcon(getClass().getResource(deco.getDecoImagePath()));
            Image image = temp.getImage();
            Image resizedImage = image.getScaledInstance(180,180,Image.SCALE_SMOOTH);
            ImageIcon itemIcon = new ImageIcon(resizedImage);
            JButton button = new JButton(itemIcon);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                setPricePopup(deco);
            });
            itemPanel.add(button);
        }

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.gridwidth = GridBagConstraints.REMAINDER;
        gbc1.anchor = GridBagConstraints.NORTH;
        gbc1.fill = GridBagConstraints.NONE;
        gbc1.weightx = 1;
        gbc1.weighty = 1;
        gbc1.insets = new Insets(320, 0, 0, 0);

        backPanel.add(itemPanel, gbc1);
        panel.revalidate();
        panel.repaint();
    }

    private void setPricePopup(Deco deco){
        JLabel messageLabel = new JLabel(deco.getDecoName() + " : " + deco.getDecoPrice()+"원");
        messageLabel.setFont(CustomFont.loadCustomFont(18f));
        ImageIcon temp = new ImageIcon(getClass().getResource(deco.getDecoImagePath()));
        Image image = temp.getImage();
        Image resizedImage = image.getScaledInstance(50,50,Image.SCALE_SMOOTH);
        ImageIcon itemIcon = new ImageIcon(resizedImage);

        int result = JOptionPane.showConfirmDialog(
                backPanel,
                messageLabel,
                deco.getDecoName() + " 구매",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                itemIcon);
        if (result == JOptionPane.YES_OPTION && Objects.equals(deco.getDecoClass(),"back") && characterDecoService.countDecosByCharacterIdAndDecoId(character.getId(), deco.getId()) > 0){
            JLabel label = new JLabel("이미 보유중인 상품입니다");
            label.setFont(CustomFont.loadCustomFont(18f));
            JOptionPane.showMessageDialog(
                    backPanel,
                    label,
                    deco.getDecoName()+"구매",
                    JOptionPane.INFORMATION_MESSAGE,
                    itemIcon);
        }
        else if (result == JOptionPane.YES_OPTION){
            if (character.getMoney() >= deco.getDecoPrice()) {
                character.setMoney(character.getMoney()-deco.getDecoPrice());
                characterService.saveCharacter(character);
                Character_Deco characterDeco = new Character_Deco();
                characterDeco.setCharacterId(character.getId());
                characterDeco.setDecoId(deco.getId());
                characterDecoService.saveCharacterDeco(characterDeco);
                setCoinBar();
                backPanel.revalidate();
                backPanel.repaint();
            }else {
                JLabel label = new JLabel("잔액이 부족합니다");
                label.setFont(CustomFont.loadCustomFont(18f));
                JOptionPane.showMessageDialog(
                        backPanel,
                        label,
                        deco.getDecoName()+"구매",
                        JOptionPane.INFORMATION_MESSAGE,
                        itemIcon);
            }
        }else {

        }

    }


        public interface shopUICallback {
        void backButton();
    }
}
