package org.example;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BackgroundMusic {
    private Clip clip;

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

    public void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
