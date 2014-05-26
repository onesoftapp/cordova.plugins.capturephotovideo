package cordova.plugins.capturephotovideo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * SQLite数据库的帮助�?
 * 
 * 该类属于扩展�?主要承担数据库初始化和版本升级使�?其他核心全由核心父类完成
 * 
 * @author shimiso
 * 
 */
public class DataBaseHelper extends SDCardSQLiteOpenHelper {

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE [image_item]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [title] NVARCHAR, [bucket_name] NVARCHAR, [thumbnail_path] NVARCHAR, [image_path] NVARCHAR, [isselect] INTEGER, [image_time] TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
