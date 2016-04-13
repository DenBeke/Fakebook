function Hello($scope, $http) {
	$http.get('http://localhost:8080/Fakebook/webresources/user/1').
		success(function(data) {
			$scope.greeting = data;
		});
}