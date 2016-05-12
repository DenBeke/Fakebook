/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.User;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;


@Local
public interface AdminServiceFacadeLocal {

    public List<User> getAllUsers();
    
    public Set<User> getOnlineUsers();

    public String createUser(String email, String password, String firstName, String lastName, String gender, String birthday, Boolean admin);

    public void deleteUser(long userId);
}
