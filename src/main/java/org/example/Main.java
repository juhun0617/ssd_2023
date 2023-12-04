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


public class Main extends JFrame {

    private BackGroundPanel backGroundPanel;
    private CharacterService characterService;
    private Font customFont;


    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final String BACKGROUND_PATH = "/Image/Main.jpeg";


    private JPanel mainPanel;

    private BackgroundMusic bgMusic;

    public Main() {
        customFont = CustomFont.loadCustomFont(24f);
        initializeFrame();
        initializeMainPanel();
        attachMouseClickListener();
        startBackgroundMusic();
        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db").toString();
        System.out.println(targetPath);
        extractDatabase("/SQLiteDB.db",targetPath);


        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        EntityManager em = emf.createEntityManager();

        characterService = new CharacterService(emf);

    }

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


    private void backButtonProgress(){
        mainPanel.removeAll();
        initializeMainPanel();
        attachMouseClickListener();
        mainPanel.updateUI();

    }

    private void startBackgroundMusic() {
        // Assuming BackgroundMusic is a runnable that plays music
        bgMusic = new BackgroundMusic();
        bgMusic.startMusic("/Music.wav");
        new Thread(String.valueOf(bgMusic)).start();
    }

    private void stopBackgroundMusic(){
        bgMusic.stopMusic();
    }




    private void initializeFrame() {
        setTitle("RETRO PET");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initializeMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        backGroundPanel = new BackGroundPanel(BACKGROUND_PATH);
        mainPanel.add(backGroundPanel,BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void attachMouseClickListener() {
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fadeOutBackground();
                mainPanel.removeMouseListener(this);
            }
        });
    }

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

    private void displayGameOptions() {
        JPanel optionsPanel = createOptionsPanel();
        backGroundPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createOptionsPanel() {
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 365));
        optionsPanel.setOpaque(false);


        ShadowButton newGameButton = new ShadowButton("새 게임","/Image/Button/skyBlueButton.png");
        newGameButton.setCORNER_SIZE(40);
        newGameButton.setSHADOW_OFFSET(0);
        newGameButton.addActionListener(e -> startNewGame());
        newGameButton.setFont(customFont);
        newGameButton.setPreferredSize(new Dimension(200,70));
        ShadowButton loadGameButton = new ShadowButton("저장된 게임", "/Image/Button/skyBlueButton.png");
        loadGameButton.setCORNER_SIZE(40);
        loadGameButton.setSHADOW_OFFSET(0);
        loadGameButton.addActionListener(e -> loadSavedGame());
        loadGameButton.setFont(customFont);
        loadGameButton.setPreferredSize(new Dimension(200,70));

        optionsPanel.add(newGameButton);
        optionsPanel.add(loadGameButton);

        return optionsPanel;
    }



    private void moveToDamaUI(String name) {
        SwingUtilities.invokeLater(() -> {
            stopBackgroundMusic();
            DamaUI damaUI = new DamaUI(mainPanel, name);
            damaUI.updateUi();
        });
    }



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

    private void loadSavedGame() {
        if(characterService.isTableEmpty("Character")){
            JOptionPane.showMessageDialog(this,"세이브파일이 없습니다");
        } else {
            selectSaveFile();

        }
    }

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





    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
