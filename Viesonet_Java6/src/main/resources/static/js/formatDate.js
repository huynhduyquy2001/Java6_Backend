$scope.getFormattedTimeAgo = function(date) {
              	  var currentTime = new Date();
              	  var activityTime = new Date(date);
              	  var timeDiff = currentTime.getTime() - activityTime.getTime();
              	  var seconds = Math.floor(timeDiff / 1000);
              	  var minutes = Math.floor(timeDiff / (1000 * 60));
              	  var hours = Math.floor(timeDiff / (1000 * 60 * 60));
              	  var days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));

              	  if (days === 0) {
              	    if (hours === 0 && minutes < 60) {
              	      return minutes + ' phút trước';
              	    } else if (hours < 24) {
              	      return hours + ' giờ trước';
              	    }
              	  } else if (days === 1) {
              	    return 'hôm qua';
              	  } else {
              	    return days + ' ngày trước';
              	  }
              	};