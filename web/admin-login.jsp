<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <div class="ui container">
            
            <h2 class="ui dividing header">Admin Login</h2>

            <c:if test="${not empty error}"><p style="background: red; color: white;"><b>${error}</b></p></c:if>

            <div class="ui three column very relaxed grid">
            
                <div class="eight wide column">
                
                <form action="admin" method="POST" class="ui form">

                    <div class="field">
                        <label>Email</label>
                        <input type="text" name="adminLoginEmail" value="${email}" />
                    </div>

                    <div class="field">
                        <label>Password</label>
                        <input type="password" name="adminLoginPassword" value="${password}" />
                    </div>

                    <input type="submit" name="action" class="ui teal button" value="Admin Login" />
                </form>
                
                </div>
        </div>

<jsp:include page="footer.jsp" />