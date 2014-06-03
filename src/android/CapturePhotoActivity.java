package cordova.plugins.capturephotovideo;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class CapturePhotoActivity extends ActivitySupport implements
		OnClickListener {
	private CameraPreview mPreview;
	private ListView listview;
	private AlbumAdapter adapter;
	private List<ImageBucket> dataList;
	private RelativeLayout cameraLoyout, albumLayout, selectPhotoLayout,
			largePhoto;
	private FrameLayout camera_preview;
	private List<ImageItem> imageList;
	private GridView gridView;
	private ImageView img, btn;
	private GridviewAdapter gridAdapter;
	private String imagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(Constant.layout.activity_camera_album);
		largePhoto = (RelativeLayout) findViewById(Constant.id.preview_photo);
		cameraLoyout = (RelativeLayout) findViewById(Constant.id.camera_layout);
		camera_preview = (FrameLayout) findViewById(Constant.id.camera_preview);
		btn = (ImageView) findViewById(Constant.id.ablum_btn_main);
		albumLayout = (RelativeLayout) findViewById(Constant.id.album_layout);
		listview = (ListView) findViewById(Constant.id.listview);
		selectPhotoLayout = (RelativeLayout) findViewById(Constant.id.select_list_picture);
		gridView = (GridView) findViewById(Constant.id.gridview);
		findViewById(Constant.id.ablum_btn_main).setOnClickListener(this);
		findViewById(Constant.id.camera_btn_main).setOnClickListener(this);
		findViewById(Constant.id.back1_btn).setOnClickListener(this);
		findViewById(Constant.id.back2_btn).setOnClickListener(this);
		findViewById(Constant.id.back3_btn).setOnClickListener(this);
		findViewById(Constant.id.complete_tv).setOnClickListener(this);
		findViewById(Constant.id.back4_btn).setOnClickListener(this);
		findViewById(Constant.id.complete1_tv).setOnClickListener(this);
		img = (ImageView) findViewById(Constant.id.preview_img);
		Intent data = getIntent();
		int CompressOption = data.getExtras().getInt("CompressOption");
		int CompressHeight = data.getExtras().getInt("CompressHeight");
		int CompressWidth = data.getExtras().getInt("CompressWidth");
		mPreview = new CameraPreview(context, CompressOption, CompressHeight,
				CompressWidth);
		FrameLayout preview = (FrameLayout) findViewById(Constant.id.camera_preview);
		preview.addView(mPreview);
		updateBtn();
		initPhotoList();
		initSelectPhoto();
	}

	private void initSelectPhoto() {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				selectPhotoLayout.setVisibility(View.GONE);
				imagePath = imageList.get(pos).getImagePath();
				img.setImageBitmap(BitmapFactory.decodeFile(imagePath));
				largePhoto.setVisibility(View.VISIBLE);
			}
		});

	}

	private void initPhotoList() {

		listview.setDivider(null);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				albumLayout.setVisibility(View.GONE);
				imageList = dataList.get(arg2).imageList;
				gridAdapter = new GridviewAdapter(CapturePhotoActivity.this,
						imageList);
				gridView.setAdapter(gridAdapter);
				gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
				selectPhotoLayout.setVisibility(View.VISIBLE);
			}
		});

	}

	private void updateBtn() {
		Bitmap recentPhoto = Util.getRecentPhoto(context);
		if (recentPhoto != null) {

			btn.setImageBitmap(recentPhoto);
		} else {
			btn.setImageResource(Constant.drawable.album);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case Constant.id.ablum_btn_main:
			cameraLoyout.setVisibility(View.GONE);
			camera_preview.setVisibility(View.GONE);
			AlbumHelper helper = new AlbumHelper(context);
			dataList = helper.getImagesBucketList(false);
			if (dataList != null && dataList.size() > 0) {
				adapter = new AlbumAdapter(dataList, CapturePhotoActivity.this);
				listview.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				albumLayout.setVisibility(View.VISIBLE);
			} else {
				albumLayout.setVisibility(View.VISIBLE);
				showToast("你的相册是空的,请拍照");
			}

			break;
		case Constant.id.camera_btn_main:
			mPreview.takePhoto(context, btn);
			break;
		case Constant.id.back1_btn:
			this.finish();
			break;
		case Constant.id.back2_btn:
			albumLayout.setVisibility(View.GONE);
			cameraLoyout.setVisibility(View.VISIBLE);
			camera_preview.setVisibility(View.VISIBLE);
			break;
		case Constant.id.back3_btn:
			selectPhotoLayout.setVisibility(View.GONE);
			albumLayout.setVisibility(View.VISIBLE);
			break;
		case Constant.id.complete_tv:
			// 存放选中的图片
			ArrayList<String> list = new ArrayList<String>();
			JSONObject json = new JSONObject();
			boolean[] isSelect = gridAdapter.getIsSelected();
			for (int i = 0; i < isSelect.length; i++) {
				if (isSelect[i]) {
					try {
						json.put("thumbnailPath", imageList.get(i)
								.getThumbnailPath());
						json.put("imagePath", imageList.get(i).getImagePath());

						list.add(json.toString().replace("\\", ""));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			if (imageList.size() == 0) {
				Util.showToast("当前无选中图片", this);
			} else {
				Util.showToast("当前选中图片:" + imageList.size(), this);
			}
			Intent data = new Intent();
			data.putExtra("list", list);
			setResult(RESULT_OK, data);
			finish();
			break;
		case Constant.id.back4_btn:
			largePhoto.setVisibility(View.GONE);
			selectPhotoLayout.setVisibility(View.VISIBLE);
			break;
		case Constant.id.complete1_tv:
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			largePhoto.setVisibility(View.GONE);
			selectPhotoLayout.setVisibility(View.VISIBLE);
			break;
		}

	}

}
