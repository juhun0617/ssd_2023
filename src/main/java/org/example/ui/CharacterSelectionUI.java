package org.example.ui;

import org.example.draw.BackGroundPanel;
import org.example.etc.CustomFont;
import org.example.etc.ShadowButton;
import org.example.service.CharacterService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author juhun_park
 * 캐릭터 선택 화면을 구현하는 클래스.
 * 이 클래스는 사용자가 다양한 캐릭터 중 하나를 선택할 수 있는 인터페이스를 제공합니다.
 */
public class CharacterSelectionUI {

    private static final String CHARACTER_CAT = "/Image/character/cat.png";
    private static final String CHARACTER_GOAT = "/Image/character/goat.png";
    private static final String CHARACTER_RABBIT = "/Image/character/rabbit.png";
    private static final String CHARACTER_DUCK = "/Image/character/duck.png";
    private static final String CHECK_IMAGE = "/Image/check.png";
    private static final String BACKGROUND_PATH = "/Image/CharacterSelectionBack.jpeg";
    private final JPanel panel;
    private final CharacterCreationCallback callback;
    //data base
    EntityManagerFactory emf;
    EntityManager entityManager;
    private BackGroundPanel backGroundPanel;
    private Font customFont;
    private JLabel checkLabelCat;
    private JLabel checkLabelGoat;
    private JLabel checkLabelRabbit;
    private JLabel checkLabelDuck;
    private JLabel disciptionText;
    private JButton selectButton;
    private JButton backButton;
    private String whatCharacter;


    /**
     * CharacterSelectionUI의 생성자.
     * SQLite 데이터베이스와의 연결을 설정하고, UI 구성을 위한 초기 설정을 합니다.
     *
     * @param panel    이 UI 컴포넌트가 추가될 부모 패널
     * @param callback 사용자가 캐릭터를 선택하거나 뒤로 가기 버튼을 눌렀을 때 호출될 콜백 함수
     */
    public CharacterSelectionUI(JPanel panel, CharacterCreationCallback callback) {

        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db")
                .toString();
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        entityManager = emf.createEntityManager();
        this.panel = panel;
        this.callback = callback;
    }

    /**
     * UI 컴포넌트를 초기화하고 캐릭터 선택 화면을 구성합니다.
     */
    public void updateUI() {

        initializeBackPanel();
        addCharacterSelectionOptions();
        addText();
        selectCharacter();
        backProgress();

        // 패널에 변경 사항을 적용합니다.
        panel.revalidate();
        panel.repaint();
    }

    /**
     * 캐릭터 선택 화면의 배경 패널을 초기화합니다.
     * 배경 이미지를 설정하고 패널의 레이아웃을 GridBagLayout으로 설정합니다.
     */
    private void initializeBackPanel() {
        panel.removeAll();

        backGroundPanel = new BackGroundPanel(BACKGROUND_PATH);
        backGroundPanel.setLayout(new GridBagLayout()); // GridBagLayout으로 설정합니다.
        panel.add(backGroundPanel, BorderLayout.CENTER);
    }

