/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.User;
import javax.ejb.Stateful;

/**
 *
 * @author Mathias
 */
@Stateful
public class CurrentUser {

    private User user;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public CurrentUser() {
    }
    
    public CurrentUser(User user) {
        if(this.user == null) {
            System.out.println("CurrentUser constructed with null pointer");
        }
        this.user = user;
    }
    
    public User getUser() {
        if(this.user == null) {
            System.out.println("User == null");
        }
        return this.user;
    }
    
}
