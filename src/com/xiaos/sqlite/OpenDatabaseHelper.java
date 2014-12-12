package com.xiaos.sqlite;

import android.os.Environment;


public class OpenDatabaseHelper {

	public static final String INTERNAL_DATABASE_PATH = Environment.getExternalStorageDirectory().getPath()+"/data.db3";

	public static final String EXTERNAL_DATABASE_PATH = "";

	public static String DEFAULT_DATABASE_PATH = INTERNAL_DATABASE_PATH;

	private static android.database.sqlite.SQLiteDatabase mDatabase;

	private static void createDatabase(android.content.Context context) {
		
		if (mDatabase != null) {
			if (mDatabase.isOpen())
				mDatabase.close();
			mDatabase = null;
		}
		mDatabase = android.database.sqlite.SQLiteDatabase
				.openOrCreateDatabase(DEFAULT_DATABASE_PATH, null);
		java.io.InputStream in = null;
		java.io.BufferedReader reader = null;
		try {
			in = context.getAssets().open("create_tables.sql");
			reader = new java.io.BufferedReader(new java.io.InputStreamReader(
					in));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				if (line.trim().endsWith(";")) {
					if(null != mDatabase){
						mDatabase.execSQL(sb.toString().replace(";", ""));
						sb.setLength(0);//清空缓冲区
					}
				}
			}
		} catch (java.io.IOException e) {
			mDatabase = null;
			java.io.File tmp = new java.io.File(DEFAULT_DATABASE_PATH);
			if (tmp.exists())
				tmp.delete();
		} finally {
			try {
				if (null != reader)
					reader.close();
				if (null != in)
					in.close();
			} catch (java.io.IOException e) {
			}
		}
	}

	public void upgradeDatabase() {

	}

	public static android.database.sqlite.SQLiteDatabase getDatabase(
			android.content.Context context) {
		java.io.File dbfile = new java.io.File(DEFAULT_DATABASE_PATH);
		if (dbfile.exists()) {
			if(null != mDatabase && mDatabase.isOpen()){
				return mDatabase;
			}
			mDatabase = android.database.sqlite.SQLiteDatabase.openDatabase(
					DEFAULT_DATABASE_PATH, null,
					android.database.sqlite.SQLiteDatabase.OPEN_READWRITE);
		} else {
			createDatabase(context);
		}
		return mDatabase;
	}

	public String getDatabaseVersion() {
		if (null != mDatabase && mDatabase.isOpen()) {
			android.database.Cursor cursor = mDatabase.rawQuery(
					"pragma main.user_version", null);
			cursor.moveToFirst();
			return cursor.getString(0);
		}
		return null;
	}
	
	public static void close(android.database.sqlite.SQLiteDatabase database)
	{
		if(null != database)
		{
			if(database.isOpen())
				database.close();
			database = null;
		}
	}
}
