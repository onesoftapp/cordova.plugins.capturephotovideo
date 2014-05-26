package cordova.plugins.capturephotovideo;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class CustomeDialog extends Dialog {

	public CustomeDialog(Context context) {
		super(context);
	}

	private static final int default_width = 160; // 默认宽度
	private static final int default_height = 120;// 默认高度
	private static final String default_message=" 正在加载相册..";

	public CustomeDialog(Context context, int layout, int style) {
		this(context, default_width, default_height, layout, style,default_message);
	}

	/**
	 * 自定义宽度高度 布局
	 * */
	public CustomeDialog(Context context, int width, int height, int layout,
			int style,String message) {
		super(context, style);
		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		// set width,height by density and gravity
		float density = getDensity(context);
		params.width = (int) (width * density);
		params.height = (int) (height * density);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		TextView message_tv=(TextView) findViewById(Constant.id.message);
		message_tv.setText(message);
	}

	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}

//	public void changeMessage(String content){
//		message_tv.setText(content);
//	}
}
