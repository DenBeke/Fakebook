/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import javax.ejb.Local;

@Local
public interface FriendServiceFacadeLocal {

    public String sendFriendshipRequest(Long userFromId, Long userToId);

    public void acceptFriendshipReques(Long userId, Long friendId);

    public void removeFriend(Long userId, Long friendId);
}

