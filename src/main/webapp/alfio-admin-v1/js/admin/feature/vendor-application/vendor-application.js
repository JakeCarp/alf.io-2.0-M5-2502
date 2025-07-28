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
        .filter('truncateString', function() {
            return function(string, maxLength) {
                if(!angular.isDefined(string)) {
                    return "";
                }
                var l = angular.isDefined(maxLength) ? maxLength : 50;
                return string.length > l ? (string.substring(0, l-4) + '...') : string;
            }
        });


    function VendorApplicationListController(VendorApplicationService, $location, $stateParams) {
        var ctrl = this;

        var currentSearch = $location.search();
        ctrl.currentPage = currentSearch.page || 1;
        ctrl.toSearch = currentSearch.search || '';

        ctrl.applications = [];
        ctrl.publicIdentifier = $stateParams.eventName || $stateParams.subscriptionId;
        ctrl.contextType = $stateParams.eventName ? 'event' : 'subscription';
        ctrl.itemsPerPage = 50;
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
        }


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

    VendorApplicationDetailController.prototype.$inject = ['VendorApplicationService', '$stateParams'];

    function VendorApplicationService($http, HttpErrorHandler) {

        this.loadApplicationList = function(type, publicIdentifier, page, search) {
            return $http.get('/admin/api/'+publicIdentifier+'/vendor-application', {params: {page: page, pageSize: 50, search: search}}).error(HttpErrorHandler.handle);
        };

        this.loadApplicationDetail = function(type, publicIdentifier, applicationId) {
            return $http.get('/admin/api/'+publicIdentifier+'/vendor-application/'+applicationId).error(HttpErrorHandler.handle);
        }
    }

    VendorApplicationService.prototype.$inject = ['$http', 'HttpErrorHandler'];

})();
