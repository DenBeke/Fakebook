<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login page</title>
        
        <link rel="stylesheet" type="text/css" class="ui" href="semantic/semantic.min.css">
        
        <script>
            window.fbAsyncInit = function() {
                FB.init({
                  appId      : '1011216375625925',
                  cookie     : true,  // enable cookies to allow the server to access the session
                  xfbml      : true,  // parse social plugins on this page
                  version    : 'v2.5' // use graph api version 2.5
                });
            };
            
            // Load the SDK asynchronously
            (function(d, s, id) {
              var js, fjs = d.getElementsByTagName(s)[0];
              if (d.getElementById(id)) return;
              js = d.createElement(s); js.id = id;
              js.src = "//connect.facebook.net/en_US/sdk.js";
              fjs.parentNode.insertBefore(js, fjs);
            }(document, 'script', 'facebook-jssdk'));
            
            function checkLoginState() {
              FB.getLoginStatus(function(response) {
                fblogin(response);
              });
            }

            function fblogin(response) {
                if (response.status === 'connected') {
                    accessToken = response.authResponse.accessToken;
                    FB.api('/me', { access_token: accessToken }, function(response) {

                        var form = document.createElement('form');
                        form.hidden = true;
                        form.method = 'post';
                        form.action = 'login';
 
                        var input = document.createElement('input');
                        input.type = "text";
                        input.name = "fbToken";
                        input.value = accessToken;
                        form.appendChild(input);

                        document.body.appendChild(form);
                        form.submit();
                    });
                }
                else {
                    // TODO: Error (status === 'not_authorized' or something else)
                }
            }
          </script>
    </head>
    <body>
        
        <p></p>
        
        <div id="header" class="ui container">
            <div class="ui menu">
                <div class="header item">
                    Fakebook
                </div>
                <a class="item">
                    Home
                </a>
                <a class="item">
                    Login
                </a>
            </div>
        </div>
        
        <div class="ui container">
            
            <h2></h2>
            
            <h2 class="ui dividing header">Login</h2>
            
            <h2></h2>

            <c:if test="${not empty error}"><p style="background: red; color: white;"><b>${error}</b></p></c:if>

            
            <div class="ui three column very relaxed grid">
            
                <div class="eight wide column">
                
                <form action="login" method="POST" class="ui form">

                    <div class="field">
                        <label>Email</label>
                        <input type="text" name="email" value="${email}" />
                    </div>

                    <div class="field">
                        <label>Password</label>
                        <input type="password" name="password" value="${password}" />
                    </div>

                    <input type="submit" name="action" class="btn btn-default ui primary button" value="Login" />
                    <a href="register" class="ui button">Register</a>
                </form>
                
                </div>
                       

                <div class="eight wide column">
                        
                    <fb:login-button class="" scope="public_profile,user_birthday,email,user_posts" onlogin="checkLoginState();"></fb:login-button>
                    
                </div>
            
        </div>
            
    </body>
</html>
