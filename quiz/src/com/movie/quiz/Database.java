package com.movie.quiz;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "contactsManager";

	// Contacts table name
	private static final String TABLE_CONTACTS = "contacts";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String BUTTON_ID = "button_id";
	private static ArrayList<Integer> listId = new ArrayList<Integer>();

	/**
	 * 
	 * @param context
	 */
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//onCreate(getWritableDatabase());
	//	onCreate(getWritableDatabase());
		
	}

	/**
 * 
 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try{
		for (int i = 0; i < 10; i++) {
			Log.e("Database ", "on create ");
		}
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + BUTTON_ID + " INTEGER"
				+ ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}
		catch (Exception e){
			
		}}

	/**
 * 
 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++) {
			Log.e("Database ", "on Upgrade ");
		}
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * 
	 * @param contact
	 */
	public void addContact(int button_id) {
		if (this.getAllContacts().contains(button_id)) {
			return;
		}

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(BUTTON_ID, button_id); // Contact button id
		// values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
		// Number

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * 
	 * @return
	 */
	// Getting All Contacts
	public ArrayList<Integer> getAllContacts() {
		ArrayList<Integer> contactList = new ArrayList<Integer>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				int currentFoundButtonId = 0;
				int id = 0;
				id = Integer.parseInt(cursor.getString(0));
				currentFoundButtonId = cursor.getInt(1);

				// contact.setName(cursor.getString(1));
				// contact.setPhoneNumber(cursor.getString(2));
				// Adding contact to list
				contactList.add(currentFoundButtonId);
				listId.add(id);
			} while (cursor.moveToNext());
		}
		// return contact list
		return contactList;
	}

	/**
		  * 
		  */
	// Deleting all contacts
	public void removeAll() {
		if (listId.isEmpty()) {
			getAllContacts();
		}
		if (listId.isEmpty()) {
			return;
		}
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Integer> array = getAllContacts();
		int counter = getCorrectCount();
		while (!listId.isEmpty()) {
			Log.e("removeAll", "function");
			Log.e("number ", Integer.toString(counter));
			db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
					new String[] { String.valueOf(listId.remove(0)) });
		}
		db.close();

	}/*
	 * public void removeAll(){ SQLiteDatabase db = this.getWritableDatabase();
	 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS); db.close(); }
	 */

	public int getCorrectCount() {
		return getAllContacts().size();
	}

}
