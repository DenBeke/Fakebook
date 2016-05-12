<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
            <h2>Friends</h2>
            <h3>Search users</h3>
            <form action="" method="POST" class="">
                <div class="ui action input">
                    <input name="searched_friend" placeholder="Search..." type="text" value="${searchedFriend}">
                    <button class="ui icon button">
                        <i class="search icon"></i>
                    </button>
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
                                <c:if test="${not user.getId().equals(friend.getId())
                                  and not user.getFriends().contains(friend)
                                  and not user.getFriendshipRequests().contains(friend)
                                  and not friend.getFriendshipRequests().contains(user)}">
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
            
            <c:if test="${not empty user.getFriendshipRequests()}">
                <h3>Friend requests</h3>
                <table class="ui very basic collapsing celled table">
                    <tbody>
                        <c:forEach items="${user.getFriendshipRequests()}" var="friend">
                            <tr>
                                <td>
                                    <a class="avatar">
                                        <img class="ui mini image" src="<c:out value="${friend.getProfilePic()}"/>">
                                    </a>
                                </td>

                                <td><a href="wall?uid=${friend.getId()}"><c:out value="${friend.getName()}"/></a></td>
                                <td>
                                    <form action="" id="accept_friendship_form" method="POST" class="ui form">
                                        <input type="hidden" name="accepted_friend_id" value="${friend.getId()}">
                                        <input type="submit" value="Accept" class="ui button">
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <h3>Current friends</h3>
            <c:if test="${empty user.getFriends()}">
                <p>You don't have any friends yet :(</p>
            </c:if>
            <table class="ui very basic collapsing celled table">
                <tbody>
                    <c:forEach items="${user.getFriends()}" var="friend">
                        <tr>
                            <td>
                                <a class="avatar">
                                    <img class="ui mini image" src="<c:out value="${friend.getProfilePic()}"/>">
                                </a>
                            </td>
                            <td><a href="wall?uid=${friend.getId()}"><c:out value="${friend.getName()}"/></a></td>
                            <td>
                                <form action="" method="POST" class="ui form">
                                    <input type="hidden" name="removed_friend_id" value="${friend.getId()}">
                                    <input type="submit" value="Remove" class="ui teal button">
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        </div>

<jsp:include page="footer.jsp" />