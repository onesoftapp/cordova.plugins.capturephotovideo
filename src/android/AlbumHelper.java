package cordova.plugins.capturephotovideo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;

/**
 * 涓撹緫甯姪绫?
 * 
 * @author Administrator
 * 
 */
public class AlbumHelper {

	// 缩略图列�?
	HashMap<String, String> thumbnailList = new HashMap<String, String>();
	// 专辑列表
	List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
	HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();
	List<ImageItem> list;
	List<VideoItem> vlist;
	

	public AlbumHelper(Context context) {
		list = ImageManager.getInstance(context).getAllImageList();
	}

	public AlbumHelper(Context context, boolean b) {
		vlist = ImageManager.getInstance(context).getAllVideoList();
	}

	/**
	 * 得到图片�?
	 * 
	 * @param refresh
	 * @return
	 */
	public List<ImageBucket> getImagesBucketList(boolean refresh) {
		ImageBucket imageBucket = new ImageBucket();
		if(list!=null&&list.size()>0){
			imageBucket.setBucketName(list.get(0).getBucketname());
			imageBucket.setCount(list.size());
			imageBucket.setImageList(list);
			List<ImageBucket> tmpList = new ArrayList<ImageBucket>();
			tmpList.add(imageBucket);
			return tmpList;
		}else{
			return null;
		}
		
	}

	public List<VideoItem> getVideoList(boolean b) {
		
		return vlist;
	}

}
