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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;

/**
 *
 * @author robin
 */
@Entity
@NamedQueries({@NamedQuery(name="Posts.getAll",query="SELECT p FROM Post p"),
               @NamedQuery(name="Posts.getOnWall", query="SELECT p FROM Post p WHERE p.wall.id = :uId"),
               @NamedQuery(name="Posts.getByPoster", query="SELECT p FROM Post p WHERE p.poster.id = :uId")})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne 
    private User poster;  // The poster who posted the post
    @ManyToOne
    private User wall;    // The user on whose wall it will be posted, not necessarily the same as poster.
    @ManyToMany
    private List<User> mentioned; // All users who are mentioned in the post, so on
    @OneToMany
    private List<Post> comments;  // Comments on this post
    
    @Temporal(DATE)
    private Date timestamp; // The time the post was posted
    
    private String text;    // The actual post Text
    
    private String type;    // type of the post ("picture", "link", "video" or not specified)
    private String picture; // URL of the attached photo if there is one
    private String video;   // URL of the attached video if there is one
    private String link;    // URL of the link if there is one
    
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
    public Post(User poster, User wall, List<User> mentioned, List<Post> comments, Date timestamp, String text) {
        this.poster = poster;
        this.wall = wall;
        this.mentioned = mentioned;
        this.comments = comments;
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

    public List<Post> getComments() {
        return comments;
    }

    public void setPost(List<Post> comments) {
        this.comments = comments;
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
    
    public String getPicture() {
        return picture;
    }
    
    public void setPicture(String picture) {
        this.type = "picture";
        this.picture = picture;
    }
    
    public String getVideo() {
        return video;
    }
    
    public void setVideo(String video) {
        this.type = "video";
        this.video = video;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.type = "link";
        this.link = link;
    }

    public String getType() {
        return type;
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
