<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
            
            <c:choose>
            <c:when test="${user == -1}">
                <h1>Wall of user</h1>
                <p>Error: The user you tried to access does not exist!</p>
            </c:when>
            <c:otherwise>
                
                <h2 class="ui dividing header">Wall of user ${user}</h2>


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
                                <img src="<c:out value="${post.getPoster().getProfilePic()}" escapeXml="true"/>">
                            </a>
                            <div class="content">
                                <a class="author" href="wall?uid=${post.getPoster().getId()}"><c:out value="${post.getPoster().getFirstName()} ${post.getPoster().getLastName()}" escapeXml="true"/></a>
                                <div class="metadata">
                                    <span class="date"><c:out value="${post.getTimestamp()}" escapeXml="true"/></span>
                                </div>
                                <div class="text">
                                    <c:out value="${post.getText()}" escapeXml="true"/>

                                    <c:if test="${not empty post.getText() && not empty post.getType()}">
                                        <br>
                                    </c:if>

                                    <c:if test="${post.getType() eq 'link' && not empty post.getLink()}">
                                        <a href="<c:out value="${post.getLink()}" escapeXml="true"/>"><c:out value="${post.getLink()}" escapeXml="true"/></a>
                                    </c:if>
                                    <c:if test="${post.getType() eq 'picture' && not empty post.getPicture()}">
                                        <a href="<c:out value="${post.getPicture()}" escapeXml="true"/>"><img src="<c:out value="${post.getPicture()}" escapeXml="true"/>" /></a>
                                    </c:if>
                                    <c:if test="${post.getType() eq 'video' && not empty post.getVideo()}">
                                         <video controls>
                                            <source src="<c:out value="${post.getVideo()}" escapeXml="true"/>">
                                            <a href="<c:out value="${post.getVideo()}" escapeXml="true"/>"><c:out value="${post.getVideo()}" escapeXml="true"/></a>
                                          </video>
                                    </c:if>
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
                                        <img src="${comment.getPoster().getProfilePic()}">
                                    </a>
                                    <div class="content">
                                        <a class="author" href="wall?uid=${comment.getPoster().getId()}"><c:out value="${comment.getPoster().getFirstName()} ${comment.getPoster().getLastName()}" escapeXml="true"/></a>
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
                                                                <img src="${subComment.getPoster().getProfilePic()}">
                                                            </a>
                                                            <div class="content">
                                                                <a class="author" href="wall?uid=${subComment.getPoster().getId()}"><c:out value="${subComment.getPoster().getFirstName()} ${subComment.getPoster().getLastName()}" escapeXml="true"/></a>
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