<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <jsp:include page="admin-header.jsp" />

        <div class="ui container">
            
            
            <h2>Biometric</h2>
            
            
            <div class="ui comments admin-posts">
            
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
                                <c:if test="${not empty post.getCue()}">
                                    <div class="ui label">
                                        <i class="flag icon"></i>
                                        Cue
                                        <a class="detail"><c:out value="${post.getCue()}"/></a>
                                    </div> 
                                </c:if>
                                    
                                    <div class="ui label">
                                        <i class="warning sign icon"></i>
                                        Offensiveness
                                        <a class="detail offensiveness-field"> 0%</a>
                                    </div>
                            
                                    <div class="ui label">
                                        <i class="eye sign icon"></i>
                                        Seen
                                        <a class="detail"><c:out value="${post.getSeen()}"/></a>
                                    </div>
                                    
                           
                            <div class="actions">
                            </div>
                        </div>
                                    
            </div>
                                    
            </div>
            
            
            <canvas id="myChart" width="800" height="250"></canvas>
            
            <script>
                
            var bullyAnalyzerUrl = "http://localhost:8080/BullyAnalyzerJava/webresources/analyzer"
                
                $(".offensiveness-field").each(function(){
        
                    var field = $(this);
                    var postContent = field.parent().prevAll('.text').text();
                    
                    
                    $.ajax(
                            {
                                type:"POST",
                                contentType: 'text/plain',
                                dataType: "json",
                                url:bullyAnalyzerUrl,
                                data: postContent,
                                success:function (data){
                                    
                                    field.html(data.value * 100 + "%");
                                    
                            }
                        }
                    )
        
                });
                
                
                
            var ctx = document.getElementById("myChart");
            var myChart = new Chart(ctx, {
                type: 'line',
                data: {
                
                    labels: [
                    <c:forEach items="${biometric_data}" var="item">
                            '<c:out value="${item.getTimestampString()}"/>',
                    </c:forEach>
                    ],
                    
                    //labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
                    
                    datasets: [{
                        label: 'Heartrate',
                                data: [
                                    <c:forEach items="${biometric_data}" var="item">
                                        <c:out value="${item.getHeartrate()}"/>,
                                    </c:forEach>
                                ],
                                backgroundColor: "rgba(52,152,219, 0.5)",
                                borderColor: "#2980b9",
                            }],
                    },
                    options: {
                        scales: {
                            yAxes: [{
                                    ticks: {
                                        beginAtZero: true
                                    }
                                }]
                        }
                    }
                });
                
            </script>

        </div>

<jsp:include page="footer.jsp" />