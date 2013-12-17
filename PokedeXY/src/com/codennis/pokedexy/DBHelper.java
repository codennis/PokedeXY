package com.codennis.pokedexy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author codennis
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	private static DBHelper sInstance = null;
	
	private static String DB_PATH;
	private static String DB_NAME = "pokedex";
	private SQLiteDatabase database;
	public final Context context;
	private static int VERSION = 6;
	
	private Map<Integer,Integer> caught = new HashMap<Integer,Integer>();
	
	public SQLiteDatabase getDb() {
		return database;
	}
	
	public static DBHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DBHelper(context.getApplicationContext());
		}
		return sInstance;
	}
	
	DBHelper(Context context) {
		super(context, DB_NAME ,null,VERSION);
		this.context = context;
		
		String packageName = context.getPackageName();
		DB_PATH = String.format("/data/data/%s/databases/",packageName);
		createDatabase();
		openDatabase();
	}
	
	// Create database if not yet created
	public void createDatabase() {
		//Log.i("Creating", "database");
		boolean dbExists = checkDatabase();
		if (!dbExists) {
			this.getReadableDatabase();
			try {
				copyDatabase();
			} catch (IOException e) {
				//Log.e(this.getClass().toString(), "Copy error");
				throw new RuntimeException(e);
			}
		} else {
			Log.i(this.getClass().toString(),"Database already exists");
		}
	}
	
	// Check if database already exists
	private boolean checkDatabase() {
		SQLiteDatabase checkDb = null;
		try {
			String path = DB_PATH + DB_NAME;
			checkDb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			//Log.i("CHECKED","DATABASE");
		} catch (SQLException e) {
			Log.e(this.getClass().toString(), "Error while checking database");
		}
		
		if (checkDb !=  null) {
			checkDb.close();
		}
		return checkDb != null;
	}
	
	// Copy database from file
	private void copyDatabase() throws IOException {
		//Log.i("Copying", "Database");
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

	// Open database for read/write
	public SQLiteDatabase openDatabase() throws SQLException {
		database = this.getWritableDatabase();
		//Log.i("OPEN",database.getVersion() + "");
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
    public void onCreate(SQLiteDatabase db) {
    	//Log.i("ON","CREATE");
    	//createDatabase();
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	//Log.i("ON","UPGRADE");
    	updateDatabase(db);
    }
    
    private void updateDatabase(SQLiteDatabase db) {
    	// Copy data from existing DB
		String query = "SELECT _id, caught FROM pokedex";
		Cursor c = db.rawQuery(query,null);
		c.moveToFirst();
		if (!c.isAfterLast()) {
			do {
				caught.put(c.getInt(0),c.getInt(1));
			} while (c.moveToNext());
		}
		c.close();
    	
		try {
			copyDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ContentValues vals;
		for (Map.Entry entry:caught.entrySet()) {
			vals = new ContentValues();
			vals.put("caught", (Integer)entry.getValue());
			db.update("pokedex", vals, "_id = " + (Integer)entry.getKey(),null);
		}
    	
    }
}
