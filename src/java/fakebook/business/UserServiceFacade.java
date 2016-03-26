/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import test.persistence.User;

/**
 *
 * @author robin
 */
@Stateless
public class UserServiceFacade implements UserServiceFacadeLocal{
    
    // Add the persistence context
    @PersistenceContext
    private EntityManager em;

    
    /**
     * Registers a new user in the database
     * @return returns codes to indicate what went wrong if something went wrong.
     *          0 : Everything went OK
     *          1 : User already existed
     *          2 : Unknown Error.
     * @param user 
     */
    @Override
    public int newUser(User user) {
        // Check if user email address is already in database.
        if (this.emailUsed(user.getEmail())) {
            return 1;
        } 
        // Email is not in database, persist the user
        try {
            em.persist(user);
        } catch (Exception e) {
            return 2;
        }
        return 0;
    }

    @Override
    public Boolean deleteAccount(long userId) {
        if (this.getUser(userId) == null) {
            return false;
        } else {
            em.remove(getUser(userId));
            return true;
        }
    }
    
    /**
     * checks if an email is already in the database.
     * @param email
     * @return true if email is in database, false otherwise.
     */
    private Boolean emailUsed(String email) {
        List<User> users = em.createNamedQuery("Users.getByEmail")
            .setParameter("email", email)
            .getResultList();
        if (!users.isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }
    
    @Override
    public User getUser(long userId) {
        return em.find(User.class, userId);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<User> getAllUsers() {
        return em.createNamedQuery("Users.getAll").getResultList();
    }
}
