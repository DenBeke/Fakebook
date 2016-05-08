<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
            <h2>Friends</h2>
            <h3>Search users</h3>
            <!-- TODO: Make visually appealing + don't redirect? -->
            <form action="" method="POST" class="">
                <div class="ui action input">
                    <!--<input type="text" placeholder="Search...">-->
  
                    <input name="searched_friend" placeholder="Search..." type="text" value="${searchedFriend}">
                    <button class="ui icon button">
                        <i class="search icon"></i>
                    </button>
                    <!--<input type="submit" value="Search" class="ui button">-->
                </div>
            </form>
            <table class="ui very basic collapsing celled table">
                <tbody>
                    <c:forEach items="${searchResult}" var="friend">
                        <tr>
                            <td>
                                <a class="avatar">
                                    <img class="ui mini image" src="<c:out value="${friend.getProfilePic()}"/>">
                                </a>
                            </td>
                            <td><a href="wall?uid=${friend.getId()}"><c:out value="${friend.getName()}"/></a></td>
                            <td>
                                <c:if test="${not currentUser.getId().equals(friend.getId())
                                  and not currentUser.getFriends().contains(friend)
                                  and not currentUser.getFriendshipRequests().contains(friend)
                                  and not friend.getFriendshipRequests().contains(currentUser)}">
                                    <form action="friend-request" id="send_friendship_form" method="POST" class="ui form">
                                        <input type="hidden" name="friend_user_id" value="${friend.getId()}">
                                        <input type="submit" value="Send friend request" class="ui button">
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <c:if test="${not empty currentUser.getFriendshipRequests()}">
                <h3>Friend requests</h3>
                <table class="ui very basic collapsing celled table">
                    <tbody>
                        <c:forEach items="${currentUser.getFriendshipRequests()}" var="friend">
                            <tr>
                                <td>
                                    <a class="avatar">
                                        <img class="ui mini image" src="<c:out value="${friend.getProfilePic()}"/>">
                                    </a>
                                </td>

                                <td><a href="wall?uid=${friend.getId()}"><c:out value="${friend.getName()}"/></a></td>
                                <!-- TODO: Don't redirect + make visually appealing -->
                                <td><form action="" id="accept_friendship_form" method="POST" class="ui form">
                                        <input type="hidden" name="accepted_friend_id" value="${friend.getId()}">
                                        <input type="submit" value="Accept" class="ui button">
                                    </form></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <h3>Current friends</h3>
            <c:if test="${empty currentUser.getFriends()}">
                <p>You don't have any friends yet :(</p>
            </c:if>
            <table class="ui very basic collapsing celled table">
                <tbody>
                    <c:forEach items="${currentUser.getFriends()}" var="friend">
                        <tr>
                            <td>
                                <a class="avatar">
                                    <img class="ui mini image" src="<c:out value="${friend.getProfilePic()}"/>">
                                </a>
                            </td>
                            <td><a href="wall?uid=${friend.getId()}"><c:out value="${friend.getName()}"/></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        </div>

<jsp:include page="footer.jsp" />