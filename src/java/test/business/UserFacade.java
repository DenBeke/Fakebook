/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.business;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import test.persistence.User;

/**
 *
 * @author robin
 * 
 * Class which implements the methods described in UserFacadeLocal
 */
@Stateless
public class UserFacade implements UserFacadeLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public void addUser(User user) {
        em.persist(user);
    }

    @Override
    public void editUser(User user) {
        em.merge(user);
    }

    @Override
    public void deleteUser(long userId) {
        System.out.println(userId);
        System.out.println("Name " + getUser(userId).getName());
        em.remove(getUser(userId));
    }

    @Override
    public User getUser(long userId) {
        System.out.println("Get user with ID" + userId);
        return em.find(User.class, userId);
    }

    @Override
    public List<User> getAllUsers() {
        return em.createNamedQuery("Users.getAll").getResultList();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
