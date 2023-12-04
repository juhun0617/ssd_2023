package org.example.game.Snake;

import org.example.Entity.Character;
import org.example.service.CharacterService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * @author younghun_kim
 * <p>
 * 스네이크 게임의 메인 어플리케이션 클래스입니다.
 */
public class SnakeGame extends JFrame {

    private Clip clip;


    /**
     * SnakeGame 클래스의 생성자입니다.
     *
     * @param character 게임 캐릭터
     */
    public SnakeGame(Character character) {
        playBackgroundMusic("/Snake/sounds/sbg.wav");
        this.add(new GamePanel(character, clip, this));
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    /**
     * 배경 음악을 재생하는 메서드입니다.
     *
     * @param filePath 재생할 음악 파일의 경로
     */
    private void playBackgroundMusic(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(filePath)));
            clip = AudioSystem.getClip();
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


}

/**
 * Snake 게임의 게임 화면을 나타내는 패널 클래스입니다.
 */

class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    public int money = 0;
    JButton restartButton;
    JButton exitButton;
    JLabel scoreLabel;
    int DELAY = 100;
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    int animal;
    EntityManagerFactory emf;
    CharacterService characterService;
    private Clip clip1;
    private Character character;
    private JFrame frame;

    /**
     * GamePanel 클래스의 생성자입니다.
     *
     * @param character 게임 캐릭터
     * @param clip      음악 클립
     * @param frame     게임 프레임
     */

    GamePanel(Character character, Clip clip, JFrame frame) {
        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db")
                .toString();
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        characterService = new CharacterService(emf);
        this.character = character;
        this.frame = frame;
        this.clip1 = clip;
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        initializeGameOverComponents();
        startGame();
    }

    /**
     * 게임 오버 화면의 컴포넌트를 초기화하는 메서드입니다.
     */
    private void initializeGameOverComponents() {
        // Initialize the components
        restartButton = new JButton("Restart");
        exitButton = new JButton("Exit");
        scoreLabel = new JLabel();

        // Set the layout to null so we can set absolute positions
        setLayout(null);

        // Position the score label
        scoreLabel.setBounds(350, 300, 200, 50);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Ink Free", Font.BOLD, 20));
        add(scoreLabel);

        // Position the restart button
        restartButton.setBounds(250, 500, 100, 50);
        add(restartButton);
        restartButton.addActionListener(e -> restartGame());

        // Position the exit button
        exitButton.setBounds(450, 500, 100, 50);
        add(exitButton);
        exitButton.addActionListener(e -> {
            character.setXp(character.getXp() + 10);
            character.setFun(character.getFun() + 10);
            character.setHungry(character.getHungry());
            character.setMoney(character.getMoney() + (money));
            if (character.getMax_score_2() < money) {
                character.setMax_score_2(money);
            }
            characterService.saveCharacter(character);
            stopBackgroundMusic();
            frame.dispose();
        });

        // Initially, these components should not be visible
        restartButton.setVisible(false);
        exitButton.setVisible(false);
        scoreLabel.setVisible(false);
    }

    /**
     * 배경 음악을 정지하는 메서드입니다.
     */
    private void stopBackgroundMusic() {
        clip1.stop();
    }

    /**
     * 게임을 재시작하는 메서드입니다.
     */
    private void restartGame() {
        // 게임 재시작 로직
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        DELAY = 100;
        scoreLabel.setVisible(false);
        restartButton.setVisible(false);
        exitButton.setVisible(false);
        startGame();
    }

    /**
     * 게임 오버 화면을 그리는 메서드입니다.
     *
     * @param g 그래픽스 컨텍스트
     */
    public void gameOver(Graphics g) {

        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        int finalScore = applesEaten * 50;
        scoreLabel.setText("Score: " + finalScore);
        scoreLabel.setVisible(true);
        restartButton.setVisible(true);
        exitButton.setVisible(true);
    }

    /**
     * 사운드 효과를 재생하는 메서드입니다.
     *
     * @param filePath 재생할 사운드 효과 파일의 경로
     */
    private void playSoundEffect(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(filePath)));
            System.out.println(filePath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * 게임을 시작하는 메서드입니다.
     */
    public void startGame() {
        for (int i = 0; i < bodyParts; i++) {
            x[i] = SCREEN_WIDTH / 2 - i * UNIT_SIZE;
            y[i] = SCREEN_HEIGHT / 2;
        }
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        animal = 2; // 동물색
    }

    /**
     * 패널의 그래픽 컴포넌트를 그리는 메서드입니다.
     *
     * @param g 그래픽스 컨텍스트
     */

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * 게임 화면을 그리는 메서드입니다.
     *
     * @param g 그래픽스 컨텍스트
     */

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    switch (animal) {
                        case 0:
                            g.setColor(Color.darkGray);
                            break;
                        case 1:
                            g.setColor(Color.white);
                            break;
                        case 2:
                            g.setColor(Color.pink);
                            break;
                        case 3:
                            g.setColor(Color.blue);
                            break;
                        default:
                            g.setColor(new Color(255, 165, 0)); // 기본 주황색
                            break;
                    }
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    switch (animal) {
                        case 0:
                            g.setColor(Color.darkGray.darker());
                            break;
                        case 1:
                            g.setColor(Color.white.darker());
                            break;
                        case 2:
                            g.setColor(Color.pink.darker());
                            break;
                        case 3:
                            g.setColor(Color.blue.darker());
                            break;
                        default:
                            g.setColor(new Color(255, 165, 0).darker());
                            break;
                    }
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.green);
            g.fillRect(0, 0, SCREEN_WIDTH, UNIT_SIZE);
            g.fillRect(0, 0, UNIT_SIZE, SCREEN_HEIGHT);
            g.fillRect(SCREEN_WIDTH - UNIT_SIZE, 0, UNIT_SIZE, SCREEN_HEIGHT);
            g.fillRect(0, SCREEN_HEIGHT - UNIT_SIZE, SCREEN_WIDTH, UNIT_SIZE);

        } else {
            gameOver(g);
        }
    }

    /**
     * 새로운 사과를 생성하는 메서드입니다.
     */
    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE) - 2) * UNIT_SIZE + UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE) - 2) * UNIT_SIZE + UNIT_SIZE;
    }

    /**
     * 뱀을 움직이는 메서드입니다.
     */
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    /**
     * 사과를 확인하고 먹는 메서드입니다.
     */
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            money += 50;
            newApple();
            if (DELAY > 10) {
                DELAY -= 5;
                timer.setDelay(DELAY);
            }
            playSoundEffect("/Snake/sounds/sitem.wav");
        }
    }

    /**
     * 충돌을 확인하는 메서드입니다.
     */
    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        if (x[0] < UNIT_SIZE || x[0] >= SCREEN_WIDTH - UNIT_SIZE || y[0] < UNIT_SIZE || y[0] >= SCREEN_HEIGHT - UNIT_SIZE) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    /**
     * ActionListener를 구현한 메서드입니다. 타이머에 의해 주기적으로 호출됩니다.
     * 게임이 실행 중일 때 뱀을 움직이게하고 충돌을 확인한 후 화면을 다시 그립니다.
     *
     * @param e ActionEvent 이벤트 객체
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }


    /**
     * KeyAdapter를 확장한 MyKeyAdapter 클래스입니다.
     * 사용자의 키 입력을 처리하고 뱀의 방향을 바꿉니다.
     */

    public class MyKeyAdapter extends KeyAdapter {


        /**
         * 사용자의 키 입력을 처리하고 뱀의 방향을 제어합니다.
         *
         * @param e KeyEvent 이벤트 객체
         */
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}