package com.xiaos.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Cursor对象的常用方法：
 * 
 * c.move(int offset); //以当前位置为参考,移动到指定行 c.moveToFirst(); //移动到第一行
 * c.moveToLast(); //移动到最后一行 c.moveToPosition(int position); //移动到指定行
 * c.moveToPrevious(); //移动到前一行 c.moveToNext(); //移动到下一行 c.isFirst();
 * //是否指向第一条 c.isLast(); //是否指向最后一条 c.isBeforeFirst(); //是否指向第一条之前
 * c.isAfterLast(); //是否指向最后一条之后 c.isNull(int columnIndex); //指定列是否为空(列基数为0)
 * c.isClosed(); //游标是否已关闭 c.getCount(); //总数据项数 c.getPosition();
 * //返回当前游标所指向的行数 c.getColumnIndex(String columnName);//返回某列名对应的列索引值
 * c.getString(int columnIndex); //返回当前行指定列的值
 */

/**
 * SqLite 数据库管理类
 * 
 * @author SoAi
 * 
 */
public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context, String dbname) {
		helper = new DBHelper(context, dbname);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	/**
	 * 添加
	 * 
	 * @param 
	 */
	public boolean add(List<String> list) {
		boolean flag = false;
		db.beginTransaction(); // 开始事务
		try {
			db.execSQL("delete from user");
			for (String str : list) {
				db.execSQL("INSERT INTO user VALUES(?, ? ,?)",
						new Object[] { str, str, str});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
			flag = true;
		} finally {
			db.endTransaction(); // 结束事务
		}
		return flag;
	}

	/**
	 * 查詢
	 * 
	 * @return
	 */
	public List<String> query() {
		List<String> list = new ArrayList<String>();
		Cursor c = db.rawQuery("SELECT * FROM user", null);
		while (c.moveToNext()) {
			list.add(c.getInt(c.getColumnIndex("id"))+"");
			list.add(c.getString(c.getColumnIndex("username")));
			list.add(c.getString(c.getColumnIndex("password")));
		}
		c.close();
		return list;
	}

	/**
	 * 关闭数据库
	 */
	public void closeDB() {
		db.close();
	}

}
