package cordova.plugins.capturephotovideo;

import java.io.Serializable;
import java.util.Date;
import android.graphics.Bitmap;
import android.widget.CheckBox;

/**
 * 图片对象
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable, Comparable<ImageItem> {
	private Integer imageId;
	private String title;
	private String bucketname;
	private String thumbnailPath;
	private String imagePath;
	private Bitmap bitmap;
	public CheckBox cb;
	public Integer isSelect;// 0:选中 1:没选中
	private String time;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ImageItem() {
	}

	public String getBucketname() {
		return bucketname;
	}

	public void setBucketname(String bucketname) {
		this.bucketname = bucketname;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public CheckBox getCb() {
		return cb;
	}

	public void setCb(CheckBox cb) {
		this.cb = cb;
	}

	public Integer getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Integer isSelect) {
		this.isSelect = isSelect;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int compareTo(ImageItem item) {
		if (null == this.getTime() || null == item.getTime()) {
			return 0;
		}
		String format = null;
		String time1 = "";
		String time2 = "";
		if (this.getTime().length() == item.getTime().length()
				&& this.getTime().length() == 23) {
			time1 = this.getTime();
			time2 = item.getTime();
			format = Constant.FORMART;
		} else {
			time1 = this.getTime().substring(0, 19);
			time2 = item.getTime().substring(0, 19);
		}
		Date da1 = DateUtil.str2Date(time1, format);
		Date da2 = DateUtil.str2Date(time2, format);
		if (da1.before(da2)) {
			return 1;
		}
		if (da2.before(da1)) {
			return -1;
		}

		return 0;
	}
}