/**
 * Created by melvin on 2/16/2017.
 */
(function() {
    'use strict';
    /* globals SockJS, Stomp */

    angular
        .module('chatApp')
        .factory('StompManager', StompManager);

    StompManager.$inject = ['$window', '$q', 'AuthServerProvider'];

    function StompManager ($window, $q, AuthServerProvider) {

        const END_POINT = 'websocket/tracker';

        let stompClient = null;
        let connected = $q.defer();
        let subscribeMap = new Map();


        var service = {
            connect: connect,
            disconnect: disconnect,
            getListener: getListener,
            send: send,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
            isConnected: isConnected,
            reconnect: reconnect
        };

        return service;

        function connect (headers = {}) {
            let socket = new SockJS(buildUrl());
            stompClient = Stomp.over(socket);
            stompClient.connect(headers,
                () => connected.resolve('success'),
                () => connect(headers));

            return connected.promise;
        }

        function isConnected() {
            return stompClient !== null && stompClient.connected;
        }

        function buildUrl() {
            let loc = $window.location;
            let url = `//${loc.host}${loc.pathname}${END_POINT}`;

            let authToken = AuthServerProvider.getToken();
            if(authToken){
                url += `?access_token=${authToken}`;
            }
            return url;
        }

        function disconnect () {
            if (stompClient !== null) {
                stompClient.disconnect();
                stompClient = null;
                connected = $q.defer();
            }
        }

        function getListener (url) {
            let value = subscribeMap.get(url);
            if(!value) {
                throw `there is no subscription to url = ${url}.`
            }
            return value.listener.promise;
        }

        function send(url, payload, headers = {}) {
            connected.promise.then(() => {
                if (isConnected()) {
                    stompClient.send(url, headers, angular.toJson(payload));
                }
            });
        }

        function reconnect(headers = {}) {
            disconnect();
            for (let [url, value] of subscribeMap) {
                subscribeKeyValue(url, value);
            }
            return connect(headers);
        }

        function subscribe (url) {
            let value = subscribeMap.get(url)
                || { subscriber: null, listener: $q.defer() };
            subscribeKeyValue(url, value);
        }
        function subscribeKeyValue(url, value) {
            subscribeMap.set(url, value);
            connected.promise.then(() => {
                value.subscriber = stompClient.subscribe(url, data =>
                    value.listener.notify(angular.fromJson(data.body)));
            });
        }

        function unsubscribe (url) {
            let value = subscribeMap.get(url);
            if (value && value.subscriber !== null) {
                value.subscriber.unsubscribe();
                subscribeMap.delete(url);
            }
        }
    }
})();
