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
import java.util.regex.Pattern;


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
    public Boolean deleteAccount(User user) {
        if (user == null) {
            return false;
        } else {
            // There always has to be an admin
            List<User> users = getAllUsers();
            for (User u : users) {
                if (!u.getId().equals(user.getId()) && u.getIsAdmin()) {
                    user.setIsDeleted(true);
                    em.merge(user);
                    return true;
                }
            }
            
            return false;
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

    @Override
    public List<User> getAllUsers() {
        return em.createNamedQuery("User.getAll").getResultList();
    }

    @Override
    public List<User> searchUser(String partOfName) {
        List<User> matchedUsers = new ArrayList<>();
        List<User> allUsers = getAllUsers();
        for (User user : allUsers) {
            if (Pattern.compile(Pattern.quote(partOfName), Pattern.CASE_INSENSITIVE).matcher(user.getName()).find()) {
                matchedUsers.add(user);
            }
        }
        return matchedUsers;
    }
}