    /**
     * 캐릭터 선택 화면에 설명 텍스트를 추가합니다.
     */
    private void addText() {
        customFont = CustomFont.loadCustomFont(25f);

        JLabel label = new JLabel("<html><body>가장 친구가 되고 싶은 동물을 선택해봐!<br>각각의 동물을 누르면 특성을 확인할 수 있어</body></html>");
        label.setFont(customFont);
        label.setHorizontalAlignment(JLabel.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1; // 이 값에 따라 레이블의 수직 위치가 결정됩니다.
        gbc.insets = new Insets(280, 0, 0, 0); // 레이블의 위치를 조정합니다.

        backGroundPanel.add(label, gbc); // GridBagConstraints를 사용하여 레이블 추가
    }

    /**
     * 선택된 캐릭터에 대한 설명을 화면에 표시합니다.
     *
     * @param character 선택된 캐릭터의 이름
     */
    private void addDescription(String character) {

        customFont = CustomFont.loadCustomFont(25f);
        String discription = "";
        if (Objects.equals(character, "cat")) {
            discription = "<html><body>고양이<br>청결함</body></html>";
        } else if (Objects.equals(character, "goat")) {
            discription = "<html><body>염소<br>배고픔을 잘참음</body></html>";
        } else if (Objects.equals(character, "rabbit")) {
            discription = "<html><body>토끼<br>심심하지 않음</body></html>";
        } else if (Objects.equals(character, "duck")) {
            discription = "<html><body>오리<br>잘 안아픔</body></html>";
        }

        if (disciptionText != null) {
            backGroundPanel.remove(disciptionText);
        }
        disciptionText = new JLabel(discription);
        disciptionText.setFont(customFont);
        disciptionText.setHorizontalAlignment(JLabel.LEFT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1; // 이 값에 따라 레이블의 수직 위치가 결정됩니다.
        gbc.insets = new Insets(550, 140, 0, 0); // 레이블의 위치를 조정합니다.

        backGroundPanel.add(disciptionText, gbc); // GridBagConstraints를 사용하여 레이블 추가

    }


    /**
     * 캐릭터 선택 화면에서 'Select' 버튼의 동작을 정의합니다.
     * 사용자가 선택한 캐릭터에 따라 새로운 캐릭터를 생성하고, 콜백 함수를 호출합니다.
     */
    private void selectCharacter() {
        selectButton = new ShadowButton("SELECT", "/Image/Button/selectButton1.png");
        customFont = CustomFont.loadCustomFont(30f);
        selectButton.setFont(customFont);

        selectButton.setForeground(Color.WHITE);
        // 버튼의 선호하는 크기를 설정합니다.
        selectButton.setPreferredSize(new Dimension(200, 50));
        selectButton.addActionListener(e -> makeCharacter());
        // 버튼 패널을 backGroundPanel의 세로 중앙에 배치하기 위한 GridBagConstraints 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 120, 130);

        backGroundPanel.add(selectButton, gbc);
    }

    /**
     * 캐릭터가 선택 되었을 때 체크 레이블 색을 변경합니다.
     */
    private void changeSelButtonColor() {
        ((ShadowButton) selectButton).setImagePath("/Image/Button/selectButton2.png");
    }

    /**
     * 캐릭터를 생성하는 메서드 입니다.
     */
    private void makeCharacter() {
        System.out.println(whatCharacter);
        if (whatCharacter == null) {
            JOptionPane.showMessageDialog(backGroundPanel, "캐릭터를 선택해주세요");
        } else {
            String name = JOptionPane.showInputDialog(backGroundPanel, "캐릭터의 이름을 입력해주세요:", "이름 정하기", JOptionPane.PLAIN_MESSAGE);
            if (name != null && !name.trim()
                    .isEmpty()) {
                System.out.println("입력받은 이름: " + name);
                CharacterService characterService = new CharacterService(emf);
                characterService.setCharacter(name, whatCharacter);
                callback.onCharacterCreated(name);

            } else {
                // 사용자가 입력을 취소하거나 비어 있는 이름을 입력했을 때의 처리를 작성합니다.
                System.out.println("이름 입력이 취소되었거나 비어 있습니다.");
            }
        }
    }

    /**
     * 캐릭터 선택 옵션을 화면에 추가합니다.
     * 각 캐릭터에 대한 버튼을 생성하고, 이벤트 리스너를 추가하여 사용자의 선택을 처리합니다.
     */
    private void addCharacterSelectionOptions() {
        // 버튼들 사이의 간격을 적당히 조절하고 중앙에 배치합니다.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0)); // 여기서 5와 5는 버튼 사이의 간격입니다.
        buttonPanel.setOpaque(false);

        // 각 캐릭터 버튼 생성
        JButton characterButtonCat = createCharacterButton(CHARACTER_CAT);
        JButton characterButtonGoat = createCharacterButton(CHARACTER_GOAT);
        JButton characterButtonRabbit = createCharacterButton(CHARACTER_RABBIT);
        JButton characterButtonDuck = createCharacterButton(CHARACTER_DUCK);

        characterButtonCat.addActionListener(e -> {
            selectCharacter("cat", checkLabelCat);
            changeSelButtonColor();
        });
        characterButtonGoat.addActionListener(e -> {
            selectCharacter("goat", checkLabelGoat);
            changeSelButtonColor();
        });
        characterButtonRabbit.addActionListener(e -> {
            selectCharacter("rabbit", checkLabelRabbit);
            changeSelButtonColor();
        });
        characterButtonDuck.addActionListener(e -> {
            selectCharacter("duck", checkLabelDuck);
            changeSelButtonColor();
        });

        // 체크 이미지 레이블 생성 및 초기 설정
        checkLabelCat = createCheckLabel();
        checkLabelGoat = createCheckLabel();
        checkLabelRabbit = createCheckLabel();
        checkLabelDuck = createCheckLabel();

        // 각 버튼과 체크 레이블을 담을 패널 설정
        JPanel characterCatPanel = createCharacterPanel(characterButtonCat, checkLabelCat);
        JPanel characterGoatPanel = createCharacterPanel(characterButtonGoat, checkLabelGoat);
        JPanel characterRabbitPanel = createCharacterPanel(characterButtonRabbit, checkLabelRabbit);
        JPanel characterDuckPanel = createCharacterPanel(characterButtonDuck, checkLabelDuck);


        // 버튼 패널에 각 캐릭터 패널을 추가
        buttonPanel.add(characterCatPanel);
        buttonPanel.add(characterGoatPanel);
        buttonPanel.add(characterRabbitPanel);
        buttonPanel.add(characterDuckPanel);

        // 버튼 패널을 backGroundPanel의 세로 중앙에 배치
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0; // 버튼 패널이 추가 공간을 수직으로 차지하지 않게 합니다.
        gbc.insets = new Insets(0, 0, 250, 0); // 상단에 300 픽셀의 여백을 줍니다.
        gbc.anchor = GridBagConstraints.SOUTH;

        backGroundPanel.add(buttonPanel, gbc);

        panel.revalidate();
        panel.repaint();
    }

    /**
     * 버튼과 체크 이미지 레이블을 포함하는 패널을 생성하는 메서드
     *
     * @param button     : 상위 메서드에서 버튼을 인자로 받습니다.
     * @param checkLabel : 상위 메서드에 라벨을 인자로 받습니다
     * @return : 생성된 패널을 반환합니다
     */
    private JPanel createCharacterPanel(JButton button, JLabel checkLabel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        button.setAlignmentX(Component.CENTER_ALIGNMENT); // 버튼을 X축(가로축)의 중앙에 정렬합니다.
        checkLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 체크 레이블을 X축(가로축)의 중앙에 정렬합니다.

        panel.add(button);
        panel.add(checkLabel);

        return panel;
    }

    /**
     * 체크 라벨을 만들어서 리턴합니다.
     *
     * @return : 만들어진 체크 레이블.
     */
    private JLabel createCheckLabel() {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(CHECK_IMAGE));
        Image image = originalIcon.getImage();
        Image resizedImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(resizedImage);
        JLabel checkLabel = new JLabel(icon);
        checkLabel.setVisible(false); // 초기에는 보이지 않게 설정
        return checkLabel;
    }

    /**
     * 선택된 캐릭터에 따라 UI를 업데이트합니다.
     * 선택된 캐릭터의 이름과 체크 마크를 표시하고, 설명을 업데이트합니다.
     *
     * @param characterName 선택된 캐릭터의 이름
     * @param checkLabel    선택된 캐릭터를 나타내는 체크 마크 레이블
     */
    private void selectCharacter(String characterName, JLabel checkLabel) {
        whatCharacter = characterName;
        // 다른 모든 체크 레이블을 숨깁니다.
        hideAllCheckLabels();
        // 선택한 캐릭터의 체크 레이블을 표시합니다.
        checkLabel.setVisible(true);
        System.out.println(characterName + " has been selected.");
        addDescription(characterName);
    }

    private void hideAllCheckLabels() {
        checkLabelCat.setVisible(false);
        checkLabelGoat.setVisible(false);
        checkLabelRabbit.setVisible(false);
        checkLabelDuck.setVisible(false);
    }

    /**
     * 캐릭터 선택 버튼을 생성합니다.
     *
     * @param imagePath 선택된 캐릭터의 이미지 경로
     * @return 생성된 JButton 객체
     */
    private JButton createCharacterButton(String imagePath) {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));
        Image image = originalIcon.getImage();
        Image resizedImage = image.getScaledInstance(130, 130, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(resizedImage);

        JButton button = new JButton();
        button.setIcon(icon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        return button;
    }

    /**
     * 캐릭터 선택 화면에서 'Back' 버튼의 동작을 정의합니다.
     * 콜백 함수를 호출하여 이전 화면으로 돌아갑니다.
     */
    private void backProgress() {
        backButton = new ShadowButton("back", "/Image/Button/backButton.png");
        backButton.setFont(customFont);
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(100, 50));
        backButton.addActionListener(e -> callback.backBotton());


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(60, 30, 0, 0);

        backGroundPanel.add(backButton, gbc);
    }

    /**
     * 캐릭터 생성에 대한 콜백 인터페이스.
     * 사용자가 캐릭터를 생성하거나 뒤로 가기 버튼을 눌렀을 때 호출됩니다.
     */
    public interface CharacterCreationCallback {
        void onCharacterCreated(String name);

        void backBotton();
    }
}
