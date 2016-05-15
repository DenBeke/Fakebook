<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <div class="ui container">
            
            
            <jsp:include page="admin-header.jsp" />
            

            
            
            <div class="ui statistics">
                <div class="statistic">
                    <div class="value">
                        <c:out value="${postCount}"/>
                    </div>
                    <div class="label">
                        Posts
                    </div>
                </div>
                <div class="statistic">
                    <div class="value">
                        <c:out value="${commentCount}"/>
                    </div>
                    <div class="label">
                        Comments
                    </div>
                </div>
                <div class="statistic">
                    <div class="value">
                        <c:out value="${videoCount}"/>
                    </div>
                    <div class="label">
                        Videos
                    </div>
                </div>
                <div class="statistic">
                    <div class="value">
                        <c:out value="${pictureCount}"/>
                    </div>
                    <div class="label">
                        Pictures
                    </div>
                </div>
            </div>
            
            
        </div>

<jsp:include page="footer.jsp" />