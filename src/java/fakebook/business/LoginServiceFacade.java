/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Comment;
import com.restfb.types.Likes;
import com.restfb.types.NamedFacebookType;
import fakebook.persistence.Post;
import javax.ejb.Stateless;
import fakebook.persistence.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;

/**
 *
 * @author robin
 */
@Stateless
public class LoginServiceFacade implements LoginServiceFacadeLocal {
    
    @EJB
    private UserServiceFacadeLocal userService;
    
    @EJB
    private PostServiceFacadeLocal postService;
    

    @Override
    public Map<String, Object> login(String fbToken, String email, String password) {
        if (fbToken != null) {
            FacebookClient facebookClient = new DefaultFacebookClient(fbToken, Version.LATEST);
            com.restfb.types.User fbuser = facebookClient.fetchObject("me", com.restfb.types.User.class, Parameter.with("fields","id,first_name,last_name,email,gender,birthday,picture"));

            if (fbuser.getEmail() != null && !fbuser.getEmail().isEmpty()) {
                email = fbuser.getEmail();

                String profilePic = null;
                if (fbuser.getPicture() != null)
                    profilePic = fbuser.getPicture().getUrl();

                // Check if user exists
                boolean firstFacebookLogin;
                User user = userService.getUserByEmail(email);
                if (user != null) {
                    if (user.getIsDeleted()) {
                        return new HashMap<String, Object>() {{ put("error", "Failed to login, the email belongs to a deleted user"); }};
                    }

                    if (user.getFbId() != null) {
                        firstFacebookLogin = false;
                    }
                    else {
                        firstFacebookLogin = true;
                        user.setFbId(fbuser.getId());
                    }

                    // Update user data
                    if (fbuser.getFirstName() != null && !fbuser.getFirstName().isEmpty())
                        user.setFirstName(fbuser.getFirstName());
                    if (fbuser.getLastName() != null && !fbuser.getLastName().isEmpty())
                        user.setLastName(fbuser.getLastName());
                    if (fbuser.getGender() != null && !fbuser.getGender().isEmpty())
                        user.setGender(fbuser.getGender());
                    if (fbuser.getBirthday() != null && !fbuser.getBirthday().isEmpty())
                        user.setBirthday(fbuser.getBirthday());
                    if (profilePic != null && !fbuser.getPicture().getIsSilhouette())
                        user.setProfilePic(profilePic);

                    userService.updateUser(user);
                }
                else { // New account
                    firstFacebookLogin = true;

                    user = new User(fbuser.getEmail(),
                                    fbuser.getId(),
                                    null,
                                    fbuser.getFirstName(),
                                    fbuser.getLastName(),
                                    fbuser.getGender(),
                                    fbuser.getBirthday(),
                                    false,
                                    profilePic);

                    if (userService.newUser(user) != 0) {
                        return new HashMap<String, Object>() {{ put("error", "Failed to register user"); }};
                    }
                }

                if (firstFacebookLogin) {
                    retrieveFriends(facebookClient, user);
                }
                syncFacebook(facebookClient, user);
                
                Map<String, Object> result = new HashMap<>();
                result.put("error", "");
                result.put("user", user.getId());
                return result;
            }
            else {
                return new HashMap<String, Object>() {{ put("error", "Facebook did not provide an email address"); }};
            }
        }
        else { // Non-facebook login
            if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                return new HashMap<String, Object>() {{ put("error", "Please provide both an email and a password"); }};
            }

            // Check if user exists
            User user = userService.getUserByEmail(email);
            if (user != null) {
                if (user.getIsDeleted()) {
                    return new HashMap<String, Object>() {{ put("error", "Failed to login, the email belongs to a deleted user"); }};
                }
                if (password.equals(user.getPassword())) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("error", "");
                    result.put("user", user.getId());
                    return result;
                }
                else {
                    return new HashMap<String, Object>() {{ put("error", "Incorrect email or password"); }};
                }
            }
            else { // User did not exist yet
                return new HashMap<String, Object>() {{ put("error", "Incorrect email or password"); }};
            }
        }
    }

    
    private void retrieveFriends(FacebookClient facebookClient, User user) {
        List<com.restfb.types.User> fbfriends = facebookClient.fetchConnection("me/friends", com.restfb.types.User.class, Parameter.with("fields", "id")).getData();
        for (com.restfb.types.User fbfriend : fbfriends) {
            User friend = userService.getUserByFacebookId(fbfriend.getId());
            if (friend != null) {
                user.addFriend(friend);
                friend.addFriend(user);

                userService.updateUser(user);
                userService.updateUser(friend);
            }
        }
    }
    

    private void syncFacebook(FacebookClient facebookClient, User user)
    {
        List<com.restfb.types.Post> fbposts = facebookClient.fetchConnection("me/feed", com.restfb.types.Post.class, Parameter.with("fields","message,from,to,comments,created_time,likes,type,full_picture,link,source")).getData();
        for (com.restfb.types.Post fbpost : fbposts) {
            if (fbpost.getCreatedTime() == null || fbpost.getType() == null || fbpost.getType().isEmpty()) {
                continue;
            }

            User poster = userService.getUserByFacebookId(fbpost.getFrom().getId());
            if (poster != null) {

                // Check if we already have the post (there could still be new comments even if we do)
                Post existingPost = null;
                List<Post> comments = new ArrayList<>();
                List<User> likes = new ArrayList<>();
                for (Post post : postService.getPostsOnWall(user.getId())) {
                    if (post.getTimestamp().equals(fbpost.getCreatedTime())) {
                        existingPost = post;
                        comments = existingPost.getComments();
                        likes = existingPost.getLikes();
                        break;
                    }
                }

                // Gather the comments
                com.restfb.types.Post.Comments fbcomments = fbpost.getComments();
                if (fbcomments != null) {
                    for (Comment fbcommentData : fbcomments.getData()) {
                        com.restfb.types.Post fbcomment = facebookClient.fetchObject(fbcommentData.getId(), com.restfb.types.Post.class, Parameter.with("fields","message,from,comments,created_time,likes"));
                        User commentPoster = userService.getUserByFacebookId(fbcomment.getFrom().getId());
                        if (commentPoster != null && fbcomment.getMessage() != null && !fbcomment.getMessage().isEmpty()) {
                            
                            // Check if we already have the comment (there could still be new subcomments even if we do)
                            Post existingComment = null;
                            List<Post> subcomments = new ArrayList<>();
                            List<User> commentLikes = new ArrayList<>();
                            if (existingPost != null) {
                                for (Post comment : existingPost.getComments()) {
                                    if (comment.getTimestamp().equals(fbcomment.getCreatedTime())) {
                                        existingComment = comment;
                                        subcomments = existingComment.getComments();
                                        commentLikes = existingComment.getLikes();
                                        break;
                                    }
                                }
                            }
                            
                            // Get subcomments
                            if (fbcomment.getComments() != null) {
                                com.restfb.types.Post.Comments fbSubComments = fbcomment.getComments();
                                for (Comment fbSubCommentData : fbSubComments.getData()) {
                                    com.restfb.types.Post fbSubComment = facebookClient.fetchObject(fbSubCommentData.getId(), com.restfb.types.Post.class, Parameter.with("fields","message,from,comments,created_time,likes"));
                                    User subCommentPoster = userService.getUserByFacebookId(fbSubComment.getFrom().getId());
                                    if (subCommentPoster != null && fbSubComment.getMessage() != null && !fbSubComment.getMessage().isEmpty()) {

                                        // Don't duplicate subcomments
                                        Post existingSubComment = null;
                                        List<User> subcommentLikes = new ArrayList<>();
                                        if (existingComment != null) {
                                            for (Post subcomment : existingComment.getComments()) {
                                                if (subcomment.getTimestamp().equals(fbSubComment.getCreatedTime())) {
                                                    existingSubComment = subcomment;
                                                    subcommentLikes = subcomment.getLikes();
                                                    break;
                                                }
                                            }
                                        }
                                        
                                        // Get the likes of the subcomment
                                        Likes fblikes = fbSubComment.getLikes();
                                        if (fblikes != null) {
                                            for (NamedFacebookType fblike : fblikes.getData()) {
                                                User likingUser = userService.getUserByFacebookId(fblike.getId());
                                                if (likingUser != null) {
                                                    if (!subcommentLikes.contains(likingUser)) {
                                                        subcommentLikes.add(likingUser);
                                                    }
                                                }
                                            }
                                        }

                                        if (existingSubComment != null) {
                                            postService.updatePost(existingSubComment);
                                            continue;
                                        }

                                        Post subcomment = new Post(subCommentPoster, null, new ArrayList<>(), new ArrayList<>(), subcommentLikes, fbSubComment.getCreatedTime(), fbSubComment.getMessage());
                                        subcomments.add(subcomment);
                                        postService.newPost(subcomment);
                                    }
                                }
                            }
                            
                            // Get the likes of the comment
                            Likes fblikes = fbcomment.getLikes();
                            if (fblikes != null) {
                                for (NamedFacebookType fblike : fblikes.getData()) {
                                    User likingUser = userService.getUserByFacebookId(fblike.getId());
                                    if (likingUser != null) {
                                        if (!commentLikes.contains(likingUser)) {
                                            commentLikes.add(likingUser);
                                        }
                                    }
                                }
                            }
                            
                            // Don't continue if comment was already imported
                            if (existingComment != null) {
                                postService.updatePost(existingComment);
                                continue;
                            }
                            
                            Post comment = new Post(commentPoster, null, new ArrayList<>(), subcomments, commentLikes, fbcomment.getCreatedTime(), fbcomment.getMessage());
                            comments.add(comment);
                            postService.newPost(comment);
                        }
                    }
                }

                // Gather the likes
                Likes fblikes = fbpost.getLikes();
                if (fblikes != null) {
                    for (NamedFacebookType fblike : fblikes.getData()) {
                        User likingUser = userService.getUserByFacebookId(fblike.getId());
                        if (likingUser != null) {
                            if (!likes.contains(likingUser)) {
                                likes.add(likingUser);
                            }
                        }
                    }
                }
                
                // Don't continue if post was already imported
                if (existingPost != null) {
                    postService.updatePost(existingPost);
                    continue;
                }

                // Gather the mentions in the post
                List<User> mentions = new ArrayList<>();
                for (NamedFacebookType fbmention : fbpost.getTo()) {
                    User existingUser = userService.getUserByFacebookId(fbmention.getId());
                    if (existingUser != null) {
                        mentions.add(existingUser);
                    }
                }

                Post post = new Post(poster, user, mentions, comments, likes, fbpost.getCreatedTime(), fbpost.getMessage());

                if (fbpost.getType().equals("photo") && fbpost.getFullPicture() != null && !fbpost.getFullPicture().isEmpty())
                    post.setPicture(fbpost.getFullPicture());
                if (fbpost.getType().equals("video") && fbpost.getSource() != null && !fbpost.getSource().isEmpty())
                    post.setVideo(fbpost.getSource());
                if (fbpost.getType().equals("link") && fbpost.getLink() != null && !fbpost.getLink().isEmpty())
                    post.setLink(fbpost.getLink());

                postService.newPost(post);
            }
        }
    }
}
