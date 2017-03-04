(function() {
    'use strict';
    angular
        .module('chatApp')
        .factory('Images', Images);

    Images.$inject = ['$resource'];

    function Images ($resource) {
        var resourceUrl =  'api/images/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
