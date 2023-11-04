package org.example;

import org.example.draw.BackGroundPanel;
import org.example.etc.BackgroundMusic;
import org.example.etc.CustomFont;
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
import java.util.List;

public class Main extends JFrame {

    private BackGroundPanel backGroundPanel;
    private CharacterService characterService;
    private Font customFont;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final String BACKGROUND_PATH = "/Image/Main.jpeg";
    private static final String FONT_PATH = "/font/neodgm.ttf";


    private JPanel mainPanel;

    public Main() {
        customFont = CustomFont.loadCustomFont(24f);
        backGroundPanel = new BackGroundPanel(BACKGROUND_PATH);
        initializeFrame();
        initializeMainPanel();
        attachMouseClickListener();
        //startBackgroundMusic();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManager em = emf.createEntityManager();

        characterService = new CharacterService(em);



    }

    private void startBackgroundMusic() {
        // Assuming BackgroundMusic is a runnable that plays music
        BackgroundMusic bgMusic = new BackgroundMusic();
        bgMusic.startMusic("/Music.wav");
        new Thread(String.valueOf(bgMusic)).start();
    }




    private void initializeFrame() {
        setTitle("Main Game Screen");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initializeMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
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
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 300));
        optionsPanel.setOpaque(false);


        JButton newGameButton = createButton("새 게임", e -> startNewGame());
        JButton loadGameButton = createButton("저장된 게임", e -> loadSavedGame());

        optionsPanel.add(newGameButton);
        optionsPanel.add(loadGameButton);

        return optionsPanel;
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(customFont);
        button.setPreferredSize(new Dimension(300, 70));
        button.addActionListener(actionListener);
        return button;
    }

    private void moveToDamaUI(String name) {
        DamaUI damaUI = new DamaUI(mainPanel,name);
        damaUI.updateUi();
    }



    private void startNewGame() {
        CharacterSelectionUI characterSelectionUI = new CharacterSelectionUI(mainPanel, new CharacterSelectionUI.CharacterCreationCallback() {
            @Override
            public void onCharacterCreated(String name) {
                moveToDamaUI(name);
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
