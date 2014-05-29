package cordova.plugins.capturephotovideo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;

import org.apache.commons.codec.binary.Base64;
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

    private CallbackContext callbackContext;

    public CapturePhotoVideo() {

    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
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
            return captureVideo();
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                break;
            }
        } else if (Activity.RESULT_CANCELED == resultCode) {
            this.failPicture("Camera cancelled.");
        }
    }

    private boolean capturePhoto() {
        if (null != callbackContext) {
            Intent intent = new Intent()
            .putExtra("CompressWidth", CompressWidth)
            .putExtra("CompressHeight", CompressHeight)
            .putExtra("CompressOption", CompressOption)
            .setClassName(cordova.getActivity(), CLASS);
            cordova.startActivityForResult(this, intent, CAMERA_PHOTO);
            return true;
        } else {
            return false;
        }
    }

    private boolean captureVideo() {
        if (null != callbackContext) {
            Intent intent = new Intent()
            .setClassName(cordova.getActivity(), CLASS_VIDEO);
            cordova.startActivityForResult(this, intent, APTURE_VIDEO);
            return true;
        } else {
            return false;
        }
    }

    private String encodeFileToBase64String(String filename) throws IOException {
        File file = new File(filename);

        try (FileInputStream stream = new FileInputStream(file)) {
            byte[] bytes = new byte[(int)file.length];

            if (bytes.length == stream.read(bytes, 0, bytes.length)) {
                byte[] encoded = Base64.encodeBase64(bytes);
                String encodedString = new String(encoded);

                return encodedString;
            }
        }
    }

    private void failPicture(String err) {
        this.callbackContext.error(err);
    }
}
