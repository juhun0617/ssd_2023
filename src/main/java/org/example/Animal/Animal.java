package org.example.Animal;

import org.example.Entity.Character;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author juhun_park
 * 동물들의 전체적인 타이머를 세팅해주는 클래스
 * 각 동물들의 부모클래스
 */
public abstract class Animal {

    private Character character;
    private Timer hungerTimer;
    private Timer thirstTimer;
    private Timer funTimer;
    private Timer healthTimer;

    /**
     * Animal 클래스의 생성자
     * 캐릭터를 파라메터로 받아 초기화 한다
     *
     * @param character : 캐릭터 객체
     */
    public Animal(Character character) {
        this.character = character;
        this.hungerTimer = new Timer();
        this.thirstTimer = new Timer();
        this.funTimer = new Timer();
        this.healthTimer = new Timer();
    }

    /**
     * 배고픔 타이머를 정의한다
     */
    public void startHungerTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Hunger timer task executed at: " + new java.util.Date());
                character.setHungry(character.getHungry() - 1);
            }
        };
        hungerTimer.scheduleAtFixedRate(task, 0, 30000); // 30초마다 실행
    }

    /**
     * 목마름 타이머를 정의한다
     */
    public void startThirstTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Thirst timer task executed at: " + new java.util.Date());
                character.setThirst(character.getThirst() - 1);
            }
        };
        thirstTimer.scheduleAtFixedRate(task, 0, 45000); // 45초마다 실행
    }

    /**
     * 흥미 타이머를 정의한다
     */
    public void startFunTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Fun timer task executed at: " + new java.util.Date());
                character.setFun(character.getFun() - 1);
            }
        };
        funTimer.scheduleAtFixedRate(task, 0, 60000); // 60초마다 실행
    }

    /**
     * 체력 타이머를 정의한다
     */
    public void startHealthTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Health timer task executed at: " + new java.util.Date());
                character.setHealth(character.getHealth() - 1);
            }
        };
        healthTimer.scheduleAtFixedRate(task, 0, 90000); // 90초마다 실행
    }

    /**
     * 모든 타이머를 시작하는 메서드
     */
    public void startAllTimers() {
        startHungerTimer();
        startThirstTimer();
        startFunTimer();
        startHealthTimer();
    }

    /**
     * 모든 타이머를 중지하는 메서드
     */
    public void stopAllTimers() {
        hungerTimer.cancel();
        thirstTimer.cancel();
        funTimer.cancel();
        healthTimer.cancel();
    }

    /**
     * 이미지 경로를 리턴해주는 메서드
     * 각 자식 클래스에서 오버라이딩함
     *
     * @return : 이미지 경로를 반환
     */
    public abstract String getPATH();
}

