'use strict';

angular.module('petstore.new', ['ngRoute', 'ngFileUpload'])

        .config(['$routeProvider', function ($routeProvider) {
                $routeProvider.when('/new', {
                    templateUrl: 'pet/form/form.html',
                    controller: 'NewPetCtrl'
                });
            }])

        .controller('NewPetCtrl', ['$scope',
            '$resource',
            '$http',
            '$location',
            'PetFactory',
            function ($scope, $resource, $http, $location, PetFactory) {

                $scope.resetErrorMsgs();
                $scope.resetInfoMsgs();
                $scope.initCarousel();

                $scope.clear = function () {
                    $scope.resetErrorMsgs();
                    $scope.resetInfoMsgs();
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

                $scope.clear();

                $scope.newPet.selectedPhotoUrls = [];
                $scope.editedPhotoUrl = "";
                $scope.newPet.selectedCategory = 0;
                $scope.newPet.tagsSelection = [];

                $scope.savePet = function () {
                    $scope.resetErrorMsgs();
                    $scope.resetInfoMsgs();
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
                    PetFactory.create(pet,
                            function (resp, headers) {
                                // todo: display alert to confirm it s saved
                                $scope.infoMsgsStatus.saved = true;
                            },
                            function (err) {
                                $scope.errorMsgsStatus.idAlreadyExists = true;
                            });
                };

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
            }])