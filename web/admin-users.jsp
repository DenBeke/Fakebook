<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
            <h2>Users</h2>
            <h3>Online</h3>
            <c:if test="${empty onlineUsers}">
                <p>No users are currently online.</p>
            </c:if>
            <c:forEach items="${onlineUsers}" var="user">
                <p><a href="wall?uid=${user.getId()}"><c:out value="${user.getName()}"/></a></p>
            </c:forEach>
            <h3>All users</h3>
            <c:forEach items="${allUsers}" var="user">
                <p><a href="wall?uid=${user.getId()}"><c:out value="${user.getName()}"/></a></p>
            </c:forEach>

        </div>

<jsp:include page="footer.jsp" />