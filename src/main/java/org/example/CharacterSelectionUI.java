package org.example;

import javax.swing.*;
import java.awt.*;
public class CharacterSelectionUI {

    private JPanel panel;

    public CharacterSelectionUI(JPanel panel){
        this.panel = panel;
    }


    public void updateUI() {
        JButton character1Button = new JButton("Character 1");
        JButton character2Button = new JButton("Character 2");
        JButton character3Button = new JButton("Character 3");

        panel.add(character1Button);
        panel.add(character2Button);
        panel.add(character3Button);

        // 패널의 레이아웃을 설정합니다.
        panel.setLayout(new FlowLayout());

        // 패널에 변경 사항을 적용합니다.
        panel.revalidate();
        panel.repaint();
    }

}
