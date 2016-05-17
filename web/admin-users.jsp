<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <jsp:include page="admin-header.jsp" />

        <div class="ui container">
            
            
            <h2>Users</h2>
            <h3>Create new users</h3>
            <c:if test="${not empty error}"><p style="background: red; color: white;"><b>${error}</b></p></c:if>
            <a onclick="if ($(this).next('form').is(':hidden')) { $(this).next('form').show() } else { $(this).next('form').hide(); } $(this).hide();">Create a new user</a>
            <form action="" method="POST" class="ui form" hidden>
                <div class="field">
                    <label>Email *</label>
                    <input name="new_user_email" type="email" placeholder="john@doe.com" value="${newUserEmail}">
                </div>
                <div class="field">
                    <label>Password *</label>
                    <input name="new_user_password" type="password" placeholder="*******" value="${newUserPassword}">
                </div>
                <div class="field">
                    <label>First Name *</label>
                    <input name="new_user_firstName" type="text" placeholder="John" value="${newUserFirstName}">
                </div>
                <div class="field">
                    <label>Last Name</label>
                    <input name="new_user_lastName" type="text" placeholder="Doe" value="${newUserLastName}">
                </div>
                <div class="field">
                    <label>Gender</label>
                    <input name="new_user_gender" type="text" placeholder="Male" value="${newUserGender}">
                </div>
                <div class="field">
                    <label>Birthday</label>
                    <input name="new_user_birthday" type="text" placeholder="9/05/1994" value="${newUserBirthday}">
                </div>
                <div class="field">
                    <c:choose>
                        <c:when test="${newUserAdmin eq 'true'}">
                            <input name="new_user_admin" type="checkbox" value="true" checked> Admin
                        </c:when>
                        <c:otherwise>
                            <input name="new_user_admin" type="checkbox" value="true"> Admin
                        </c:otherwise>
                    </c:choose>
                </div>
                <input type="submit" value="Create user" class="ui teal button">
            </form>
            <h3>Online users</h3>
            <c:if test="${empty onlineUsers}">
                <p>No users are currently online.</p>
            </c:if>
            <table class="ui very basic collapsing celled table">
            <tbody>
                <c:forEach items="${onlineUsers}" var="user">
                    <tr>
                        <td>
                            <a class="avatar">
                                <img class="ui mini image" src="<c:out value="${user.getProfilePic()}"/>">
                            </a>
                        </td>

                        <td><a href="wall?uid=${user.getId()}"><c:out value="${user.getName()}"/></a></td>
                        <td>
                            <form action="" method="POST" class="ui form">
                                <input type="hidden" name="download_user_id" value="${user.getId()}">
                                <input type="submit" value="Download data" class="ui teal button">
                            </form>
                        </td>
                        <td>
                            <c:if test="${currentUser != user.getId()}">
                                <form action="" method="POST" class="ui form">
                                    <input type="hidden" name="deleted_user_id" value="${user.getId()}">
                                    <input type="submit" value="Delete" class="ui teal button">
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
            </table>

            <h3>All users</h3>
            <table class="ui very basic collapsing celled table">
            <tbody>
                <c:forEach items="${allUsers}" var="user">
                    <tr>
                        <td>
                            <a class="avatar">
                                <img class="ui mini image" src="<c:out value="${user.getProfilePic()}"/>">
                            </a>
                        </td>

                        <td><a href="wall?uid=${user.getId()}"><c:out value="${user.getName()}"/></a></td>
                        <td>
                            <form action="" method="POST" class="ui form">
                                <input type="hidden" name="download_user_id" value="${user.getId()}">
                                <input type="submit" value="Download data" class="ui teal button">
                            </form>
                        </td>
                        <td>
                            <c:if test="${currentUser != user.getId()}">
                                <form action="" method="POST" class="ui form">
                                    <input type="hidden" name="deleted_user_id" value="${user.getId()}">
                                    <input type="submit" value="Delete" class="ui teal button">
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
            </table>

        </div>

<jsp:include page="footer.jsp" />