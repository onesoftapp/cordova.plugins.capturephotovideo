package cordova.plugins.capturephotovideo;

import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;

public class CapturePhotoVideo extends CordovaPlugin {
	public static final String TAG = "CapturePhotoVideo";
	public static final int CAMERA_PHOTO = 100;
	public static final int APTURE_VIDEO = 200;
	// 0-100,100表示不压缩,值越小压缩
	public static int CompressOption = 95;
	// 压缩相片尺寸
	public static int CompressHeight = 320;
	public static int CompressWidth = 280;
	private CallbackContext callbackContext;

	public CapturePhotoVideo() {
	}

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {

		this.callbackContext = callbackContext;

		if (action.equals("capturePhoto")) {

			if (args != null&&args.length()>0) {
				if (args.getInt(2) > 0 && args.getInt(2) <= 100) {
					CompressOption = args.getInt(2);
				}

				if (args.getInt(1) > 0) {
					CompressHeight = args.getInt(1);
				}
				if (args.getInt(0) > 0) {
					CompressWidth = args.getInt(0);
				}
			}

			return capturePhoto();
		} else if (action.equals("captureVideo")) {
			return captureVideo();
		} else {
			return false;
		}

	}

	private boolean capturePhoto() {
		if (callbackContext != null) {
			Intent intent = new Intent();
			intent.putExtra("CompressOption", CompressOption);
			intent.putExtra("CompressHeight", CompressHeight);
			intent.putExtra("CompressWidth", CompressWidth);
			intent.setClassName(cordova.getActivity(),
					"com.onesoft.cordova.plugins.openalbumandcamera.CapturePhotoActivity");
			cordova.startActivityForResult(this, intent, CAMERA_PHOTO);
			return true;
		} else {
			return false;
		}
	}

	private boolean captureVideo() {
		if (callbackContext != null) {
			Intent intent = new Intent()
					.setClassName(cordova.getActivity(),
							"com.onesoft.cordova.plugins.openalbumandcamera.CaptureVideoActivity");
			cordova.startActivityForResult(this, intent, APTURE_VIDEO);
			return true;
		} else {
			return false;
		}
	}

	public void failPicture(String err) {
		this.callbackContext.error(err);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case CAMERA_PHOTO:
				List<String> json = data.getStringArrayListExtra("list");

				System.out.print(json);
				JSONArray path = new JSONArray();
				for (int i = 0; i < json.size(); i++) {
					JSONObject j = JsonUtil.getJson(json.get(i));
					path.put(j);
				}
				callbackContext.success(path);
				break;

			case APTURE_VIDEO:

			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			this.failPicture("Camera cancelled.");
		}
	}
}
