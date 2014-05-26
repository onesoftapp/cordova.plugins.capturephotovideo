package cordova.plugins.capturephotovideo;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * SQLite数据库管理类
 * 
 * 主要负责数据库资源的初始�?�?��,关闭,以及获得DatabaseHelper帮助类操�?
 * 
 * @author shimiso
 * 
 */
public class DBManager {
	private int version = 1;
	private String databaseName;

	// 本地Context对象
	private Context mContext = null;

	private static DBManager dBManager = null;

	/**
	 * 构�?函数
	 * 
	 * @param mContext
	 */
	private DBManager(Context mContext) {
		super();
		this.mContext = mContext;

	}

	public static DBManager getInstance(Context mContext, String databaseName) {
		if (null == dBManager) {
			dBManager = new DBManager(mContext);
		}
		dBManager.databaseName = databaseName;
		return dBManager;
	}

	/**
	 * 关闭数据�?注意:当事务成功或者一次�?操作完毕时�?再关�?
	 */
	public void closeDatabase(SQLiteDatabase dataBase, Cursor cursor) {
		if (null != dataBase) {
			dataBase.close();
		}
		if (null != cursor) {
			cursor.close();
		}
	}

	/**
	 * 打开数据�?�?SQLiteDatabase资源�?��被关�?该底层会重新产生�?��新的SQLiteDatabase
	 */
	public SQLiteDatabase openDatabase() {
		return getDatabaseHelper().getWritableDatabase();
	}

	/**
	 * 获取DataBaseHelper
	 * 
	 * @return
	 */
	public DataBaseHelper getDatabaseHelper() {
		return new DataBaseHelper(mContext, this.databaseName, null,
				this.version);
	}

}
