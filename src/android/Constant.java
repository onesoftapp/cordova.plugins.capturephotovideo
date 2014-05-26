package cordova.plugins.capturephotovideo;

import java.io.File;
import android.os.Environment;

public class Constant {

	public static final String TAG = "camera";

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int IMAGE_PICTURE = 3;
	public static final int IMAGE_THUMB = 4;
	public static final String CAPTURE_IMAGE = "captureImage";// 数据库名
	public static final String FORMART = "yyyy-MM-dd HH:mm:ss SSS";// 日期格式
	public static final File IMAGE_PATH = Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);// 图片存放路径
	public static final String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/albumAndCamera/database/";// 数据库存放路径

	public static final class attr {
	
		public static final int roundHeight = 0x7f010001;
		
		public static final int roundWidth = 0x7f010000;
	}

	public static final class dimen {
		
		public static final int activity_horizontal_margin = 0x7f050000;
		public static final int activity_vertical_margin = 0x7f050001;
	}

	public static final class drawable {
		public static final int action_bar_light_glyph_back = 0x7f020000;
		public static final int album = 0x7f020001;
		public static final int back25 = 0x7f020002;
		public static final int back40 = 0x7f020003;
		public static final int background = 0x7f020004;
		public static final int ic_launcher = 0x7f020005;
		public static final int icon_bn_start = 0x7f020006;
		public static final int icon_bn_stop = 0x7f020007;
		public static final int pai50 = 0x7f020008;
		public static final int pai90 = 0x7f020009;
		public static final int pai_btn = 0x7f02000a;
		public static final int pai_pressed = 0x7f02000b;
		public static final int pp_bg_dialog = 0x7f02000c;
		public static final int top_bg = 0x7f02000d;
		public static final int top_bg2 = 0x7f02000e;
	}

	public static final class id {
		public static final int ablumName_tv = 0x7f090017;
		public static final int ablumNum_tv = 0x7f090018;
		public static final int ablum_btn_main = 0x7f090003;
		public static final int ablum_img = 0x7f090016;
		public static final int album_layout = 0x7f090006;
		public static final int back1_btn = 0x7f090005;
		public static final int back2_btn = 0x7f090008;
		public static final int back3_btn = 0x7f09000c;
		public static final int back4_btn = 0x7f090010;
		public static final int camera_btn_main = 0x7f090002;
		public static final int camera_layout = 0x7f090001;
		public static final int camera_preview = 0x7f090000;
		public static final int complete1_tv = 0x7f090011;
		public static final int complete_tv = 0x7f09000d;
		public static final int gridview = 0x7f09000e;
		public static final int image = 0x7f090014;
		public static final int listview = 0x7f090009;
		public static final int message = 0x7f090013;
		public static final int preview_img = 0x7f090012;
		public static final int preview_photo = 0x7f09000f;
		public static final int select_cb = 0x7f090015;
		public static final int select_list_picture = 0x7f09000a;
		public static final int text = 0x7f090004;
		public static final int top = 0x7f09000b;
		public static final int top_layout = 0x7f090007;
	}

	public static final class layout {
		public static final int activity_album_detail = 0x7f030000;
		public static final int activity_camera_album = 0x7f030001;
		public static final int dialog_layout2 = 0x7f030002;
		public static final int fragment_main = 0x7f030003;
		public static final int item_image_grid = 0x7f030004;
		public static final int listview_item_album = 0x7f030005;
	}

	public static final class menu {
		public static final int main = 0x7f080000;
	}

	public static final class string {
		public static final int action_settings = 0x7f060001;
		public static final int app_name = 0x7f060000;
		public static final int db_dir = 0x7f060004;
		public static final int dir = 0x7f060003;
		public static final int hello_world = 0x7f060002;
	}

	public static final class style {
		public static final int Theme_dialog = 0x7f070000;
	}

	public static final class xml {
		public static final int config = 0x7f040000;
	}

	public static final class styleable {

		public static final int[] RoundAngleImageView = { 0x7f010000,
				0x7f010001 };
	
		public static final int RoundAngleImageView_roundHeight = 1;
		
		public static final int RoundAngleImageView_roundWidth = 0;
	};

}