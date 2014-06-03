package cordova.plugins.capturephotovideo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.widget.Toast;

public class Util {
	/**
	 * 获得camera对象
	 * */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}

	/**
	 * 拍照录像存储目录
	 * */
	public static File getOutputMediaFile(int type, int type1) {

		File mediaStorageDir = null;
		if (type == Constant.IMAGE_PICTURE) {
			mediaStorageDir = new File(Constant.IMAGE_PATH, "MyCameraApp");

			if (!mediaStorageDir.exists()) {
				if (!mediaStorageDir.mkdirs()) {
					return null;
				}
			}
		} else if (type == Constant.IMAGE_THUMB) {
			mediaStorageDir = new File(Constant.IMAGE_PATH, "thumb");
			if (!mediaStorageDir.exists()) {
				if (!mediaStorageDir.mkdirs()) {
					return null;
				}
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type1 == Constant.MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type1 == Constant.MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	public static void showToast(String content, Context activity) {
		Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
	}

	public static Bitmap decodeSampledBitmapFromResource(String filePath,
			int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(filePath, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	// mImageView.setImageBitmap(
	// decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));

	public static Bitmap copressImage(String imgPath) {
		Bitmap bmap = null;
		File picture = new File(imgPath);
		Options bitmapFactoryOptions = new BitmapFactory.Options();
		// 下面这个设置是将图片边界不可调节变为可调节
		bitmapFactoryOptions.inJustDecodeBounds = true;
		bitmapFactoryOptions.inSampleSize = 2;
		int outWidth = bitmapFactoryOptions.outWidth;
		int outHeight = bitmapFactoryOptions.outHeight;
		bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
				bitmapFactoryOptions);
		float imagew = 100;
		float imageh = 100;
		int yRatio = (int) Math.ceil(bitmapFactoryOptions.outHeight / imageh);
		int xRatio = (int) Math.ceil(bitmapFactoryOptions.outWidth / imagew);
		if (yRatio > 1 || xRatio > 1) {
			if (yRatio > xRatio) {
				bitmapFactoryOptions.inSampleSize = yRatio;
			} else {
				bitmapFactoryOptions.inSampleSize = xRatio;
			}

		}
		bitmapFactoryOptions.inJustDecodeBounds = false;
		bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
				bitmapFactoryOptions);
		if (bmap != null) {

			return bmap;
		}
		return null;
	}

	/**
	 * 得到最新拍的照片
	 * */
	public static Bitmap getRecentPhoto(Context context) {
		List<ImageItem> list = ImageManager.getInstance(context)
				.getAllImageList();
		if (list != null && list.size() > 0) {
			String imagepath = list.get(list.size() - 1).getImagePath();
			Bitmap bmp = null;
			try {
				InputStream fis = new FileInputStream(new File(imagepath));
				bmp = BitmapFactory.decodeStream(fis);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bmp;
		} else {
			return null;
		}

	}
	/**
	 * 得到最新拍的照片
	 * */
	public static Bitmap getVideoPhoto(Context context) {
		List<VideoItem> list = ImageManager.getInstance(context)
				.getAllVideoList();
		if (list != null && list.size() > 0) {
			String imagepath = list.get(list.size() - 1).getThumbnailPath();
			Bitmap bmp = null;
			try {
				InputStream fis = new FileInputStream(new File(imagepath));
				bmp = BitmapFactory.decodeStream(fis);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bmp;
		} else {
			return null;
		}
		
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 压缩成快照
	 * 
	 * @param targetHeight
	 * 
	 * */
	public static void compressBmpToThumb(Bitmap bmp, File thumbFile,
			int targetWidth, int targetHeight) {
		try {
			int bitmapWidth = bmp.getWidth();
			int bitmapHeight = bmp.getHeight();
			float scaleWidth = (float) targetWidth / bitmapWidth;
			float scaleHeight = (float) targetHeight / bitmapHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到调整大小后的bitmap
			Bitmap resizeBitmap = Bitmap.createBitmap(bmp, 0, 0, bitmapWidth,
					bitmapHeight, matrix, false);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)) {
				FileOutputStream fos = new FileOutputStream(thumbFile);
				fos.write(baos.toByteArray());
				fos.flush();
				fos.close();
			}

			if (!bmp.isRecycled()) {
				bmp.recycle();// 释放内存
			}
			if (!resizeBitmap.isRecycled()) {
				resizeBitmap.recycle();
			}
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * 压缩照片
	 * 
	 * @param thumbFile
	 * 
	 * @param currentDisplay
	 * @param context
	 * */
	public static void compressBmpToFile(Bitmap bitmap, File file,
			File thumbFile, int compressOption, int targetWidth,
			int targetHeight, Context context) {
		try {
			int bitmapWidth = bitmap.getWidth();
			int bitmapHeight = bitmap.getHeight();
			float scaleWidth = (float) targetWidth / bitmapWidth;
			float scaleHeight = (float) targetHeight / bitmapHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到调整大小后的bitmap
			Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmapWidth, bitmapHeight, matrix, false);
			compressBmpToThumb(bitmap, thumbFile, resizeBitmap);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG,
					compressOption, baos)) {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(baos.toByteArray());
				fos.flush();
				fos.close();
			}

			if (!bitmap.isRecycled()) {
				bitmap.recycle();// 释放内存
			}
			if (!resizeBitmap.isRecycled()) {
				resizeBitmap.recycle();
			}
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		saveImageAndThumbnails(bitmap, context, file, thumbFile);
	}

	private static void compressBmpToThumb(Bitmap bitmap, File thumbFile,
			Bitmap resizeBitmap) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)) {
			FileOutputStream fos = new FileOutputStream(thumbFile);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		}
	}

	private static void saveImageAndThumbnails(Bitmap bmp, Context context,
			File file, File thumbFile) {
		ImageItem item = new ImageItem();
		item.setTitle(file.getName());
		item.setBucketname("camera");
		item.setImagePath(file.toString());
		item.setThumbnailPath(thumbFile.toString());
		item.setTime(DateUtil.date2Str(new Date()));
		item.setIsSelect(1);
		ImageManager.getInstance(context).saveImage(item);
	}

	public static void saveVideoThumbnail(Context context, String videopath,
			String thumbnail) {
		File video = new File(videopath);
		VideoItem item = new VideoItem();
		item.setTitle(video.getName());
		item.setVideoPath(videopath);
		item.setThumbnailPath(thumbnail);
		item.setTime(DateUtil.date2Str(new Date()));
		item.setIsSelect(1);
		ImageManager.getInstance(context).saveVideo(item);

	}
}
