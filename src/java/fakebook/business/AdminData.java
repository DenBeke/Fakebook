/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.Post;
import fakebook.persistence.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Startup
@Singleton
public class AdminData implements HttpSessionListener {

    @EJB
    private UserServiceFacadeLocal userService;

    private static final Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();
    
    public AdminData() {
    }
    
    @PostConstruct 
    public void init() {
        User admin = userService.getUserByEmail("admin@admin");
        if (admin == null) {
            admin = new User("admin@admin", null, "root", "admin", "", "", "", true, "");
            userService.newUser(admin);
        }
    }
    
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        sessions.put(session.getId(), session);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        sessions.remove(event.getSession().getId());
    }
}
