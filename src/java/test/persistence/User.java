/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.persistence;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author robin
 */
@Entity(name = "Users")
@Table
@NamedQueries({@NamedQuery(name="Users.getAll",query="SELECT e FROM Users e")})
public class User implements Serializable {

    public User() {
    }

    public User(long id, String name, String password, String email) {
        this.userId = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    protected Long userId;
    
    @Column
    protected String name;
    @Column
    protected String password;
    @Column
    protected String email;
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
