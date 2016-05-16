/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.Post;
import fakebook.persistence.User;
import java.util.List;
import javax.ejb.Local;


@Local
public interface WallServiceFacadeLocal {

    public void addPost(Post post);

    public void addComment(User author, String parentPostId, String newComment, String cue);

    public void addLike(User user, String postId);

    public List<Post> getWallPosts(Long userId);

}

