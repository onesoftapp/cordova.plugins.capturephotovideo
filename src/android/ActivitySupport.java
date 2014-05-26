package cordova.plugins.capturephotovideo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

public class ActivitySupport extends Activity implements IActivitySupport{
	protected Context context = null;
	protected ProgressDialog pg = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		pg = new ProgressDialog(context);

	}


	@Override
	public void checkMemoryCard() {
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			new AlertDialog.Builder(context)
					.setTitle("提示")
					.setMessage("请检查内存卡")
					.setPositiveButton("设置",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									Intent intent = new Intent(
											Settings.ACTION_SETTINGS);
									context.startActivity(intent);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									//eimApplication.exit();
								}
							}).create().show();
		}
		
	}

	@Override
	public void showToast(String text, int longint) {
		Toast.makeText(context, text, longint).show();
		
	}

	@Override
	public void showToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public ProgressDialog getProgressDialog() {
		// TODO Auto-generated method stub
		return pg;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return context;
	}


	@Override
	public boolean checkCameraStatus() {
		int numCameras = Camera.CAMERA_ERROR_UNKNOWN;
		if(numCameras == 1){
			return false;
		}else{
			return true;
		}
	}
}
