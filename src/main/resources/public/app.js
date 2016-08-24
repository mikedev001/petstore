'use strict';

var petstoreApplication = angular.module("PetstoreApp", ['ngResource', 'ngRoute', 'petstore.new', 'petstore.edit', 'petstore.searchbyid', 'petstore.searchbystatus', 'petstore.CRUDService', 'petstore.directives', 'ngAnimate', 'ngSanitize', 'ui.bootstrap']);

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
            idAlreadyExists: false,
            statusFieldEmpty: false
        };

        $scope.resetErrorMsgs = function () {
            $scope.errorMsgsStatus.noResultFound = false;
            $scope.errorMsgsStatus.nameMandatory = false;
            $scope.errorMsgsStatus.urlFieldEmpty = false;
            $scope.errorMsgsStatus.idAlreadyExists = false;
            $scope.errorMsgsStatus.statusFieldEmpty = false;
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
            "categories": [],
            "tags": [],
            "statuses": []
        };

        $http.get('categories').success(function (result) {
            $scope.dropdownsContent.categories = result;
        }).error(function (data, status, headers, config) {});

        $http.get('tags').success(function (result) {
            $scope.dropdownsContent.tags = result;
        }).error(function (data, status, headers, config) {});

        $scope.dropdownsContent.statuses = ['', 'Available', 'Pending', 'Sold'];

        $scope.refreshCarousel = function () {
            if ($scope.newPet.id) {
                $http.get('photos/' + $scope.newPet.id).success(function (result) {
                    $scope.carousel.slides = result;
                }).error(function (data, status, headers, config) {});
            }
        };

        $scope.carousel = {
            myInterval: 5000,
            noWrapSlides: false,
            active: 0,
            slides: []
        };

        $scope.initCarousel = function () {
            $scope.carousel.myInterval = 5000;
            $scope.carousel.noWrapSlides = false;
            $scope.carousel.active = 0;
            $scope.carousel.slides = [];
        };

        $scope.initCarousel();
    }
]);
