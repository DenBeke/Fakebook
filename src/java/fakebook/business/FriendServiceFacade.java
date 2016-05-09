/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import javax.ejb.Stateless;
import fakebook.persistence.User;
import javax.ejb.EJB;

/**
 *
 * @author robin
 */
@Stateless
public class FriendServiceFacade implements FriendServiceFacadeLocal {
    
    @EJB
    private UserServiceFacadeLocal userService;

    @Override
    public String sendFriendshipRequest(Long userFromId, Long userToId) {
        if (!userToId.equals(userFromId)) {
            User userFrom = userService.getUser(userFromId);
            User userTo = userService.getUser(userToId);

            if (userTo != null) {
                if (!userTo.getFriends().contains(userFrom)) {
                    if (!userTo.getFriendshipRequests().contains(userFrom)) {
                        userTo.getFriendshipRequests().add(userFrom);
                        userService.updateUser(userTo);
                    }
                    return "";
                }
                else {
                    return "You are already friends!";
                }
            }
            else {
                return "The user you are trying to become friends with does not exist!";
            }
        }
        else {
            return "You can't send a friend request to yourself!";
        }
    }
    
    @Override
    public void acceptFriendshipReques(Long userId, Long friendId) {
        User user = userService.getUser(userId);
        User friend = userService.getUser(friendId);

        if (friend != null) {
            if (user.getFriendshipRequests().contains(friend)) {
                user.getFriendshipRequests().remove(friend);

                if (friend.getFriendshipRequests().contains(user)) {
                    friend.getFriendshipRequests().remove(user);
                }

                if (!user.getFriends().contains(friend)) {
                    user.getFriends().add(friend);
                }
                if (!friend.getFriends().contains(user)) {
                    friend.getFriends().add(user);
                }

                userService.updateUser(user);
                userService.updateUser(friend);
            }
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        User user = userService.getUser(userId);
        User friend = userService.getUser(friendId);

        if (user != null && friend != null) {
            if (user.getFriends().contains(friend)) {
                user.getFriends().remove(friend);
            }
            if (friend.getFriends().contains(user)) {
                friend.getFriends().remove(user);
            }

            userService.updateUser(user);
            userService.updateUser(friend);
        }
    }
}
