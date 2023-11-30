package org.example.service;

import org.example.Entity.Character_Deco;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

public class Character_DecoService {

    private EntityManagerFactory entityManagerFactory;
    private Character_Deco characterDeco;

    public Character_DecoService(EntityManagerFactory entityManagerFactory){this.entityManagerFactory = entityManagerFactory;};


    public List<Long> findDecoIdsByCharacterId(Long characterId) { //특정 캐릭터가 가지고 있는 요소의 id반환
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Character_Deco> query = entityManager.createQuery(
                "SELECT cd FROM Character_Deco cd WHERE cd.characterId = :characterId", Character_Deco.class);
        query.setParameter("characterId", characterId);
        List<Character_Deco> results = query.getResultList();

        // 결과에서 decoId만 추출하여 리스트로 반환
        return results.stream()
                .map(Character_Deco::getDecoId)
                .collect(Collectors.toList());
    }


    public int countDecosByCharacterIdAndDecoId(Long characterId, Long decoId) { //얘는 특정 캐릭이 가지고 있는 특정 요소수
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(cd) FROM Character_Deco cd WHERE cd.characterId = :characterId AND cd.decoId = :decoId",
                Long.class);
        query.setParameter("characterId", characterId);
        query.setParameter("decoId", decoId);
        Long count = query.getSingleResult();

        return count.intValue();
    }

    public void saveCharacterDeco(Character_Deco characterDeco){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(characterDeco);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void deleteFirstMatchingCharacterDeco(Long characterId, Long decoId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            TypedQuery<Character_Deco> query = entityManager.createQuery(
                    "SELECT cd FROM Character_Deco cd WHERE cd.characterId = :characterId AND cd.decoId = :decoId",
                    Character_Deco.class);
            query.setParameter("characterId", characterId);
            query.setParameter("decoId", decoId);
            query.setMaxResults(1);

            List<Character_Deco> results = query.getResultList();
            if (!results.isEmpty()) {
                Character_Deco characterDecoToDelete = results.get(0);
                entityManager.remove(characterDecoToDelete);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } finally {
            entityManager.close();
        }
    }


}
