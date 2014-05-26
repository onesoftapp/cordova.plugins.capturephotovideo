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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumAdapter extends BaseAdapter {

	
	List<ImageBucket> dataList;
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	Context  ct;
	public AlbumAdapter(List<ImageBucket> ImageBucket,Context  ct){
		this.dataList=ImageBucket;
		this.ct=ct;
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
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(ct, Constant.layout.listview_item_album, null);
			holder.iv = (ImageView) convertView.findViewById(Constant.id.ablum_img);
//			holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
			holder.name = (TextView) convertView.findViewById(Constant.id.ablumName_tv);
			holder.count = (TextView) convertView.findViewById(Constant.id.ablumNum_tv);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		ImageBucket item = dataList.get(position);
		holder.count.setText("" + item.count);
		holder.name.setText(item.getBucketName());
		
		if (item.imageList != null && item.imageList.size() > 0) {
			String thumbPath = item.imageList.get(0).getThumbnailPath();
			String sourcePath = item.imageList.get(0).getImagePath();
			displayBmp(holder.iv,thumbPath,sourcePath);
		} else {
			holder.iv.setImageBitmap(null);
		}
		return convertView;
	}
	
	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView name;
		private TextView count;
	}

	public void displayBmp(final ImageView iv, final String thumbPath,
			final String sourcePath) {
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

//		if (imageCache.containsKey(path)) {
//			SoftReference<Bitmap> reference = imageCache.get(path);
//			Bitmap bmp = reference.get();
//			if (bmp != null) {
//				iv.setImageBitmap(bmp);
//				return;
//			}
//		}else{
			Bitmap thumb =null;
			if (isThumbPath) {
//				thumb = BitmapFactory.decodeFile(thumbPath);
				thumb=Util.decodeSampledBitmapFromResource(thumbPath, 100, 100);
				 if (thumb == null) {
//					 thumb=Util.decodeSampledBitmapFromResource(thumbPath, 100, 100);
						try {
							thumb = revitionImageSize(sourcePath);
						} catch (IOException e) {
							e.printStackTrace();
						}						
					}		
			} else {
				thumb=Util.decodeSampledBitmapFromResource(sourcePath, 100, 100);
//				 thumb = BitmapFactory.decodeFile(sourcePath);
			}
			iv.setImageBitmap(thumb);
//			put(path, thumb);
//		}


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
}
