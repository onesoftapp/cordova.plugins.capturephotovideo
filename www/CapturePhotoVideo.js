var exec = require('cordova/exec');

module.exports = {

	capturePhoto: function(successCallback, errorCallback) {
		exec(successCallback, errorCallback, 'CapturePhotoVideo', 'capturePhoto', []);
	},

	captureVideo: function(successCallback, errorCallback) {
		exec(successCallback, errorCallback, 'CapturePhotoVideo', 'captureVideo', []);
	}

};
