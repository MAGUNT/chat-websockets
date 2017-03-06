(function() {
    'use strict';

    angular
        .module('chatApp')
        .controller('EventAngDetailController', EventAngDetailController);

    EventAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Event', 'Mesage', 'WSMessage', 'Principal'];

    function EventAngDetailController($scope, $rootScope, $stateParams, previousState, entity, Event, Mesage, WSMessage, Principal) {
        const EVENT_ID = entity.id;
        let msgsParams = {
            idEvent : EVENT_ID,
            page: 0,
            size: 10,
            sort: 'date,desc'
        };

        var vm = this;
        vm.event = entity;
        vm.previousState = previousState.name;
        vm.sendMessage = sendMessage;
        vm.loadPage = loadPage;
        vm.messages = [];

        loadPage();

        Principal.identity()
            .then(p => {
                vm.myUserId = p.id;
                vm.login = p.login
            });

        WSMessage.subscribe(EVENT_ID)
            .then(null, null, addNewMessage);


        function loadPage() {
            Mesage.eventMessages(msgsParams).$promise
                .then(addPage);
        }

        function addPage(msgs){
            if(msgs.length === 0
                && msgsParams.page !== 0) {
                return;
            }
            let mapRevMsgs = msgs
                .reverse()
                .map(mapMessage);

            vm.messages.unshift(...mapRevMsgs);
            ++msgsParams.page;
        }

        function addNewMessage(m) {
            if(vm.messages.length
                % msgsParams.size === 0) {
                vm.messages.shift();
            }
            vm.messages.push(mapMessage(m));
        }

        function sendMessage(message, username) {
            if(message && message !== '') {
                WSMessage.sendMessage(EVENT_ID, {mesage: message});
            }
        }

        function mapMessage(m) {
            return {
                'date' : m.date,
                'username': m.emisorLogin,
                'content': m.mesage,
                'fromUserId': m.emisorId
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
