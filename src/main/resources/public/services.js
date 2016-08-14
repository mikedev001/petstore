'use strict';

var petstoreCRUDService = angular.module('petstore.CRUDService', ['ngResource']);

// with id service for show, update, delete
petstoreCRUDService.factory('PetFactory', function ($resource) {
    return $resource('pet/:id', {}, {
        show: { method: 'GET' },
        update: { method: 'PUT', params: {id: '@id'} },
        delete: { method: 'DELETE', params: {id: '@id'} },
        query: { method: 'GET', isArray: true },
        create: { method: 'POST' }
    });
});