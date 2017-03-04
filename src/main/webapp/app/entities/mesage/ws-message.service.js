/**
 * Created by melvin on 3/4/2017.
 */
(function() {
    'use strict';


    angular
        .module('chatApp')
        .factory('WSMessage', JhiTrackerService);

    JhiTrackerService.$inject = ['$rootScope', '$window', '$cookies', '$http', '$q', 'AuthServerProvider', 'StompManager'];

    function JhiTrackerService ($rootScope, $window, $cookies, $http, $q, AuthServerProvider, StompManager) {
        const SUBSCRIBE_TRACKER_URL = '/topic/chat/';
        const SEND_ACTIVITY_URL = '/topic/message/';

        var service = {
            receive: receive,
            sendMessage: sendMessage,
            subscribe: subscribe,
            unsubscribe: unsubscribe
        };

        return service;

        function receive (idEvent) {
            return StompManager.getListener(SUBSCRIBE_TRACKER_URL + idEvent);
        }

        function sendMessage(idEvent, message) {
            StompManager.send(SEND_ACTIVITY_URL + idEvent, message);
        }

        function subscribe (idEvent) {
            StompManager.subscribe(SUBSCRIBE_TRACKER_URL + idEvent);
            return receive(idEvent);
        }

        function unsubscribe (idEvent) {
            StompManager.unsubscribe(SUBSCRIBE_TRACKER_URL + idEvent);

        }
    }
})();

