(function() {
	var app = angular.module('address', []);
	app.controller('AddressController', ['$http', function($http) {
		ctrl = this;
		ctrl.address = "";
		ctrl.postcode = 9999999;

		this.lookup = function() {
			$http.get('/'+this.postcode)
			.success(function(data) {
				ctrl.address = [
					data.prefecture_kanji,
					data.municipality_kanji,
					data.neighbourhood_kanji
				].join('\n');
			})
			.error(function(data) {
				ctrl.address = "";
			});
		};
	}]);

})();
