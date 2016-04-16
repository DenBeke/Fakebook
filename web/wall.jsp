<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">

            <c:if test="${user == -1}">
                <h1>Wall of user</h1>
                <p>Error: The user you tried to access does not exist!</p>
            </c:if>
            <c:if test="${user != -1}">
                <h1>Wall of user ${user}</h1>


                <form action="?uid=${user}" id="wall_form" method="POST" class="ui form">
                    <div class="field">
                        <textarea name="new_wall_post"></textarea>
                    </div>
                    <input type="hidden" name="wall_user_id" value="${user}">
                    <input type="submit" value="Write on wall" class="ui primary button">
                </form>


                <c:if test="${empty posts}"><p>This user has no posts on his wall yet.</p></c:if>

                
                    <div class="ui comments">
                    <c:forEach items="${posts}" var="post">

                        <div class="comment">
                            <a class="avatar">
                                <img src="https://www.kirkleescollege.ac.uk/wp-content/uploads/2015/09/default-avatar.png">
                            </a>
                            <div class="content">
                                <a class="author" href="wall?uid=${post.getPoster().getId()}"><c:out value="${post.getPoster().getFirstName()} ${post.getPoster().getLastName()} <${post.getPoster().getEmail()}>" escapeXml="true"/></a>
                                <div class="metadata">
                                    <span class="date"><c:out value="${post.getTimestamp()}" escapeXml="true"/></span>
                                </div>
                                <div class="text">
                                    <c:out value="${post.getText()}" escapeXml="true"/>
                                </div>
                                <div class="actions">
                                    <a class="reply">Reply</a>
                                </div>
                            </div>
                                
                            <c:if test="${not empty post.getComments()}">
                            <div class="comments">
                            <c:forEach items="${post.getComments()}" var="comment">
                                
                                <div class="comment">
                                    <a class="avatar">
                                        <img src="https://www.kirkleescollege.ac.uk/wp-content/uploads/2015/09/default-avatar.png">
                                    </a>
                                    <div class="content">
                                        <a class="author" href="wall?uid=${comment.getPoster().getId()}"><c:out value="${comment.getPoster().getFirstName()} ${comment.getPoster().getLastName()} <${comment.getPoster().getEmail()}>" escapeXml="true"/></a>
                                        <div class="metadata">
                                            <span class="date"><c:out value="${comment.getTimestamp()}" escapeXml="true"/></span>
                                        </div>
                                        <div class="text">
                                            <c:out value="${comment.getText()}" escapeXml="true"/>
                                        </div>
                                        <div class="actions">
                                            <a class="reply">Reply</a>
                                        </div>
                                    </div>

                                        
                                            <c:if test="${not empty comment.getComments()}">
                                                <div class="comments">
                                                    <c:forEach items="${comment.getComments()}" var="subComment">

                                                        <div class="comment">
                                                            <a class="avatar">
                                                                <img src="https://www.kirkleescollege.ac.uk/wp-content/uploads/2015/09/default-avatar.png">
                                                            </a>
                                                            <div class="content">
                                                                <a class="author" href="wall?uid=${subComment.getPoster().getId()}"><c:out value="${subComment.getPoster().getFirstName()} ${subComment.getPoster().getLastName()} <${subComment.getPoster().getEmail()}>" escapeXml="true"/></a>
                                                                <div class="metadata">
                                                                    <span class="date"><c:out value="${subComment.getTimestamp()}" escapeXml="true"/></span>
                                                                </div>
                                                                <div class="text">
                                                                    <c:out value="${subComment.getText()}" escapeXml="true"/>
                                                                </div>
                                                            </div>

                                                        </div>

                                                    </c:forEach>

                                                </div>


                                            </c:if>
                                        
                                </div>
                                
                            </c:forEach>
                                
                            </div>
                                
                        </c:if>
                                
                        </div>


                        <!--
                        <pre><c:out value="${post.getText()}" escapeXml="true"/></pre>
                        Posted by: <a href="wall?uid=${post.getPoster().getId()}"><c:out value="${post.getPoster().getFirstName()} ${post.getPoster().getLastName()} <${post.getPoster().getEmail()}>" escapeXml="true"/></a><br>
                        Timestamp: <c:out value="${post.getTimestamp()}" escapeXml="true"/><br>
                        <c:if test="${not empty post.getMentioned()}">
                            Mentioned:
                            <c:forEach items="${post.getMentioned()}" var="mentionedUser">
                                <a href="wall?uid=${mentionedUser.getId()}"><c:out value="${mentionedUser.getFirstName()} ${mentionedUser.getLastName()} <${mentionedUser.getEmail()}>" escapeXml="true"/></a><br>
                            </c:forEach>
                        </c:if>
                        <c:if test="${not empty post.getComments()}">
                            Comments:
                            <c:forEach items="${post.getComments()}" var="comment">
                                <pre><c:out value="${comment.getText()}" escapeXml="true"/></pre>
                                Comment posted by: <a href="wall?uid=${comment.getPoster().getId()}"><c:out value="${comment.getPoster().getFirstName()} ${comment.getPoster().getLastName()} <${comment.getPoster().getEmail()}>" escapeXml="true"/></a><br>
                                Comment timestamp: <c:out value="${comment.getTimestamp()}" escapeXml="true"/><br>
                                <c:if test="${not empty comment.getComments()}">
                                    Sub-comments:
                                    <c:forEach items="${comment.getComments()}" var="subcomment">
                                        <pre><c:out value="${subcomment.getText()}" escapeXml="true"/></pre>
                                        Sub-comment posted by: <a href="wall?uid=${subcomment.getPoster().getId()}"><c:out value="${subcomment.getPoster().getFirstName()} ${subcomment.getPoster().getLastName()} <${subcomment.getPoster().getEmail()}>" escapeXml="true"/></a><br>
                                        Sub-comment timestamp: <c:out value="${subcomment.getTimestamp()}" escapeXml="true"/><br>
                                        <br>
                                    </c:forEach>
                                </c:if>
                                <br>
                            </c:forEach>
                        </c:if>
                        -->
                        <!--<br><br>-->
                    </c:forEach>

                </div>
            </c:if>
                                    
        </div>

<jsp:include page="footer.jsp" />