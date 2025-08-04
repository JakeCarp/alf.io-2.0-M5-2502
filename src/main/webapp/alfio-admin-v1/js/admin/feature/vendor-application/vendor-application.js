(function () {
    "use strict";

    angular.module('alfio-vendor', ['adminServices'])
        .config(['$stateProvider', function($stateProvider) {
            $stateProvider
                .state('events.single.vendor-applications', {
                    url: '/vendor-applications',
                    templateUrl: window.ALFIO_CONTEXT_PATH + '/resources/angular-templates/admin/partials/vendor-application/list.html',
                    controller: VendorApplicationListController,
                    controllerAs: 'ctrl'
                })
                .state('events.single.vendor-application-detail', {
                    url: '/vendor-application/:applicationId',
                    templateUrl: window.ALFIO_CONTEXT_PATH + '/resources/angular-templates/admin/partials/vendor-application/entry-detail.html',
                    controller: VendorApplicationDetailController,
                    controllerAs: 'detailCtrl'
                })
        }])
        .service('VendorApplicationService', VendorApplicationService)
        .service('VendorBoothTypeService', VendorBoothTypeService)
        .filter('truncateString', function() {
            return function(string, maxLength) {
                if(!angular.isDefined(string)) {
                    return "";
                }
                var l = angular.isDefined(maxLength) ? maxLength : 50;
                return string.length > l ? (string.substring(0, l-4) + '...') : string;
            }
        });


    function VendorApplicationListController(VendorApplicationService, VendorBoothTypeService, $location, $stateParams) {
        var ctrl = this;

        var currentSearch = $location.search();
        ctrl.currentPage = currentSearch.page || 1;
        ctrl.toSearch = currentSearch.search || '';
        ctrl.statusFilter = '';
        ctrl.boothTypeFilter = ''

        ctrl.applications = [];
        ctrl.boothTypes = [];
        ctrl.publicIdentifier = $stateParams.eventName || $stateParams.subscriptionId;
        VendorBoothTypeService.setPublicIdentifier(ctrl.publicIdentifier);
        VendorApplicationService.setPublicIdentifier(ctrl.publicIdentifier);
        ctrl.contextType = $stateParams.eventName ? 'event' : 'subscription';
        ctrl.itemsPerPage = 50;
        ctrl.addorEditApplication = addOrEditApplication;
        ctrl.deleteApplication = function(application) {
            if (confirm('Are you sure you want to delete this application?')) {
                VendorApplicationService.deleteApplication(application.id).then(function () {
                }, function (error) {
                    console.error('Error deleting application:', error);
                });
                loadData();
            }
        };
        ctrl.loadData = loadData();
        ctrl.updateFilteredData = function() {
            loadData();
        }

        loadData();

        function loadData() {
            $location.search({page: ctrl.currentPage, search: ctrl.toSearch});
            VendorApplicationService.loadApplicationList(ctrl.contextType, ctrl.publicIdentifier, ctrl.currentPage - 1, ctrl.toSearch).success(function(results) {
                ctrl.applications = results.left;
                ctrl.totalItems = results.right;
            });
            VendorBoothTypeService.loadBoothTypeList(ctrl.contextType, ctrl.publicIdentifier).success(function(boothTypes) {
                ctrl.boothTypes = boothTypes.left;
            });
        }


        function addOrEditApplication(application = {}) {
            $uibModal.open({
                templateUrl: window.ALFIO_CONTEXT_PATH + '/resources/angular-templates/admin/partials/vendor-application/vendor-application-modal.html',
                backdrop: 'static',
                controller: function ($scope) {
                    $scope.application = application || {};
                    $scope.save = function () {
                        if ($scope.application.id) {
                            VendorApplicationService.updateApplication(application).then(function () {
                                loadData();
                                $scope.$close();
                            }, function (error) {
                                console.error('Error updating application:', error);
                            });
                        } else {
                            VendorApplicationService.createApplication(application).then(function () {
                                loadData();
                                $scope.$close();
                            }, function (error) {
                                console.error('Error creating application:', error);
                            });
                        }
                    };
                    $scope.cancel = function () {
                        $scope.$dismiss();
                    };
                }
            });
        }

        ctrl.filteredApplications = function () {
        return ctrl.applications.filter(function (app) {
        const matchesStatus = !ctrl.statusFilter || app.status === ctrl.statusFilter;
        const matchesBooth = !ctrl.boothTypeFilter || app.boothType === ctrl.boothTypeFilter;
        return matchesStatus && matchesBooth;
    });
};


    }

    VendorApplicationListController.prototype.$inject = ['VendorApplicationService', '$location', '$stateParams'];

    function VendorApplicationDetailController(VendorApplicationService, $stateParams) {
        var self = this;

       
        self.publicIdentifier = $stateParams.eventName;
        self.contextType = $stateParams.eventName;
        VendorApplicationService.loadApplicationDetail(self.contextType, self.publicIdentifier, $stateParams.applicationId).success(function(result) {
            self.application = result;
        });
    }

    VendorApplicationDetailController.prototype.$inject = ['VendorApplicationService', 'VendorBoothTypeService', '$stateParams'];

    function VendorApplicationService($http, HttpErrorHandler) {
        let publicIdentifier = null;

        this.setPublicIdentifier = function (id) {
            publicIdentifier = id;
        };

        this.loadApplicationList = function(contextType, page, search) {
            return $http.get('/admin/api/'+publicIdentifier+'/vendor-application', {params: {page: page, pageSize: 50, search: search}}).error(HttpErrorHandler.handle);
        };

        this.loadApplicationDetail = function(contextType, applicationId) {
            return $http.get('/admin/api/'+publicIdentifier+'/vendor-application/'+applicationId).error(HttpErrorHandler.handle);
        }
         
         this.createApplication = function (application) {
            return $http.post('/admin/api/' + publicIdentifier + '/vendor-application', application,
                { headers: { 'Content-Type': 'application/json' } }
            );
         }

         this.updateApplication = function (application) {
            return $http.put('/admin/api/' + publicIdentifier + '/vendor-application/' + application.id, application,
                { headers: { 'Content-Type': 'application/json' } }
            );
         }

         this.deleteApplication = function (applicationId) {
             return $http.delete('/admin/api/' + publicIdentifier + '/vendor-application/' + applicationId)
                 .error(function (error) {
                     console.error('Error deleting application:', error);
                 });
         }
    }

    VendorApplicationService.prototype.$inject = ['$http', 'HttpErrorHandler'];

     function VendorBoothTypeService($http) {
        let publicIdentifier = null;

    this.setPublicIdentifier = function (id) {
        publicIdentifier = id;
         };
         
        this.loadBoothTypeList = function (contextType, page, search) {
            return $http.get('/admin/api/' + publicIdentifier + '/vendor-booth-type', {
                params: {
                    contextType: contextType,
                    publicIdentifier: publicIdentifier,
                    page: page,
                    search: search
                }
            });
         };
       
    }

    VendorBoothTypeService.$inject = ['$http', 'HttpErrorHandler'];

})();
