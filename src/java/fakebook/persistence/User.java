/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.persistence;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author robin
 * User Entity to store users in database
 */
@Entity
public class User implements Serializable {

    public User() {
    }
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;   // The email has to be unique, in order to avoid duplicate users.
    private String fbToken; // The token used to access the graph API for the user.
    private Boolean isAdmin;  // A boolean which indicates whether the user is an admin or not, TODO set default to false, so no problems can arrise.

    /**
     * Creates a new User object, the Id is auto generated.
     * @param firstName
     * @param lastName
     * @param email
     * @param fbToken
     * @param Admin
     */
    public User(String firstName, String lastName, String email, String fbToken, Boolean Admin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.fbToken = fbToken;
        // Make sure that admin is not null.
        if (Admin != null) {
            this.isAdmin = Admin;
        } else {
            this.isAdmin = false;   // Default to false, making sure that no-one gets admin rights by accident.
        }
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
