(function() {
    'use strict';


    angular
        .module('chatApp')
        .factory('JhiTrackerService', JhiTrackerService);

    JhiTrackerService.$inject = ['$rootScope', '$window', '$cookies', '$http', '$q', 'AuthServerProvider', 'StompManager'];

    function JhiTrackerService ($rootScope, $window, $cookies, $http, $q, AuthServerProvider, StompManager) {
        const SUBSCRIBE_TRACKER_URL = '/topic/tracker';
        const SEND_ACTIVITY_URL ='/topic/activity';
        var alreadyConnectedOnce = false;

        var service = {
            connect: connect,
            receive: receive,
            sendActivity: sendActivity,
            subscribe: subscribe,
            unsubscribe: unsubscribe
        };

        return service;

        function connect () {
            let stateChangeStart;
            StompManager.connect().then(() => {
                sendActivity();
                if (!alreadyConnectedOnce) {
                    stateChangeStart = $rootScope.$on('$stateChangeStart', function () {
                        sendActivity();
                    });
                    alreadyConnectedOnce = true;
                }
            });
            $rootScope.$on('$destroy', function () {
                if(angular.isDefined(stateChangeStart) && stateChangeStart !== null){
                    stateChangeStart();
                }
            });
        }


        function receive () {
            return StompManager.getListener(SUBSCRIBE_TRACKER_URL);
        }

        function sendActivity() {
            StompManager.send(SEND_ACTIVITY_URL, {'page': $rootScope.toState.name});
        }

        function subscribe () {
            StompManager.subscribe(SUBSCRIBE_TRACKER_URL);
        }

        function unsubscribe () {
            StompManager.unsubscribe(SUBSCRIBE_TRACKER_URL);
        }
    }
})();

