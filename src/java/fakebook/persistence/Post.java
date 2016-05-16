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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.TIMESTAMP;


@Entity
@NamedQueries({@NamedQuery(name="Posts.getAll",query="SELECT p FROM Post p"),
               @NamedQuery(name="Posts.getAllWallPosts",query="SELECT p FROM Post p WHERE p.wall IS NOT NULL"),
               @NamedQuery(name="Posts.getOnWall", query="SELECT p FROM Post p WHERE p.wall.id = :uId"),
               @NamedQuery(name="Posts.getByPoster", query="SELECT p FROM Post p WHERE p.poster.id = :uId"),
               @NamedQuery(name="Posts.getWallPostsByPeriod", query="SELECT p FROM Post p WHERE p.wall IS NOT NULL AND p.timestamp BETWEEN :startDate AND :endDate"),
               @NamedQuery(name="Posts.getCommentsByPeriod", query="SELECT p FROM Post p WHERE p.wall IS NULL AND p.timestamp BETWEEN :startDate AND :endDate"),
               @NamedQuery(name="Posts.getPicturePostsByPeriod", query="SELECT p FROM Post p WHERE p.type = 'picture' AND p.timestamp BETWEEN :startDate AND :endDate"),
               @NamedQuery(name="Posts.getVideoPostsByPeriod", query="SELECT p FROM Post p WHERE p.type = 'video' AND p.timestamp BETWEEN :startDate AND :endDate"),
               @NamedQuery(name="Posts.getPicturePosts", query="SELECT p FROM Post p WHERE p.type = 'picture'"),
               @NamedQuery(name="Posts.getVideoPosts", query="SELECT p FROM Post p WHERE p.type = 'video'"),
               @NamedQuery(name="Posts.getWallPosts", query="SELECT p FROM Post p WHERE p.wall IS NOT NULL"),
               @NamedQuery(name="Posts.getComments", query="SELECT p FROM Post p WHERE p.wall IS NULL")})
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private User poster;  // The poster who posted the post
    @ManyToOne
    private User wall;    // The user on whose wall it will be posted, not necessarily the same as poster.
    
    @ManyToMany
    @JoinTable(name="POST_MENTIONS",
               joinColumns=@JoinColumn(name="POST_ID"),
               inverseJoinColumns=@JoinColumn(name="USER_ID"))
    private List<User> mentioned; // All users who are mentioned in the post, so on
    
    @ManyToMany
    @JoinTable(name="POST_LIKES",
               joinColumns=@JoinColumn(name="POST_ID"),
               inverseJoinColumns=@JoinColumn(name="USER_ID"))
    private List<User> likes; // All users that like this post

    @OneToMany
    private List<Post> comments;  // Comments on this post
    
    @Temporal(TIMESTAMP)
    private Date timestamp; // The time the post was posted
    
    private String text;    // The actual post Text
    
    private String type;    // type of the post ("picture", "link", "video" or not specified)
    private String picture; // URL of the attached photo if there is one
    private String video;   // URL of the attached video if there is one
    private String link;    // URL of the link if there is one
    
    private String cue; // The offensivness 'cue'
    
    @Temporal(TIMESTAMP)
    private Date seen; // The time the post was seen by the "wall owner".

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
     * @param comments
     * @param likes
     * @param timestamp
     * @param text
     * @param cue
     */
    public Post(User poster, User wall, List<User> mentioned, List<Post> comments, List<User> likes, Date timestamp, String text, String cue) {
        this.poster = poster;
        this.wall = wall;
        this.mentioned = mentioned;
        this.comments = comments;
        this.likes = likes;
        this.timestamp = timestamp;
        this.text = text;
        this.cue = cue;
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
    
    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(List<User> likes) {
        this.likes = likes;
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
    
    public Date getSeen() {
        return this.seen;
    }
    
    public void setSeen(Date date) {
        this.seen = date;
    }
    
    public void setCue(String c) {
        this.cue = c;
    }
    
    public String getCue() {
        return this.cue;
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
