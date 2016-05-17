<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
            
            <h2>Feed</h2>

            <c:if test="${empty posts}"><p>Neither you or your friends have posts yet :(.</p></c:if>

            <div class="ui comments">
                <c:forEach items="${posts}" var="post">

                    <div class="comment fb-post" data-id="<c:out value="${post.getId()}"/>" data-seen="<c:out value="${post.getSeen()}"/>">
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
                            </div>
                            <div class="actions">
                                <form action="?uid=${user}" method="POST" class="ui form wall_like">
                                    <input type="hidden" name="liked_post_id" value="${post.getId()}">
                                    <a class="like">
                                        <i class="like icon" onclick="$(this).closest('form').submit();"></i>
                                        <span class="fb-likes"><c:out value="${post.getLikes().size()}"/> like<c:if test="${post.getLikes().size() != 1}">s</c:if></span>
                                    </a>
                                </form>
                            </div>
                        </div>

                        <c:if test="${not empty post.getComments()}">
                        <div class="comments">
                        <c:forEach items="${post.getComments()}" var="comment">

                            <div class="comment fb-comment" data-id="<c:out value="${comment.getId()}"/>" data-seen="<c:out value="${post.getSeen()}"/>">
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
                                    </div>
                                    <div class="actions">
                                        <form action="?uid=${user}" method="POST" class="ui form wall_like">
                                            <input type="hidden" name="liked_post_id" value="${comment.getId()}">
                                            <a class="like">
                                                <i class="like icon" onclick="$(this).closest('form').submit();"></i>
                                                <span class="fb-likes"><c:out value="${comment.getLikes().size()}"/> like<c:if test="${comment.getLikes().size() != 1}">s</c:if></span>
                                            </a>
                                        </form>
                                    </div>
                                </div>

                                <c:if test="${not empty comment.getComments()}">
                                    <div class="comments">
                                        <c:forEach items="${comment.getComments()}" var="subComment">

                                            <div class="comment fb-subcomment" data-id="<c:out value="${subComment.getId()}"/>" data-seen="<c:out value="${post.getSeen()}"/>">
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
                                                    </div>

                                                    <div class="actions">
                                                        <form action="?uid=${user}" method="POST" class="ui form wall_like">
                                                            <input type="hidden" name="liked_post_id" value="${subComment.getId()}">
                                                            <a class="like">
                                                                <i class="like icon" onclick="$(this).closest('form').submit();"></i>
                                                                <span class="fb-likes"><c:out value="${subComment.getLikes().size()}"/> like<c:if test="${subComment.getLikes().size() != 1}">s</c:if></span>
                                                            </a>
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
                
        </div>


<script>
    $("form.wall_like").submit(function(e){
        var form = $(this);
        var request = "http://localhost:8080/Fakebook/postlike?liked_post_id=" + form.find("[name='liked_post_id']").val();
                    $.get( request, function( data ) {
                        text = ' like';
                        if(parseInt(data) !== 1) {
                            text = text + 's';
                        }
                        form.find('.fb-likes').text(data + text);
                    });
        return false;
     });
     
</script>

<jsp:include page="footer.jsp" />