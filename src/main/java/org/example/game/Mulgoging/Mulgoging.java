package org.example.game.Mulgoging;

import org.example.Entity.Character;
import org.example.service.CharacterService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Mulgoging 게임의 메인 클래스입니다.
 * 게임의 주요 화면을 관리하고, 게임의 시작과 종료를 담당합니다.
 * 또한 캐릭터 관련 데이터 처리를 담당합니다.
 */
public class Mulgoging {

    public JFrame frame; // 게임의 메인 프레임
    EntityManagerFactory emf; // 엔티티 관리자 팩토리
    CharacterService characterService; // 캐릭터 서비스
    private CardLayout cl; // 화면 전환을 위한 카드 레이아웃
    private GamePanel gamePanel; // 게임 화면
    private IntroPanel introPanel; // 소개 화면
    private EndPanel endPanel; // 종료 화면
    private Character character; // 게임 캐릭터

    /**
     * Mulgoging 객체를 생성하고 초기화합니다.
     *
     * @param character 사용할 게임 캐릭터
     */
    public Mulgoging(Character character) {
        this.character = character;
        initialize(character.getAnimal());
    }

    /**
     * 게임 화면과 데이터베이스 연결을 초기화합니다.
     *
     * @param characterName 사용할 캐릭터 이름
     */
    private void initialize(String characterName) {
        // 데이터베이스 설정 및 연결
        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db")
                .toString();
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);

        emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        characterService = new CharacterService(emf);

        // 화면 설정
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cl = new CardLayout();
        frame.getContentPane()
                .setLayout(cl);

        introPanel = new IntroPanel(this);
        gamePanel = new GamePanel(frame, cl, this, characterName);
        endPanel = new EndPanel(this, frame, 0); // 수정: Main 객체 참조와 함께 초기 스코어 전달

        frame.getContentPane()
                .add(introPanel, "intro");
        frame.getContentPane()
                .add(gamePanel, "game");
        frame.getContentPane()
                .add(endPanel, "end");

        // 기타 초기화 로직
    }

    /**
     * 게임을 시작합니다. 게임 화면을 표시하고 게임을 준비 및 시작합니다.
     */
    public void startGame() {
        cl.show(frame.getContentPane(), "game");
        gamePanel.gameReady();
        gamePanel.gameStart();
    }

    /**
     * 게임을 종료하고 점수를 처리합니다.
     * 최종 점수를 EndPanel에 업데이트하고, 필요한 경우 캐릭터 데이터를 업데이트합니다.
     *
     * @param finalScore 게임에서 얻은 최종 점수
     */
    public void endGame(int finalScore) {
        endPanel.updateScore(finalScore);
        cl.show(frame.getContentPane(), "end");
        // 캐릭터 점수 및 상태 업데이트 로직
    }

    /**
     * 게임을 다시 시작합니다.
     * GamePanel을 다시 표시하고, 게임을 재시작합니다.
     */
    public void restartGame() {
        cl.show(frame.getContentPane(), "game");
        gamePanel.restartGame();
    }


}