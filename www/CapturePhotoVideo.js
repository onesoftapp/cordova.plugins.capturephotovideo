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
		if (!option) option = {};
		if (!option.width) option.width = 480; //宽
		if (!option.height) option.height = 480; //高
		if (!option.quality) option.quality = 5; //质量(1-8)对应100－800万像素
		if (!option.time) option.time = 8000; //录制时间
		exec(callback, null, 'CapturePhotoVideo', 'captureVideo', [option.width, option.height, option.quality, option.time]);
	}

};