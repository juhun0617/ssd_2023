package org.example.game.Mulgoging;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 게임 화면을 관리하고 게임 로직을 처리하는 클래스입니다.
 * 화살 발사, 타이머 관리, 물고기 움직임 등의 기능을 담당합니다.
 */
public class GamePanel extends JPanel {

    private static final double MAX_SPEED = 350; // 예시 값
    private static final double MIN_SPEED = 210; // 예시 값
    private final double arrowAngle; // 화살의 방향
    private JFrame superFrame;
    private CardLayout cl;
    private Mulgoging mainClass;
    private EndPanel endPanel;
    private SoundManager hitSoundManager;
    //타이머
    private int remainingTime = 30; // 타이머 시간 (초 단위)
    private Timer gameTimer;
    //화살
    private Arrow arrow; // 화살 객체
    private int arrowPower; // 화살의 힘
    private int arrowPowerProgress; // 화살 힘 조절 바의 진행 상태
    private int arrowDirectionProgress; // 화살 방향 조절 바의 진행 상태
    private boolean settingArrowPower; // 현재 화살 힘을 설정 중인지 여부
    private boolean settingArrowDirection; // 현재 화살 방향을 설정 중인지 여부
    private boolean sliderMovingRight = true; // 슬라이더가 오른쪽으로 이동 중인지 여부
    private boolean directionSliderMovingRight = true;
    private boolean arrowAngleIncreasing = true;
    private boolean angleSet = false; // 각도가 설정되었는지 나타내는 플래그
    //게임
    private int score; // 점수
    private boolean gameRunning; // 게임 진행 상태
    private Clip clip1;
    //이미지
    private Image backgroundImage;
    private Image bunnyImage;
    private Image powerBarImage; // 힘 조절 바 이미지
    //타이머
    private Image directionImage;
    private Image sliderImage; // 슬라이더 이미지
    private Timer sliderTimer;
    private Timer directionTimer;
    private Timer arrowAngleTimer;
    private Character_Mulgoging currentCharacter;
    private List<Fish> fishes; // 물고기 리스트

