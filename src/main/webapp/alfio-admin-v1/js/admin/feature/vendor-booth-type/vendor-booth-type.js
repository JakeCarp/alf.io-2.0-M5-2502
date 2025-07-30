(function () {
    "use strict";


    angular.module('alfio-vendor-booth-type', ['adminServices'])
        .config(['$stateProvider', function ($stateProvider) {
            $stateProvider
                .state('events.single.vendor-booth-types', {
                    url: '/vendor-booth-types',
                    templateUrl: window.ALFIO_CONTEXT_PATH + '/resources/angular-templates/admin/partials/vendor-booth-type/list.html',
                    controller: VendorBoothTypeListController,
                    controllerAs: 'ctrl'
                })
                .state('events.single.vendor-booth-type-detail', {
                    url: '/vendor-booth-type/:boothTypeId',
                    templateUrl: window.ALFIO_CONTEXT_PATH + '/resources/angular-templates/admin/partials/vendor-booth-type/entry-detail.html',
                    controller: VendorBoothTypeDetailController,
                    controllerAs: 'detailCtrl'
                });
        }])
    
        .service('VendorBoothTypeService', VendorBoothTypeService)
        .filter('truncateString', function () {
            return function (string, maxLength) {
                if (!angular.isDefined(string)) {
                    return "";
                }
                var l = angular.isDefined(maxLength) ? maxLength : 50;
                return string.length > l ? (string.substring(0, l - 4) + '...') : string;
            }
        });
    
    function VendorBoothTypeListController(VendorBoothTypeService, $uibModal, $location, $stateParams) {
        var ctrl = this;

        var currentSearch = $location.search();
        ctrl.currentPage = currentSearch.page || 1;
        ctrl.toSearch = currentSearch.search || '';
        ctrl.boothTypes = [];
        ctrl.vendorBoothType = {};
        ctrl.publicIdentifier = $stateParams.eventName || $stateParams.subscriptionId;
        ctrl.contextType = $stateParams.eventName ? 'event' : 'subscription';
        ctrl.itemsPerPage = 50;
        ctrl.loadData = loadData();
        ctrl.addOrEditBoothType = addOrEditBoothType;
        ctrl.deleteBoothType = deleteBoothType;
        ctrl.updateFilteredData = function () {
            loadData();
        }

        loadData();

        function loadData() {
            $location.search({ page: ctrl.currentPage, search: ctrl.toSearch });
            VendorBoothTypeService.loadBoothTypeList(ctrl.contextType, ctrl.publicIdentifier, ctrl.currentPage - 1, ctrl.toSearch).success(function (results) {
                ctrl.boothTypes = results.left;
                ctrl.totalItems = results.right;
            });
        }

        function addOrEditBoothType(boothType = {}) {
            $uibModal.open({
                size: 'lg',
                templateUrl: window.ALFIO_CONTEXT_PATH + '/resources/angular-templates/admin/partials/vendor-booth-type/vendor-booth-modal.html',
                backdrop: 'static',
                controller: function ($scope) {
                    $scope.boothType = boothType || {};
                    $scope.save = function () {
                        if ($scope.boothType.id) {
                            VendorBoothTypeService.updateBoothType($scope.boothType).then(function () {
                                loadData();
                                $scope.$close();
                            }, function (error) {
                                console.error('Error updating booth type:', error);
                            });
                        } else {
                            VendorBoothTypeService.createBoothType($scope.boothType).then(function () {
                                loadData();
                                $scope.$close();
                            }, function (error) {
                                console.error('Error creating booth type:', error);
                            });
                        }
                    };
                    $scope.cancel = function () {
                        $scope.$dismiss();
                    };
                }
            });
        };

        function deleteBoothType(boothType) {
            if (confirm('Are you sure you want to delete this booth type?')) {
                VendorBoothTypeService.deleteBoothType(boothType.id).then(function () {
                    loadData();
                }, function (error) {
                    console.error('Error deleting booth type:', error);
                });
            }
        }
    }


    VendorBoothTypeListController.$inject = ['VendorBoothTypeService', '$uibModal', '$location', '$stateParams'];

    function VendorBoothTypeDetailController(VendorBoothTypeService, $stateParams) {
        var detailCtrl = this;
        detailCtrl.boothTypeId = $stateParams.boothTypeId;

        VendorBoothTypeService.loadBoothTypeDetail(detailCtrl.boothTypeId).success(function (data) {
            detailCtrl.boothType = data;
        });
    }

    VendorBoothTypeDetailController.$inject = ['VendorBoothTypeService', '$stateParams'];


    function VendorBoothTypeService($http) {
        this.loadBoothTypeList = function (contextType, publicIdentifier, page, search) {
            return $http.get('/admin/api/' + publicIdentifier + '/vendor-booth-type', {
                params: {
                    contextType: contextType,
                    publicIdentifier: publicIdentifier,
                    page: page,
                    search: search
                }
            });
        };

        this.loadBoothTypeDetail = function (boothTypeId) {
            return $http.get('/admin/api/' + publicIdentifier + '/' + boothTypeId + '/vendor-booth-type');
        };
        this.createBoothType = function (boothType) {
            return $http.post('/admin/api/' + publicIdentifier + '/vendor-booth-type', boothType);
        };
        this.updateBoothType = function (boothType) {
            return $http.put('/admin/api/' + publicIdentifier + '/vendor-booth-type/' + boothType.id, boothType);
        };
        this.deleteBoothType = function (boothTypeId) {
            return $http.delete('/admin/api/' + publicIdentifier + '/vendor-booth-type/' + boothTypeId);
        };
    }

    VendorBoothTypeService.$inject = ['$http', 'HttpErrorHandler'];
})();