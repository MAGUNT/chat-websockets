(function() {
    'use strict';

    angular
        .module('chatApp')
        .controller('ImagesAngDialogController', ImagesAngDialogController);

    ImagesAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Images'];

    function ImagesAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Images) {
        var vm = this;

        vm.images = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.images.id !== null) {
                Images.update(vm.images, onSaveSuccess, onSaveError);
            } else {
                Images.save(vm.images, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('chatApp:imagesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, images) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        images.image = base64Data;
                        images.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
