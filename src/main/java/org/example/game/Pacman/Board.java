package org.example.game.Pacman;

import org.example.Entity.Character;
import org.example.service.CharacterService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * @author 조설빈
 * Pacman 게임의 게임 보드를 나타내는 클래스.
 * JPanel을 상속하며 ActionListener를 구현하여 게임의 상태 변화에 따라 화면을 갱신한다.
 */

public class Board extends JPanel implements ActionListener {


    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);
    private final Color dotColor = new Color(192, 192, 0);
    //게임의 환경 설정에
    private final int BLOCK_SIZE = 48;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int PAC_ANIM_DELAY = 2;
    private final int PACMAN_ANIM_COUNT = 4;
    private final int MAX_GHOSTS = 12;
    private final int PACMAN_SPEED = 6;
    private final short levelData[] = {
            19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21, 0, 0, 0, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 24, 20,
            25, 16, 16, 16, 24, 24, 28, 0, 25, 24, 24, 16, 20, 0, 21,
            1, 17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 21,
            1, 17, 16, 16, 18, 18, 22, 0, 19, 18, 18, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 21,
            1, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
            9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 28
    };
    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;
    private Character character;
    private CharacterService characterService;
    //게임 화면의 크기와 관련된 변수 설정
    private Dimension d;
    // 이미지 및 생상 관련 변수 설정
    private Image ii;
    private Color mazeColor;
    //게임의 상태를 확인하는 변수 설정
    private boolean inGame = false;
    private boolean dying = false;
    private int pacAnimCount = PAC_ANIM_DELAY;
    private int pacAnimDir = 1;
    private int pacmanAnimPos = 0;
    private int N_GHOSTS = 6;
    private int pacsLeft, score;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;
    private Image ghost;
    private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
    private Image pacman3up, pacman3down, pacman3left, pacman3right;
    private Image pacman4up, pacman4down, pacman4left, pacman4right;
    private int pacman_x, pacman_y, pacmand_x, pacmand_y;
    private int req_dx, req_dy, view_dx, view_dy;
    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;
    private JFrame frame;
    private EntityManagerFactory emf;
    private Clip clip1;

    /**
     * Board 클래스의 생성자.
     *
     * @param frame     Pacman 게임의 JFrame 객체
     * @param character Pacman 캐릭터 객체
     */
    public Board(JFrame frame, Character character) {

        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db")
                .toString();
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        this.frame = frame;
        this.character = character;
        characterService = new CharacterService(emf);
        loadImages(character.getAnimal());
        initVariables();
        initBoard();
        playBackgroundMusic("/Pacman/Sound/pacman_opening.wav");
    }

    /**
     * 게임 보드 초기화를 수행하는 메서드.
     */
    private void initBoard() {

        addKeyListener(new TAdapter());

        setFocusable(true);

        setBackground(Color.white);
    }

    /**
     * 초기 변수 설정을 수행하는 메서드.
     */
    private void initVariables() {

        screenData = new short[N_BLOCKS * N_BLOCKS];
        mazeColor = new Color(80, 165, 249);
        d = new Dimension(800, 800);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];

        timer = new Timer(40, this);
        timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        initGame();
    }

    private void doAnim() {

        pacAnimCount--;

        if (pacAnimCount <= 0) {
            pacAnimCount = PAC_ANIM_DELAY;
            pacmanAnimPos = pacmanAnimPos + pacAnimDir;

            if (pacmanAnimPos == (PACMAN_ANIM_COUNT - 1) || pacmanAnimPos == 0) {
                pacAnimDir = -pacAnimDir;
            }
        }
    }

    /**
     * 게임 상태에 따라 화면을 갱신하는 메서드.
     *
     * @param g2d Graphics 객체
     */
    private void playGame(Graphics2D g2d) {

        if (dying) {

            death(g2d);

        } else {

            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
    }

    /**
     * 게임 시작 화면을 보여주는 메서드.
     *
     * @param g2d Graphics2D 객체
     */
    private void showIntroScreen(Graphics2D g2d) {

        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);

        String s = "s를 눌러 시작하세요.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(s, (SCREEN_SIZE - metr.stringWidth(s)) / 2, SCREEN_SIZE / 2);
    }

    /**
     * 게임 오버 화면을 보여주는 메서드.
     *
     * @param g2d Graphics2D 객체
     */
    private void showOutroScreen(Graphics2D g2d) {
        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);

        String s = "score : " + score + "           s를 눌러 시작하기, e를 눌러 끝내기.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(s, (SCREEN_SIZE - metr.stringWidth(s)) / 2, SCREEN_SIZE / 2);
    }

    /**
     * 현재 상태에서의 점수를 그리는 메서드.
     *
     * @param g Graphics 객체
     */
    private void drawScore(Graphics2D g) {

        int i;
        String s;

        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (i = 0; i < pacsLeft; i++) {
            g.drawImage(pacman3left, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }

    /**
     * 미로 상태를 확인하고, 조건 충족 시 다음 레벨 초기화
     * 모든 블록이 특정 조건을 만족하면 점수 증가, 유령 수 및 속도 조정 후 다음 레벨 초기화.
     */
    private void checkMaze() {

        short i = 0;
        boolean finished = true;

        while (i < N_BLOCKS * N_BLOCKS && finished) {

            if ((screenData[i] & 48) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {

            score += 50;

            if (N_GHOSTS < MAX_GHOSTS) {
                N_GHOSTS++;
            }

            if (currentSpeed < maxSpeed) {
                currentSpeed++;
            }

            initLevel();
        }
    }

    /**
     * 배경 음악을 재생하는 메소드
     *
     * @param filePath 재생할 배경 음악 파일의 경로.
     * @throws UnsupportedAudioFileException 미지원된 오디오 파일 형식 예외.
     * @throws IOException                   입출력 예외.
     * @throws LineUnavailableException      사용 불가능한 라인 예외.
     */
    private void playBackgroundMusic(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(filePath)));
            clip1 = AudioSystem.getClip();
            clip1 = AudioSystem.getClip();
            clip1.open(audioInputStream);
            clip1.start();
            clip1.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopBackgroundMusic() {
        clip1.stop();
    }

    /**
     * 죽음 시나리오 처리
     *
     * @param g2d Graphics2D 객체
     */
    private void death(Graphics2D g2d) {

        pacsLeft--;
        playSoundEffect("/Pacman/Sound/pacman_death.wav");

        if (pacsLeft == 0) {
            inGame = false;
        }

        continueLevel();
    }

    /**
     * 유령을 이동시키는 메서드.
     *
     * @param g2d Graphics2D 객체
     */
    private void moveGhosts(Graphics2D g2d) {
        short i;
        int pos;
        int count;

        for (i = 0; i < N_GHOSTS; i++) {
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);

                count = 0;

                if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                // 새로운 랜덤 방향을 설정
                if (count > 0) {
                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }
                // 유령이 팩맨을 향해 이동하도록 수정
                if (inGame) {
                    int ghostTileX = ghost_x[i] / BLOCK_SIZE;
                    int ghostTileY = ghost_y[i] / BLOCK_SIZE;
                    int pacmanTileX = pacman_x / BLOCK_SIZE;
                    int pacmanTileY = pacman_y / BLOCK_SIZE;

                    // 팩맨이 유령의 범위 내에 있으면 팩맨을 향해 이동
                    if (Math.abs(ghostTileX - pacmanTileX) + Math.abs(ghostTileY - pacmanTileY) <= 5) {
                        if (ghostTileX < pacmanTileX) {
                            ghost_dx[i] = 1;
                            ghost_dy[i] = 0;
                        } else if (ghostTileX > pacmanTileX) {
                            ghost_dx[i] = -1;
                            ghost_dy[i] = 0;
                        } else if (ghostTileY < pacmanTileY) {
                            ghost_dx[i] = 0;
                            ghost_dy[i] = 1;
                        } else if (ghostTileY > pacmanTileY) {
                            ghost_dx[i] = 0;
                            ghost_dy[i] = -1;
                        }
                    }
                }

            }

            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);

            if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                    && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                    && inGame) {
                dying = true;
            }
        }
    }

    /**
     * 유령을 그리는 메소드
     *
     * @param g2d 그래픽을 렌더링하는 데 사용되는 Graphics2D 객체.
     * @param x   유령의 x 좌표
     * @param y   유령의 y 좌표
     */
    private void drawGhost(Graphics2D g2d, int x, int y) {

        g2d.drawImage(ghost, x, y, this);
    }

    /**
     * 오디오 이펙트를 재생하는 메소드
     *
     * @param filePath 재생할 오디오 이펙트의 파일 경로
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
     * 팩맨의 이동을 처리하는 메서드.
     */
    private void movePacman() {

        int pos;
        short ch;

        if (req_dx == -pacmand_x && req_dy == -pacmand_y) {
            pacmand_x = req_dx;
            pacmand_y = req_dy;
            view_dx = pacmand_x;
            view_dy = pacmand_y;
        }

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score++;
                playSoundEffect("/Pacman/Sound/pacman_eating.wav");
            }

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                    view_dx = pacmand_x;
                    view_dy = pacmand_y;
                }
            }

            // Check for standstill
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        }
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;
    }

    /**
     * 팩맨을 그리는 메서드.
     *
     * @param g2d Graphics2D 객체
     */
    private void drawPacman(Graphics2D g2d) {

        if (view_dx == -1) {
            drawPacmanLeft(g2d);
        } else if (view_dx == 1) {
            drawPacmanRight(g2d);
        } else if (view_dy == -1) {
            drawPacmanUp(g2d);
        } else {
            drawPacmanDown(g2d);
        }
    }

    private void drawPacmanUp(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2up, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3up, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4up, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman2up, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanDown(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2down, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3down, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4down, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman2down, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanLeft(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2left, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3left, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4left, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman2left, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanRight(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2right, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3right, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4right, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman2right, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    /**
     * 미로를 그리는 메서드.
     *
     * @param g2d Graphics2D 객체
     */
    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(mazeColor);
                g2d.setStroke(new BasicStroke(2));

                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(dotColor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }

                i++;
            }
        }
    }

    /**
     * 게임 초기화를 수행하는 메서드.
     */
    private void initGame() {

        pacsLeft = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 6;
        currentSpeed = 3;
    }

    /**
     * 레벨 초기화를 수행하는 메서드.
     */
    private void initLevel() {

        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }

        continueLevel();
    }

    /**
     * 레벨을 계속 진행하는 메서드.
     */
    private void continueLevel() {

        short i;
        int dx = 1;
        int random;

        for (i = 0; i < N_GHOSTS; i++) {

            ghost_y[i] = 4 * BLOCK_SIZE;
            ghost_x[i] = 4 * BLOCK_SIZE;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[random];
        }

        pacman_x = 7 * BLOCK_SIZE;
        pacman_y = 11 * BLOCK_SIZE;
        pacmand_x = 0;
        pacmand_y = 0;
        req_dx = 0;
        req_dy = 0;
        view_dx = -1;
        view_dy = 0;
        dying = false;
    }

    private void loadImages(String name) {

        ghost = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/tiger.png"))).getImage();
        pacman1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + ".png"))).getImage();
        pacman2up = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_up1.png"))).getImage();
        pacman3up = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_up2.png"))).getImage();
        pacman4up = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_up3.png"))).getImage();
        pacman2down = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_down1.png"))).getImage();
        pacman3down = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_down2.png"))).getImage();
        pacman4down = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_down3.png"))).getImage();
        pacman2left = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_left1.png"))).getImage();
        pacman3left = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_left2.png"))).getImage();
        pacman4left = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_left3.png"))).getImage();
        pacman2right = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_right1.png"))).getImage();
        pacman3right = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_right2.png"))).getImage();
        pacman4right = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Pacman/images/" + name + "_right3.png"))).getImage();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    /**
     * 게임 보드를 그리는 메서드.
     *
     * @param g Graphics 객체
     */
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);
        doAnim();

        if (inGame) {
            playGame(g2d);
        } else {
            showOutroScreen(g2d);
        }

        g2d.drawImage(ii, 5, 5, this);
        Toolkit.getDefaultToolkit()
                .sync();
        g2d.dispose();
    }

    /**
     * ActionListener를 구현한 메서드. 주기적으로 게임 상태를 갱신하여 화면을 다시 그린다.
     *
     * @param e ActionEvent 객체
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        repaint();
    }

    /**
     * 노드의 휴리스틱 값을 계산하는 메서드.
     *
     * @param current 현재 노드
     * @param end     목표 노드
     * @return 휴리스틱 값
     */
    private double calculateHeuristic(Node current, Node end) {
        return Math.abs(current.x - end.x) + Math.abs(current.y - end.y);
    }

    /**
     * 주어진 노드의 이웃 노드 목록을 반환하는 메서드.
     *
     * @param node 기준 노드
     * @return 이웃 노드 목록
     */
    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        for (int i = 0; i < dx.length; i++) {
            int newX = node.x + dx[i];
            int newY = node.y + dy[i];
            if (isValidPosition(newX, newY)) {
                neighbors.add(new Node(newX, newY));
            }
        }
        return neighbors;
    }

    /**
     * 주어진 좌표가 유효한지 확인하는 메서드.
     *
     * @param x x 좌표
     * @param y y 좌표
     * @return 유효한 좌표인지 여부
     */
    private boolean isValidPosition(int x, int y) {
        if (x < 0 || x >= N_BLOCKS || y < 0 || y >= N_BLOCKS) return false;
        int pos = y * N_BLOCKS + x;
        return (screenData[pos] & 16) == 0; // 벽이 없는 위치인지 확인
    }

    /**
     * 키 입력을 처리하는 KeyAdapter 클래스.
     */
    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                } else if (key == KeyEvent.VK_PAUSE) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                }
            } else {
                if (key == 's' || key == 'S') {
                    inGame = true;
                    initGame();
                } else if (key == 'e' || key == 'E') {
                    character.setFun(character.getFun() + 10);
                    character.setMoney(character.getMoney() + (score * 10));
                    character.setHungry(character.getHungry() - 10);
                    if (character.getMax_score_4() < score) {
                        character.setMax_score_4(score);
                    }
                    character.setXp(character.getXp() + 10);
                    characterService.saveCharacter(character);
                    stopBackgroundMusic();
                    frame.dispose();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == Event.LEFT || key == Event.RIGHT
                    || key == Event.UP || key == Event.DOWN) {
                req_dx = 0;
                req_dy = 0;
            }
        }
    }

}

/**
 * 경로 찾기 알고리즘에 사용되는 그리드 상의 지점을 나타내는 노드 클래스
 */
class Node {
    int x, y;
    Node parent;
    double g, h, f;

    /**
     * 좌표를 초기화하고 부모 노드를 null로 설정하는 생성자
     *
     * @param x X 좌표
     * @param y Y 좌표
     */
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.parent = null;
    }

    /**
     * 두 노드가 같은지 비교하는 메서드
     *
     * @param obj 비교 대상 객체
     * @return 두 노드가 같으면 true, 그렇지 않으면 false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Node))
            return false;
        Node node = (Node) obj;
        return x == node.x && y == node.y;
    }
}