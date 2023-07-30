// Tạo một Service để lưu trữ và chia sẻ dữ liệu
angular.module('myApp').service('dataService', function() {
  var sharedData = {};

  return {
    setData: function(data) {
      sharedData = data;
    },
    getData: function() {
      return sharedData;
    }
  };
});