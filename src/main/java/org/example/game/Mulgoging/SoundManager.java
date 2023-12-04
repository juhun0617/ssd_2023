package org.example.game.Mulgoging;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Objects;


/**
 * 게임의 사운드 효과를 관리하는 클래스입니다.
 */
public class SoundManager {

    /**
     * 사운드 효과를 담고 있는 오디오 클립입니다.
     */
    private Clip clip;

    /**
     * 새로운 SoundManager 객체를 생성합니다.
     *
     * @param soundFilePath 사운드 파일의 경로입니다.
     */
    public SoundManager(String soundFilePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(soundFilePath)));
            System.out.println(soundFilePath);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 사운드 효과를 재생합니다.
     */
    public void play() {
        if (clip != null) {
            // 소리 재생 위치를 처음으로 설정
            clip.setFramePosition(0);
            // 소리 재생 시작
            clip.start();
        }
    }

    /**
     * 사운드 효과를 중지합니다.
     */
    public void stop() {
        if (clip != null && clip.isRunning()) {
            // 소리 재생 중지
            clip.stop();
        }
    }
}