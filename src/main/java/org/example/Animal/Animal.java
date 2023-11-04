package org.example.Animal;

import org.example.Entity.Character;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Animal {

    private Character character;
    private Timer hungerTimer;
    private Timer thirstTimer;
    private Timer funTimer;
    private Timer healthTimer;

    public Animal(Character character) {
        this.character = character;
        this.hungerTimer = new Timer();
        this.thirstTimer = new Timer();
        this.funTimer = new Timer();
        this.healthTimer = new Timer();
    }

    // 각 스탯별로 타이머를 설정하는 메서드를 정의합니다.
    public void startHungerTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Hunger timer task executed at: " + new java.util.Date());
                character.setHungry(character.getHungry()-1);
            }
        };
        hungerTimer.scheduleAtFixedRate(task, 0, 30000); // 30초마다 실행
    }

    public void startThirstTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Thirst timer task executed at: " + new java.util.Date());
                character.setThirst(character.getThirst()-1);
            }
        };
        thirstTimer.scheduleAtFixedRate(task, 0, 45000); // 45초마다 실행
    }

    public void startFunTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Fun timer task executed at: " + new java.util.Date());
                character.setFun(character.getFun()-1);
            }
        };
        funTimer.scheduleAtFixedRate(task, 0, 60000); // 60초마다 실행
    }

    public void startHealthTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Health timer task executed at: " + new java.util.Date());
                character.setHealth(character.getHealth()-1);
            }
        };
        healthTimer.scheduleAtFixedRate(task, 0, 90000); // 90초마다 실행
    }

    // 모든 타이머를 시작합니다.
    public void startAllTimers() {
        startHungerTimer();
        startThirstTimer();
        startFunTimer();
        startHealthTimer();
    }

    // 모든 타이머를 중지합니다.
    public void stopAllTimers() {
        hungerTimer.cancel();
        thirstTimer.cancel();
        funTimer.cancel();
        healthTimer.cancel();
    }

    public String getPath() {
        return null; // 기본 Animal은 경로가 없으므로 null을 반환합니다.
    }

    public abstract String getPATH();
}

