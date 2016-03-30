<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
    </head>
    <body>
        <h1>Register</h1>

        <c:if test="${not empty error}"><p style="background: red; color: white;"><b>${error}</b></p></c:if>
        
        <form action="register" method="POST">
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
    </body>
</html>
