/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.User;
import java.util.Set;

/**
 *
 * @author texus
 */
public interface AdminDataLocal {

    public Set<User> getOnlineUsers();
    
}
