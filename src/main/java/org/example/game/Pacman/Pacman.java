package org.example.game.Pacman;

import org.example.Entity.Character;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.awt.*;

public class Pacman extends JFrame {

    public Pacman(Character character) {

        initUI(character);
    }

    private void initUI(Character character) {

        add(new Board(this,character));

        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
    }

}
