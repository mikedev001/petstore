'use strict';

angular.module('petstore.searchbyid', ['ngRoute'])

        .config(['$routeProvider', function ($routeProvider) {
                $routeProvider.when('/searchbyid', {
                    templateUrl: 'pet/search/searchbyid.html',
                    controller: 'SearchByIdCtrl'
                });
            }])

        .controller('SearchByIdCtrl', ['$scope', 'PetFactory', '$location', function ($scope, PetFactory, $location) {

                $scope.resetErrorMsgs();
        
                $scope.findPetById = function (petId) {
                    $scope.resetErrorMsgs();
                    if ($scope.isNullOrEmptyOrUndefined(petId)) {
                        $scope.errorMsgsStatus.noResultFound = true;
                        return;
                    }
                    PetFactory.show({id: petId}, function (result) {
                        $scope.errorMsgsStatus.noResultFound = false;
                        $scope.petToFind = result;
                    }, function (data, status, headers, config) {
                        $scope.petToFind = {};
                        $scope.errorMsgsStatus.noResultFound = true;
                    });
                };

                $scope.editPet = function () {

                    $scope.newPet['id'] = $scope.petToFind['id'];
                    $scope.newPet['name'] = $scope.petToFind['name'];
                    $scope.newPet['category'] = $scope.petToFind['category'];
                    
                    angular.forEach($scope.dropdownsContent.categories, function (value, index) {
                        if ($scope.petToFind['category'].name == value.name) {
                            $scope.newPet['selectedCategory'] = index;
                        }
                    });
                    
                    $scope.newPet['status'] = $scope.petToFind['status'];
                    $scope.newPet['photoUrls'] = [];
                    $scope.newPet['selectedPhotoUrls'] = [];
                    angular.forEach($scope.petToFind.photoUrls, function (value, index) {
                        $scope.newPet['photoUrls'].push(value);
                        $scope.newPet['selectedPhotoUrls'].push(value);
                    });
                    $scope.newPet['tags'] = [];
                    $scope.newPet['tagsSelection'] = [];
                    angular.forEach($scope.petToFind.tags, function (value, index) {
                        $scope.newPet['tags'].push($scope.dropdownsContent.tags[value]);
                        $scope.newPet['tagsSelection'].push(value.id);
                    });
                    $location.path('/edit');
                };

            }]);
