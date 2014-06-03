package cordova.plugins.capturephotovideo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.view.SurfaceView;

/**
 * 视频录制压缩合并
 * 
 * @authorjiang
 * 
 */
public class RecorderManager {
	private MediaRecorder mediaRecorder = null;
	private CameraManager cameraManager = null;
	private String parentPath = null;
	private List<String> videoTempFiles = new ArrayList<String>();
	private SurfaceView mySurfaceView = null;
	private boolean isMax = false;
	private long videoStartTime;
	private int totalTime = 0;
	private boolean isStart = false;
	Context context = null;
	private final Semaphore semp = new Semaphore(1);
	public int VideoOption, VideoWidth, VideoHeight, CaptureTime;

	public RecorderManager(CameraManager cameraManager,
			SurfaceView mySurfaceView, Context context, int videoOption,
			int videoWidth, int videoHeight, int captureTime) {
		this.cameraManager = cameraManager;
		this.mySurfaceView = mySurfaceView;
		this.context = context;
		this.VideoHeight = videoHeight;
		this.VideoOption = videoOption;
		this.VideoWidth = videoWidth;
		this.CaptureTime = captureTime;
		parentPath = generateParentFolder();
		reset();
	}

	private Camera getCamera() {
		return cameraManager.getCamera();
	}

	public boolean isStart() {
		return isStart;
	}

	public long getVideoStartTime() {
		return videoStartTime;
	}

	public int checkIfMax(long timeNow) {
		int during = 0;
		if (isStart) {
			during = (int) (totalTime + (timeNow - videoStartTime));
			if (during >= CaptureTime) {
				stopRecord();
				during = CaptureTime;
				isMax = true;
			}
		} else {
			during = totalTime;
			if (during >= CaptureTime) {
				during = CaptureTime;
			}
		}

		return during;
	}

	public void reset() {
		for (String file : videoTempFiles) {
			File tempFile = new File(file);
			if (tempFile.exists()) {
				tempFile.delete();
			}
		}
		videoTempFiles = new ArrayList<String>();
		isStart = false;
		totalTime = 0;
		isMax = false;
	}

	public List<String> getVideoTempFiles() {
		return videoTempFiles;
	}

	public String getVideoParentpath() {
		return parentPath;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public void startRecord(boolean isFirst) {
		if (isMax) {
			return;
		}
		semp.acquireUninterruptibly();
		getCamera().stopPreview();
		mediaRecorder = new MediaRecorder();
		Camera camera = cameraManager.getCamera();
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPreviewFpsRange(30, 45);
		parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
		camera.unlock();
		mediaRecorder.setCamera(camera);
		if (cameraManager.isUseBackCamera()) {
			mediaRecorder.setOrientationHint(90);
		} else {
			mediaRecorder.setOrientationHint(90 + 180);
		}
		Size defaultSize = cameraManager.getDefaultSize();

		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// �输出格式为mp4
		if (defaultSize != null) {
			mediaRecorder.setVideoSize(defaultSize.width, defaultSize.height);
		} else {
			mediaRecorder.setVideoSize(VideoWidth, VideoHeight);
		}
		mediaRecorder.setVideoEncodingBitRate(VideoOption * 1024 * 1024);
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);// 视频编码
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频编码
		mediaRecorder.setMaxDuration(10000);// 最大期限
		String fileName = parentPath + "/" + videoTempFiles.size() + ".mp4";
		mediaRecorder.setOutputFile(fileName);
		mediaRecorder.setPreviewDisplay(mySurfaceView.getHolder().getSurface());// 预览
		videoTempFiles.add(fileName);
		mediaRecorder.setPreviewDisplay(mySurfaceView.getHolder().getSurface());
		try {
			mediaRecorder.prepare();
		} catch (Exception e) {
			e.printStackTrace();
			stopRecord();
		}

		try {
			mediaRecorder.start();
			isStart = true;
			videoStartTime = new Date().getTime();

		} catch (Exception e) {
			e.printStackTrace();
			if (isFirst) {
				startRecord(false);
			} else {
				stopRecord();
			}
		}
	}

	public void stopRecord() {
		if (!isMax) {
			totalTime += new Date().getTime() - videoStartTime;
			videoStartTime = 0;
		}
		//
		isStart = false;

		//
		if (mediaRecorder == null) {
			return;
		}
		try {
			mediaRecorder.setPreviewDisplay(null);
			mediaRecorder.stop();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				mediaRecorder.reset();
				mediaRecorder.release();
				mediaRecorder = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				getCamera().reconnect();
			} catch (Exception e) {
			}
			getCamera().lock();
			semp.release();
		}

	}

	public String generateParentFolder() {
		String parentPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/mycapture/video/temp";
		File tempFile = new File(parentPath);
		if (!tempFile.exists()) {
			tempFile.mkdirs();
		}
		return parentPath;

	}
}
