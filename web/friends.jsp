<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
            <h1>Friends</h1>
            <h3>Search users</h3>
            <!-- TODO: Make visually appealing + don't redirect? -->
            <form action="" method="POST" class="ui form">
                <div class="field">
                    <input name="searched_friend" type="text" value="${searchedFriend}">
                </div>
                <input type="submit" value="Search" class="ui button">
            </form>
            <c:forEach items="${searchResult}" var="friend">
                <p><a href="wall?uid=${friend.getId()}"><c:out value="${friend.getName()}"/></a></p>
                <c:if test="${not currentUser.getId().equals(friend.getId())
                          and not currentUser.getFriends().contains(friend)
                          and not currentUser.getFriendshipRequests().contains(friend)
                          and not friend.getFriendshipRequests().contains(currentUser)}">
                    <form action="friend-request" id="send_friendship_form" method="POST" class="ui form">
                        <input type="hidden" name="friend_user_id" value="${friend.getId()}">
                        <input type="submit" value="Send friend request" class="ui button">
                    </form>
                </c:if>
            </c:forEach>
            
            <c:if test="${not empty currentUser.getFriendshipRequests()}">
                <h3>Friend requests</h3>
                <c:forEach items="${currentUser.getFriendshipRequests()}" var="friend">
                    <a href="wall?uid=${friend.getId()}"><c:out value="${friend.getName()}"/></a>
                    <!-- TODO: Don't redirect + make visually appealing -->
                    <form action="" id="accept_friendship_form" method="POST" class="ui form">
                        <input type="hidden" name="accepted_friend_id" value="${friend.getId()}">
                        <input type="submit" value="Accept" class="ui button">
                    </form>
                <br>
                </c:forEach>
            </c:if>
            <h3>Current friends</h3>
            <c:if test="${empty currentUser.getFriends()}">
                <p>You don't have any friends yet :(</p>
            </c:if>
            <c:forEach items="${currentUser.getFriends()}" var="friend">
                <a href="wall?uid=${friend.getId()}"><c:out value="${friend.getName()}"/></a>
            </c:forEach>

        </div>

<jsp:include page="footer.jsp" />