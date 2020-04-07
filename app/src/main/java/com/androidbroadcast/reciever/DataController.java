package com.androidbroadcast.reciever;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataController
{
	public static final String CONTACTS_COLUMN_NAME = "Name";
	public static final String CONTACTS_COLUMN_EMAIL = "Email";
	public static final String CONTACTS_COLUMN_PASSWORD = "Password";
	public static final String TABLE_NAME="Login_Table";
	public static final String DATABASE_NAME="Login.db";
	public static final int DATABASE_VERSION=4;
	public static final String TABLE_CREATE="create table Login_Table (Name text not null, Email text not null, Password not null);";


	DataBaseHelper dbHelper;
	Context context;
	SQLiteDatabase db;
	
	public DataController(Context context)
	{
		this.context=context;
		dbHelper=new DataBaseHelper(context);
	}
	
	public DataController open()
	{
		db=dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public long insert(String name,String email,String password)
	{
		ContentValues contentValues=new ContentValues();
		contentValues.put("name", name);
		contentValues.put("password",password);
		contentValues.put("email", email);
		return db.insertOrThrow(TABLE_NAME, null, contentValues);
	}
	
	public Cursor retrieve(String email)
	{
		Log.d("retr", "retrieve: "+email);
		//return db.query(TABLE_NAME, new String[]{CONTACTS_COLUMN_EMAIL}, CONTACTS_COLUMN_EMAIL + " like " + email, null, null, null, null);
		Cursor res =  db.rawQuery( "select * from Login_Table where Email = ?",new String[] {email});
		return  res;

	}
	
	private static class DataBaseHelper extends SQLiteOpenHelper
	{

		public DataBaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try
			{
				db.execSQL(TABLE_CREATE);
			}
			catch(SQLiteException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS Login_Table");
			onCreate(db);
		}
		
	}
	
}