	app.config(function($translateProvider, $routeProvider) {
		$translateProvider.useStaticFilesLoader({
			prefix: '/json/', // Thay đổi đường dẫn này cho phù hợp
			suffix: '.json'
		});
		$routeProvider.when('/', {
			templateUrl: "/ngview/home.html",
			controller: 'IndexController'
		}).when('/search', {
			templateUrl: "/ngview/search.html",
			controller: 'SearchController'
		}).when('/recommend', {
			templateUrl: "/ngview/recommend.html",
			controller: 'RecommendController'
		}).when('/message', {
			templateUrl: "/ngview/message.html",
			controller: 'MessageController'
		});
		// Set the default language
		var storedLanguage = localStorage.getItem('myAppLangKey') || 'vie';
		$translateProvider.preferredLanguage(storedLanguage);
	})