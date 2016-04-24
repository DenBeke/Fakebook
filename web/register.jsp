<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
        
        <h2 class="ui dividing header">Register</h2>

        <c:if test="${not empty error}">
            <!--<p style="background: red; color: white;"><b>${error}</b></p>-->
            <div class="ui negative message">
                <!--<i class="close icon"></i>-->
                <div class="header">
                    Oops, something went wrong with your registration
                </div>
                <p>${error}</p>
            </div>
        </c:if>
        
            <div class="ui three column very relaxed grid">

                <div class="eight wide column">


                    <form action="register" method="POST" class="ui form">
                        <div class="field">
                            <label>Email *</label>
                            <input name="email" type="email" placeholder="john@doe.com" value="${email}">
                    </div>
                    <div class="field">
                        <label>Password *</label>
                        <input name="password" type="password" placeholder="*******" value="${password}">
                    </div>
                    <div class="field">
                        <label>First Name *</label>
                        <input name="firstName" type="text" placeholder="John" value="${firtName}">
                    </div>
                    <div class="field">
                        <label>Last Name</label>
                        <input name="lastName" type="text" placeholder="Doe" value="${lastName}">
                    </div>
                    <div class="field">
                        <label>Gender</label>
                        <input name="gender" type="text" placeholder="Male" value="${gender}">
                    </div>
                    <div class="field">
                        <label>Birthday</label>
                        <input name="birthday" type="text" placeholder="9/05/1994" value="${birthday}">
                    </div>
                    <div class="field">
                        <input type="submit" name="action" class="ui teal button" value="Register" />
                    </div>
                </form>

                <br>
                    
                <p>Already have an account? Go to <a href="login">login page</a>.</p>

            </div>
        </div>

<jsp:include page="footer.jsp" />
