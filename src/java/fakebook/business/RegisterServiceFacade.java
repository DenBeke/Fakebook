/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.User;
import javax.ejb.Stateless;
import javax.ejb.EJB;

/**
 *
 * @author robin
 */
@Stateless
public class RegisterServiceFacade implements RegisterServiceFacadeLocal {
    
    @EJB
    private UserServiceFacadeLocal userService;

    @Override
    public String register(String email, String password, String firstName, String lastName, String gender, String birthday, boolean b) {
        // Check if user already exists
        User user = userService.getUserByEmail(email);
        if (user != null) {

            // Check if existing user was a facebook account
            if (user.getPassword() == null) {
                if (user.getIsDeleted()) {
                    return "Failed to register account, the email belongs to a deleted user";
                }

                // TODO: Merge acount
                //       Security issue: shouldn't the user be asked to login to facebook at this point?

                user.setPassword(password);
                userService.updateUser(user);

                return "";
            }
            else { // Account was already registered
                return "An account has already been created with the email address";
            }
        }
        else { // Account did not exist yet
            user = new User(email, null, password, firstName, lastName, gender, birthday, false, "");
            if (userService.newUser(user) != 0) {
                return "Failed to register user";
            }
            else {
                return "";
            }
        }
    }
}
