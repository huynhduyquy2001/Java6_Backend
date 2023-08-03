angular.module('myApp').service('dataService', function() {
    var data = {};

    return {
        getData: function() {
            return data;
        },
        setData: function(userId) {
            data.userId = userId;
        }
    };
});
