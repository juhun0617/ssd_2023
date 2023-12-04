package org.example.Animal;

import org.example.Entity.Character;

/**
 * @author juhun_park
 * rabbit 정의 클래스
 * Animal 클래스를 상속받음
 */
public class Rabbit extends Animal {
    public final String PATH = "/Image/character/rabbit.png";

    /**
     * rabbit 객체를 만들어주는 생성자
     *
     * @param character : character 객체를 파라메터로 받는다
     */
    public Rabbit(Character character) {
        super(character);
    }

    /**
     * rabbit에 맞는 이미지 경로를 반환해주는 메서드
     *
     * @return : rabbit에 알맞은 이미지 경로
     */
    @Override
    public String getPATH() {
        return PATH;
    }


}
