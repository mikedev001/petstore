'use strict';

angular.module('petstore.edit', ['ngRoute', 'ngFileUpload'])

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
                $scope.resetInfoMsgs();
                
                $scope.refreshCarousel();

                $scope.addPhotoUrl = function () {
                    $scope.resetErrorMsgs();
                    $scope.resetInfoMsgs();
                    if ($scope.petForm.photoUrlField.$valid == false) {
                        $scope.errorMsgsStatus.urlFieldNotValid = true;
                        return;
                    } else {
                        $scope.errorMsgsStatus.urlFieldNotValid = false;
                    }
                    if ($scope.isNullOrEmptyOrUndefined($scope.carousel.editedPhotoUrl) == true) {
                        $scope.errorMsgsStatus.urlFieldEmpty = true;
                        return;
                    } else {
                        $scope.errorMsgsStatus.urlFieldEmpty = false;
                    }
                    $scope.newPet.selectedPhotoUrls.push(angular.copy($scope.carousel.editedPhotoUrl));
                    
                    $http.post("petdownload?url=" + encodeURIComponent($scope.carousel.editedPhotoUrl) + 
                                                "&name=" +
                                                "&petId=" + encodeURIComponent($scope.newPet.id))
                            .success(function (result) {
                                $scope.infoMsgsStatus.saved = true;
                            })
                            .error(function (data, status, headers, config) {
                                $scope.errorMsgsStatus.idAlreadyExists = true;
                            });
                    
                    $scope.carousel.editedPhotoUrl = "";
                };

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
                                $scope.infoMsgsStatus.saved = true;
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
                                        $scope.refreshCarousel();
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
                    
                };

            }])
