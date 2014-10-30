package com.sackmesser.excel.dao;

import com.sackmesser.excel.domain.Player;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Diogo
 * Date: 08/10/14
 * Time: 17:25
 * To change this template use File | Settings | File Templates.
 */

@Component
public class PlayerDao {
    @PersistenceUnit
    private EntityManagerFactory emf;

    public void mergeAll(List<Player> list){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try{
            for (Player p : list){
                em.merge(p);
            }
            em.getTransaction().commit();
        }catch(Exception e){
            if(em.getTransaction()!= null){
                em.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public List<Player> listAll(){
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("select p from Player p");
        return q.getResultList();
    }

}
