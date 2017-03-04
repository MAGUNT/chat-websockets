(function() {
    'use strict';

    angular
        .module('chatApp')
        .controller('MesageAngDetailController', MesageAngDetailController);

    MesageAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Mesage', 'User', 'Event'];

    function MesageAngDetailController($scope, $rootScope, $stateParams, previousState, entity, Mesage, User, Event) {
        var vm = this;

        vm.mesage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('chatApp:mesageUpdate', function(event, result) {
            vm.mesage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
