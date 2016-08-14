'use strict';

var petstoreApplication = angular.module("PetstoreApp", ['ngResource', 'ngRoute', 'petstore.new', 'petstore.edit', 'petstore.searchbyid', 'petstore.searchbystatus', 'petstore.CRUDService', 'petstore.directives']);

petstoreApplication.config([
    '$locationProvider',
    '$routeProvider',
    function ($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');
        $routeProvider.otherwise({redirectTo: '/new'});
    }
]);

petstoreApplication.controller("PetstoreController", [
    '$scope',
    '$resource',
    '$http',
    '$location',
    'PetFactory',
    function ($scope, $resource, $http, $location, PetFactory) {

        $scope.errorMsgsStatus = {
            noResultFound: false,
            nameMandatory: false,
            urlFieldEmpty: false,
            idAlreadyExists: false
        };

        $scope.resetErrorMsgs = function () {
            $scope.errorMsgsStatus.noResultFound = false;
            $scope.errorMsgsStatus.nameMandatory = false;
            $scope.errorMsgsStatus.urlFieldEmpty = false;
            $scope.errorMsgsStatus.idAlreadyExists = false;
        };

        $scope.resetErrorMsgs();

        $scope.isNullOrEmptyOrUndefined = function (value) {
            if (value === "" || value === null || typeof value === "undefined") {
                return true;
            }
        };

        $scope.newPet = {
            "category": {},
            "name": "",
            "photoUrls": [],
            "status": "",
            "tags": [],
            "tagsSelection": [],
            "selectedPhotoUrls": [],
            "selectedCategory": 0
        };

        $scope.dropdownsContent = {
            "categories" : [],
            "tags" : [],
            "statuses" : []
        };
        
        $http.get('categories').success(function (result) {
            $scope.dropdownsContent.categories = result;
        }).error(function (data, status, headers, config) {});

        $http.get('tags').success(function (result) {
            $scope.dropdownsContent.tags = result;
        }).error(function (data, status, headers, config) {});

        $scope.dropdownsContent.statuses = ['', 'Available', 'Pending', 'Sold'];

    }
]);