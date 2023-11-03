package org.example.ui;

import org.example.draw.BackGroundPanel;

import javax.swing.*;
import java.awt.*;

public class CharacterSelectionUI {

    private JPanel panel;
    private BackGroundPanel backGroundPanel;

    private static final String BACKGROUND_PATH = "/Image/CharacterSelectionBack.jpeg";
    public CharacterSelectionUI(JPanel panel){
        this.panel = panel;
    }


    public void updateUI() {

        initializeBackPanel();

        // 패널에 변경 사항을 적용합니다.
        panel.revalidate();
        panel.repaint();
    }

    private void initializeBackPanel() {
        panel.removeAll();

        backGroundPanel = new BackGroundPanel(BACKGROUND_PATH);
        backGroundPanel.setLayout(new BorderLayout());
        panel.add(backGroundPanel,BorderLayout.CENTER);
    }

}
