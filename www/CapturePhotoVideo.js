var exec = require('cordova/exec');

module.exports = {

	capturePhoto: function(callback, option) {
		if (!option) option = {};
		if (!option.width) option.width = 1280;
		if (!option.height) option.height = 800;
		if (!option.quality) option.quality = 75;

		exec(callback, null, 'CapturePhotoVideo', 'capturePhoto', [option.width, option.height, option.quality]);
	},

	captureVideo: function(callback) {
		exec(callback, null, 'CapturePhotoVideo', 'captureVideo', []);
	}

};