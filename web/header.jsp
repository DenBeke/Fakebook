<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Fakebook</title>
        
        <link href='https://fonts.googleapis.com/css?family=Exo+2' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" type="text/css" class="ui" href="semantic/semantic.min.css">
        <link rel="stylesheet" type="text/css" class="ui" href="style.css">
        <link rel="stylesheet" type="text/css" class="ui" href="lightbox2/dist/css/lightbox.min.css">
        <link rel="stylesheet" type="text/css" class="ui" href="jquery.highlighttextarea.min.css">
        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <script src="semantic/semantic.min.js"></script>
        <script src="jquery.rest.min.js"></script>
        <script src="jquery.viewport.js"></script>
        <script src="Chart.min.js"></script>
        <script src="jquery.highlighttextarea.min.js"></script>
        
        
    </head>
    <body>
        
        <div id="header">

            <div class="ui container">
                <h1>Fakebook</h1>
            </div>

        </div>

        
        <div id="bully-sidebar" class="ui bottom sidebar">
            
            <div id="bully-indicator" class="hidden">
                <h3>Is this post offensive?</h3>
                <h2><span></span></h2>
            </div>
            
        </div>
        
        <div class="">
        
        <div class="ui left demo vertical inverted sidebar labeled icon menu visible">
            <a class="item" href="./">
                <i class="home icon"></i>
                Home
            </a>
            
            <c:choose>
                <c:when test="${empty currentUser}">
                    <a class="item" href="login">
                        <i class="user icon"></i>
                        Login
                    </a>
                    <a class="item" href="register">
                        <i class="add user icon"></i>
                        Register
                    </a>
                </c:when>
                <c:otherwise>
                    <a class="item" href="wall">
                        <i class="smile icon"></i>
                        Wall
                    </a>
                    <a class="item" href="friends">
                        <i class="users icon"></i>
                        Friends
                    </a>
                    <a class="item" href="logout">
                        <i class="power icon"></i>
                        Logout
                    </a>
                </c:otherwise>
            </c:choose>
        </div>