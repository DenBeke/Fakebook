<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Wall</title>
    </head>
    <body>
        <h1>Wall of user ${user}</h1>
        
        <c:if test="${empty posts}"><p>This user has no posts on his wall yet.</p></c:if>
        
        <c:forEach items="${posts}" var="post">
            <p><c:out value="${post.getText()}" escapeXml="true"/></p>
        </c:forEach>
    </body>
</html>
