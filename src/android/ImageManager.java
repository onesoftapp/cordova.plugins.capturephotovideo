package cordova.plugins.capturephotovideo;

import java.util.List;
import java.util.Map;

import cordova.plugins.capturephotovideo.SQLiteTemplate.RowMapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ImageManager {

	private static ImageManager imageManger = null;
	private static DBManager manager = null;
	/**
	 *
	 */
	public static Map<String, ImageItem> contacters = null;

	private ImageManager(Context context) {
		manager = DBManager.getInstance(context, Constant.CAPTURE_IMAGE);
	}

	public static ImageManager getInstance(Context context) {

		if (imageManger == null) {
			imageManger = new ImageManager(context);
		}

		return imageManger;
	}

	/**
	 * 
	 * 保存图片
	 * 
	 * @param image
	 */
	public long saveImage(ImageItem image) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		if (StringUtil.notEmpty(image.getTitle())) {
			contentValues.put("title", StringUtil.doEmpty(image.getTitle()));
		}
		if (StringUtil.notEmpty(image.getBucketname())) {
			contentValues.put("bucket_name",
					StringUtil.doEmpty(image.getBucketname()));
		}
		if (StringUtil.notEmpty(image.getImagePath())) {
			contentValues.put("image_path",
					StringUtil.doEmpty(image.getImagePath()));
		}
		if (StringUtil.notEmpty(image.getThumbnailPath())) {
			contentValues.put("thumbnail_path",
					StringUtil.doEmpty(image.getThumbnailPath()));
		}
		if (image.getIsSelect() != null) {
			contentValues.put("isselect", image.getIsSelect());
		}
		contentValues.put("image_time", image.getTime());
		return st.insert("image_item", contentValues);
	}

	/**
	 * 保存视频
	 * 
	 * @param item
	 */
	public long saveVideo(VideoItem item) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		if (StringUtil.notEmpty(item.getTitle())) {
			contentValues.put("title", StringUtil.doEmpty(item.getTitle()));
		}

		if (StringUtil.notEmpty(item.getVideoPath())) {
			contentValues.put("video_path",
					StringUtil.doEmpty(item.getVideoPath()));
		}
		if (StringUtil.notEmpty(item.getThumbnailPath())) {
			contentValues.put("thumbnail_path",
					StringUtil.doEmpty(item.getThumbnailPath()));
		}
		if (item.getIsSelect() != null) {
			contentValues.put("isselect", item.getIsSelect());
		}
		contentValues.put("video_time", item.getTime());
		return st.insert("video_item", contentValues);
	}

	/**
	 * 
	 * 更新图片信息
	 * 
	 * @param isSelect
	 */
	public void updateStatus(String id, Integer isSelect) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("isSelect", isSelect);
		st.updateById("image_item", id, contentValues);
	}

	/**
	 * 删除选择图片
	 * 
	 * @param isSelect
	 */
	public int delImage(Integer isSelect) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.deleteByCondition("image_item", "isSelect=?",
				new String[] { "" + isSelect });
	}

	/**
	 * 删除所有图片
	 * 
	 * @param
	 */
	public int delAllImage() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.deleteByCondition("image_item", "isSelect=?",
				new String[] { "" + "*" });
	}

	/**
	 * 
	 * 查询所有图片
	 * 
	 * @return
	 */
	public List<ImageItem> getAllImageList() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<ImageItem> list = st
				.queryForList(
						new RowMapper<ImageItem>() {

							@Override
							public ImageItem mapRow(Cursor cursor, int index) {
								ImageItem image = new ImageItem();
								image.setImageId(cursor.getInt(cursor
										.getColumnIndex("_id")));
								image.setImagePath(cursor.getString(cursor
										.getColumnIndex("image_path")));
								image.setBucketname(cursor.getString(cursor
										.getColumnIndex("bucket_name")));
								image.setThumbnailPath(cursor.getString(cursor
										.getColumnIndex("thumbnail_path")));
								image.setIsSelect(cursor.getInt(cursor
										.getColumnIndex("isselect")));
								image.setTime(cursor.getString(cursor
										.getColumnIndex("image_time")));
								return image;
							}

						},
						"select _id,bucket_name,thumbnail_path,image_path,isselect,image_time from image_item",
						null);
		return list;
	}

	public List<VideoItem> getAllVideoList() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<VideoItem> list = st
				.queryForList(
						new RowMapper<VideoItem>() {

							@Override
							public VideoItem mapRow(Cursor cursor, int index) {
								VideoItem image = new VideoItem();
								image.setVideoId(cursor.getInt(cursor
										.getColumnIndex("_id")));
								image.setVideoPath(cursor.getString(cursor
										.getColumnIndex("video_path")));
								image.setThumbnailPath(cursor.getString(cursor
										.getColumnIndex("thumbnail_path")));
								image.setIsSelect(cursor.getInt(cursor
										.getColumnIndex("isselect")));
								image.setTime(cursor.getString(cursor
										.getColumnIndex("video_time")));
								return image;
							}

						},
						"select _id,thumbnail_path,video_path,isselect,video_time from video_item",
						null);
		return list;
	}

}
