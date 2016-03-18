<%-- 
    Document   : index
    Created on : Mar 17, 2016, 9:28:23 PM
    Author     : robin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hello</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        
        <form action="./UserServlet" method="POST">
            <table>
                <tr>
                    <td>User Id</td>
                    <td><input type="text" name="userId" value="${user.userId}" /></td>
                </tr>
                <tr>
                    <td>Name</td>
                    <td><input type="text" name="name" value="${user.name}" /></td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td><input type="text" name="password" value="${user.password}" /></td>
                </tr>
                <tr>
                    <td>email</td>
                    <td><input type="text" name="email" value="${user.email}" /></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" name="action" class="btn btn-default" value="Add" />
                        <input type="submit" name="action" value="Edit" class="btn btn-default" />
                        <input type="submit" name="action" value="Delete" class="btn btn-default" />
                        <input type="submit" name="action" value="Search" class="btn btn-default" />
                    </td>                
                </tr>            
            </table>
        </form>        
        <table border="1">
            <th>ID</th>
            <th>Name</th>
            <th>Password</th>
            <th>Email</th>
                <c:forEach items="${allUsers}" var="u">
                <tr>
                    <td>${u.getUserId()}</td>
                    <td>${u.name}</td>
                    <td>${u.password}</td>
                    <td>${u.email}</td>
                </tr>
                </c:forEach>
        </table>  
    </body>
</html>
