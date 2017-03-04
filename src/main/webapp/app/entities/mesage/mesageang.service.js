(function() {
    'use strict';
    angular
        .module('chatApp')
        .factory('Mesage', Mesage);

    Mesage.$inject = ['$resource', 'DateUtils'];

    function Mesage ($resource, DateUtils) {
        var resourceUrl =  'api/mesages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'eventMessages' : {
                method: 'GET',
                isArray: true,
                url: 'api/mesages/event/:idEvent'
            }
        });
    }
})();
