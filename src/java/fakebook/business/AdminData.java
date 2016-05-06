/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Startup
@Singleton
@WebListener
public class AdminData implements AdminDataLocal, HttpSessionListener {

    @EJB
    private UserServiceFacadeLocal userService;

    private static final Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();
    
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
    
    @Override
    public Set<User> getOnlineUsers() {
        Set<User> users = new HashSet<>();
        
        for (HttpSession session : sessions.values()) {
            User user = (User)session.getAttribute("currentUser");
            if (user != null) {
                users.add(user);
            }
        }
        
        return users;
    }
}
