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

public class DecoFunc {
    private Deco deco;
    private Character character;

    EntityManagerFactory emf;
    EntityManager em;
    CharacterService characterService;


    public DecoFunc(Character character,Deco deco){

        String homeDirectory = System.getProperty("user.home");
        String targetPath = Paths.get(homeDirectory, "sqlite.db").toString();
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + targetPath);


        emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        em = emf.createEntityManager();

        characterService = new CharacterService(emf);
        this.character = character;
        this.deco = deco;

    }
    public void funcStart(){
        if (Objects.equals(deco.getDecoClass(),"back")){
            backGroundFunc();
        } else if (Objects.equals(deco.getDecoClass(),"food")){
            foodFunc();
        } else {
            furnitureFunc();
        }
    }

    private void furnitureFunc(){
        String decoName = deco.getDecoName();
        if (decoName.contains("테이블")){
            character.setTableId(deco.getId());
            characterService.saveCharacter(character);
        } else {
            character.setChairId(deco.getId());
            characterService.saveCharacter(character);
        }

    }


    private void backGroundFunc(){
        character.setBackId(deco.getId());
        characterService.saveCharacter(character);
    }
    private void foodFunc(){
        if (deco.getId() == 13) { //약 먹었을 때
            character.setHealth(character.getHealth() + 30);
            characterService.saveCharacter(character);
        } else if (deco.getId() == 14){ //도넛 먹었을 때
            character.setHungry(character.getHungry() + 30);
            characterService.saveCharacter(character);
        } else if (deco.getId() == 15){ //케이크 먹었을 때
            character.setHungry(character.getHungry() + 60);
            characterService.saveCharacter(character);
        } else if (deco.getId() == 16){ //오렌지 주스 먹었을 때
            character.setThirst(character.getThirst() + 20);
            characterService.saveCharacter(character);
        } else if (deco.getId() == 17){ //커피
            character.setThirst(character.getThirst() + 30);
            character.setHealth(character.getHealth() + 10);
            character.setFun(character.getFun() + 20);
            characterService.saveCharacter(character);
        } else if (deco.getId() == 18){ //스무디 (궁극의 음료)
            character.setThirst(100);
            character.setHealth(100);
            character.setFun(100);
            character.setHungry(100);
            characterService.saveCharacter(character);
        }
    }

}
