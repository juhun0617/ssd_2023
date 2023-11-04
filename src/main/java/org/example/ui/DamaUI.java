package org.example.ui;

import org.example.Entity.Character;
import org.example.draw.BackGroundPanel;
import org.example.service.CharacterService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;

public class DamaUI {

    private final int[] BACKGROUND = {252,255,217};


    EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    EntityManager em = emf.createEntityManager();
    CharacterService characterService = new CharacterService(em);




    private final JPanel panel;
    private Character character;


    public DamaUI(JPanel panel,String name) {
        this.panel = panel;
        this.character = characterService.findCharacterByName(name);
    }

    public void updateUi(){

        initializeBackPanel();
        System.out.println(character.getAnimal() + "-" + character.getName());

        panel.revalidate();
        panel.repaint();

    }

    private void initializeBackPanel(){
        panel.removeAll();
        panel.setBackground(new Color(BACKGROUND[0],BACKGROUND[1],BACKGROUND[2]));

    }



}
