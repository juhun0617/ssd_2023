package org.example;

import org.example.draw.BackGroundPanel;
import org.example.etc.BackgroundMusic;
import org.example.etc.CustomFont;
import org.example.etc.ShadowButton;
import org.example.service.CharacterService;
import org.example.ui.CharacterSelectionUI;
import org.example.ui.DamaUI;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * JFrame을 상속받는 Main클래스
 *
 * @author juhun_park
 */

public class Main extends JFrame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final String BACKGROUND_PATH = "/Image/Main.jpeg";
    private BackGroundPanel backGroundPanel;
    private CharacterService characterService;
    private Font customFont;
    private JPanel mainPanel;

    private BackgroundMusic bgMusic;

    /**
     * Main 클래스의 생성자
     * 커스텀 폰트를 로드하고 프레임을 초기화,
     * 리스너를 추가하고 여러 요소를 초기화, 설정한다
     * 데이터베이스를 복사하는 코드도 포함한다.
     */
    public Main() {
        customFont = CustomFont.loadCustomFont(24f);
        initializeFrame();
        initializeMainPanel();
        attachMouseClickListener();
        startBackgroundMusic();
        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db")
                .toString();
        System.out.println(targetPath);
        extractDatabase("/SQLiteDB.db", targetPath);


        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        EntityManager em = emf.createEntityManager();

        characterService = new CharacterService(emf);

    }

    /**
     * jar파일(프로젝트 폴더) 내부에 있는 데이터베이스를 유저의 홈 디렉토리에 복사하는 메서드
     *
     * @param resourcePath : 내부 데이터베이스 경로
     * @param targetPath   : 복사할 위치 지정
     */
    public static void extractDatabase(String resourcePath, String targetPath) {
        File targetFile = new File(targetPath);


        // 대상 파일이 이미 존재하지 않는 경우에만 추출
        if (!targetFile.exists()) {
            System.out.println(resourcePath);
            try (InputStream is = Main.class.getResourceAsStream(resourcePath);
                 OutputStream os = new FileOutputStream(targetFile)) {

                // 리소스 스트림에서 데이터를 읽어서 파일에 쓴다
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                e.printStackTrace(); // 오류 처리
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }

    /**
     * 뒤로가기 버튼을 눌렀을 때 메인패널의 전부를 지우고 다시 업데이트 한다
     */
    private void backButtonProgress() {
        mainPanel.removeAll();
        initializeMainPanel();
        attachMouseClickListener();
        mainPanel.updateUI();

    }

    /**
     * 배경음악을 시작하는 메서드
     */
    private void startBackgroundMusic() {
        // Assuming BackgroundMusic is a runnable that plays music
        bgMusic = new BackgroundMusic();
        bgMusic.startMusic("/Music.wav");
        new Thread(String.valueOf(bgMusic)).start();
    }

    /**
     * 배경음악을 종료하는 메서드
     */
    private void stopBackgroundMusic() {
        bgMusic.stopMusic();
    }

    /**
     * 메인 프레임을 초기화 하는 메서드
     */
    private void initializeFrame() {
        setTitle("RETRO PET");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * 메인 패널을 초기화하는 메서드
     */
    private void initializeMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        backGroundPanel = new BackGroundPanel(BACKGROUND_PATH);
        mainPanel.add(backGroundPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    /**
     * 메인화면에서의 클릭을 감지하는 메서드
     */
    private void attachMouseClickListener() {
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fadeOutBackground();
                mainPanel.removeMouseListener(this);
            }
        });
    }

    /**
     * 클릭을 하였을 때 화면을 흐리게 전환해주는 메서드
     */
    private void fadeOutBackground() {
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backGroundPanel.transparency -= 0.1f;
                if (backGroundPanel.transparency <= 0.2f) {
                    backGroundPanel.transparency = 0.2f;
                    ((Timer) e.getSource()).stop();
                    displayGameOptions();
                }
                backGroundPanel.repaint();
            }
        });
        timer.start();
    }

    /**
     * 새 게임, 저장된 게임 옵션을 디스플레이 해주는 메서드
     */
    private void displayGameOptions() {
        JPanel optionsPanel = createOptionsPanel();
        backGroundPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * 게임 옵션 패널을 제작하는 메서드
     *
     * @return : 제작된 게임 옵션 패널
     */
    private JPanel createOptionsPanel() {
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 365));
        optionsPanel.setOpaque(false);


        ShadowButton newGameButton = new ShadowButton("새 게임", "/Image/Button/skyBlueButton.png");
        newGameButton.setCORNER_SIZE(40);
        newGameButton.setSHADOW_OFFSET(0);
        newGameButton.addActionListener(e -> startNewGame());
        newGameButton.setFont(customFont);
        newGameButton.setPreferredSize(new Dimension(200, 70));
        ShadowButton loadGameButton = new ShadowButton("저장된 게임", "/Image/Button/skyBlueButton.png");
        loadGameButton.setCORNER_SIZE(40);
        loadGameButton.setSHADOW_OFFSET(0);
        loadGameButton.addActionListener(e -> loadSavedGame());
        loadGameButton.setFont(customFont);
        loadGameButton.setPreferredSize(new Dimension(200, 70));

        optionsPanel.add(newGameButton);
        optionsPanel.add(loadGameButton);

        return optionsPanel;
    }

    /**
     * 다마고치 화면으로 전환을 해주는 메서드
     * 현재 패널을 인자로 넘겨주어 패널을 재샤용
     *
     * @param name : 사용자가 선택한 캐릭터의 이름
     */
    private void moveToDamaUI(String name) {
        SwingUtilities.invokeLater(() -> {
            stopBackgroundMusic();
            DamaUI damaUI = new DamaUI(mainPanel, name);
            damaUI.updateUi();
        });
    }

    /**
     * 새 게임을 시작하는 메서드
     * 게임옵션에서 새 게임을 선택하였을 때 호출되는 메서드
     */
    private void startNewGame() {
        CharacterSelectionUI characterSelectionUI = new CharacterSelectionUI(mainPanel, new CharacterSelectionUI.CharacterCreationCallback() {
            @Override
            public void onCharacterCreated(String name) {
                moveToDamaUI(name);
            }

            @Override
            public void backBotton() {
                backButtonProgress();
            }


        });
        characterSelectionUI.updateUI();
    }

    /**
     * 저장된 게임을 시작하는 메서드
     * 게임옵션에서 저장된 게임을 선택하였을 때 호출되는 메서드
     */
    private void loadSavedGame() {
        if (characterService.isTableEmpty("Character")) {
            JOptionPane.showMessageDialog(this, "세이브파일이 없습니다");
        } else {
            selectSaveFile();

        }
    }

    /**
     * 저장된 게임을 눌렀을 때 세이브 파일을 선택하는 메서
     */
    private void selectSaveFile() {
        // 데이터베이스 또는 서비스 계층으로부터 캐릭터 이름 목록을 가져옵니다.
        List<String> characterNames = characterService.getCharacterNames();

        // JOptionPane으로 사용자에게 선택을 제시하고 선택 결과를 가져옵니다.
        String selectedName = (String) JOptionPane.showInputDialog(
                null,
                "캐릭터를 선택해주세요:",
                "캐릭터 선택",
                JOptionPane.PLAIN_MESSAGE,
                null,
                characterNames.toArray(),
                characterNames.get(0)); // 기본 선택

        // 선택한 이름이 null이 아닐 때만 다음 메서드를 호출합니다.
        if (selectedName != null && !selectedName.isEmpty()) {
            moveToDamaUI(selectedName);
        }
    }
}
