/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import java.util.List;
import javax.ejb.Stateless;
import fakebook.persistence.User;
import javax.ejb.Local;

@Local
public interface UserServiceFacadeLocal {

    /**
     * Registers a new user in the database
     * @return returns codes to indicate what went wrong if something went wrong.
     *          0 : Everything went OK
     *          1 : User already existed
     *          2 : Unknown Error.
     * @param user 
     */
    public int newUser(User user) ;
    
    
    /**
     * Update an existing user
     * @param user 
     */
    public void updateUser(User user);
    
    
    /**
     * Deletes an account
     * The account will remain in the database and will only be hidden
     * @param user
     * @return true if successful, false otherwise
     */
    public Boolean deleteAccount(User user);
    
    /**
     * returns the user corresponding to the email
     * @param email
     * @return the user if it exists, null otherwise
     */
    public User getUserByEmail(String email);
    
    /**
     * returns the user corresponding to its facebook id.
     * @param fbId
     */
    public User getUserByFacebookId(String fbId);

    /**
     * Gets the user with corresponding userId
     * if userId is not used, it will return null
     * @param userId
     * @return 
     */
    User getUser(long userId);
    
    /**
     * Returns a list with all users in the database.
     * @return 
     */
    List<User> getAllUsers();

    /**
     * Search for users given a part of their name
     * @param partOfName
     * @return 
     */
    public List<User> searchUser(String partOfName);
}

