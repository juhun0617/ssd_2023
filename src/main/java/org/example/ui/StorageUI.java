package org.example.ui;

import org.example.Entity.Character;
import org.example.Entity.Character_Deco;
import org.example.Entity.Deco;
import org.example.draw.BackGroundPanel;
import org.example.etc.CustomFont;
import org.example.etc.DecoFunc;
import org.example.etc.ShadowButton;
import org.example.service.CharacterService;
import org.example.service.Character_DecoService;
import org.example.service.DecoService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class StorageUI {

    private static String BACKGROUND_PATH = "/Image/shopBack.png";


    EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    EntityManager em = emf.createEntityManager();
    DecoService decoService = new DecoService(em);
    CharacterService characterService = new CharacterService(emf);
    Character_DecoService characterDecoService = new Character_DecoService(emf);
    private JPanel panel;
    private BackGroundPanel backPanel;
    private Character character;

    private JButton class1Button;
    private JButton class2Button;
    private JButton class3Button;
    private ImageIcon imageIcon;
    private ImageIcon imageIcon2;
    private JPanel itemPanel;

    private final storageUICallback callback;
    public StorageUI(JPanel panel, Character character, storageUICallback callback){
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
        setClassBar();
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
    private void setItemList(String decoClass){

        if (itemPanel != null){
            backPanel.remove(itemPanel);
        }

        itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayout(2,3));
        itemPanel.setBackground(new Color(142,118,71));

        List<Deco> decoList = getDecoList(decoClass);

        for (Deco deco : decoList) {
            System.out.println(deco.getDecoImagePath());
            ImageIcon temp = new ImageIcon(getClass().getResource(deco.getDecoImagePath()));
            Image image = temp.getImage();
            Image resizedImage = image.getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            ImageIcon itemIcon = new ImageIcon(resizedImage);

            JButton button = new JButton(itemIcon) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    if (characterDecoService.countDecosByCharacterIdAndDecoId(character.getId(), deco.getId()) == 0) {
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    }
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };

            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                setUsePopup(deco);
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

    private void setUsePopup(Deco deco){
        JLabel messageLabel = new JLabel(deco.getDecoName() + " : " +characterDecoService.countDecosByCharacterIdAndDecoId(character.getId(), deco.getId())+"개");
        messageLabel.setFont(CustomFont.loadCustomFont(18f));
        ImageIcon temp = new ImageIcon(getClass().getResource(deco.getDecoImagePath()));
        Image image = temp.getImage();
        Image resizedImage = image.getScaledInstance(50,50,Image.SCALE_SMOOTH);
        ImageIcon itemIcon = new ImageIcon(resizedImage);

        int result = JOptionPane.showConfirmDialog(
                backPanel,
                messageLabel,
                deco.getDecoName() + " 사용",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                itemIcon);
        if (result == JOptionPane.YES_OPTION && characterDecoService.countDecosByCharacterIdAndDecoId(character.getId(),deco.getId()) >= 1){
            if (Objects.equals(deco.getDecoClass(), "food")){
                characterDecoService.deleteFirstMatchingCharacterDeco(character.getId(), deco.getId());
            }
            DecoFunc decoFunc = new DecoFunc(character,deco);
            decoFunc.funcStart();
        }else if (result == JOptionPane.YES_OPTION && characterDecoService.countDecosByCharacterIdAndDecoId(character.getId(),deco.getId()) == 0) {
            JLabel label = new JLabel("재고가 부족합니다");
            label.setFont(CustomFont.loadCustomFont(18f));
            JOptionPane.showMessageDialog(
                    backPanel,
                    label,
                    deco.getDecoName()+"사용",
                    JOptionPane.INFORMATION_MESSAGE,
                    itemIcon);
        }
    }




    public interface storageUICallback{
        void backButton();
    }
}
