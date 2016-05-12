/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import java.util.Map;
import javax.ejb.Local;


@Local
public interface LoginServiceFacadeLocal {

    public Map<String, Object> login(String fbToken, String email, String password);

}

