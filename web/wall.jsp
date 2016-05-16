<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
            
            <c:choose>
            <c:when test="${user == -1}">
                <h2>User wall</h2>
                <p>Error: The user you tried to access does not exist!</p>
            </c:when>
            <c:when test="${not friends}">
                <h2>Wall of ${userName}</h2>
                <p>You do not have permission to view this wall!</p>
                <p>You need to be friends with ${userName} to access the wall.</p>

                <form action="friend-request" id="friend_request_form" method="POST" class="ui form">
                    <input type="hidden" name="friend_user_id" value="${user}">
                    <input type="submit" value="Send friend request" class="ui teal button">
                </form>
            </c:when>
            <c:otherwise>
                
                <h2 class="ui dividing header">Wall of ${userName}</h2>
                
                <c:if test="${not empty error}">
                    <div class="ui negative message">
                        <div class="header">
                            Oops, something went wrong
                        </div>
                        <p>${error}</p>
                    </div>
                </c:if>

                <form enctype="multipart/form-data" action="?uid=${user}" id="wall_form" method="POST" class="ui form">
                    
                    <div class="field">
                        <div class="eyes"></div>
                        <div class="progress">
                            <div class="value">
                            </div>
                        </div>
                        <div class="of-words">
                        </div>
                        <textarea name="new_wall_post"></textarea>
                    </div>
                    Attach picture/video: <input type="file" name="attachment" accept="image/bmp, image/png, image/jpeg, image/gif, video/mp4, video/ogg, video/webm" /><br>
                    <input type="hidden" name="wall_user_id" value="${user}">
                    <input type="hidden" name="cue" value="">
                    <input type="submit" value="Write on wall" class="ui teal button">
                </form>

                <c:if test="${empty posts}"><p>This user has no posts on his wall yet.</p></c:if>

                
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

                                    <a class="reply" onclick="if ($(this).next('form').is(':hidden')) { $(this).next('form').show() } else { $(this).next('form').hide(); }">Reply</a>
                                    <form action="?uid=${user}" method="POST" class="reply-form ui form" hidden>
                                        <div class="field">
                                            <div class="eyes"></div>
                                                    <div class="progress">
                                                        <div class="value">
                                                        </div>
                                                    </div>
                                                    <div class="of-words">
                                                    </div>
                                            <textarea name="new_comment"></textarea>
                                        </div>
                                        <input type="hidden" name="parent_post_id" value="${post.getId()}">
                                        <input type="hidden" name="cue" value="">
                                        <input type="submit" value="Post comment" class="ui teal button">
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
                                            <!-- TODO: Don't redirect -->
                                            <form action="?uid=${user}" method="POST" class="ui form wall_like">
                                                <input type="hidden" name="liked_post_id" value="${comment.getId()}">
                                                <a class="like">
                                                    <i class="like icon" onclick="$(this).closest('form').submit();"></i>
                                                    <span class="fb-likes"><c:out value="${comment.getLikes().size()}"/> like<c:if test="${comment.getLikes().size() != 1}">s</c:if></span>
                                                </a>
                                            </form>
                                                
                                            <a class="reply" onclick="if ($(this).next('form').is(':hidden')) { $(this).next('form').show() } else { $(this).next('form').hide(); }">Reply</a>
                                            <form action="?uid=${user}" method="POST" class="reply-form ui form" hidden>
                                                <div class="field">
                                                    <div class="eyes"></div>
                                                    <div class="progress">
                                                        <div class="value">
                                                        </div>
                                                    </div>
                                                    <div class="of-words">
                                                    </div>
                                                    <textarea name="new_comment"></textarea>
                                                </div>
                                                <input type="hidden" name="parent_post_id" value="${comment.getId()}">
                                                <input type="hidden" name="cue" value="">
                                                <input type="submit" value="Post comment" class="ui teal button">
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
                                                            <!-- TODO: Don't redirect -->
                                                            <form action="?uid=${user}" method="POST" class="ui form wall_like">
                                                                <input type="hidden" name="liked_post_id" value="${subComment.getId()}">
                                                                <a class="like">
                                                                    <i class="like icon" onclick="$(this).closest('form').submit();"></i>
                                                                    <span class="fb-likes"><c:out value="${subComment.getLikes().size()}"/> like<c:if test="${subComment.getLikes().size() != 1}">s</c:if></span>
                                                                </a>
                                                            </form>
                                                                
                                                            <a class="reply" onclick="if ($(this).next('form').is(':hidden')) { $(this).next('form').show() } else { $(this).next('form').hide(); }">Reply</a>
                                                            <form action="?uid=${user}" method="POST" class="reply-form ui form" hidden>
                                                                <div class="field">
                                                                    <div class="eyes"></div>
                                                                    <div class="progress">
                                                                        <div class="value">
                                                                        </div>
                                                                    </div>
                                                                    <div class="of-words">
                                                                    </div>
                                                                    <textarea name="new_comment"></textarea>
                                                                </div>
                                                                <input type="hidden" name="parent_post_id" value="${comment.getId()}"> <!-- "comment" instead of "subComment" because you can't comment on the subcomment itself -->
                                                                <input type="hidden" name="cue" value="">
                                                                <input type="submit" value="Post comment" class="ui teal button">
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
    var bullyAnalyzerUrl = "http://localhost:8080/BullyAnalyzerJava/webresources/analyzer"
    $("form.wall_like").submit(function(e){
    var form = $(this);
    var request = "http://localhost:8080/Fakebook/postlike?liked_post_id=" + form.find("[name='liked_post_id']").val();
                //console.log(id);
                //console.log(request);
                //console.log(request)
                $.get( request, function( data ) {
                    text = ' like';
                    if(parseInt(data) !== 1) {
                        text = text + 's';
                    }
                    form.find('.fb-likes').text(data + text);
                });
    return false;
 });
    
    
    $(window).scroll(function(){
        $('.fb-post:in-viewport').each(function(){
            if( $(this).data('seen').length === 0) {
                var id = $(this).data('id');
                var date = Math.floor(Date.now() / 1000);
                $(this).data('seen', date);
                
                var request = "http://localhost:8080/Fakebook/postseen?post=" + id + "&date=" + date;
                //console.log(id);
                //console.log(request);
                $.get( request, function( data ) {
                    //$( ".result" ).html( data );
                    //alert( "Load was performed." );
                    //console.log(data);
                });
            }
            
        });
    });
    
    //var client = new $.RestClient("http://localhost:5000/");

    //post = client.add("post");
    
    
    $("[name='cue']").each(function(){
        
        var cue = ['eyes', 'highlight', 'degree'][Math.floor(Math.random() * 3)];
        $(this).val(cue);
        
    });
    
    
    $( 'form#wall_form, form.reply-form' ).keypress(function() {
        
        var form = $(this);
        var postContent = form.find('textarea').val();

        $.ajax(
                {
                    type:"POST",
                    contentType: 'text/plain',
                    dataType: "json",
                    url:bullyAnalyzerUrl,
                    data: postContent,
                    success:function (data){
                        
                        console.log(postContent);
                        console.log(data);
                        
                        var cue = form.find("[name='cue']").val();
                        
                        console.log(cue);
                        
                        
                        if(cue === "eyes") {
                            
                            if(data.value >= 0.3) {
                                form.find('.eyes' ).fadeIn();
                            }
                            
                        }
                        else if(cue === "highlight") {
                            form.find(".of-words").html("");
                            data.profanities.forEach(function(entry) {
                                form.find(".of-words").append("<span>" + entry + "</span>");
}                           );
                        }
                        else if(cue === "degree") {
                            if(data.value >= 0.3) {
                                form.find('.progress').fadeIn();
                            }
                            form.find('.progress .value').height(data.value * 100);
                        }
                        
                    }
                }
        )
        
})  ;
    
    
    
    $('.text pre').click(function(event) {
        var postContent = $(this).text().trim();
        
        //console.log("CLICKED!")
        //console.log(postContent)
        $.ajax(
                {
                    type:"POST",
                    contentType: 'text/plain',
                    dataType: "json",
                    url:bullyAnalyzerUrl,
                    data: postContent,
                    success:function (data){
            //alert('I have data: ' + data.value);
            
            //console.log("response")
            //console.log(data)
            //console.log("done")
            $.when(function() {
                
                /*
                $('#bully-indicator').progress({
                    percent: (data.value * 100)
                });
                */
                
            }).then(function() {
                
                $('#bully-indicator').removeClass("success warning error hidden");
                $('#bully-sidebar').sidebar('toggle');
                
                if(data.value <= 1 && data.value > 0.6) {
                    $('#bully-indicator').addClass("error");
                    $('#bully-sidebar span').text("Yes");
                }
                if(data.value <= 0.6 && data.value > 0.3) {
                    $('#bully-indicator').addClass("warning");
                    $('#bully-sidebar span').text("Maybe");
                }
                if(data.value <= 0.3) {
                    $('#bully-indicator').addClass("success");
                    $('#bully-sidebar span').text("No");
                }
                
            });
            
        }    
        });
    });
    
</script>

<jsp:include page="footer.jsp" />