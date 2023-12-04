package org.example.service;


import org.example.Entity.Character;

import javax.persistence.*;
import java.util.List;


/**
 * @author juhun_park
 * 'Character' 엔티티와 관련된 데이터베이스 작업을 관리하는 서비스 클래스.
 * 이 클래스는 'Character' 엔티티를 조회, 저장, 삭제하는 메서드를 제공합니다.
 */
public class CharacterService {

    private EntityManagerFactory entityManagerFactory;
    private Character character;

    /**
     * CharacterService 클래스의 생성자.
     *
     * @param entityManagerFactory 데이터베이스 작업을 위한 EntityManagerFactory 객체
     */
    public CharacterService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    /**
     * 지정된 테이블이 비어 있는지 여부를 확인합니다.
     *
     * @param tableName 확인할 테이블의 이름
     * @return 테이블이 비어있으면 true, 그렇지 않으면 false
     */
    public boolean isTableEmpty(String tableName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT COUNT(t) FROM " + tableName + " t");
        Long count = (Long) query.getSingleResult();
        return count == 0;
    }

    /**
     * 모든 'Character' 이름의 리스트를 반환합니다.
     *
     * @return 저장된 모든 'Character'의 이름 리스트
     */
    public List<String> getCharacterNames() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<String> query = entityManager.createQuery(
                "SELECT c.name FROM Character c", String.class);
        return query.getResultList();
    }

    /**
     * 이름을 기반으로 특정 'Character' 엔티티를 찾아 반환합니다.
     *
     * @param name 찾고자 하는 'Character'의 이름
     * @return 일치하는 'Character' 엔티티, 또는 일치하는 결과가 없으면 null
     */
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

    /**
     * 'Character' 엔티티를 데이터베이스에 저장합니다.
     *
     * @param character 저장할 'Character' 엔티티 객체
     */
    public void saveCharacter(Character character) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction()
                    .begin(); // Start transaction

            if (!entityManager.contains(character)) {
                character = entityManager.merge(character); // Merge if detached
            } else {
                entityManager.persist(character); // Persist if new
            }

            entityManager.getTransaction()
                    .commit(); // Commit transaction
        } catch (Exception e) {
            entityManager.getTransaction()
                    .rollback(); // Rollback in case of error
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    /**
     * 데이터베이스에서 'Character' 엔티티를 삭제합니다.
     *
     * @param character 삭제할 'Character' 엔티티 객체
     */
    public void deleteCharacter(Character character) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction()
                    .begin();


            if (!entityManager.contains(character)) {
                character = entityManager.merge(character);
            }


            entityManager.remove(character);

            entityManager.getTransaction()
                    .commit();
        } catch (Exception e) {
            entityManager.getTransaction()
                    .rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }


    /**
     * 새로운 'Character' 엔티티를 생성하고 초기화한 후 데이터베이스에 저장합니다.
     *
     * @param name          캐릭터의 이름
     * @param whatCharacter 캐릭터의 종류
     */
    public void setCharacter(String name, String whatCharacter) {
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
        character.setTableId(-1L);
        character.setChairId(-1L);

        try {
            entityManager.getTransaction()
                    .begin();
            entityManager.persist(character);
            entityManager.getTransaction()
                    .commit();
        } catch (Exception e) {
            entityManager.getTransaction()
                    .rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }


}





