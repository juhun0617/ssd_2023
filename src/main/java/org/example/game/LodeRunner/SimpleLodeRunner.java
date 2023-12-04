package org.example.game.LodeRunner;

import org.example.Entity.Character;
import org.example.service.CharacterService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;


public class SimpleLodeRunner extends JFrame {

    private Clip clip;
    private EntityManagerFactory emf;


    public SimpleLodeRunner(Character character) {
        playBackgroundMusic("/LoadRunner/sounds/lbg.wav");
        add(new GamePanel(character,this,clip));
        pack();
        this.setTitle("Simple Lode Runner Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    public void playBackgroundMusic (String filePath) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            System.out.println("Error with playing sound: " + e.getMessage());
        }
    }
    public void playSoundEffect(String filePath) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error with playing sound effect: " + e.getMessage());
        }
    }
    static class GamePanel extends JPanel implements ActionListener {

        private JButton restartButton;
        private JButton exitButton;
        private JLabel scoreLabel;


        private List<Point> enemies; // 적의 위치를 저장하는 리스트
        private List<EnemyAI> enemyAIs;
        private boolean gameOver = false;
        private boolean gameClear = false;
        private static final int PANEL_WIDTH = 800;
        private static final int PANEL_HEIGHT = 800;
        private static final int UNIT_SIZE = 32;
        private static final int DELAY = 150;
        private static final long MOVE_DELAY = 150;
        private final Set<Point> ground = new HashSet<>();
        private final Set<Point> ladders = new HashSet<>();
        private final Set<Point> coins = new HashSet<>();
        private final Set<Point> brokenGrounds = new HashSet<>();
        private final Map<Point, Long> brokenGroundsTimers = new HashMap<>();
        private Point player, enemy;
        private EnemyAI enemyAI;
        private Timer timer;
        private int collectedCoins = 0;
        private static final int TOTAL_COINS = 5;
        private boolean isFalling = false;
        private long lastMoveTime = 0;

        private Image playerImage,ladderImage, keyImage, wallImage, enemyImage,
                walkImage1, walkImage2, climbImage, digImage, backgroundImage;
        private Image currentWalkImage;
        private Timer walkTimer;
        private static final int WALK_ANIMATION_DELAY = 500;
        private String playerState = "idle";
        private boolean facingRight = true;

        public int money = 0;
        private int currentStage = 1;
        private static final int MAX_STAGES = 3;
        private JFrame frame;

        String homeDirectory = System.getProperty("user.home");

        CharacterService characterService;

        enum GameState {
            PLAYING,
            TRANSITIONING,
            NEXT_STAGE
        }

        private GameState gameState = GameState.PLAYING;
        private float transitionProgress = 0.0f;


        private Character character;

        private Clip clip1;
        public GamePanel(Character character,JFrame frame,Clip clip) {
            String targetPath = Paths.get(homeDirectory, "sqlite.db").toString();
            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


            EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
            characterService = new CharacterService(emf);

            // Inside the GamePanel constructor
            initializeGameOverComponents();
            this.clip1 = clip;
            this.character = character;
            this.frame = frame;
            enemies = new ArrayList<>();
            enemyAIs = new ArrayList<>();
            this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
            this.setBackground(Color.BLACK);
            this.setFocusable(true);
            this.addKeyListener(new MyKeyAdapter());

            walkTimer = new Timer(WALK_ANIMATION_DELAY, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (playerState.equals("walking")) {
                        currentWalkImage = currentWalkImage == walkImage1 ? walkImage2 : walkImage1;
                        repaint();
                    }
                }
            });
            walkTimer.start();


            try {
                // Player images
                ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LoadRunner/image/player/idle.png")));
                Image originalImage = originalIcon.getImage();
                playerImage = originalImage.getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH);

                walkImage1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LoadRunner/image/player/run1.png"))).getImage().getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH);
                walkImage2 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LoadRunner/image/player/run2.png"))).getImage().getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH);
                climbImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LoadRunner/image/player/up.png"))).getImage().getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH);
                digImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LoadRunner/image/player/broke.png"))).getImage().getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH);

                // Sprite images
                ladderImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LoadRunner/image/sprite/lad4.png"))).getImage().getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH);
                keyImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LoadRunner/image/sprite/gold.png"))).getImage().getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH);
                wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LoadRunner/image/sprite/wall3.png"))).getImage().getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH);
                enemyImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LoadRunner/image/sprite/enemy.png"))).getImage().getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH);
                backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LoadRunner/image/sprite/bg1.png"))).getImage();

            } catch (Exception e) {
                System.out.println("Error loading images: " + e.getMessage());
            }

            startGame();
        }

        private void stopBackgroundMusic(){
            clip1.stop();
        }
        private void initializeGameOverComponents() {
            // Initialize the components
            restartButton = new JButton("Restart");
            exitButton = new JButton("Exit");
            scoreLabel = new JLabel();


            setLayout(null);


            scoreLabel.setBounds(350, 300, 200, 50);
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setFont(new Font("Ink Free", Font.BOLD, 20));
            add(scoreLabel);


            restartButton.setBounds(300, 500, 100, 50);
            add(restartButton);
            restartButton.addActionListener(e -> restartGame());


            exitButton.setBounds(450, 500, 100, 50);
            add(exitButton);
            exitButton.addActionListener(e -> {

                character.setXp(character.getXp()+10);
                character.setFun(character.getFun()+10);
                character.setHungry(character.getHungry());
                character.setMoney(character.getMoney() + (money));
                if (character.getMax_score_1() < money){
                    character.setMax_score_1(money);
                }
                characterService.saveCharacter(character);
                stopBackgroundMusic();
                frame.dispose();
            });


            restartButton.setVisible(false);
            exitButton.setVisible(false);
            scoreLabel.setVisible(false);
        }

        private void restartGame() {

            resetGame();
            startGame();


            restartButton.setVisible(false);
            exitButton.setVisible(false);
            scoreLabel.setVisible(false);
        }



        public void startGame() {

            if (currentStage == 3) {

                player = new Point(UNIT_SIZE * 4, PANEL_HEIGHT - 4 * UNIT_SIZE);


                enemies.clear();
                enemyAIs.clear();


                Point enemy1 = new Point(UNIT_SIZE * 5, PANEL_HEIGHT - 9 * UNIT_SIZE); // 1층
                Point enemy2 = new Point(PANEL_WIDTH - 8 * UNIT_SIZE, PANEL_HEIGHT - 16 * UNIT_SIZE); // 2층

                Point enemy3 = new Point(PANEL_WIDTH - 10 * UNIT_SIZE, PANEL_HEIGHT - 20 * UNIT_SIZE); // 4층
                Point enemy4 = new Point(UNIT_SIZE * 7, PANEL_HEIGHT - 24 * UNIT_SIZE); // 5층



                enemies.addAll(Arrays.asList(enemy1, enemy2, enemy3, enemy4));
                for (Point enemy : enemies) {
                    enemyAIs.add(new EnemyAI(enemy, ladders, ground, UNIT_SIZE));
                }


                for (int i = 0; i < PANEL_WIDTH / UNIT_SIZE; i++) {
                    ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 2 * UNIT_SIZE)); // 1층
                    if (i < 12 || i > 16) {
                        ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 6 * UNIT_SIZE)); // 2층
                    }
                    if (i > 6 && i < 20) {
                        ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 10 * UNIT_SIZE)); // 3층
                    }
                    if (i < 10 || i > 12) {
                        ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 14 * UNIT_SIZE)); // 4층
                    }
                    if (i > 3 && i < 19) {
                        ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 18 * UNIT_SIZE)); // 5층
                    }
                    if (i < 15) {
                        ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 22 * UNIT_SIZE)); // 6층
                    }
                    ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 26 * UNIT_SIZE)); // 7층
                }


                for (int i = 0; i < 9; i++) {
                    ladders.add(new Point(UNIT_SIZE * 11, PANEL_HEIGHT - 3 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 19, PANEL_HEIGHT - 7 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 7, PANEL_HEIGHT - 11 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 13, PANEL_HEIGHT - 15 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 10, PANEL_HEIGHT - 19 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 3, PANEL_HEIGHT - 23 * UNIT_SIZE - i * UNIT_SIZE));
                }
                for (int i = 0; i < 13; i++) {
                    ladders.add(new Point(UNIT_SIZE * 1, PANEL_HEIGHT - 3 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 22, PANEL_HEIGHT - 3 * UNIT_SIZE - i * UNIT_SIZE));

                    ladders.add(new Point(UNIT_SIZE * 15, PANEL_HEIGHT - 11 * UNIT_SIZE - i * UNIT_SIZE));
                }



                coins.add(new Point(UNIT_SIZE * 18, PANEL_HEIGHT - 7 * UNIT_SIZE));
                coins.add(new Point(UNIT_SIZE * 8, PANEL_HEIGHT - 11 * UNIT_SIZE));
                coins.add(new Point(UNIT_SIZE * 14, PANEL_HEIGHT - 15 * UNIT_SIZE));
                coins.add(new Point(UNIT_SIZE * 10, PANEL_HEIGHT - 19 * UNIT_SIZE));

                coins.add(new Point(UNIT_SIZE * 4, PANEL_HEIGHT - 23 * UNIT_SIZE));
            }


            else if (currentStage == 2) {

                player = new Point(UNIT_SIZE * 3, PANEL_HEIGHT - 3 * UNIT_SIZE);


                enemies.clear();
                enemyAIs.clear();


                Point enemy1 = new Point(UNIT_SIZE * 18, PANEL_HEIGHT - 19 * UNIT_SIZE);
                Point enemy2 = new Point(PANEL_WIDTH - 7 * UNIT_SIZE, PANEL_HEIGHT - 3 * UNIT_SIZE);

                Point enemy3 = new Point(PANEL_WIDTH - 10 * UNIT_SIZE, PANEL_HEIGHT - 15 * UNIT_SIZE);

                enemies.addAll(Arrays.asList(enemy1, enemy2, enemy3));
                for (Point enemy : enemies) {
                    enemyAIs.add(new EnemyAI(enemy, ladders, ground, UNIT_SIZE));
                }


                for (int i = 0; i < PANEL_WIDTH / UNIT_SIZE; i++) {

                    ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 2 * UNIT_SIZE));
                    if (i < 15 || i > PANEL_WIDTH / UNIT_SIZE - 6) {
                        ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 6 * UNIT_SIZE));
                    }
                    if (i > 4 && i < 18) {
                        ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 10 * UNIT_SIZE));
                    }
                    ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 14 * UNIT_SIZE));
                    ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 18 * UNIT_SIZE));
                }


                for (int i = 0; i < 5; i++) {
                    ladders.add(new Point(UNIT_SIZE * 14, PANEL_HEIGHT - 3 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 3, PANEL_HEIGHT - 3 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 8, PANEL_HEIGHT - 7 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 5, PANEL_HEIGHT - 11 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 15, PANEL_HEIGHT - 11 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 10, PANEL_HEIGHT - 15 * UNIT_SIZE - i * UNIT_SIZE));
                }
                for (int i = 0; i < 16; i++) {
                    ladders.add(new Point(UNIT_SIZE * 22, PANEL_HEIGHT - 3 * UNIT_SIZE - i * UNIT_SIZE));
                }

                coins.add(new Point(UNIT_SIZE * 8, PANEL_HEIGHT - 3 * UNIT_SIZE));
                coins.add(new Point(UNIT_SIZE * 7, PANEL_HEIGHT - 7 * UNIT_SIZE));
                coins.add(new Point(UNIT_SIZE * 20, PANEL_HEIGHT - 7 * UNIT_SIZE));
                coins.add(new Point(UNIT_SIZE * 3, PANEL_HEIGHT - 15 * UNIT_SIZE));
                coins.add(new Point(UNIT_SIZE * 5, PANEL_HEIGHT - 19 * UNIT_SIZE));
            }

            else if (currentStage == 1) {

                player = new Point(UNIT_SIZE * 3, PANEL_HEIGHT - 3 * UNIT_SIZE);


                enemies.clear();
                enemyAIs.clear();


                Point enemy1 = new Point(PANEL_WIDTH - 3 * UNIT_SIZE, PANEL_HEIGHT - 3 * UNIT_SIZE);
                Point enemy2 = new Point(UNIT_SIZE * 10, PANEL_HEIGHT - 15 * UNIT_SIZE);
                Point enemy3 = new Point(PANEL_WIDTH - 2 * UNIT_SIZE, PANEL_HEIGHT - 11 * UNIT_SIZE);

                Point enemy4 = new Point(PANEL_WIDTH - 10 * UNIT_SIZE, PANEL_HEIGHT - 19 * UNIT_SIZE);

                enemies.addAll(Arrays.asList(enemy1, enemy2, enemy3, enemy4));
                for (Point enemy : enemies) {
                    enemyAIs.add(new EnemyAI(enemy, ladders, ground, UNIT_SIZE));
                }


                for (int i = 0; i < PANEL_WIDTH / UNIT_SIZE; i++) {
                    ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 2 * UNIT_SIZE));
                    if (i < 22) {
                        ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 6 * UNIT_SIZE));
                    }
                    if (i > 4 && i < 24) {
                        ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 10 * UNIT_SIZE));
                    }
                    if (i < 18) {
                        ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 14 * UNIT_SIZE));
                    }
                    if (i > 3 && i < 19) {
                        ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 18 * UNIT_SIZE));
                    }
                    ground.add(new Point(i * UNIT_SIZE, PANEL_HEIGHT - 22 * UNIT_SIZE));
                }


                for (int i = 0; i < 5; i++) {
                    ladders.add(new Point(UNIT_SIZE * 12, PANEL_HEIGHT - 3 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 17, PANEL_HEIGHT - 7 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 13, PANEL_HEIGHT - 11 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 10, PANEL_HEIGHT - 15 * UNIT_SIZE - i * UNIT_SIZE));
                    ladders.add(new Point(UNIT_SIZE * 6, PANEL_HEIGHT - 19 * UNIT_SIZE - i * UNIT_SIZE));
                }

                for (int i = 0; i < 21; i++) {
                    ladders.add(new Point(UNIT_SIZE * 2, PANEL_HEIGHT - 3 * UNIT_SIZE - i * UNIT_SIZE));
                }


                coins.add(new Point(UNIT_SIZE * 11, PANEL_HEIGHT - 3 * UNIT_SIZE));
                coins.add(new Point(UNIT_SIZE * 15, PANEL_HEIGHT - 7 * UNIT_SIZE));

                coins.add(new Point(UNIT_SIZE * 9, PANEL_HEIGHT - 15 * UNIT_SIZE));
                coins.add(new Point(UNIT_SIZE * 7, PANEL_HEIGHT - 19 * UNIT_SIZE));
                coins.add(new Point(UNIT_SIZE * 5, PANEL_HEIGHT - 23 * UNIT_SIZE));
            }

            if (timer == null) {
                timer = new Timer(DELAY, this);
            }

            timer.start();
        }

        private void resetGame() {
            if (timer != null) {
                timer.stop();
            }

            ground.clear();
            ladders.clear();
            coins.clear();
            brokenGrounds.clear();
            brokenGroundsTimers.clear();

            collectedCoins = 0;
            gameOver = false;

        }


        protected void paintComponent(Graphics g) {



            super.paintComponent(g);




            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, this);
            }

            draw(g);

            drawText(g);

            if (gameState == GameState.TRANSITIONING) {
                g.setColor(new Color(0, 0, 0, Math.min(1.0f, transitionProgress)));
                g.fillRect(0, 0, getWidth(), getHeight());
            } else if (gameState == GameState.NEXT_STAGE) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.WHITE);
                g.setFont(new Font("Ink Free", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                String nextStageText = "Next Stage";
                g.drawString(nextStageText, (PANEL_WIDTH - metrics.stringWidth(nextStageText)) / 2, PANEL_HEIGHT / 2);
            }



            if (gameOver) {
                showGameStatus(g, "Game over");
            }

            if (gameClear) {
                showGameStatus(g, "Game Clear");
            }
        }

        private void drawText(Graphics g) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            String text = "You can break the block with 'Q' or 'E'.";
            g.drawString(text, 10, getHeight() - 10);
        }

        public void draw(Graphics g) {




            for (Point pt : ground) {
                g.drawImage(wallImage, pt.x, pt.y, this);
            }

            // 사다리 그리기
            for (Point pt : ladders) {
                g.drawImage(ladderImage, pt.x, pt.y, this);
            }

            // 코인 그리기
            for (Point pt : coins) {
                g.drawImage(keyImage, pt.x, pt.y, this);
            }


            // 플레이어 그리기
            if (player.y < PANEL_HEIGHT) {

                if (player.y < PANEL_HEIGHT) {
                    Image imageToDraw;
                    if (playerState.equals("walking")) {
                        imageToDraw = currentWalkImage;
                    } else if (playerState.equals("climbing")) {
                        imageToDraw = climbImage;
                    } else if (playerState.equals("digging")) {
                        imageToDraw = digImage;
                    } else {
                        imageToDraw = playerImage;
                    }

                    if (!facingRight) {
                        // 이미지 반전
                        imageToDraw = flipImageHorizontally(imageToDraw);
                    }

                    g.drawImage(imageToDraw, player.x, player.y, this);
                }



            }
            for (EnemyAI enemyAI : enemyAIs) {
                Point enemyPosition = enemyAI.getEnemyPosition();
                if (enemyPosition.y < PANEL_HEIGHT) {
                    g.drawImage(enemyImage, enemyPosition.x, enemyPosition.y, this);
                }
            }






        }




        private Image flipImageHorizontally(Image img) {
            int width = img.getWidth(this);
            int height = img.getHeight(this);
            BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = flippedImage.createGraphics();
            g.drawImage(img, 0, 0, width, height, width, 0, 0, height, null);
            g.dispose();
            return flippedImage;
        }



        public void actionPerformed(ActionEvent e) {
            if (gameState == GameState.TRANSITIONING) {
                transitionProgress += 0.02f;
                if (transitionProgress >= 1.0f) {
                    gameState = GameState.NEXT_STAGE;
                    timer.stop();
                    Timer nextStageTimer = new Timer(2000, event -> {
                        gameState = GameState.PLAYING;
                        goToNextStage();
                        timer.setDelay(DELAY);
                        timer.restart();
                    });
                    nextStageTimer.setRepeats(false);
                    nextStageTimer.start();
                }
            } else if (gameState == GameState.PLAYING) {

                for (EnemyAI enemyAI : enemyAIs) {
                    if (enemyAI != null) {
                        enemyAI.update(player);
                    }
                }

                regenerateBrokenGrounds();
                applyGravity();
                checkCoinCollection();
                checkGameOver();
            }
            repaint();
        }




        private void checkGameOver() {
            if (player.y > PANEL_HEIGHT) {
                gameOver = true;
                timer.stop();
            }

            for (EnemyAI enemyAI : enemyAIs) {
                if (enemyAI != null) {
                    Point enemyPosition = enemyAI.getEnemyPosition();
                    if (player.distance(enemyPosition) < UNIT_SIZE) {
                        gameOver = true;
                        ((SimpleLodeRunner) SwingUtilities.getWindowAncestor(this)).playSoundEffect("/LoadRunner/sounds/over.wav");
                        timer.stop();
                        break;
                    }
                }
            }

            if (collectedCoins >= TOTAL_COINS) {
                if (currentStage < MAX_STAGES) {
                    money += 1000;
                    gameState = GameState.TRANSITIONING;
                    transitionProgress = 0.0f;
                    timer.setDelay(10);
                } else {

                    money += 3000;
                    gameClear = true;

                    timer.stop();
                }
            }

            if (gameOver) {
                // Display the score
                scoreLabel.setText("Score: " + money); // Update this based on your scoring system
                scoreLabel.setVisible(true);

                // Show the buttons
                restartButton.setVisible(true);
                exitButton.setVisible(true);
            }

            if (gameClear) {
                // Display the score
                scoreLabel.setText("Score: " + money); // Update this based on your scoring system
                scoreLabel.setVisible(true);

                // Show the buttons

                exitButton.setVisible(true);
            }
        }



        private void goToNextStage() {

            currentStage++;

            resetGame();
            startGame();
        }



        private void regenerateBrokenGrounds() {
            long currentTime = System.currentTimeMillis();
            Iterator<Point> iterator = brokenGrounds.iterator();
            while (iterator.hasNext()) {
                Point brokenGround = iterator.next();
                Long brokenTime = brokenGroundsTimers.get(brokenGround);
                if (brokenTime != null && (currentTime - brokenTime) >= 4000) {
                    ground.add(brokenGround);
                    iterator.remove();
                    brokenGroundsTimers.remove(brokenGround);
                }
            }
        }

        private void applyGravity() {
            Point below = new Point(player.x, player.y + UNIT_SIZE);
            if (!ground.contains(below) && !ladders.contains(below)) {
                player.y += UNIT_SIZE;
            }

            if (!ground.contains(new Point(player.x, player.y + UNIT_SIZE)) &&
                    !ladders.contains(new Point(player.x, player.y))) {
                player.y = player.y + UNIT_SIZE;
                isFalling = true;
            } else {
                isFalling = false;
            }


            for (EnemyAI enemyAI : enemyAIs) {
                if (enemyAI != null) {
                    Point enemyPosition = enemyAI.getEnemyPosition();
                    Point belowEnemy = new Point(enemyPosition.x, enemyPosition.y + UNIT_SIZE);
                    if (!ground.contains(belowEnemy) && !ladders.contains(belowEnemy)) {
                        enemyPosition.y += UNIT_SIZE;
                    }
                }
            }
        }



        private void checkCoinCollection() {
            Iterator<Point> iterator = coins.iterator();
            while (iterator.hasNext()) {
                Point coin = iterator.next();
                if (player.distance(coin) < UNIT_SIZE) {
                    ((SimpleLodeRunner) SwingUtilities.getWindowAncestor(this)).playSoundEffect("/LoadRunner/sounds/coin.wav");

                    iterator.remove();
                    collectedCoins++;
                    if (collectedCoins >= TOTAL_COINS) {
                        ((SimpleLodeRunner) SwingUtilities.getWindowAncestor(this)).playSoundEffect("/LoadRunner/sounds/clear.wav");
                        checkGameOver();
                    }
                }
            }
        }


        private void showGameStatus(Graphics g, String status) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(status, (PANEL_WIDTH - metrics.stringWidth(status)) / 2, PANEL_HEIGHT / 2);
        }

        private class MyKeyAdapter extends KeyAdapter {
            public void keyPressed(KeyEvent e) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastMoveTime < MOVE_DELAY) return;

                int xDirection = 0;
                boolean moved = false;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        xDirection = -1;
                        facingRight = false;
                        moved = true;
                        playerState = "walking";
                        break;
                    case KeyEvent.VK_RIGHT:
                        xDirection = 1;
                        facingRight = true;
                        moved = true;
                        playerState = "walking";
                        break;
                    case KeyEvent.VK_UP:
                        climbLadder(-1);
                        moved = true;
                        playerState = "climbing";
                        break;
                    case KeyEvent.VK_DOWN:
                        climbLadder(1);
                        moved = true;
                        playerState = "climbing";
                        break;
                    case KeyEvent.VK_Q:
                        digGround(-1);
                        moved = true;
                        playerState = "digging";
                        break;
                    case KeyEvent.VK_E:
                        digGround(1);
                        moved = true;
                        playerState = "digging";
                        break;
                }



                if (moved) {
                    lastMoveTime = currentTime;
                }

                if (isFalling) {
                    applyHorizontalGravityMovement(xDirection);
                } else {
                    movePlayer(xDirection, 0);
                }
            }
            public void keyReleased(KeyEvent e) {

                playerState = "idle";
            }
        }

        private void movePlayer(int xDirection, int yDirection) {
            int newX = player.x + xDirection * UNIT_SIZE;
            int newY = player.y + yDirection * UNIT_SIZE;

            boolean onLadder = ladders.contains(new Point(player.x, player.y));
            boolean moveToLadder = ladders.contains(new Point(newX, newY));
            boolean onGround = ground.contains(new Point(player.x, player.y + UNIT_SIZE));
            boolean moveToGround = ground.contains(new Point(newX, newY + UNIT_SIZE));

            if (newX >= 0 && newX < PANEL_WIDTH && (onGround || onLadder || moveToLadder)) {
                player.setLocation(newX, player.y);
            }

            if (newY >= 0 && newY < PANEL_HEIGHT && (onLadder || moveToLadder)) {
                player.setLocation(player.x, newY);
            }

            if (newX >= 0 && newX < PANEL_WIDTH && newY >= 0 && newY < PANEL_HEIGHT) {
                if (!ground.contains(new Point(newX, newY))) {
                    player.setLocation(newX, newY);
                }
            }
        }

        private void applyHorizontalGravityMovement(int xDirection) {
            int newX = player.x + xDirection * UNIT_SIZE;
            if (newX >= 0 && newX < PANEL_WIDTH) {
                player.setLocation(newX, player.y);
            }
        }

        private void climbLadder(int yDirection) {
            int newY = player.y + yDirection * UNIT_SIZE;
            if (ladders.contains(new Point(player.x, newY)) && newY >= 0 && newY < PANEL_HEIGHT) {
                player.setLocation(player.x, newY);
            }
        }

        private void digGround(int direction) {
            int x = player.x + direction * UNIT_SIZE;
            int y = player.y + UNIT_SIZE;
            Point digPoint = new Point(x, y);
            if (ground.contains(digPoint)) {
                ((SimpleLodeRunner) SwingUtilities.getWindowAncestor(this)).playSoundEffect("/LoadRunner/sounds/broke.wav");
                ground.remove(digPoint);
                brokenGrounds.add(digPoint);
                brokenGroundsTimers.put(digPoint, System.currentTimeMillis());
            }
        }
    }



    static class EnemyAI {
        private Point enemyPosition;
        private Set<Point> ladders;
        private Set<Point> ground;
        private int unitSize;
        private double enemySpeed;
        private double moveCounter = 0;

        public EnemyAI(Point enemyPosition, Set<Point> ladders, Set<Point> ground, int unitSize) {
            this.enemyPosition = enemyPosition;
            this.ladders = ladders;
            this.ground = ground;
            this.unitSize = unitSize;
            this.enemySpeed = 0.6;
        }

        public void update(Point playerPosition) {
            moveCounter += enemySpeed;
            if (moveCounter >= 1) {

                if (playerPosition.y < enemyPosition.y) {
                    moveToNearestLadder(playerPosition, true);
                } else if (playerPosition.y > enemyPosition.y) {
                    moveToNearestLadder(playerPosition, false);
                } else {
                    trackPlayer(playerPosition);
                }
                applyGravity();
                moveCounter -= 1;
            }
        }

        private void moveToNearestLadder(Point playerPosition, boolean goingUp) {
            Point nearestLadder = findNearestLadder(playerPosition, goingUp);
            if (nearestLadder != null) {
                moveTowardsPoint(nearestLadder, goingUp);
            }
        }

        private Point findNearestLadder(Point playerPosition, boolean goingUp) {
            Point nearestLadder = null;
            int minDistance = Integer.MAX_VALUE;
            for (Point ladder : ladders) {
                int distance = Math.abs(ladder.x - enemyPosition.x);
                if (distance < minDistance && ((goingUp && ladder.y < enemyPosition.y) || (!goingUp && ladder.y > enemyPosition.y))) {
                    nearestLadder = ladder;
                    minDistance = distance;
                }
            }
            return nearestLadder;
        }

        private void moveTowardsPoint(Point target, boolean goingUp) {
            int xDirection = Integer.compare(target.x, enemyPosition.x);
            int yDirection = goingUp ? -1 : 1;
            if (target.x != enemyPosition.x) {
                moveEnemy(xDirection, 0);
            } else if (isOnLadder() && ladders.contains(new Point(enemyPosition.x, enemyPosition.y + yDirection * unitSize))) {
                moveOnLadder(yDirection);
            }
        }

        private boolean isOnLadder() {
            return ladders.contains(enemyPosition);
        }

        private void moveOnLadder(int yDirection) {
            int newY = enemyPosition.y + yDirection * unitSize;
            if (ladders.contains(new Point(enemyPosition.x, newY))) {
                enemyPosition.setLocation(enemyPosition.x, newY);
            }
        }

        private void trackPlayer(Point playerPosition) {
            int xDirection = Integer.compare(playerPosition.x, enemyPosition.x);
            moveEnemy(xDirection, 0);
        }

        private void moveEnemy(int xDirection, int yDirection) {
            int newX = enemyPosition.x + xDirection * unitSize;
            int newY = enemyPosition.y + yDirection * unitSize;

            if (canMove(newX, newY)) {
                enemyPosition.setLocation(newX, newY);
            }
        }

        private boolean canMove(int x, int y) {
            boolean onLadder = ladders.contains(new Point(enemyPosition.x, enemyPosition.y));
            boolean moveToLadder = ladders.contains(new Point(x, y));
            boolean onGround = ground.contains(new Point(enemyPosition.x, enemyPosition.y + unitSize));

            return x >= 0 && x < GamePanel.PANEL_WIDTH && y >= 0 && y < GamePanel.PANEL_HEIGHT &&
                    (onGround || onLadder || moveToLadder);
        }

        private void applyGravity() {
            Point below = new Point(enemyPosition.x, enemyPosition.y + unitSize);
            if (!ground.contains(below) && !ladders.contains(below)) {
                enemyPosition.y += unitSize;
            }
        }

        public Point getEnemyPosition() {
            return enemyPosition;
        }

        public void setEnemySpeed(double speed) {
            this.enemySpeed = speed;
        }
    }











}
