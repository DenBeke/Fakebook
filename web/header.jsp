<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Fakebook</title>
        
        <link rel="stylesheet" type="text/css" class="ui" href="semantic/semantic.min.css">
        <link rel="stylesheet" type="text/css" class="ui" href="style.css">
        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <script src="semantic/semantic.min.js"></script>
        
    </head>
    <body>
        
        <div id="header">

            <div class="ui container">
                <h1>Fakebook</h1>
            </div>

        </div>
        
        <div class="ui left demo vertical inverted sidebar labeled icon menu visible">
            <a class="item" href="./">
                <i class="home icon"></i>
                Home
            </a>
            
            <c:if test="${empty currentUser}">
                <a class="item" href="login">
                    <i class="user icon"></i>
                    Login
                </a>
                <a class="item" href="register">
                    <i class="add user icon"></i>
                    Register
                </a>
            </c:if>
            
            <c:if test="${!empty currentUser}">
                <a class="item" href="login">
                    <i class="smile icon"></i>
                    Wall
                </a>
                <a class="item" href="register">
                    <i class="users icon"></i>
                    Friends
                </a>
            </c:if>
            
        </div>