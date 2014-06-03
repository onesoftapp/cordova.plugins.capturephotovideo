package cordova.plugins.capturephotovideo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;


public class VGridviewAdapter extends BaseAdapter {

	List<VideoItem> dataList;
	Context context;
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	
	private boolean[] isSelected;//是否选中
	public VGridviewAdapter(Context context, List<VideoItem> dataList) {
		this.context = context;
		this.dataList = dataList;
		
		isSelected=new boolean[dataList.size()];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, Constant.layout.item_image_grid, null);
			holder.iv = (ImageView) convertView.findViewById(Constant.id.image);
			holder.selected =  (CheckBox) convertView
					.findViewById(Constant.id.select_cb);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if(isSelected[position]){//得到选中视频״̬
			holder.selected.setChecked(true);
		}else{
			holder.selected.setChecked(false);
		}
		holder.selected.setOnClickListener(new listener(position,holder.selected));
		final VideoItem item = dataList.get(position);
		displayBmp(holder.iv, item.getThumbnailPath(), item.getVideoPath(), item);
		return convertView;
	}

	class Holder {
		private ImageView iv;
		private CheckBox selected;
	}

	public void displayBmp(final ImageView iv, final String thumbPath,
			final String sourcePath, VideoItem item) {
		if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath)) {
			return;
		}
		final String path;
		final boolean isThumbPath;
		if (!TextUtils.isEmpty(thumbPath)) {
			path = thumbPath;
			isThumbPath = true;
		} else if (!TextUtils.isEmpty(sourcePath)) {
			path = sourcePath;
			isThumbPath = false;
		} else {
			return;
		}

		if (imageCache.containsKey(path)) {
			SoftReference<Bitmap> reference = imageCache.get(path);
			Bitmap bmp = reference.get();
			if (bmp != null) {
				iv.setImageBitmap(bmp);
				if (item.getBitmap() == null) {
					item.setBitmap(bmp);
				}
				return;
			}
		} else {
			Bitmap thumb = null;
			if (isThumbPath) {
//				thumb = BitmapFactory.decodeFile(thumbPath);
				thumb=Util.decodeSampledBitmapFromResource(thumbPath, 100, 100);
				if (thumb == null) {
					try {
						thumb = revitionImageSize(sourcePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
//				thumb = BitmapFactory.decodeFile(sourcePath);
				thumb=Util.decodeSampledBitmapFromResource(sourcePath, 100, 100);
			}
			iv.setImageBitmap(thumb);
			if (item.getBitmap() == null) {
				item.setBitmap(thumb);
			}
			put(path, thumb);
		}

	}

	public void put(String path, Bitmap bmp) {
		if (!TextUtils.isEmpty(path) && bmp != null) {
			imageCache.put(path, new SoftReference<Bitmap>(bmp));
		}
	}

	public Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 256)
					&& (options.outHeight >> i <= 256)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
	
	class listener implements OnClickListener{

		int position;
		CheckBox cb;
		public listener(int position,CheckBox cb){
			this.position=position;
			this.cb=cb;
		}
		@Override
		public void onClick(View v) {
			
			if(cb.isChecked()){
				cb.setSelected(true);
				isSelected[position]=true;
			}else{
				cb.setSelected(false);
				isSelected[position]=false;
			}
			
		}
		
	}

	public boolean[] getIsSelected() {
		return isSelected;
	}
	
}
