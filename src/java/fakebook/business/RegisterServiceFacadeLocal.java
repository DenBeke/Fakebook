/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import javax.ejb.Local;


@Local
public interface RegisterServiceFacadeLocal {

    public String register(String email, String password, String firstName, String lastName, String gender, String birthday, boolean b);

}

