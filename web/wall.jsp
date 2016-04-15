<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Wall</title>
    </head>
    <body>
        <c:if test="${user == -1}">
            <h1>Wall of user</h1>
            <p>Error: The user you tried to access does not exist!</p>
        </c:if>
        <c:if test="${user != -1}">
            <h1>Wall of user ${user}</h1>

            <c:if test="${empty posts}"><p>This user has no posts on his wall yet.</p></c:if>

            <c:forEach items="${posts}" var="post">
                <pre><c:out value="${post.getText()}" escapeXml="true"/></pre>
                Posted by: <a href="wall?uid=${post.getPoster().getId()}"><c:out value="${post.getPoster().getFirstName()} ${post.getPoster().getLastName()} <${post.getPoster().getEmail()}>" escapeXml="true"/></a><br>
                Timestamp: <c:out value="${post.getTimestamp()}" escapeXml="true"/><br>
                <c:if test="${not empty post.getMentioned()}">
                    Mentioned:
                    <c:forEach items="${post.getMentioned()}" var="mentionedUser">
                        <c:out value="${mentionedUser.getFirstName()} ${mentionedUser.getLastName()} <${mentionedUser.getEmail()}>" escapeXml="true"/><br>
                    </c:forEach>
                </c:if>
                <c:if test="${not empty post.getComments()}">
                    Comments:
                    <c:forEach items="${post.getComments()}" var="comment">
                        <pre><c:out value="${comment.getText()}" escapeXml="true"/></pre>
                        Comment posted by: <a href="wall?uid=${comment.getPoster().getId()}"><c:out value="${comment.getPoster().getFirstName()} ${comment.getPoster().getLastName()} <${comment.getPoster().getEmail()}>" escapeXml="true"/></a><br>
                        Comment timestamp: <c:out value="${comment.getTimestamp()}" escapeXml="true"/><br>
                        <br>
                    </c:forEach>
                </c:if>
                <br><br>
            </c:forEach>
        </c:if>
    </body>
</html>
