(function() {
    'use strict';

    angular
        .module('chatApp')
        .controller('EventAngDetailController', EventAngDetailController);

    EventAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Event', 'Mesage', 'WSMessage', 'Principal'];

    function EventAngDetailController($scope, $rootScope, $stateParams, previousState, entity, Event, Mesage, WSMessage, Principal) {
        var vm = this;
        const EVENT_ID = entity.id;
        vm.event = entity;
        vm.previousState = previousState.name;
        vm.sendMessage = sendMessage;
        vm.messages = [];

        Mesage.eventMessages({
                idEvent : EVENT_ID,
                sort: 'date,asc'
            })
            .$promise
            .then((msgs) => vm.messages.push(...msgs.map(mapMessage)));

        Principal
            .identity()
            .then(p => vm.login = p.login);

        WSMessage
            .subscribe(EVENT_ID)
            .then(null, null, m => vm.messages.push(mapMessage(m)));


        function sendMessage(message, username) {
            if(message && message !== '') {
                WSMessage.sendMessage(EVENT_ID, {mesage: message});
            }
        }

        function mapMessage(m) {
            return {
                'date' : m.date,
                'username':m.emisorLogin,
                'content': m.mesage,
            };
        }

        let unsubscribe = $rootScope.$on('chatApp:eventUpdate',
            (event, result) => vm.event = result);

        $scope.$on('$destroy', () => {
            unsubscribe();
            WSMessage.unsubscribe(EVENT_ID);
        });
    }
})();
