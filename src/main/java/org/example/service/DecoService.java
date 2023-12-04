package org.example.service;

import org.example.Entity.Deco;
import org.hibernate.mapping.DenormalizedTable;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author juhun_park
 * 'Deco' 엔티티와 관련된 데이터베이스 작업을 관리하는 서비스 클래스.
 * 이 클래스는 'Deco' 엔티티를 조회하는 메서드를 제공합니다.
 */
public class DecoService {

    private EntityManager entityManager;
    private Deco deco;

    /**
     * DecoService 클래스의 생성자.
     *
     * @param entityManager 데이터베이스 작업을 위한 EntityManager 객체
     */
    public DecoService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 주어진 데코레이션 클래스에 해당하는 모든 'Deco' 엔티티를 조회합니다.
     *
     * @param decoClass 조회할 데코레이션의 클래스
     * @return 해당 클래스의 모든 'Deco' 엔티티 리스트
     */
    public List<Deco> findDecoByClass(String decoClass) {
        TypedQuery<Deco> query = entityManager.createQuery(
                "SELECT d FROM Deco d WHERE d.decoClass = :decoClass", Deco.class);
        query.setParameter("decoClass", decoClass);
        return query.getResultList();
    }

    /**
     * 주어진 ID에 해당하는 'Deco' 엔티티를 조회합니다.
     *
     * @param id 조회할 데코레이션의 ID
     * @return 일치하는 'Deco' 엔티티, 또는 일치하는 결과가 없으면 null
     */
    public Deco findDecoById(Long id) {
        try {
            TypedQuery<Deco> query = entityManager.createQuery(
                    "SELECT c FROM Deco c WHERE c.id = :id", Deco.class
            );
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
