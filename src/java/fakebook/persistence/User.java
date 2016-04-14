/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author robin
 * User Entity to store users in database
 */
@Entity(name = "FBUser")
@NamedQueries({@NamedQuery(name="User.getAll",query="SELECT e FROM FBUser e"), @NamedQuery(name="User.getByEmail", query="SELECT e FROM FBUser e WHERE e.email LIKE :email")})
public class User implements Serializable {

    public User() {
    }
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String email;   // The email has to be unique, in order to avoid duplicate users.
    
    private String firstName;
    private String lastName;
    private String gender;
    private String birthday;

    private String fbToken; // The token used to access the graph API for the user.
    private String password; // TODO: We should store a hash instead
    private Boolean isAdmin;  // A boolean which indicates whether the user is an admin or not, TODO set default to false, so no problems can arrise.
    
    @ManyToMany
    private List<User> friends; // The friends of this user TODO make sure it is both ways.
    
    @ManyToMany
    private List<User> friendshipRequests; // The users to which this user has send a friendship request, not symetric
    
    /**
     * Creates a new User object, the Id is auto generated.
     * @param email
     * @param fbToken
     * @param password
     * @param firstName
     * @param lastName
     * @param gender
     * @param birthday
     * @param Admin
     */
    public User(String email, String fbToken, String password, String firstName, String lastName, String gender, String birthday, Boolean Admin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.fbToken = fbToken;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;

        // Make sure that admin is not null.
        if (Admin != null) {
            this.isAdmin = Admin;
        } else {
            this.isAdmin = false;   // Default to false, making sure that no-one gets admin rights by accident.
        }
        this.friends = new ArrayList<>();
        // users start with empty friendship lists.
    }

    public List<User> getFriendshipRequests() {
        return friendshipRequests;
    }

    public void setFriendshipRequests(List<User> friendshipRequests) {
        this.friendshipRequests = friendshipRequests;
    }
    
    public Boolean addFriend(User friend) {
        friends.add(friend);
        return true;
    }
    
    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fakebook.persistence.User[ id=" + id + " ]";
    }
    
}
