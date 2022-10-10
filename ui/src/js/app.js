var app = angular.module('fantalegheEV',['env']);

app.controller('HomeController', ['$scope', '$http', 'ENV', function($scope, $http, ENV) {
    $scope.first_goal_threshold = 66;
    $scope.goal_threshold = 6;

    $scope.league_name = "";
    $scope.table_content = [];
    $scope.error_message=""
    $scope.show_error=false;
    $scope.checkValues = function() {
        $scope.show_error = false;
        if (!angular.isNumber(parseInt($scope.goal_threshold))) {
            $scope.error_message = "Soglia per ogni gol non corretta"
            $scope.show_error = true;
            return false;
        }
        if (!angular.isNumber(parseInt($scope.first_goal_threshold))) {
            $scope.error_message = "Soglia per primo gol non corretta"
            $scope.show_error = true;
            return false;
        }
        $scope.show_error = false;
        return true;
    }

    $scope.calculate = function() {
        if (!$scope.checkValues()) {
            return false;
        }
        var data = {}
        data["leagueName"] = $scope.league_name
        data["firstGoalThreshold"] = $scope.first_goal_threshold
        data["goalThreshold"] = $scope.goal_threshold
        var req = {
            method: 'POST',
            url: '/calculate',
            dataType: 'json',
            headers: {
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(data)
        }
        // console.log("HTTP IS ", $http)
        // $http.post('/calculate', data).then($scope.calculateSuccess);
	    //
	console.log(data)
        $http({
//            method: 'POST',
		method: 'GET',
//            url: ENV.URL + ":" + ENV.PORT + "/calculate",
	    url: "/calculate?league_name=" + $scope.league_name,
            data: JSON.stringify(data),
            headers: {'Content-Type': 'application/json'}
        }).then(function(response) {
            $scope.table_content = response.data
        })
    }



}]);
