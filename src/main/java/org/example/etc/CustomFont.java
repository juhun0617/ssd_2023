package org.example.etc;

import java.awt.*;
import java.io.InputStream;


/**
 * @author juhun_park
 * 사용자 정의 폰트를 로드하고 관리하는 클래스입니다.
 * 이 클래스는 지정된 폰트 파일을 로드하여 시스템에 등록하고, 해당 폰트를 사용할 수 있도록 합니다.
 */
public class CustomFont {
    private static final String FONT_PATH = "/font/neodgm.ttf";
    private static Font customFont;


    /**
     * 지정된 크기로 사용자 정의 폰트를 로드하고 반환합니다.
     * <p>
     * 이 메서드는 클래스 경로에서 폰트 파일을 찾아 로드하고, 지정된 크기로 폰트를 조정합니다.
     * 폰트 파일이 없거나 로드에 실패할 경우 null을 반환합니다.
     * </p>
     *
     * @param size 폰트 크기
     * @return 조정된 크기의 Font 객체, 또는 로드 실패 시 null
     */
    public static Font loadCustomFont(float size) {
        if (customFont == null) {
            try (InputStream is = CustomFont.class.getResourceAsStream(FONT_PATH)) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, is)
                        .deriveFont(size);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return customFont.deriveFont(size);
    }

}