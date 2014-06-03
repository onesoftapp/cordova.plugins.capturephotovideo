package cordova.plugins.capturephotovideo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Base64;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CapturePhotoVideo extends CordovaPlugin {
	public static final String TAG = "CapturePhotoVideo";

	public static final String CLASS_PHOTO = "cordova.plugins.capturephotovideo.CapturePhotoActivity";
	public static final String CLASS_VIDEO = "cordova.plugins.capturephotovideo.CaptureVideoActivity";
	public static final int APTURE_VIDEO = 200;
	public static final int CAMERA_PHOTO = 100;
	public static int CompressWidth = 1280;
	public static int CompressHeight = 800;
	public static int CompressOption = 75;
	public static int VideoOption = 5;//1-8(相当于100－800万像素）值越大越清晰
	public static int VideoWidth = 480;
	public static int VideoHeight = 480;
	public static int CaptureTime = 8000;

	private CallbackContext callbackContext;

	public CapturePhotoVideo() {

	}

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;

		if (action.equals("capturePhoto")) {
			if (null != args && 0 < args.length()) {
				if (0 < args.getInt(0)) {
					CompressWidth = args.getInt(0);
				}
				if (0 < args.getInt(1)) {
					CompressHeight = args.getInt(1);
				}
				if (0 < args.getInt(2) && 100 >= args.getInt(2)) {
					CompressOption = args.getInt(2);
				}
			}

			return capturePhoto();
		} else if (action.equals("captureVideo")) {
			if (null != args && 0 < args.length()) {
				if (0 < args.getInt(0)) {
					VideoWidth = args.getInt(0);
				}
				if (0 < args.getInt(1)) {
					VideoHeight = args.getInt(1);
				}
				if (0 < args.getInt(2) && 8 >= args.getInt(2)) {
					VideoOption = args.getInt(2);
				}
				if (6000 < args.getInt(3) && 12000 >= args.getInt(3)) {
					CaptureTime = args.getInt(3);
				}
			}
			return captureVideo();
		} else {
			return false;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			if (Activity.RESULT_OK == resultCode) {
				switch (requestCode) {
				case CAMERA_PHOTO:
					List<String> json = data.getStringArrayListExtra("list");

					System.out.print(json);

					JSONArray path = new JSONArray();

					for (int i = 0, c = json.size(); c > i; i++) {
						JSONObject j = JsonUtil.getJson(json.get(i));
						String imagePath = j.getString("imagePath");
						String imageBase64String = encodeFileToBase64String(imagePath);
						String thumbnailPath = j.getString("thumbnailPath");
						String thumbnailBase64String = encodeFileToBase64String(thumbnailPath);
						j.put("imageBase64String", imageBase64String);
						j.put("thumbnailBase64String", thumbnailBase64String);
						path.put(j);
					}
					callbackContext.success(path);
					break;
				case APTURE_VIDEO:
					List<String> json1 = data.getStringArrayListExtra("list");
					System.out.print(json1);
					JSONArray path1 = new JSONArray();
					for (int i = 0, c = json1.size(); c > i; i++) {
						JSONObject j = JsonUtil.getvJson(json1.get(i));
						String videoPath = j.getString("videoPath");
						String imageBase64String = encodeFileToBase64String(videoPath);
						String thumbnailPath = j.getString("thumbnailPath");
						String thumbnailBase64String = encodeFileToBase64String(thumbnailPath);
						j.put("imageBase64String", imageBase64String);
						j.put("thumbnailBase64String", thumbnailBase64String);
						path1.put(j);
					}
					callbackContext.success(path1);
					break;
				}
			} else if (Activity.RESULT_CANCELED == resultCode) {
				this.failPicture("Camera cancelled.");
			}
		} catch (IOException e) {
			System.out.print(e);
		} catch (JSONException e) {
			System.out.print(e);
		}
	}

	private boolean capturePhoto() {
		if (null != callbackContext) {
			Intent intent = new Intent()
					.putExtra("CompressWidth", CompressWidth)
					.putExtra("CompressHeight", CompressHeight)
					.putExtra("CompressOption", CompressOption)
					.setClassName(cordova.getActivity(), CLASS_PHOTO);
			cordova.startActivityForResult(this, intent, CAMERA_PHOTO);
			return true;
		} else {
			return false;
		}
	}

	private boolean captureVideo() {
		if (null != callbackContext) {
			Intent intent = new Intent().putExtra("VideoOption", VideoOption)
					.putExtra("VideoWidth", VideoWidth)
					.putExtra("VideoHeight", VideoHeight)
					.putExtra("CaptureTime", CaptureTime)
					.setClassName(cordova.getActivity(), CLASS_VIDEO);
			cordova.startActivityForResult(this, intent, APTURE_VIDEO);
			return true;
		} else {
			return false;
		}
	}

	private String encodeFileToBase64String(String filename) throws IOException {
		File file = new File(filename);
		FileInputStream stream = null;

		try {
			stream = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];

			if (bytes.length == stream.read(bytes, 0, bytes.length)) {
				String encodedString = Base64.encodeToString(bytes,
						Base64.DEFAULT);

				return encodedString;
			}
		} finally {
			stream.close();
		}

		return null;
	}

	private void failPicture(String err) {
		this.callbackContext.error(err);
	}
}
