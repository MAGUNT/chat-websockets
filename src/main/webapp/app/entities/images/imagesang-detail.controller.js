(function() {
    'use strict';

    angular
        .module('chatApp')
        .controller('ImagesAngDetailController', ImagesAngDetailController);

    ImagesAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Images'];

    function ImagesAngDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Images) {
        var vm = this;

        vm.images = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('chatApp:imagesUpdate', function(event, result) {
            vm.images = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
