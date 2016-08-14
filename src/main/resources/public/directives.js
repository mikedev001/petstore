'use strict';

var petstoreDirectives = angular.module('petstore.directives', []);

// with id service for show, update, delete
petstoreDirectives.directive('alerts', function () {
    return {
        templateUrl: 'alerts.html'
    };
});
