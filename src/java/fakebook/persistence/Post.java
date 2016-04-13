/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author robin
 */
@Entity
@NamedQueries({@NamedQuery(name="Posts.getAll",query="SELECT p FROM Post p"), @NamedQuery(name="Posts.getByPoster", query="SELECT p FROM Post p WHERE p.poster LIKE :uId")})

public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne 
    private User poster;  // The poster who posted the post
    @ManyToOne
    private User wall;    // The user on whose wall it will be posted, not necessarily the same as poster.
    @ManyToMany
    private List<User> mentioned; // All users who are mentioned in the post, so on
    @OneToOne
    private Post post;  // If not null: post on which this was a comment.
    
    private Date timestamp; // The time the post was posted
    
    private String text;    // The actual post Text
    
    // TODO , depending on assignment allow for images/video/...

    /**
     * Empty Creator needed for Java EE.
     */
    public Post() {
    }
    
    /**
     * Creates a new post object.
     * @param poster
     * @param wall
     * @param mentioned
     * @param post
     * @param timestamp
     * @param text
     */
    public Post(User poster, User wall, List<User> mentioned, Post post, Date timestamp, String text) {
        this.poster = poster;
        this.wall = wall;
        this.mentioned = mentioned;
        this.post = post;
        this.timestamp = timestamp;
        this.text = text;
    }        

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public User getWall() {
        return wall;
    }

    public void setWall(User wall) {
        this.wall = wall;
    }

    public List<User> getMentioned() {
        return mentioned;
    }

    public void setMentioned(List<User> mentioned) {
        this.mentioned = mentioned;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fakebook.persistence.Post[ id=" + id + " ]";
    }
    
}
