package cordova.plugins.capturephotovideo;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Activity帮助支持类接口.
 * 
 */
public interface IActivitySupport {

	/**
	 * 检查摄像头状态F
	 * 
	 * @return
	 */
	public abstract boolean checkCameraStatus();

	/**
	 * 
	 * 检查内存卡.
	 * 
	 */
	public abstract void checkMemoryCard();

	/**
	 * 
	 * 显示toast.
	 * 
	 * @param text
	 *            内容
	 * @param longint
	 *            内容显示多长时间
	 */
	public abstract void showToast(String text, int longint);

	/**
	 * 
	 * 短时间显示toast.
	 * 
	 * @param text
	 */
	public abstract void showToast(String text);

	/**
	 * 
	 * 获取进度条.
	 * 
	 * @return
	 */
	public abstract ProgressDialog getProgressDialog();

	/**
	 * 
	 * 返回当前Activity上下文.
	 * 
	 * @return
	 */
	public abstract Context getContext();
}
