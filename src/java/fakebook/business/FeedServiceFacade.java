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
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class FeedServiceFacade implements FeedServiceFacadeLocal {

    @EJB
    private UserServiceFacadeLocal userService;
    
    @EJB
    private PostServiceFacadeLocal postService;
    
    @EJB
    private WallServiceFacadeLocal wallService;
    
    @Override
    public void addLike(User user, String postId) {
        wallService.addLike(user, postId);
    }

    @Override
    public List<Post> getPosts(Long userId) {
        List<Post> posts = postService.getPostsOnWall(userId);
        
        // Also get the posts of your friends
        User user = userService.getUser(userId);
        List<User> friends = user.getFriends();
        for (User friend : friends) {
            posts.addAll(postService.getPostsOnWall(friend.getId()));
        }
        
        // Sort the posts based on their creation time (newest first)
        Collections.sort(posts, new Comparator<Post>() {
            public int compare(Post post1, Post post2) {
                return post2.getTimestamp().compareTo(post1.getTimestamp());
            }
        });
        
        return posts;
    }
    
}
