package org.example.service;

import org.example.Entity.Deco;
import org.hibernate.mapping.DenormalizedTable;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class DecoService {

    private EntityManager entityManager;
    private Deco deco;

    public DecoService(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public List<Deco> findDecoByClass(String decoClass) {
        TypedQuery<Deco> query = entityManager.createQuery(
                "SELECT d FROM Deco d WHERE d.decoClass = :decoClass", Deco.class);
        query.setParameter("decoClass", decoClass);
        return query.getResultList();
    }

    public Deco findDecoById(Long id){
        try {
            TypedQuery<Deco> query = entityManager.createQuery(
                    "SELECT c FROM Deco c WHERE c.id = :id", Deco.class
            );
            query.setParameter("id",id);
            return query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

}
