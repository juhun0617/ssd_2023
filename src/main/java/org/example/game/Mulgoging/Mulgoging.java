package org.example.game.Mulgoging;

import org.example.Entity.Character;
import org.example.service.CharacterService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;

public class Mulgoging {

    public JFrame frame;
    private CardLayout cl;
    private GamePanel gamePanel;
    private IntroPanel introPanel;
    private EndPanel endPanel;
    private Character character;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");

    CharacterService characterService = new CharacterService(emf);


    public Mulgoging(Character character) {
        this.character = character;
        initialize(character.getAnimal());
    }

    private void initialize(String characterName) {
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cl = new CardLayout();
        frame.getContentPane().setLayout(cl);

        introPanel = new IntroPanel(this);
        gamePanel = new GamePanel(frame, cl, this, characterName);
        endPanel = new EndPanel(this,frame,0); // 수정: Main 객체 참조와 함께 초기 스코어 전달

        frame.getContentPane().add(introPanel, "intro");
        frame.getContentPane().add(gamePanel, "game");
        frame.getContentPane().add(endPanel, "end");

        // 기타 초기화 로직
    }


    public void startGame() {
        cl.show(frame.getContentPane(), "game");
        gamePanel.gameReady();
        gamePanel.gameStart();
    }

    public void endGame(int finalScore) {
        endPanel.updateScore(finalScore); // EndPanel에 최종 점수 업데이트
        cl.show(frame.getContentPane(), "end"); // EndPanel을 보여줍니다.
        int current_Score = character.getMax_score_3();
        if (finalScore > current_Score){
            character.setMax_score_3(finalScore);
            character.setMoney(character.getMoney()+(finalScore*100));
            character.setFun(character.getFun()+10);
            character.setHungry(character.getHungry()-10);
            characterService.saveCharacter(character);
        }
    }

    public void restartGame() {
        cl.show(frame.getContentPane(), "game"); // GamePanel 보여주기
        gamePanel.restartGame(); // GamePanel의 restartGame 메서드 호출
    }

    // 필요한 경우 추가적인 메서드 구현
}
