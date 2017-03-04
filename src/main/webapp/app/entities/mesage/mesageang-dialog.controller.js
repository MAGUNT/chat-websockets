(function() {
    'use strict';

    angular
        .module('chatApp')
        .controller('MesageAngDialogController', MesageAngDialogController);

    MesageAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Mesage', 'User', 'Event'];

    function MesageAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Mesage, User, Event) {
        var vm = this;

        vm.mesage = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mesage.id !== null) {
                Mesage.update(vm.mesage, onSaveSuccess, onSaveError);
            } else {
                Mesage.save(vm.mesage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('chatApp:mesageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