    /**
     * GamePanel 생성자. 게임 화면 및 게임 로직을 초기화합니다.
     *
     * @param superFrame    상위 JFrame
     * @param cl            카드 레이아웃
     * @param mainClass     메인 클래스 인스턴스
     * @param characterName 캐릭터 이름
     */
    public GamePanel(JFrame superFrame, CardLayout cl, Mulgoging mainClass, String characterName) {
        playBackgroundMusic("/Mulgoging/Sound/Mulgoging.wav");
        this.superFrame = superFrame;
        this.cl = cl;
        this.mainClass = mainClass;
        this.hitSoundManager = new SoundManager("/Sound/sound.wav");
        arrowAngle = 0;
        new Timer(40, e -> updateGame()).start();

        arrow = new Arrow(210, 500, "/Mulgoging/Image/arrow_1.png");

        sliderTimer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSliderPosition();
            }
        });
        directionTimer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDirectionSliderPosition();
            }
        });
        arrowAngleTimer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateArrowAngle();
            }
        });
        fishes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fishes.add(new Fish(Math.random() * getWidth(), Math.random() * getHeight(), 1, "/Mulgoging/Image/fish.png", getWidth(), getHeight()));
        }

        setFocusable(true);
        requestFocus();
        initListeners();
        gameReset();
        loadImages();
        requestFocusInWindow();
        setCurrentCharacter(characterName);

    }

    private String formatTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

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
     * 게임에 필요한 이미지를 로드하는 메서드입니다.
     * 배경 이미지, 힘 조절 바 이미지, 방향 조절 바 이미지, 슬라이더 이미지를 로드합니다.
     */
    private void loadImages() {
        ImageIcon backgroundIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Mulgoging/Image/물고깅배경.jpg")));
        backgroundImage = backgroundIcon.getImage();


        ImageIcon powerBarIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Mulgoging/Image/powerbar.png"))); // 힘 조절 바 이미지 경로
        powerBarImage = powerBarIcon.getImage();

        ImageIcon directionIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Mulgoging/Image/directionbar.png"))); // 힘 조절 바 이미지 경로
        directionImage = directionIcon.getImage();

        ImageIcon sliderIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Mulgoging/Image/barstick.png"))); // 힘 조절 바 이미지 경로
        sliderImage = sliderIcon.getImage();


    }

    /**
     * 현재 캐릭터를 설정하는 메서드입니다.
     * 게임에서 사용할 캐릭터의 이름을 기반으로 캐릭터 객체를 생성합니다.
     *
     * @param characterName 게임에서 사용할 캐릭터의 이름
     */
    public void setCurrentCharacter(String characterName) {
        this.currentCharacter = new Character_Mulgoging(characterName);
    }

    /**
     * 게임을 리셋하는 메서드입니다
     */
    private void gameReset() {
        arrowPower = 0;
        arrow.setAngle(0);
        score = 0;
        gameRunning = false;
        // 게임 점수 및 기타 변수 초기화

        remainingTime = 30; // 예를 들어, 타이머를 10초로 다시 설정

        // 기존 게임 타이머가 실행 중이라면 중지
        if (gameTimer != null) {
            gameTimer.stop();
        }

        // 게임 타이머 다시 설정
        gameTimer = new Timer(1000, e -> {
            remainingTime--;
            if (remainingTime <= 0) {
                gameOver();
            }
            repaint();
        });
    }


    /**
     * 게임을 준비하는 메서드입니다. 게임 상태를 초기화하고, 화살 힘 설정을 시작합니다.
     */
    public void gameReady() {
        gameReset();
        requestFocusInWindow();

        settingArrowPower = true; // 화살 힘 설정 시작
        sliderTimer.start();

        settingArrowDirection = false;
        directionTimer.start(); // 방향 조절 타이머 시작
        arrowAngleTimer.start();
    }

    /**
     * 게임을 시작하는 메서드입니다. 게임 상태를 '진행 중'으로 변경하고, 물고기 객체들을 초기화합니다.
     */
    public void gameStart() {


        gameRunning = true; // 게임 상태를 '진행 중'으로 설정
        initializeFishes(); // 물고기 객체 초기화
        startGameTimer();


    }

    /**
     * 게임을 재시작하는 메서드입니다. 게임 상태와 화살 상태를 초기화하고, 게임을 다시 준비합니다.
     */
    public void restartGame() {
        gameReset(); // 게임 상태 초기화
        resetArrow(); // 화살 상태 초기화
        initializeFishes();// 물고기 위치 초기화
        //   initListeners();
        gameReady(); // 게임 준비 상태로 설정
        gameStart(); // 게임 시작


    }

    /**
     * 물고기 객체들을 초기화하는 메서드입니다.
     */
    private void initializeFishes() {
        fishes.clear();
        for (int i = 0; i < 5; i++) {
            double x = Math.random() * getWidth();
            double y = Math.random() * getHeight();
            String imagePath = "/Mulgoging/Image/fish.png"; // 올바른 이미지 경로
            fishes.add(new Fish(x, y, 1, imagePath, getWidth(), getHeight()));
        }
    }

    /**
     * 화살 상태를 초기화하는 메서드입니다.
     */
    private void resetArrow() {
        arrow = new Arrow(210, 500, "/Mulgoging/Image/arrow_1.png");
        // 화살 관련 기타 설정
    }


    /**
     * 게임을 종료하는 메서드입니다. 게임 상태를 종료로 변경하고, 최종 점수를 처리합니다.
     */
    private void gameOver() {
        gameRunning = false;
        mainClass.endGame(score);
        stopBackgroundMusic();
    }


    /**
     * 이벤트 리스너를 초기화하는 메서드입니다.
     */
    private void initListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameRunning && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    handleSpacebarPress();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameRunning) {
                    handleMousePress(e);
                }
            }
        });
    }


    /**
     * 스페이스바를 누를 때의 처리를 하는 메서드입니다.
     */
    public void handleSpacebarPress() {
        if (settingArrowPower) {
            sliderTimer.stop();
            double speed = calculateArrowPower(arrowPowerProgress);
            arrow.setSpeed(speed); // 화살의 속도 설정
            settingArrowPower = false;
            settingArrowDirection = true;
            directionTimer.start(); // 방향 조절 슬라이더 시작
        } else if (settingArrowDirection) {
            directionTimer.stop();
            double angle = calculateArrowDirection(arrowDirectionProgress); // 각도 계산
            arrow.setAngle(Math.toRadians(angle)); // 라디안으로 변환하여 설정
            settingArrowDirection = false;
            arrow.launch(); // 화살 발사
        }
    }

    /**
     * 화살 힘 조절 슬라이더의 위치를 업데이트하는 메서드입니다.
     */
    private void updateSliderPosition() {
        if (settingArrowPower) {
            if (sliderMovingRight) {
                arrowPowerProgress++;
                if (arrowPowerProgress >= 100) {
                    arrowPowerProgress = 100;
                    sliderMovingRight = false;
                }
            } else {
                arrowPowerProgress--;
                if (arrowPowerProgress <= 0) {
                    arrowPowerProgress = 0;
                    sliderMovingRight = true;
                }
            }
            repaint();
        }
    }


    /**
     * 화살 방향 조절 슬라이더의 위치를 업데이트하는 메서드입니다.
     */

    private void updateDirectionSliderPosition() {
        if (settingArrowDirection) {
            if (directionSliderMovingRight) {
                arrowDirectionProgress++;
                if (arrowDirectionProgress >= 100) {
                    directionSliderMovingRight = false;
                }
            } else {
                arrowDirectionProgress--;
                if (arrowDirectionProgress <= 0) {
                    directionSliderMovingRight = true;
                }
            }


            updateArrowAngle();
            repaint();
        }
    }

    /**
     * 화살의 각도를 업데이트하는 메서드입니다.
     */
    private void updateArrowAngle() {
        if (!settingArrowDirection) return;

        final double minAngle = 0; // 슬라이더가 최소일 때의 각도
        final double maxAngle = -80; // 슬라이더가 최대일 때의 각도

        double angle = minAngle + (double) arrowDirectionProgress / 100 * (maxAngle - minAngle);

        if (arrow.isMoving()) {
            angle = arrow.getAngle();
        }

        arrow.setAngle(Math.toRadians(angle));
    }

    /**
     * 마우스 클릭 이벤트를 처리하는 메서드입니다.
     *
     * @param e 마우스 이벤트 객체
     */
    private void handleMousePress(MouseEvent e) {
        // 게임 진행 중일 때의 마우스 클릭 처리 로직
    }

    /**
     * 게임의 상태를 업데이트하는 메서드입니다. 화살의 위치를 업데이트하고, 화면을 다시 그립니다.
     */
    private void updateGame() {
        if (!gameRunning) return;
        // 화살 위치를 업데이트합니다.
        arrow.updatePosition(0.02);

        // 화살이 화면 밖으로 나갔는지 확인합니다.
        if (arrow.getX() < 0 || arrow.getX() > getWidth() || arrow.getY() < 0 || arrow.getY() > getHeight()) {
            // 화살이 화면 밖으로 나갔을 때의 처리를 여기에 추가합니다.
            resetArrowLaunching(); // 화살을 리셋합니다.
        }

        checkArrowFishCollision();
        for (Fish fish : fishes) {
            fish.move(getWidth(), getHeight());
        }
        repaint();
    }

    /**
     * 게임 화면을 그리는 메서드입니다.
     * 배경, 화살, 물고기, 점수 등 게임의 시각적 요소를 그립니다.
     *
     * @param g 그래픽스 컨텍스트
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 배경 이미지 그리기
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        // 캐릭터 이미지 그리기
        if (currentCharacter != null) {
            Image characterImage = currentCharacter.getImage();
            // 캐릭터 이미지 그리는 로직...
            g2d.drawImage(characterImage, 215, 400, this);
        }

        drawGameElements(g2d);

        if (settingArrowPower) {
            drawPowerBar(g2d);
        }
        if (settingArrowDirection) {
            drawDirectionBar(g2d);
        }
        if (arrow != null) {
            arrow.draw(g2d);
        }
        for (Fish fish : fishes) {
            fish.draw(g2d);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 21));
        g.drawString(formatTime(remainingTime), 147, 164); // 타이머위치

    }


    /**
     * 화살 힘 조절 바를 화면에 그리는 메서드입니다.
     * 힘 조절 바와 슬라이더의 위치를 그래픽스 컨텍스트에 그립니다.
     *
     * @param g2d 그래픽스 2D 컨텍스트
     */
    private void drawPowerBar(Graphics2D g2d) {
        int barWidth = 250;  // 전체 바의 너비
        int barHeight = 67;  // 바의 높이
        int sliderWidth = 15;  // 슬라이더의 너비
        int sliderHeight = 26;  // 슬라이더의 높이


        int sliderX = 190 + (int) (arrowPowerProgress / 100.0 * (355 - 190));

        // 힘 조절 바 그리기
        g2d.drawImage(powerBarImage, 160, 260, barWidth, barHeight, this);

        // 슬라이더 그리기 (y좌표는 282로 고정)
        g2d.drawImage(sliderImage, sliderX, 282, sliderWidth, sliderHeight, this);
    }


    /**
     * 화살 방향 조절 바를 화면에 그리는 메서드입니다.
     * 방향 조절 바와 슬라이더의 위치를 그래픽스 컨텍스트에 그립니다.
     *
     * @param g2d 그래픽스 2D 컨텍스트
     */
    private void drawDirectionBar(Graphics2D g2d) {
        int barWidth = 250;  // 전체 바의 너비
        int barHeight = 67;  // 바의 높이
        int sliderWidth = 15;  // 슬라이더의 너비
        int sliderHeight = 26;

        int DsliderX = 205 + (int) (arrowDirectionProgress / 100.0 * (355 - 190));

        g2d.drawImage(directionImage, 160, 260, barWidth, barHeight, this);
        g2d.drawImage(sliderImage, DsliderX, 281, sliderWidth, sliderHeight, this);
    }

    /**
     * 화살과 물고기의 충돌을 확인하는 메서드입니다.
     */
    private void checkArrowFishCollision() {
        List<Fish> toRemove = new ArrayList<>();
        for (Fish fish : fishes) {
            if (arrow.isMoving() && fishCollidesWithArrow(fish, arrow)) {
                score++; // 점수 증가

                toRemove.add(fish); // 제거할 물고기 추가
                arrow.stop(); // 화살 멈춤
                hitSoundManager.play();
                resetArrowLaunching();
                hitSoundManager.stop();

                Timer newFishTimer = new Timer(3000, e -> addNewFish()); // 3초 지연, 필요에 따라 시간을 조정하세요
                newFishTimer.setRepeats(false);
                newFishTimer.start();
            }
        }
        fishes.removeAll(toRemove); // 충돌한 물고기 제거
    }


    /**
     * 화살 발사를 리셋하는 메서드입니다.
     */
    private void resetArrowLaunching() {
        settingArrowPower = true;
        arrowPowerProgress = 0;
        arrowDirectionProgress = 0;
        sliderMovingRight = true;
        directionSliderMovingRight = true;
        arrow.stop();
        arrow.setAngle(0);
        arrow.resetPosition(210, 500); // 화살의 초기 위치로 리셋 (값은 게임에 따라 조정)
        sliderTimer.start();
    }

    /**
     * 물고기와 화살의 충돌을 감지하는 메서드입니다.
     *
     * @param fish  물고기 객체
     * @param arrow 화살 객체
     * @return 충돌 여부
     */
    private boolean fishCollidesWithArrow(Fish fish, Arrow arrow) {
        double fishCenterX = fish.getX() + fish.getImage()
                .getWidth(null) / 2.0;
        double fishCenterY = fish.getY() + fish.getImage()
                .getHeight(null) / 2.0;

        double arrowCenterX = arrow.getX() + arrow.getImage()
                .getWidth(null) / 2.0;
        double arrowCenterY = arrow.getY() + arrow.getImage()
                .getHeight(null) / 2.0;

        double distance = Math.sqrt(Math.pow(fishCenterX - arrowCenterX, 2) + Math.pow(fishCenterY - arrowCenterY, 2));
        double collisionDistance = 30; // 충돌 간주 거리, 필요에 따라 조정

        return distance < collisionDistance;
    }

    /**
     * 새로운 물고기를 추가하는 메서드입니다.
     */
    private void addNewFish() {
        double x = Math.random() * getWidth();
        double y = Math.random() * getHeight();
        String imagePath = "/Mulgoging/Image/fish.png"; // 올바른 이미지 경로
        fishes.add(new Fish(x, y, 1, imagePath, getWidth(), getHeight()));
    }


    /**
     * 게임 요소들을 그리는 메서드입니다.
     *
     * @param g2d 그래픽스 2D 컨텍스트
     */
    private void drawGameElements(Graphics2D g2d) { //보류
        // 게임 관련 그래픽 요소 그리기
        // 점수 표시
        g2d.setColor(Color.white);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString(" " + score, 160, 78);

        // 추가적인 게임 요소 그리기 (예: 화살, 물고기 등)
    }

    /**
     * 게임 타이머를 시작하는 메서드입니다.
     * 일정 간격으로 남은 시간을 감소시키고, 시간이 0이 되면 게임을 종료합니다.
     */
    private void startGameTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(1000, e -> {
            remainingTime--;
            if (remainingTime <= 0) {
                gameOver();
            }
            repaint();
        });
        gameTimer.start();
    }


    /**
     * 슬라이더 위치에 따라 화살의 힘을 계산하는 메서드입니다.
     *
     * @param sliderPosition 슬라이더의 현재 위치
     * @return 계산된 화살의 힘
     */
    private double calculateArrowPower(int sliderPosition) {
        // 슬라이더 위치에 따른 속도 계산
        double speedRange = Math.abs(355 - 190);
        double speed = (sliderPosition - 190) / speedRange * (MAX_SPEED - MIN_SPEED) + MIN_SPEED;
        return speed;
    }


    /**
     * 화살의 방향 각도를 계산하는 메서드입니다.
     * 슬라이더의 진행 상태에 따라 화살의 발사 각도를 계산합니다.
     *
     * @param progress 방향 조절 슬라이더의 현재 진행 상태
     * @return 계산된 화살의 방향 각도
     */
    private double calculateArrowDirection(int progress) {

        final double minAngle = -80; // 최소 각도
        final double maxAngle = 0; // 최대 각도
        double angle = minAngle + ((double) progress / 100.0) * (maxAngle - minAngle);

        return angle;


    }


}