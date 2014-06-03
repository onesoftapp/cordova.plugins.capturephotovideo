package cordova.plugins.capturephotovideo;
import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * class extends SurfaceView, main purpose is to refresh the preview
 * 
 * 
 */
public class MySurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private CameraManager cameraManager = null;

	public MySurfaceView(Context paramContext) {
		super(paramContext);
		initCameraManager(paramContext);
	}

	public MySurfaceView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		initCameraManager(paramContext);
	}

	public MySurfaceView(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		initCameraManager(paramContext);
	}

	public void initCameraManager(Context paramContext) {
		cameraManager = ((CaptureVideoActivity) paramContext).getCameraManager();
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		cameraManager.rePreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		cameraManager.startCamera(holder);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		cameraManager.closeCamera();

	}

}
