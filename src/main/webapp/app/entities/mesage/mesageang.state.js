(function() {
    'use strict';

    angular
        .module('chatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mesageang', {
            parent: 'entity',
            url: '/mesageang?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'chatApp.mesage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mesage/mesagesang.html',
                    controller: 'MesageAngController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mesage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('mesageang-detail', {
            parent: 'mesageang',
            url: '/mesageang/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'chatApp.mesage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mesage/mesageang-detail.html',
                    controller: 'MesageAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mesage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Mesage', function($stateParams, Mesage) {
                    return Mesage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'mesageang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('mesageang-detail.edit', {
            parent: 'mesageang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mesage/mesageang-dialog.html',
                    controller: 'MesageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Mesage', function(Mesage) {
                            return Mesage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mesageang.new', {
            parent: 'mesageang',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mesage/mesageang-dialog.html',
                    controller: 'MesageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                mesage: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mesageang', null, { reload: 'mesageang' });
                }, function() {
                    $state.go('mesageang');
                });
            }]
        })
        .state('mesageang.edit', {
            parent: 'mesageang',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mesage/mesageang-dialog.html',
                    controller: 'MesageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Mesage', function(Mesage) {
                            return Mesage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mesageang', null, { reload: 'mesageang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mesageang.delete', {
            parent: 'mesageang',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mesage/mesageang-delete-dialog.html',
                    controller: 'MesageAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Mesage', function(Mesage) {
                            return Mesage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mesageang', null, { reload: 'mesageang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
