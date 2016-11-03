(function() {
	angular.module('JPIApp', [])
		.controller('AddressController', function($http) {
			let ctrl = this;
			ctrl.address = "";
			ctrl.postcode = 9999999;

			this.lookup = function() {
				$http.get(['/', this.postcode].join(''))
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
		});
})();
