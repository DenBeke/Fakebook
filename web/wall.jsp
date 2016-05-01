<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
            
            <c:choose>
            <c:when test="${user == -1}">
                <h1>User wall</h1>
                <p>Error: The user you tried to access does not exist!</p>
            </c:when>
            <c:when test="${not friends}">
                <h1>Wall of ${userName}</h1>
                <p>You do not have permission to view this wall!</p>
                <p>You need to be friends with ${userName} to access the wall.</p>

                <form action="friend-request" id="friend_request_form" method="POST" class="ui form">
                    <input type="hidden" name="friend_user_id" value="${user}">
                    <input type="submit" value="Send friend request" class="ui teal button">
                </form>
            </c:when>
            <c:otherwise>
                
                <h2 class="ui dividing header">Wall of ${userName}</h2>

                <form action="?uid=${user}" id="wall_form" method="POST" class="ui form">
                    <div class="field">
                        <textarea name="new_wall_post"></textarea>
                    </div>
                    <input type="hidden" name="wall_user_id" value="${user}">
                    <input type="submit" value="Write on wall" class="ui teal button">
                </form>


                <c:if test="${empty posts}"><p>This user has no posts on his wall yet.</p></c:if>

                
                <div class="ui comments">
                    <c:forEach items="${posts}" var="post">

                        <div class="comment">
                            <a class="avatar">
                                <img src="<c:out value="${post.getPoster().getProfilePic()}"/>">
                            </a>
                            <div class="content">
                                <a class="author" href="wall?uid=${post.getPoster().getId()}"><c:out value="${post.getPoster().getName()}"/></a>
                                <div class="metadata">
                                    <span class="date"><c:out value="${post.getTimestamp()}"/></span>
                                </div>
                                <div class="text">
                                    <c:if test="${not empty post.getText()}">
                                        <pre><c:out value="${post.getText()}"/></pre>
                                    </c:if>

                                    <c:if test="${post.getType() eq 'link' && not empty post.getLink()}">
                                        <p><a href="<c:out value="${post.getLink()}"/>"><c:out value="${post.getLink()}"/></a></p>
                                    </c:if>
                                    <c:if test="${post.getType() eq 'picture' && not empty post.getPicture()}">
                                        <a href="<c:out value="${post.getPicture()}"/>" data-lightbox="image-1" data-title="<c:out value="${post.getText()}"/>"><img class="ui rounded image" src="<c:out value="${post.getPicture()}"/>" /></a>
                                    </c:if>
                                    <c:if test="${post.getType() eq 'video' && not empty post.getVideo()}">
                                         <video controls>
                                            <source src="<c:out value="${post.getVideo()}"/>">
                                            <a href="<c:out value="${post.getVideo()}"/>"><c:out value="${post.getVideo()}"/></a>
                                          </video>
                                    </c:if>
                                    
                                    <c:if test="${not empty post.getLikes()}">
                                    Likes: <c:out value="${post.getLikes().size()}"/>
                                    </c:if>
                                </div>
                                <div class="actions">
                                    <a class="reply">Reply</a>
                                    <!-- TODO: Don't redirect + make visually appealing -->
                                    <form action="?uid=${user}" id="wall_like_post" method="POST" class="ui form">
                                        <input type="hidden" name="liked_post_id" value="${post.getId()}">
                                        <input type="submit" value="Like" class="ui button">
                                    </form>
                                </div>
                            </div>
                                
                            <c:if test="${not empty post.getComments()}">
                            <div class="comments">
                            <c:forEach items="${post.getComments()}" var="comment">
                                
                                <div class="comment">
                                    <a class="avatar">
                                        <img src="${comment.getPoster().getProfilePic()}">
                                    </a>
                                    <div class="content">
                                        <a class="author" href="wall?uid=${comment.getPoster().getId()}"><c:out value="${comment.getPoster().getName()}"/></a>
                                        <div class="metadata">
                                            <span class="date"><c:out value="${comment.getTimestamp()}"/></span>
                                        </div>
                                        <div class="text">
                                            <pre><c:out value="${comment.getText()}"/></pre>
                                            <c:if test="${not empty comment.getLikes()}">
                                            Likes: <c:out value="${comment.getLikes().size()}"/>
                                            </c:if>
                                        </div>
                                        <div class="actions">
                                            <a class="reply">Reply</a>
                                            <!-- TODO: Don't redirect + make visually appealing -->
                                            <form action="?uid=${user}" id="wall_like_post" method="POST" class="ui form">
                                                <input type="hidden" name="liked_post_id" value="${comment.getId()}">
                                                <input type="submit" value="Like" class="ui button">
                                            </form>
                                        </div>
                                    </div>

                                    <c:if test="${not empty comment.getComments()}">
                                        <div class="comments">
                                            <c:forEach items="${comment.getComments()}" var="subComment">

                                                <div class="comment">
                                                    <a class="avatar">
                                                        <img src="${subComment.getPoster().getProfilePic()}">
                                                    </a>
                                                    <div class="content">
                                                        <a class="author" href="wall?uid=${subComment.getPoster().getId()}"><c:out value="${subComment.getPoster().getName()}"/></a>
                                                        <div class="metadata">
                                                            <span class="date"><c:out value="${subComment.getTimestamp()}"/></span>
                                                        </div>
                                                        <div class="text">
                                                            <pre><c:out value="${subComment.getText()}"/></pre>
                                                            <c:if test="${not empty subComment.getLikes()}">
                                                            Likes: <c:out value="${subComment.getLikes().size()}"/>
                                                            </c:if>
                                                        </div>
                                                            
                                                        <div class="actions">
                                                            <a class="reply">Reply</a>
                                                            <!-- TODO: Don't redirect + make visually appealing -->
                                                            <form action="?uid=${user}" id="wall_like_post" method="POST" class="ui form">
                                                                <input type="hidden" name="liked_post_id" value="${subComment.getId()}">
                                                                <input type="submit" value="Like" class="ui button">
                                                            </form>
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

                    </c:forEach>

                </div>
            </c:otherwise>
            </c:choose>
                
        </div>


<script>
    
    var client = new $.RestClient("http://localhost:5000/");

    post = client.add("post")
    
    $('.text').click(function() {
        var postContent = $(this).text().trim().replace(" ", "%20");
        
        post.read(postContent).done(function (data){
            //alert('I have data: ' + data.Value);
            
            console.log(data)
            
            $.when(function() {
                
                /*
                $('#bully-indicator').progress({
                    percent: (data.Value * 100)
                });
                */
                
            }).then(function() {
                
                $('#bully-indicator').removeClass("success warning error hidden");
                $('#bully-sidebar').sidebar('toggle');
                
                if(data.Value <= 1 && data.Value > 0.6) {
                    $('#bully-indicator').addClass("error");
                    $('#bully-sidebar span').text("Yes");
                }
                if(data.Value <= 0.6 && data.Value > 0.3) {
                    $('#bully-indicator').addClass("warning");
                    $('#bully-sidebar span').text("Maybe");
                }
                if(data.Value <= 0.3) {
                    $('#bully-indicator').addClass("success");
                    $('#bully-sidebar span').text("No");
                }
                
            });
            
            
        });
    });
    
</script>

<jsp:include page="footer.jsp" />