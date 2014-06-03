package cordova.plugins.capturephotovideo;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
	
	public static JSONObject getJson(String s){
		int thumfirstIndex = 18;
		int thumendIndex = s.indexOf(",")-1;
		int imageIndex = thumendIndex+15;
		int imageLastIndex = s.indexOf("}")-1;
		String thumbnailPath = s.substring(thumfirstIndex,thumendIndex);
		String imagePath = s.substring(imageIndex,imageLastIndex);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("thumbnailPath", thumbnailPath);
		
		jsonObject.put("imagePath", imagePath);
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		return jsonObject;
	}
	
	public static JSONObject getvJson(String s){
		int thumfirstIndex = 18;
		int thumendIndex = s.indexOf(",")-1;
		int imageIndex = thumendIndex+15;
		int imageLastIndex = s.indexOf("}")-1;
		String thumbnailPath = s.substring(thumfirstIndex,thumendIndex);
		String videoPath = s.substring(imageIndex,imageLastIndex);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("thumbnailPath", thumbnailPath);
		
		jsonObject.put("videoPath", videoPath);
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		return jsonObject;
	}
	
	
}
