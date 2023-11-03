package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.Objects;

public class Main extends JFrame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final String BACKGROUND_PATH = "/Main.jpeg";
    private static final String FONT_PATH = "/font/neodgm.ttf";

    private Image backgroundImage;
    private float transparency = 1.0f;
    private JPanel mainPanel;
    private Font customFont;

    public Main() {
        loadCustomFont();
        loadAndResizeBackgroundImage(BACKGROUND_PATH);
        initializeFrame();
        initializeMainPanel();
        attachMouseClickListener();
        startBackgroundMusic();
    }

    private void startBackgroundMusic() {
        // Assuming BackgroundMusic is a runnable that plays music
        // BackgroundMusic bgMusic = new BackgroundMusic("/Music.wav");
        // new Thread(bgMusic).start();
    }

    private void loadCustomFont() {
        try (InputStream is = getClass().getResourceAsStream(FONT_PATH)) {
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new JLabel().getFont();
        }
    }

    private void loadAndResizeBackgroundImage(String path) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
        backgroundImage = icon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    }

    private void initializeFrame() {
        setTitle("Main Game Screen");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initializeMainPanel() {
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
                g2d.drawImage(backgroundImage, 0, 0, this);
                g2d.dispose();
            }
        };
        mainPanel.setLayout(null);
        mainPanel.setOpaque(false);
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
                transparency -= 0.1f;
                if (transparency <= 0.2f) {
                    transparency = 0.2f;
                    ((Timer) e.getSource()).stop();
                    displayGameOptions();
                }
                mainPanel.repaint();
            }
        });
        timer.start();
    }

    private void displayGameOptions() {
        JPanel optionsPanel = createOptionsPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
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

    private void startNewGame() {
        System.out.println("Start a new game");
        // Implement new game logic here
    }

    private void loadSavedGame() {
        System.out.println("Load a saved game");
        // Implement load game logic here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
