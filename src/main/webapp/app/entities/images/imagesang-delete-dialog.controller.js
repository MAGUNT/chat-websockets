(function() {
    'use strict';

    angular
        .module('chatApp')
        .controller('ImagesAngDeleteController',ImagesAngDeleteController);

    ImagesAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'Images'];

    function ImagesAngDeleteController($uibModalInstance, entity, Images) {
        var vm = this;

        vm.images = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Images.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
