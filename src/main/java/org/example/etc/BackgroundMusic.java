package org.example.etc;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author juhun_park
 * 배경음악을 재생하기 위한 클래스
 * 배경음악을 재생하고 중지하는 메서드를 제공
 */
public class BackgroundMusic {
    private Clip clip;

    /**
     * 지정된 리소스 경로의 오디오 파일로부터 배경 음악을 시작합니다.
     * <p>
     * 이 메서드는 지정된 오디오 파일을 스트림으로 읽어 들여 재생합니다.
     * 오디오는 무한 반복되며, {@link #stopMusic()} 메서드를 호출할 때까지 계속 재생됩니다.
     * </p>
     *
     * @param resourcePath 리소스 내의 오디오 파일 경로
     */
    public void startMusic(String resourcePath) {
        // 리소스 스트림을 얻습니다.
        try (InputStream audioSrc = getClass().getResourceAsStream(resourcePath);
             // 버퍼링된 스트림을 사용하여 마크와 리셋을 지원합니다.
             BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc)) {
            // 오디오 스트림을 가져옵니다.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

            // 클립을 가져옵니다.
            clip = AudioSystem.getClip();

            // 클립에 오디오 스트림을 연결합니다.
            clip.open(audioInputStream);

            // 클립이 끝나면 자동으로 다시 시작하도록 합니다.
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // 음악 재생을 시작합니다.
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * 현재 재생 중인 배경 음악을 중지합니다.
     * <p>
     * 이 메서드는 배경 음악이 재생 중일 때만 작동합니다.
     * 음악이 재생 중이 아니라면, 아무런 작업도 수행하지 않습니다.
     * </p>
     */
    public void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
