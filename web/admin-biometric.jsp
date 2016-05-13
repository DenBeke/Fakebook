<jsp:include page="header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        
        <div class="ui container">
            <h2>Biometric</h2>
            
            
            
            <canvas id="myChart" width="800" height="400"></canvas>
            
            <script>
            var ctx = document.getElementById("myChart");
            var myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                
                    labels: [
                    <c:forEach items="${biometric_data}" var="item">
                            '<c:out value="${item.getTimestamp()}"/>',
                    </c:forEach>
                    ],
                    
                    //labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
                    
                    datasets: [{
                        label: 'Heartrate',
                                data: [
                                    <c:forEach items="${biometric_data}" var="item">
                                        <c:out value="${item.getHeartrate()}"/>,
                                    </c:forEach>
                                ]
                            }]
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