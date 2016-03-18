/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.business;

import java.util.List;
import javax.ejb.Local;
import test.persistence.User;

/**
 *
 * @author robin
 * 
 * Provides the interface to control the user entity.
 */
@Local
public interface UserFacadeLocal {
    void addUser(User user);
    
    void editUser(User user);
    
    void deleteUser(long usdrId);
    
    User getUser(long userId);
    
    List<User> getAllUsers();
    
}
