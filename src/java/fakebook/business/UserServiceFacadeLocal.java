/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import javax.ejb.Stateless;
import test.persistence.User;

/**
 *
 * @author robin
 */
@Stateless
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
     * Checks if the provided password is correct, for the given user.
     * @param userId
     * @param passwd
     * @return true if passwords match, false if they don't
     */
    public Boolean login(long userId, String passwd) ;
    
    /**
     * Deletes an account from the database
     * Also in waterfall fashion deletes all posts and friendships.
     * @param userId
     * @return true if successful, false otherwise
     */
    public Boolean deleteAccount(long userId);
}

