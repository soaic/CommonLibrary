package com.xiaos.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelper是一个辅助类，用来管理数据库的创建和版本他，它提供两个方面的功能
 * 第一，getReadableDatabase()、getWritableDatabase
 * ()可以获得SQLiteDatabase对象，通过该对象可以对数据库进行操作
 * 第二，提供了onCreate()、onUpgrade()两个回调函数，允许我们再创建和升级数据库时，进行自己的操作
 */
public class DBHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;

	/**
	 * 在SQLiteOpenHelper的子类当中，必须有该构造函数
	 * 
	 * @param context
	 *            上下文对象
	 * @param name
	 *            数据库名称
	 * @param factory
	 * @param version
	 *            当前数据库的版本，值必须是整数并且是递增的状态
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		// 必须通过super调用父类当中的构造函数
		super(context, name, factory, version);
	}

	public DBHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public DBHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	// 该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a database");
		/**
		 *    SQLite的数据类型:
		 * 
		 * 	  null:这个值为空值
		 * 
		  * 　　varchar(n):长度不固定，最大为n的字符串，n<=4000
		 * 
		  * 　　char(n):长度固定为n的字符串，n<=254
		 * 
		  * 　　integer:整数类型，不用过多说明
		 * 
		  * 　　real:所有值都是浮动的数值，被存储为8字节的IEEE浮动标记序号
		 * 
		  * 　　text 值为文本字符串，使用数据库编码存储(TUTF-8,UTF-16BE or UTF-16LE)
		 * 
		  * 　　blob:值为blob数据块，以输入的数据格式进行存储，怎样输入就怎样存储，不改变输入格式
		 * 
		  * 　　Data:包含年份、月份和日期，格式为XX-XX-XX(即年-月-日)
		 * 
		  * 　　Time:包含小时、分钟和秒，格式为XX:XX:XX(即小时:分钟:秒)
		 */
		// execSQL用于执行SQL语句
		//VERSION 1
		db.execSQL("create table user(id integer, username varchar(50), password varchar(50))");
		//VERSION 2
		updateVersion2(db);
		//...
	}
	
	/**
	 * 版本2更新
	 * @param db
	 */
	private void updateVersion2(SQLiteDatabase db){
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//表更新
		updateDbSchemaVersion(db, oldVersion, newVersion);
	}
	
	/**
	 * 将两版本之间的SQL语句全部按顺序执行一次
	 * @param oldVersion
	 * @param latestVersion
	 */
	private void updateDbSchemaVersion(SQLiteDatabase db, int oldVersion, int latestVersion) {
		if (oldVersion>=latestVersion) 
			return;
		switch (oldVersion) {
			case 1:
				updateVersion2(db);
				break;
		}
		oldVersion++;
		updateDbSchemaVersion(db, oldVersion, latestVersion);
	}
}