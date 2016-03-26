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
     * Deletes an account from the database
     * Also in waterfall fashion deletes all posts and friendships.
     * @param userId
     * @return true if successful, false otherwise
     */
    public Boolean deleteAccount(long userId);
}

