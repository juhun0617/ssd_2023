package org.example.etc;

import org.example.Entity.Character;
import org.example.Entity.Deco;
import org.example.service.CharacterService;
import org.example.service.Character_DecoService;
import org.example.service.DecoService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author juhun_park
 * 데코레이션과 캐릭터 간의 상호작용을 처리하는 클래스입니다.
 * 이 클래스는 데코레이션 아이템의 종류에 따라 캐릭터의 상태를 변경합니다.
 */
public class DecoFunc {
    EntityManagerFactory emf;
    EntityManager em;
    CharacterService characterService;
    private Deco deco;
    private Character character;


    /**
     * DecoFunc 클래스의 생성자.
     *
     * @param character 상호작용할 캐릭터 객체
     * @param deco      적용할 데코레이션 객체
     */
    public DecoFunc(Character character, Deco deco) {

        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db")
                .toString();
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        em = emf.createEntityManager();

        characterService = new CharacterService(emf);
        this.character = character;
        this.deco = deco;

    }

    /**
     * 데코레이션 기능을 시작합니다.
     * 데코레이션의 종류에 따라 적절한 기능(배경, 음식, 가구)을 실행합니다.
     */
    public void funcStart() {
        if (Objects.equals(deco.getDecoClass(), "back")) {
            backGroundFunc();
        } else if (Objects.equals(deco.getDecoClass(), "food")) {
            foodFunc();
        } else {
            furnitureFunc();
        }
    }

    /**
     * 가구 관련 기능을 처리합니다.
     * 데코레이션 이름에 따라 캐릭터의 테이블 또는 의자 ID를 설정합니다.
     */
    private void furnitureFunc() {
        String decoName = deco.getDecoName();
        if (decoName.contains("테이블")) {
            character.setTableId(deco.getId());
            characterService.saveCharacter(character);
        } else {
            character.setChairId(deco.getId());
            characterService.saveCharacter(character);
        }

    }


    /**
     * 배경 관련 기능을 처리합니다.
     * 캐릭터의 배경 ID를 데코레이션 ID로 설정합니다.
     */
    private void backGroundFunc() {
        character.setBackId(deco.getId());
        characterService.saveCharacter(character);
    }

    /**
     * 음식 관련 기능을 처리합니다.
     * 데코레이션 ID에 따라 캐릭터의 건강, 배고픔, 목마름, 즐거움 상태를 변경합니다.
     */
    private void foodFunc() {
        if (deco.getId() == 13) { //약 먹었을 때
            character.setHealth(character.getHealth() + 30);
            characterService.saveCharacter(character);
        } else if (deco.getId() == 14) { //도넛 먹었을 때
            character.setHungry(character.getHungry() + 30);
            characterService.saveCharacter(character);
        } else if (deco.getId() == 15) { //케이크 먹었을 때
            character.setHungry(character.getHungry() + 60);
            characterService.saveCharacter(character);
        } else if (deco.getId() == 16) { //오렌지 주스 먹었을 때
            character.setThirst(character.getThirst() + 20);
            characterService.saveCharacter(character);
        } else if (deco.getId() == 17) { //커피
            character.setThirst(character.getThirst() + 30);
            character.setHealth(character.getHealth() + 10);
            character.setFun(character.getFun() + 20);
            characterService.saveCharacter(character);
        } else if (deco.getId() == 18) { //스무디 (궁극의 음료)
            character.setThirst(100);
            character.setHealth(100);
            character.setFun(100);
            character.setHungry(100);
            characterService.saveCharacter(character);
        }
    }

}
