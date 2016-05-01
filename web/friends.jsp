<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
            <h1>Friends</h1>
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

                <h3>Friends</h3>
            </c:if>
            <c:if test="${empty currentUser.getFriends()}">
                <p>You don't have any friends yet :(</p>
            </c:if>
            <c:forEach items="${currentUser.getFriends()}" var="friend">
                <a href="wall?uid=${friend.getId()}"><c:out value="${friend.getName()}"/></a>
            </c:forEach>

        </div>

<jsp:include page="footer.jsp" />