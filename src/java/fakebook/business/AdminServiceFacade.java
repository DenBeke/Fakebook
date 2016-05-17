/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.Post;
import fakebook.persistence.User;
import java.util.Collections;
import java.util.Comparator;
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
public class AdminServiceFacade implements AdminServiceFacadeLocal, HttpSessionListener {

    @EJB
    private UserServiceFacadeLocal userService;
    
    @EJB
    private PostServiceFacadeLocal postService;
    
    @EJB
    private RegisterServiceFacadeLocal registerService;
    
    @EJB
    private BiometricServiceFacadeLocal biometricService;
    

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
            Long userId = (Long)session.getAttribute("currentUser");
            if (userId != null) {
                User user = userService.getUser(userId);
                if (user != null) {
                    users.add(user);
                }
            }
        }
        
        return users;
    }
    
    @Override
    public String createUser(String email, String password, String firstName, String lastName, String gender, String birthday, Boolean admin) {
        // Check if user already exists
        User user = userService.getUserByEmail(email);
        if (user != null) {

            // Check if existing user was a facebook account
            if (user.getPassword() == null && !user.getIsDeleted()) {
                user.setIsAdmin(admin);
                user.setPassword(password);
                userService.updateUser(user);
                return "";
            }
            else { // Account was already registered
                return "Failed to register user: an account has already been created with the email address";
            }
        }
        else { // Account did not exist yet
            return registerService.register(email, password, firstName, lastName, gender, birthday, admin);
        }
    }
    
    @Override
    public void deleteUser(long userId) {
        User user = userService.getUser(userId);
        if (user != null) {
            userService.deleteAccount(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = postService.getAllWallPosts();
        
        // Sort the posts based on their creation time (newest first)
        Collections.sort(posts, new Comparator<Post>() {
            public int compare(Post post1, Post post2) {
                return post2.getTimestamp().compareTo(post1.getTimestamp());
            }
        });
        
        return posts;
    }

    @Override
    public String downloadData(Long userId) {
        return biometricService.getJsonString(userId);
    }
}
