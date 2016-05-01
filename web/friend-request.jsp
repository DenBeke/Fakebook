<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
            
            <c:choose>
                <c:when test="${not empty error}">
                    <h1>Sending friend request</h1>
                    <p style="background: red; color: white;"><b><c:out value="${error}"/></b></p>
                </c:when>
                <c:otherwise>
                    <h1>Friend request with <c:out value="${userName}"/></h1>
                    <p>Your friend request has been successfully send to <a href="wall?uid=${userId}"><c:out value="${userName}"/></a></p>
                </c:otherwise>
            </c:choose>
        </div>

<jsp:include page="footer.jsp" />