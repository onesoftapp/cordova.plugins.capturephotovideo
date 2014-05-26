package cordova.plugins.capturephotovideo;

import java.io.File;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * SQLite数据库针对SD卡的核心帮助�?
 * 
 * 该类主要用于直接设置,创建,升级数据库以及打�?关闭数据库资�?
 * 
 */
public abstract class SDCardSQLiteOpenHelper {
	private static final String TAG = SDCardSQLiteOpenHelper.class
			.getSimpleName();
	private final Context mContext;
	private final String mName;
	private final CursorFactory mFactory;
	private final int mNewVersion;

	private SQLiteDatabase mDatabase = null;
	private boolean mIsInitializing = false;

	public SDCardSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		if (version < 1)
			throw new IllegalArgumentException("Version must be >= 1, was "
					+ version);

		mContext = context;
		mName = name;
		mFactory = factory;
		mNewVersion = version;
	}

	
	public synchronized SQLiteDatabase getWritableDatabase() {
		if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
			return mDatabase; // The database is already open for business

		}

		if (mIsInitializing) {
			throw new IllegalStateException(
					"getWritableDatabase called recursively");
		}

	
		boolean success = false;
		SQLiteDatabase db = null;
		try {
			mIsInitializing = true;
			if (mName == null) {
				db = SQLiteDatabase.create(null);
			} else {
				String path = getDatabasePath(mName).getPath();
				db = SQLiteDatabase.openOrCreateDatabase(path, mFactory);
			}

			int version = db.getVersion();
			if (version != mNewVersion) {
				db.beginTransaction();
				try {
					if (version == 0) {
						onCreate(db);
					} else {
						onUpgrade(db, version, mNewVersion);
					}
					db.setVersion(mNewVersion);
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
			}

			onOpen(db);
			success = true;
			return db;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mIsInitializing = false;
			if (success) {
				if (mDatabase != null) {
					try {
						mDatabase.close();
					} catch (Exception e) {
					}
				}
				mDatabase = db;
			} else {
				if (db != null)
					db.close();
			}
		}
		return db;
	}

	public synchronized SQLiteDatabase getReadableDatabase() {
		if (mDatabase != null && mDatabase.isOpen()) {
			return mDatabase; // The database is already open for business

		}

		if (mIsInitializing) {
			throw new IllegalStateException(
					"getReadableDatabase called recursively");
		}

		try {
			return getWritableDatabase();
		} catch (SQLiteException e) {
			if (mName == null)
				throw e; // Can't open a temp database read-only!

			Log.e(TAG, "Couldn't open " + mName
					+ " for writing (will try read-only):", e);
		}

		SQLiteDatabase db = null;
		try {
			mIsInitializing = true;
			String path = getDatabasePath(mName).getPath();
			db = SQLiteDatabase.openDatabase(path, mFactory,
					SQLiteDatabase.OPEN_READWRITE);
			if (db.getVersion() != mNewVersion) {
				throw new SQLiteException(
						"Can't upgrade read-only database from version "
								+ db.getVersion() + " to " + mNewVersion + ": "
								+ path);
			}

			onOpen(db);
			Log.w(TAG, "Opened " + mName + " in read-only mode");
			mDatabase = db;
			return mDatabase;
		} finally {
			mIsInitializing = false;
			if (db != null && db != mDatabase)
				db.close();
		}
	}

	/**
	 * Close any open database object.
	 */
	public synchronized void close() {
		if (mIsInitializing)
			throw new IllegalStateException("Closed during initialization");

		if (mDatabase != null && mDatabase.isOpen()) {
			mDatabase.close();
			mDatabase = null;
		}
	}

	public File getDatabasePath(String name) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) == true) {
			File f = new File(Constant.DATABASE_PATH);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
		return new File(Constant.DATABASE_PATH + name);
	}


	public abstract void onCreate(SQLiteDatabase db);

	
	public abstract void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion);

	public void onOpen(SQLiteDatabase db) {
	}
}