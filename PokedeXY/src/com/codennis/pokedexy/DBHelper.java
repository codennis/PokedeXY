package com.codennis.pokedexy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	private static String DB_PATH;
	private static String DB_NAME;
	private SQLiteDatabase database;
	public final Context context;
	
	public SQLiteDatabase getDb() {
		return database;
	}
	
	public DBHelper(Context context, String dbName) {
		super(context, dbName,null,1);
		this.context = context;
		
		String packageName = context.getPackageName();
		DB_PATH = String.format("/data/data/%s/databases/",packageName);
		DB_NAME = dbName;
		openDatabase();
	}
	
	public void createDatabase() {
		boolean dbExists = checkDatabase();
		if (!dbExists) {
			this.getReadableDatabase();
			try {
				copyDatabase();
			} catch (IOException e) {
				Log.e(this.getClass().toString(), "Copy error");
				throw new Error ("Error copying database");
			}
		} else {
			Log.i(this.getClass().toString(),"Database already exists");
		}
	}
	
	private boolean checkDatabase() {
		SQLiteDatabase checkDb = null;
		try {
			String path = DB_PATH + DB_NAME;
			checkDb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLException e) {
			Log.e(this.getClass().toString(), "Error while checking database");
		}
		
		if (checkDb !=  null) {
			checkDb.close();
		}
		return checkDb != null;
	}
	
	private void copyDatabase() throws IOException {
		InputStream iStream = context.getAssets().open(DB_NAME);
		OutputStream oStream = new FileOutputStream(DB_PATH + DB_NAME);
		
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = iStream.read(buffer)) > 0) {
			oStream.write(buffer, 0, bytesRead);
		}
		
		oStream.close();
		iStream.close();
	}

	public SQLiteDatabase openDatabase() throws SQLException {
		String path = DB_PATH + DB_NAME;
		if (database == null) {
			createDatabase();
			database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
		}
		
		return database;
	}
	
	@Override
	public synchronized void close() {
		if (database != null) {
			database.close();
		}
		super.close();
	}
	
    @Override
    public void onCreate(SQLiteDatabase db) {}
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
