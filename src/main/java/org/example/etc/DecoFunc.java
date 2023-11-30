package org.example.etc;

import org.example.Entity.Character;
import org.example.Entity.Deco;
import org.example.service.CharacterService;
import org.example.service.DecoService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Objects;

public class DecoFunc {
    private Deco deco;
    private Character character;

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    EntityManager em = emf.createEntityManager();
    CharacterService characterService = new CharacterService(emf);
    DecoService decoService = new DecoService(em);

    public DecoFunc(Character character,Deco deco){
        this.character = character;
        this.deco = deco;

    }
    public void funcStart(){
        if (Objects.equals(deco.getDecoClass(),"back")){
            backFunc();
        }
    }

    private void backFunc(){
        character.setBackId(deco.getId());
        characterService.saveCharacter(character);
    }

}
