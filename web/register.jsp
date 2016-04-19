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
                            <label>Email</label>
                            <input name="email" type="email" placeholder="john@doe.com" value="${email}">
                    </div>
                    <div class="field">
                        <label>Password</label>
                        <input name="password" type="password" placeholder="*******" value="${password}">
                    </div>
                    <div class="field">
                        <label>First Name</label>
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
                        <input type="submit" name="action" class="ui primary button" value="Register" />
                    </div>
                </form>

                <br>
                    
                <p>Already have an account? Go to <a href="login">login page</a>.</p>

            </div>
        </div>
        
        
        
        <!--<form action="register" method="POST">
            <table>
                <tr><td>Email</td><td><input type="text" name="email" value="${email}" />*</td></tr>
                <tr><td>Password</td><td><input type="password" name="password" value="${password}" />*</td></tr>
                <tr><td>First Name</td><td><input type="text" name="firstName" value="${firstName}" /></td></tr>
                <tr><td>Last Name</td><td><input type="text" name="lastName" value="${lastName}" /></td></tr>
                <tr><td>Gender</td><td><input type="text" name="gender" value="${gender}" /></td></tr>
                <tr><td>Birthday</td><td><input type="text" name="birthday" value="${birthday}" /></td></tr>
            </table>
            <input type="submit" name="action" class="btn btn-default" value="Register" />
        </form>
        <p>Already have an account? Go to <a href="login">login page</a>.</p>
        
        </div>-->
        
<jsp:include page="footer.jsp" />
