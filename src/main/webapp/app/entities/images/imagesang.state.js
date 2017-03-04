(function() {
    'use strict';

    angular
        .module('chatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('imagesang', {
            parent: 'entity',
            url: '/imagesang?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'chatApp.images.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/images/imagesang.html',
                    controller: 'ImagesAngController',
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
                    $translatePartialLoader.addPart('images');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('imagesang-detail', {
            parent: 'imagesang',
            url: '/imagesang/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'chatApp.images.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/images/imagesang-detail.html',
                    controller: 'ImagesAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('images');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Images', function($stateParams, Images) {
                    return Images.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'imagesang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('imagesang-detail.edit', {
            parent: 'imagesang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/images/imagesang-dialog.html',
                    controller: 'ImagesAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Images', function(Images) {
                            return Images.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('imagesang.new', {
            parent: 'imagesang',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/images/imagesang-dialog.html',
                    controller: 'ImagesAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                image: null,
                                imageContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('imagesang', null, { reload: 'imagesang' });
                }, function() {
                    $state.go('imagesang');
                });
            }]
        })
        .state('imagesang.edit', {
            parent: 'imagesang',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/images/imagesang-dialog.html',
                    controller: 'ImagesAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Images', function(Images) {
                            return Images.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('imagesang', null, { reload: 'imagesang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('imagesang.delete', {
            parent: 'imagesang',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/images/imagesang-delete-dialog.html',
                    controller: 'ImagesAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Images', function(Images) {
                            return Images.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('imagesang', null, { reload: 'imagesang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
