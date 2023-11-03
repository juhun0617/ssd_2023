package org.example.service;


import org.example.Entity.Character;

import javax.persistence.*;

public class CharacterService {

    private EntityManager entityManager;
    private Character character;

    public CharacterService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean isTableEmpty(String tableName){
        Query query = entityManager.createQuery("SELECT COUNT(t) FROM " + tableName + " t");
        Long count = (Long) query.getSingleResult();
        return count == 0;
    }
    public void setCharacter(String name, String whatCharacter){
        character = new Character();
        character.setName(name);
        character.setAnimal(whatCharacter);
        character.setHungry(100);
        character.setThirst(100);
        character.setHygiene(100);
        character.setHealth(100);
        character.setLevel(1);
        character.setMoney(0);
        character.setMax_score_1(0);
        character.setMax_score_2(0);
        character.setMax_score_3(0);
        character.setMax_score_4(0);


        try {
            entityManager.getTransaction().begin();
            entityManager.persist(character);
            entityManager.getTransaction().commit();
        } catch (Exception e){
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    // 리소스 정리를 위한 메서드
    public void close() {
        if (entityManager != null) {
            entityManager.close();
        }
    }
}





