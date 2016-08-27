'use strict';

angular.module('petstore.searchbystatus', ['ngRoute'])

        .config(['$routeProvider', function ($routeProvider) {
                $routeProvider.when('/searchbystatus', {
                    templateUrl: 'pet/search/searchbystatus.html',
                    controller: 'SearchByStatusCtrl'
                });
            }])

        .controller('SearchByStatusCtrl', ['$scope', 'PetFactory', '$http', function ($scope, PetFactory, $http) {

                $scope.resetErrorMsgs();
        $scope.resetInfoMsgs();
        
                $scope.statusesSelection=[1,2,3];
                $scope.csvStatuses="available,pending,sold";
                $scope.pets = [];

                $scope.deletePet = function (petId, petIndexInPets) {
                    $scope.resetErrorMsgs();
                    $scope.resetInfoMsgs();
                    PetFactory.delete({id: petId}, function (result) {
                        $scope.pets.splice(petIndexInPets, 1);
                    }, function (data, status, headers, config) {});
                };

                $scope.findPetsByStatuses = function () {
                    $scope.resetErrorMsgs();
                    $scope.resetInfoMsgs();
                    $scope.csvStatuses="";
                    angular.forEach($scope.statusesSelection, function (value, index) {
                        if (value==1) {
                            $scope.csvStatuses += "available";
                        } else if (value==2) {
                            $scope.csvStatuses += "pending";
                        } else if (value==3) {
                            $scope.csvStatuses += "sold";
                        }
                        if (index !== $scope.statusesSelection.length - 1){ 
                            $scope.csvStatuses += ",";
                        }
                    });
                    if ($scope.isNullOrEmptyOrUndefined($scope.csvStatuses)) {
                        $scope.errorMsgsStatus.noResultFound = true;
                        $scope.pets = [];
                        return;
                    }
                    $http.get('pet/findPetsByStatus?status=' + $scope.csvStatuses).success(function (result) {
                        $scope.errorMsgsStatus.noResultFound = false;
                        $scope.pets = result;
                    }).error(function (data, status, headers, config) {
                        $scope.pets = [];
                        $scope.errorMsgsStatus.noResultFound = true;
                    });
                };
                
                $scope.toggleStatusSelection = function toggleStatusSelection(statusId) {
                    var idx = $scope.statusesSelection.indexOf(statusId);
                    if (idx > -1) {
                        $scope.statusesSelection.splice(idx, 1);
                    } else {
                        $scope.statusesSelection.push(statusId);
                    }
                };

                $scope.isStatusSelected = function isStatusSelected(statusId) {
                    var idx = $scope.statusesSelection.indexOf(statusId);
                    if (idx > -1) {
                        return true;
                    } else {
                        return false;
                    }
                };

            }]);
