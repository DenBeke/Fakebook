/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import fakebook.persistence.User;

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
    @Override
    public Boolean emailUsed(String email) {
        List<User> users = em.createNamedQuery("User.getByEmail")
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
        return em.createNamedQuery("User.getAll").getResultList();
    }

    @Override
    public List<User> getFriends(long userId) {
        User u = this.getUser(userId);
        if (u == null) {
            return new ArrayList<>();
        }
        else {
            return u.getFriends();
        }
    }

    @Override
    public Boolean beginFriendship(long userId1, long userId2) {
        
        User u1 = this.getUser(userId1);
        User u2 = this.getUser(userId2);
        if (u1 == null || u2 == null) {
            // one of the users is not in the database.
            return false;
        }
        if (u1.equals(u2)) {
            // same user, not possible to add as friend
            return false;
        }
        if (u1.getFriends().contains(u2)) {
            // User1's friends already contains u2
            return false;
        }
        if (u2.getFriends().contains(u1)) {
            return false;
        }
        
        List<User> f1 = u1.getFriends();
        f1.add(u2);
        u1.setFriends(f1);
        List<User> f2 = u2.getFriends();
        f2.add(u1);
        u2.setFriends(f2);
        
        em.merge(u1);
        em.merge(u2);
        
        return true;
    }

    @Override
    public Boolean endFriendship(long userId1, long userId2) {
        User u1 = this.getUser(userId1);
        User u2 = this.getUser(userId2);
        if (u1 == null || u2 == null) {
            // one of the users is not in the database.
            return false;
        }
        if (u1.equals(u2)) {
            // same user, not possible to add as friend
            return false;
        }
        if (!u1.getFriends().contains(u2)) {
            // User1's friends does not contain u2
            return false;
        }
        if (!u2.getFriends().contains(u1)) {
            return false;
        }
        
        List<User> f1 = u1.getFriends();
        f1.remove(u2);
        u1.setFriends(f1);
        List<User> f2 = u2.getFriends();
        f2.remove(u1);
        u2.setFriends(f2);
        
        em.merge(u1);
        em.merge(u2);
        
        return true;
    }
}
