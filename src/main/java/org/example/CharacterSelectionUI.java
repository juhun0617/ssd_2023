package org.example;

import org.example.draw.BackGroundPanel;

import javax.swing.*;
import java.awt.*;

public class CharacterSelectionUI {

    private JPanel panel;
    private BackGroundPanel backGroundPanel;
    public CharacterSelectionUI(JPanel panel){
        this.panel = panel;
    }


    public void updateUI() {

        panel.removeAll();


        backGroundPanel = new BackGroundPanel("/Image/CharacterSelectionBack.jpeg");
        backGroundPanel.setLayout(new BorderLayout());
        panel.add(backGroundPanel,BorderLayout.CENTER);



        // 패널에 변경 사항을 적용합니다.
        panel.revalidate();
        panel.repaint();
    }

}
