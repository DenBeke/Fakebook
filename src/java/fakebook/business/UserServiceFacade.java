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
        if (this.getUserByEmail(user.getEmail()) != null) {
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
    public void updateUser(User user) {
        em.merge(user);
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
     * returns the user corresponding to the email
     * @param email
     * @return the user if it exists, null otherwise
     */
    @Override
    public User getUserByEmail(String email) {
        List<User> users = em.createNamedQuery("User.getByEmail")
            .setParameter("email", email)
            .getResultList();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }
    
    /**
     * returns the user corresponding to its facebook id
     * @param fbId
     */
    @Override
    public User getUserByFacebookId(String fbId) {
        List<User> users = em.createNamedQuery("User.getByFbId")
            .setParameter("fbId", fbId)
            .getResultList();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
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
