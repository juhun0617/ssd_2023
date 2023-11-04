package org.example.service;


import org.example.Entity.Character;

import javax.persistence.*;
import java.util.List;

public class CharacterService {

    private EntityManager entityManager;
    private Character character;

    public CharacterService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean isTableEmpty(String tableName) {
        Query query = entityManager.createQuery("SELECT COUNT(t) FROM " + tableName + " t");
        Long count = (Long) query.getSingleResult();
        return count == 0;
    }

    public List<String> getCharacterNames() {
        // 캐릭터 이름만 조회하여 리스트로 반환하는 쿼리 실행
        TypedQuery<String> query = entityManager.createQuery(
                "SELECT c.name FROM Character c", String.class);
        return query.getResultList();
    }

    public Character findCharacterByName(String name) {
        try {
            TypedQuery<Character> query = entityManager.createQuery(
                    "SELECT c FROM Character c WHERE c.name = :name", Character.class);
            query.setParameter("name", name);
            return query.getSingleResult(); // 단일 결과 반환
        } catch (NoResultException e) {
            return null; // 결과가 없는 경우 null 반환
        }
    }



    public void setCharacter(String name, String whatCharacter){
        character = new Character();
        character.setName(name);
        character.setAnimal(whatCharacter);
        character.setHungry(100);
        character.setThirst(100);
        character.setFun(100);
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





