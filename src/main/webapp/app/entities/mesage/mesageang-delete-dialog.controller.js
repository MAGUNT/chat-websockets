(function() {
    'use strict';

    angular
        .module('chatApp')
        .controller('MesageAngDeleteController',MesageAngDeleteController);

    MesageAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'Mesage'];

    function MesageAngDeleteController($uibModalInstance, entity, Mesage) {
        var vm = this;

        vm.mesage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Mesage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
