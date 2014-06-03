package cordova.plugins.capturephotovideo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

/**
 * 短视频录制
 * 
 * @author jiang
 * 
 */
public class CaptureVideoActivity extends ActivitySupport implements
		OnClickListener {

	private SurfaceView videoSurface = null;
	private RecorderManager recorderManager = null;
	// 相继管理类
	CameraManager cameraManager;
	// 录制进度条
	private ProgressView progressView = null;
	private VideoView videoView = null;
	private boolean isPlaying = false;
	private Runnable progressRunnable = null;
	private RelativeLayout topmask, bottom_mask, select_list_picture;
	private ImageView btn;
	private View finishView = null;
	private Button swithButton = null;
	private Button camerafeatures = null;
	Handler handler = null;
	private GridView gridView;
	private List<VideoItem> videoList;
	private VGridviewAdapter gridAdapter;
	private String videoPath;
	private String myVideoPath;
	public static int VideoOption, VideoWidth, VideoHeight, CaptureTime;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(Constant.layout.activity_capture_album);
		videoSurface = (SurfaceView) findViewById(Constant.id.cameraView);
		videoView = (VideoView) findViewById(Constant.id.mediaShow);
		swithButton = (Button) findViewById(Constant.id.switchCamera);
		progressView = (ProgressView) findViewById(Constant.id.progress);
		camerafeatures = (Button) findViewById(Constant.id.camera_features);
		topmask = (RelativeLayout) findViewById(Constant.id.top_mask);
		bottom_mask = (RelativeLayout) findViewById(Constant.id.bottom_mask);
		gridView = (GridView) findViewById(Constant.id.gridview);
		select_list_picture = (RelativeLayout) findViewById(Constant.id.select_list_picture);
		btn = (ImageView) findViewById(Constant.id.ablum_btn_main);
		btn.setOnClickListener(this);
		findViewById(Constant.id.back3_btn).setOnClickListener(this);
		findViewById(Constant.id.complete_tv).setOnClickListener(this);
		Intent data = getIntent();
		VideoOption = data.getExtras().getInt("VideoOption");
		VideoWidth = data.getExtras().getInt("VideoWidth");
		VideoHeight = data.getExtras().getInt("VideoHeight");
		CaptureTime = data.getExtras().getInt("CaptureTime");
		cameraManager = getCameraManager();
		recorderManager = new RecorderManager(cameraManager, videoSurface,
				context, VideoOption, VideoWidth, VideoHeight, CaptureTime);
		finishView = findViewById(Constant.id.finishLayOut);
		updateBtn();
		initSelectPhoto();
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				mp.setLooping(true);

			}
		});
		camerafeatures.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				capture(v, event);
				return true;
			}
		});
		// videoSurface.setOnTouchListener(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// capture(v, event);
		// return true;
		// }
		//
		// });
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				int total = ((ViewGroup) progressView.getParent())
						.getMeasuredWidth();
				if (msg.arg1 < CaptureTime) {
					finishView.setVisibility(View.INVISIBLE);

				} else {

					finishView.setVisibility(View.VISIBLE);

				}
				double length = msg.arg1 * 1.0 / CaptureTime * total;
				progressView.setWidth((int) length);
				progressView.invalidate();
				super.handleMessage(msg);
			}
		};
		progressRunnable = new ProgressRunnable();
		handler.post(progressRunnable);

	}

	private void initCamera() {
		swithButton.setVisibility(View.VISIBLE);
		recorderManager.reset();
		videoSurface.setVisibility(SurfaceView.VISIBLE);
		isPlaying = false;
		recorderManager.reset();
	}

	private void initSelectPhoto() {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				videoPath = videoList.get(pos).getVideoPath();
				select_list_picture.setVisibility(View.INVISIBLE);
				topmask.setVisibility(View.VISIBLE);
				swithButton.setVisibility(View.INVISIBLE);
				videoView.setVisibility(SurfaceView.VISIBLE);
				videoView.setVideoPath(videoPath);
				videoView.start();
			}
		});

	}

	private void updateBtn() {
		Bitmap recentPhoto = Util.getVideoPhoto(context);
		if (recentPhoto != null) {

			btn.setImageBitmap(recentPhoto);
		} else {
			btn.setImageResource(Constant.drawable.album);
		}
	}

	private void capture(View v, MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			try {
				swithButton.setVisibility(View.INVISIBLE);
				recorderManager.startRecord(true);
			} finally {
				muteAll(true);
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			try {
				recorderManager.stopRecord();
			} finally {
				muteAll(false);
				//
			}
		}
	}

	public void muteAll(boolean isMute) {
		List<Integer> streams = new ArrayList<Integer>();
		Field[] fields = AudioManager.class.getFields();
		for (Field field : fields) {
			if (field.getName().startsWith("STREAM_")
					&& Modifier.isStatic(field.getModifiers())
					&& field.getType() == int.class) {
				try {
					Integer stream = (Integer) field.get(null);
					streams.add(stream);
				} catch (IllegalArgumentException e) {

				} catch (IllegalAccessException e) {

				}
			}
		}
	}

	public CameraManager getCameraManager() {
		if (cameraManager == null) {
			cameraManager = new CameraManager();
		}
		return cameraManager;
	}

	public void onBackPressed(View view) {
		swithButton.setVisibility(View.VISIBLE);
		stopPlay();
		recorderManager.reset();
		videoView.setVisibility(SurfaceView.GONE);
		videoSurface.setVisibility(SurfaceView.VISIBLE);
		bottom_mask.setVisibility(View.VISIBLE);
		isPlaying = false;
		recorderManager.reset();
	}

	public void onFinishPressed(View view) {
		if (!isPlaying && recorderManager.getVideoTempFiles().size() != 0) {
			startPlay();
			isPlaying = true;
		} else {
			recorderManager.reset();
			videoView.setVisibility(SurfaceView.GONE);
			videoSurface.setVisibility(SurfaceView.VISIBLE);
			isPlaying = false;
		}
	}

	public void onCameraSwitchPressed(View view) {
		if (!isPlaying) {
			cameraManager.changeCamera(videoSurface.getHolder());
		}
	}

	public void startPlay() {
		combineFiles();
		recorderManager.reset();
		videoSurface.setVisibility(SurfaceView.GONE);
		topmask.setVisibility(View.VISIBLE);
		swithButton.setVisibility(View.INVISIBLE);
		bottom_mask.setVisibility(View.INVISIBLE);
		videoView.setVisibility(SurfaceView.VISIBLE);
		videoView.setVideoPath(myVideoPath);
		videoView.start();
	}

	/**
	 * 合并视频并生成缩略图保存到数据库
	 */
	private void combineFiles() {
		try {
			List<Track> videoTracks = new LinkedList<Track>();
			List<Track> audioTracks = new LinkedList<Track>();
			for (String fileName : recorderManager.getVideoTempFiles()) {
				try {
					Movie movie = MovieCreator.build(fileName);
					for (Track t : movie.getTracks()) {
						if (t.getHandler().equals("soun")) {
							audioTracks.add(t);
						}
						if (t.getHandler().equals("vide")) {
							videoTracks.add(t);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Movie result = new Movie();

			if (audioTracks.size() > 0) {
				result.addTrack(new AppendTrack(audioTracks
						.toArray(new Track[audioTracks.size()])));
			}
			if (videoTracks.size() > 0) {
				result.addTrack(new AppendTrack(videoTracks
						.toArray(new Track[videoTracks.size()])));
			}

			Container out = new DefaultMp4Builder().build(result);

			myVideoPath = getFinalVideoFileName();
			String thumbnail = getVideoThumbnail(myVideoPath);
			Util.saveVideoThumbnail(context, myVideoPath, thumbnail);
			FileChannel fc = new RandomAccessFile(String.format(myVideoPath),
					"rw").getChannel();
			out.writeContainer(fc);
			fc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 视频路径
	 * 
	 * @return
	 */
	public String getFinalVideoFileName() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		return recorderManager.getVideoParentpath() + "/VID_" + timeStamp
				+ ".mp4";
	}

	private void stopPlay() {
		try {
			videoView.stopPlayback();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		muteAll(false);
		super.onDestroy();
		recorderManager.reset();
		handler.removeCallbacks(progressRunnable);
	}

	/**
	 * 录像进度条
	 * 
	 * @author Administrator
	 * 
	 */
	private class ProgressRunnable implements Runnable {

		@Override
		public void run() {
			int time = 0;
			time = recorderManager.checkIfMax(new Date().getTime());
			Message message = new Message();
			message.arg1 = time;
			handler.sendMessage(message);
			// System.out.println(time);
			handler.postDelayed(this, 10);

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case Constant.id.ablum_btn_main:
			recorderManager.reset();
			videoSurface.setVisibility(SurfaceView.GONE);
			AlbumHelper helper = new AlbumHelper(context, false);
			videoList = helper.getVideoList(false);
			topmask.setVisibility(View.INVISIBLE);
			bottom_mask.setVisibility(View.INVISIBLE);
			if (videoList != null && videoList.size() > 0) {
				gridAdapter = new VGridviewAdapter(context, videoList);
				gridView.setAdapter(gridAdapter);
				gridAdapter.notifyDataSetChanged();
				select_list_picture.setVisibility(View.VISIBLE);
			} else {
				select_list_picture.setVisibility(View.VISIBLE);
				showToast("你的相册是空的,请拍照");
			}
			break;
		case Constant.id.back3_btn:
			select_list_picture.setVisibility(View.INVISIBLE);
			topmask.setVisibility(View.VISIBLE);
			bottom_mask.setVisibility(View.VISIBLE);
			initCamera();
			break;
		case Constant.id.complete_tv:
			// 存放选中的图片
			ArrayList<String> list = new ArrayList<String>();
			JSONObject json = new JSONObject();
			boolean[] isSelect = gridAdapter.getIsSelected();
			for (int i = 0; i < isSelect.length; i++) {
				if (isSelect[i]) {
					try {
						json.put("thumbnailPath", videoList.get(i)
								.getThumbnailPath());
						json.put("videoPath", videoList.get(i).getVideoPath());

						list.add(json.toString().replace("\\", ""));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			if (videoList.size() == 0) {
				Util.showToast("当前无选中视频", this);
			} else {
				Util.showToast("当前选中视频:" + videoList.size(), this);
			}
			Intent data = new Intent();
			data.putExtra("list", list);
			setResult(RESULT_OK, data);
			finish();
			break;

		}
	}

	/**
	 * 生成缩略图得到url
	 * 
	 * @param filePath
	 * @return
	 */
	private String getVideoThumbnail(String filePath) {
		// Bitmap bitmap = null;
		File thumbFile = null;
		View view = this.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		Rect frame = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = this.getWindowManager().getDefaultDisplay().getWidth();
		int height = this.getWindowManager().getDefaultDisplay().getHeight();
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();

		// Uri uri = Uri.parse(filePath);
		// android.media.MediaMetadataRetriever retriever = new
		// android.media.MediaMetadataRetriever();
		// try {
		// retriever.setDataSource(this,uri);
		// //String timeString = retriever
		// //.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		// //long time = Long.parseLong(timeString) *1000;
		//
		// bitmap = retriever.getFrameAtTime(videoView.getCurrentPosition() *
		// 1000,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
		// // 快照路径
		thumbFile = Util.getOutputMediaFile(Constant.IMAGE_THUMB,
				Constant.MEDIA_TYPE_IMAGE);
		Util.compressBmpToThumb(b, thumbFile, 280, 320);
		// } catch (IllegalArgumentException ex) {
		//
		// } catch (RuntimeException ex) {
		//
		// } finally {
		// try {
		// retriever.release();
		// } catch (RuntimeException ex) {
		// }
		// }

		return thumbFile.toString();
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (isPlaying) {
//				videoView.setVisibility(SurfaceView.INVISIBLE);
//				topmask.setVisibility(View.VISIBLE);
//				bottom_mask.setVisibility(View.VISIBLE);
//				initCamera();
//				return true;
//			}
//		} else {
//			
//			return true;
//		}
//
//	}
}
