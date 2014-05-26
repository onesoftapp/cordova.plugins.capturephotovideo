package cordova.plugins.capturephotovideo;

import java.io.Serializable;
import java.util.List;

/**
 * 相册对象
 * 
 * @author Administrator
 * 
 */
public class ImageBucket implements Serializable {
	public int count = 0;
	private String bucketName;
	public List<ImageItem> imageList ;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public List<ImageItem> getImageList() {
		return imageList;
	}
	public void setImageList(List<ImageItem> imageList) {
		this.imageList = imageList;
	}
	
	
	
}