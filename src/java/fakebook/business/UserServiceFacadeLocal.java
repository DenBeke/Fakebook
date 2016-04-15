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

/**
 *
 * @author robin
 */
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
     * Edits an existing user
     * @param user 
     */
    public void editUser(User user);
    
    
    /**
     * Deletes an account from the database
     * Also in waterfall fashion deletes all posts and friendships.
     * @param userId
     * @return true if successful, false otherwise
     */
    public Boolean deleteAccount(long userId);
    
    /**
     * checks if an email is already in the database.
     * @param email
     * @return true if email is in database, false otherwise.
     */
    public User getUserByEmail(String email);

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
     * Returns the list of friends for a user.
     * @param userId
     * @return 
     */
    List<User> getFriends(long userId);

    /**
     * initializes a friendship -> adds the other user to each others friends list.
     * @param userId1
     * @param userId2
     * @return 
     */
    Boolean beginFriendship(long userId1, long userId2);

    /**
     * ends the friendship -> removes other user from each others friends list
     * @param userId1
     * @param userId2
     * @return 
     */
    Boolean endFriendship(long userId1, long userId2);
}

