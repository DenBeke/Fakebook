/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.Post;
import fakebook.persistence.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.EJB;


@Stateless
public class WallServiceFacade implements WallServiceFacadeLocal {
    
    @EJB
    private PostServiceFacadeLocal postService;

    @Override
    public void addPost(User author, User user, String newPost) {
        if (!newPost.trim().isEmpty()) {
            postService.newPost(new Post(author, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new Date(), newPost));
        }
    }

    @Override
    public void addComment(User author, String parentPostId, String newComment) {
        Post parentPost = postService.getPost(Long.decode(parentPostId));
        if (parentPost != null && !newComment.trim().isEmpty()) {
            Post comment = new Post(author, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new Date(), newComment);
            postService.newPost(comment);
            
            parentPost.getComments().add(comment);
            postService.updatePost(parentPost);
        }
    }

    @Override
    public void addLike(User user, String postId) {
        if (!postId.trim().isEmpty()) {
            Post post = postService.getPost(Long.decode(postId));
            if (post != null) {
                List<User> likes = post.getLikes();
                if (!likes.contains(user)) {
                    likes.add(user);
                    postService.updatePost(post);
                }
            }
        }
    }

    @Override
    public List<Post> getWallPosts(Long userId) {
        List<Post> posts = postService.getPostsOnWall(userId);
        
        // Sort the posts based on their creation time (newest first)
        Collections.sort(posts, new Comparator<Post>() {
            public int compare(Post post1, Post post2) {
                return post2.getTimestamp().compareTo(post1.getTimestamp());
            }
        });
        
        return posts;
    }
}
