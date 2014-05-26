package cordova.plugins.capturephotovideo;

import java.io.File;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 受摄像头捕获的画面
 * */
@SuppressLint("ViewConstructor")
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private static final String TAG = CameraPreview.class.getSimpleName();
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private Context context;
	private int compressOption;
	private int compressHeight;
	private int compressWidth;

	public CameraPreview(Context context, int compressOption, int compressHeight, int compressWidth) {
		super(context);
		this.context = context;
		this.compressOption = compressOption;
		this.compressHeight = compressHeight;
		this.compressWidth = compressWidth;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		// if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// } else {

		// }

	}

	public void surfaceCreated(SurfaceHolder holder) {
		// if (mHolder.getSurface() == null) {
		// return;
		// } else {
		// mHolder = holder;
		// }
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		if (mCamera == null) {
			// mCamera=Util.getCameraInstance();
			new LoadCamera(holder).execute("");
		}
	}

	/**
	 * activity 调用Pause后在调用此方法
	 * */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
		Log.w("warn", "surface---destroyed");
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// Camera.Parameters parameters = camera.getParameters();
		// 以下注释掉的是设置预览时的图像以及拍照的一些参数
		// parameters.setPictureFormat(PixelFormat.JPEG);
		// parameters.setPreviewSize(parameters.getPictureSize().width,
		// parameters.getPictureSize().height);
		// parameters.setFocusMode("auto");
		// parameters.setPictureSize(width, height);
		// camera.setParameters(parameters);

		// start preview with new settings

		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d(Constant.TAG,
					"Error starting camera preview: " + e.getMessage());
		}
	}

	/**
	 *打开照相机
	 * */
	class LoadCamera extends AsyncTask<String, Integer, String> {

		CustomeDialog dialog = new CustomeDialog(context, 160, 120,
				Constant.layout.dialog_layout2, Constant.style.Theme_dialog, "正在打开相机..");// Dialog使用默认大小(160)
		SurfaceHolder holder;

		public LoadCamera(SurfaceHolder holder) {
			this.holder = holder;
		}

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			mCamera = Util.getCameraInstance();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog != null) {
				dialog.cancel();
				dialog.dismiss();
			}
			mCamera.setDisplayOrientation(90);
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mCamera.startPreview();
			super.onPostExecute(result);

		}
	}

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			//图片路径
			File pictureFile = Util
					.getOutputMediaFile(Constant.IMAGE_PICTURE,Constant.MEDIA_TYPE_IMAGE);
			//快照路径
			File thumbFile = Util
					.getOutputMediaFile(Constant.IMAGE_THUMB,Constant.MEDIA_TYPE_IMAGE);
			Toast.makeText(context, pictureFile.getAbsolutePath(),
					Toast.LENGTH_LONG).show();
			if (pictureFile == null) {
				Log.d(Constant.TAG,
						"Error creating media file, check storage permissions: ");
				return;
			}

			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			// 压缩
			Util.compressBmpToFile(bitmap, pictureFile,thumbFile,
					compressOption, compressWidth,
					compressHeight,context);
			camera.startPreview();// 拍照结束后画面不动 每次拍照结束后重新预览
		}
	};

	public void takePhoto(Context context, ImageView btn) {
		Bitmap recentPhoto = Util.getRecentPhoto(context);
		if (recentPhoto != null) {

			btn.setImageBitmap(recentPhoto);
		} else {
			btn.setImageResource(Constant.drawable.album);
		}
		mCamera.takePicture(null, null, mPicture);
	}

}