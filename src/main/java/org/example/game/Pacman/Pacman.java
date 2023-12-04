package org.example.game.Pacman;

import org.example.Entity.Character;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.awt.*;

/**
 * @author 조설빈
 * Pacman 게임의 메인 프레임을 나타내는 클래스
 */
public class Pacman extends JFrame {
    /**
     * Pacman 클래스의 생성자
     *
     * @param character Pacman 캐릭터 객체
     */
    public Pacman(Character character) {

        initUI(character);
    }

    /**
     * UI를 초기화하는 메서드
     *
     * @param character Pacman 캐릭터 객체
     */
    private void initUI(Character character) {

        add(new Board(this, character));

        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
    }

}