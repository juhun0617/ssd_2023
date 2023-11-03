package org.example.service;


import javax.persistence.*;

public class CharacterService {

    private EntityManager entityManager;

    public CharacterService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean isTableEmpty(String tableName){
        Query query = entityManager.createQuery("SELECT COUNT(t) FROM " + tableName + " t");
        Long count = (Long) query.getSingleResult();
        return count == 0;
    }

    // 리소스 정리를 위한 메서드
    public void close() {
        if (entityManager != null) {
            entityManager.close();
        }
    }
}





