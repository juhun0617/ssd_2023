package org.example.service;


import org.example.Entity.Character;

import javax.persistence.*;
import java.util.List;

public class CharacterService {

    private EntityManagerFactory entityManagerFactory;
    private Character character;

    public CharacterService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public boolean isTableEmpty(String tableName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT COUNT(t) FROM " + tableName + " t");
        Long count = (Long) query.getSingleResult();
        return count == 0;
    }

    public List<String> getCharacterNames() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<String> query = entityManager.createQuery(
                "SELECT c.name FROM Character c", String.class);
        return query.getResultList();
    }

    public Character findCharacterByName(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Character> query = entityManager.createQuery(
                    "SELECT c FROM Character c WHERE c.name = :name", Character.class);
            query.setParameter("name", name);
            return query.getSingleResult(); // 단일 결과 반환
        } catch (NoResultException e) {
            return null; // 결과가 없는 경우 null 반환
        }
    }

    public void saveCharacter(Character character) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin(); // Start transaction

            if (!entityManager.contains(character)) {
                character = entityManager.merge(character); // Merge if detached
            } else {
                entityManager.persist(character); // Persist if new
            }

            entityManager.getTransaction().commit(); // Commit transaction
        } catch (Exception e) {
            entityManager.getTransaction().rollback(); // Rollback in case of error
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void deleteCharacter(Character character) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();


            if (!entityManager.contains(character)) {
                character = entityManager.merge(character);
            }


            entityManager.remove(character);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }





    public void setCharacter(String name, String whatCharacter){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
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
        character.setBackId(0L);
        character.setXp(0);

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


}





