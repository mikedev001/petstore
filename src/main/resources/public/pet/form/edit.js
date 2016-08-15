'use strict';

angular.module('petstore.edit', ['ngRoute'])

        .config(['$routeProvider', function ($routeProvider) {
                $routeProvider.when('/edit', {
                    templateUrl: 'pet/form/form.html',
                    controller: 'EditPetCtrl'
                });
            }])

        .controller('EditPetCtrl', ['$scope',
            '$resource',
            '$http',
            '$location',
            'PetFactory',
            function ($scope, $resource, $http, $location, PetFactory) {

                $scope.resetErrorMsgs();
                
                $scope.clear = function () {
                    $scope.resetErrorMsgs();
                    $scope.newPet = {
                        "category": {},
                        "name": "",
                        "photoUrls": [],
                        "status": "",
                        "tags": [],
                        "tagsSelection": []
                    };
                    $scope.newPet.selectedPhotoUrls = [];
                    $scope.editedPhotoUrl = "";
                    $scope.newPet.selectedCategory = 0;
                    $scope.newPet.tagsSelection = [];
                };

                $scope.addPhotoUrl = function () {
                    $scope.resetErrorMsgs();
                    if ($scope.isNullOrEmptyOrUndefined($scope.editedPhotoUrl)) {
                        $scope.errorMsgsStatus.urlFieldEmpty = true;
                        return;
                    } else {
                        $scope.errorMsgsStatus.urlFieldEmpty = false;
                    }
                    $scope.newPet.selectedPhotoUrls.push(angular.copy($scope.editedPhotoUrl));
                    $scope.editedPhotoUrl = "";
                };

                $scope.savePet = function () {
                    $scope.resetErrorMsgs();
                    if ($scope.isNullOrEmptyOrUndefined($scope.newPet['name'])) {
                        $scope.errorMsgsStatus.nameMandatory = true;
                        return;
                    } else {
                        $scope.errorMsgsStatus.nameMandatory = false;
                    }
                    if ($scope.isNullOrEmptyOrUndefined($scope.newPet['status'])) {
                        $scope.errorMsgsStatus.statusFieldEmpty = true;
                        return;
                    } else {
                        $scope.errorMsgsStatus.statusFieldEmpty = false;
                    }
                    var pet = {};
                    pet['id'] = $scope.newPet['id'];
                    pet['name'] = $scope.newPet['name'];
                    angular.forEach($scope.dropdownsContent.categories, function (value, index) {
                        if ($scope.newPet.selectedCategory == index) {
                            pet['category'] = value;
                        }
                    });
                    pet['status'] = $scope.newPet['status'];
                    pet['photoUrls'] = [];
                    angular.forEach($scope.newPet.selectedPhotoUrls, function (value, index) {
                        pet['photoUrls'].push($scope.newPet.selectedPhotoUrls[index]);
                    });
                    pet['tags'] = [];
                    angular.forEach($scope.newPet.tagsSelection, function (value, index) {
                        pet['tags'].push($scope.dropdownsContent.tags[value]);
                    });
                    PetFactory.update(pet,
                            function (resp, headers) {
                                $scope.clear();
                            },
                            function (err) {
                                $scope.errorMsgsStatus.idAlreadyExists = true;
                            });
                }

                $scope.toggleTagsSelection = function toggleTagsSelection(tagId) {
                    var idx = $scope.newPet.tagsSelection.indexOf(tagId);
                    if (idx > -1) {
                        $scope.newPet.tagsSelection.splice(idx, 1);
                    } else {
                        $scope.newPet.tagsSelection.push(tagId);
                    }
                };

                $scope.isCheckboxSelected = function isCheckboxSelected(tagId) {
                    var idx = $scope.newPet.tagsSelection.indexOf(tagId);
                    if (idx > -1) {
                        return true;
                    } else {
                        return false;
                    }
                };
            }]);
