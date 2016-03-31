<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login page</title>
        
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
                    FB.api('/me', { access_token: accessToken, fields: 'first_name,last_name,email,gender,birthday' }, function(response) {

                        var form = document.createElement('form');
                        form.hidden = true;
                        form.method = 'post';
                        form.action = 'login';
 
                        var input = document.createElement('input');
                        input.type = "text";
                        input.name = "fbToken";
                        input.value = accessToken;
                        form.appendChild(input);
                        
                        input = document.createElement('input');
                        input.type = "text";
                        input.name = "firstName";
                        input.value = response['first_name'];
                        form.appendChild(input);
                        
                        input = document.createElement('input');
                        input.type = "text";
                        input.name = "lastName";
                        input.value = response['last_name'];
                        form.appendChild(input);
                        
                        input = document.createElement('input');
                        input.type = "text";
                        input.name = "email";
                        input.value = response['email'];
                        form.appendChild(input);
                        
                        input = document.createElement('input');
                        input.type = "text";
                        input.name = "gender";
                        input.value = response['gender'];
                        form.appendChild(input);
                        
                        input = document.createElement('input');
                        input.type = "text";
                        input.name = "birthday";
                        input.value = response['birthday'];
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
        <h1>Login</h1>

        <c:if test="${not empty error}"><p style="background: red; color: white;"><b>${error}</b></p></c:if>
        
        <form action="login" method="POST">
            <table>
                <tr>
                    <td>Email</td>
                    <td><input type="text" name="email" value="${email}" /></td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td><input type="password" name="password" value="${password}" /></td>
                </tr>
            </table>
            <input type="submit" name="action" class="btn btn-default" value="Login" />
        </form>

        <p><a href="register">Register</a></p>
        
        <fb:login-button scope="public_profile,user_birthday,email" onlogin="checkLoginState();"></fb:login-button>
    </body>
</html>
