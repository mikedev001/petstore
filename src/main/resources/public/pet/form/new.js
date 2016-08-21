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

                $scope.clear();

                $scope.newPet.selectedPhotoUrls = [];
                $scope.editedPhotoUrl = "";
                $scope.newPet.selectedCategory = 0;
                $scope.newPet.tagsSelection = [];

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
                                //$scope.pets.push(resp);
                                $scope.clear();
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

        .controller('ImageUploaderCtrl', ['$scope',
            '$resource',
            '$http',
            '$location',
            'PetFactory', 'Upload', '$timeout',
            function ($scope, $resource, $http, $location, PetFactory, Upload, $timeout) {

                $scope.$watch('files', function () {
                    $scope.upload($scope.files);
                });
                $scope.$watch('file', function () {
                    if ($scope.file != null) {
                        $scope.files = [$scope.file];
                    }
                });
                $scope.log = '';

                $scope.upload = function (files) {
                    if (files && files.length) {
                        for (var i = 0; i < files.length; i++) {
                            var file = files[i];
                            if (!file.$error) {
                                Upload.upload({
                                    url: 'pet/upload',
                                    data: {
                                        username: "",
                                        file: file,
                                        petId: $scope.newPet.id
                                    }
                                }).then(function (resp) {
                                    $timeout(function () {
                                        $scope.log = 'file: ' +
                                                resp.config.data.file.name +
                                                ', Response: ' + JSON.stringify(resp.data) +
                                                '\n' + $scope.log;
                                    });
                                }, null, function (evt) {
                                    var progressPercentage = parseInt(100.0 *
                                            evt.loaded / evt.total);
                                    $scope.log = 'progress: ' + progressPercentage +
                                            '% ' + evt.config.data.file.name + '\n' +
                                            $scope.log;
                                });
                            }
                        }
                    }
                    $scope.refreshCarousel();
                };

            }])
        