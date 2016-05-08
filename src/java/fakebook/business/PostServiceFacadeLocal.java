/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.Post;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author robin
 */
public interface PostServiceFacadeLocal {

    /**
     * Persists a post
     * @param p
     * @return 
     */
    public Boolean newPost(Post p);
    
    /**
     * Updates a post
     * @param post
     * @return 
     */
    public void updatePost(Post post);
    
    /**
     * Deletes a post
     * @param postId
     * @return 
     */
    public Boolean deletePost(Post post);
    
    /**
     * Get post corresponding to pId
     * @param pId
     * @return 
     */
    public Post getPost(long pId);
    
    /**
     * Returns a list containing all posts
     * @return 
     */
    public List<Post> getAllPosts();
    
    /**
     * Return all posts on the wall of a single user.
     * @param userId
     * @return 
     */
    public List<Post> getPostsOnWall(long userId);
    
    /**
     * Return all posts by a single user.
     * @param userId
     * @return 
     */
    public List<Post> getPostsByPoster(long userId);
    
    
    /**
     * Return all posts the user is mentioned in
     * @param userId
     * @return 
     */
    public List<Post> getPostsByMention(long userId);
}
