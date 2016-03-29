<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login page</title>
    </head>
    <body>
        <h1>Login</h1>

        <form action="./ProcessLogin" method="POST">
            <table>
                <tr>
                    <td>Email</td>
                    <td><input type="text" name="email" value="${user.email}" /></td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td><input type="text" name="password" value="${user.password}" /></td>
                </tr>
            </table>
            <input type="submit" name="action" class="btn btn-default" value="Login" />
        </form>
    </body>
</html>
