package org.example.service;

import org.example.Entity.Character_Deco;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author juhun_park
 * 'Character_Deco' 엔티티와 관련된 데이터베이스 작업을 관리하는 서비스 클래스.
 * 이 클래스는 'Character_Deco' 엔티티를 조회, 저장, 삭제하는 메서드를 제공합니다.
 */
public class Character_DecoService {

    private EntityManagerFactory entityManagerFactory;
    private Character_Deco characterDeco;

    /**
     * Character_DecoService 클래스의 생성자.
     *
     * @param entityManagerFactory 데이터베이스 작업을 위한 EntityManagerFactory 객체
     */
    public Character_DecoService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    ;


    /**
     * 주어진 캐릭터 ID에 해당하는 모든 데코레이션 ID를 조회합니다.
     *
     * @param characterId 조회할 캐릭터의 ID
     * @return 캐릭터가 가지고 있는 데코레이션 ID의 리스트
     */
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

    /**
     * 주어진 캐릭터 ID와 데코레이션 ID에 해당하는 데코레이션의 수를 계산합니다.
     *
     * @param characterId 캐릭터의 ID
     * @param decoId      데코레이션의 ID
     * @return 주어진 캐릭터가 가지고 있는 특정 데코레이션의 수
     */
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

    /**
     * 'Character_Deco' 엔티티를 데이터베이스에 저장합니다.
     *
     * @param characterDeco 저장할 'Character_Deco' 엔티티 객체
     */
    public void saveCharacterDeco(Character_Deco characterDeco) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction()
                    .begin();
            entityManager.persist(characterDeco);
            entityManager.getTransaction()
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    /**
     * 주어진 캐릭터 ID와 데코레이션 ID에 일치하는 첫 번째 'Character_Deco' 엔티티를 삭제합니다.
     *
     * @param characterId 캐릭터의 ID
     * @param decoId      삭제할 데코레이션의 ID
     */
    public void deleteFirstMatchingCharacterDeco(Long characterId, Long decoId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction()
                    .begin();

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

            entityManager.getTransaction()
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction()
                    .isActive()) {
                entityManager.getTransaction()
                        .rollback();
            }
        } finally {
            entityManager.close();
        }
    }


}
