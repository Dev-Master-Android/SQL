package com.example.sql

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class User(val name: String, val position: String, val phone: String)
class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_POSITION = "position"
        private const val COLUMN_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_POSITION TEXT," +
                "$COLUMN_PHONE TEXT);")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(name: String, position: String, phone: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_POSITION, position)
            put(COLUMN_PHONE, phone)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun userExists(name: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    fun getAllUsers(): Cursor {
        val db = this.readableDatabase
        return db.query(TABLE_NAME,  null, null,null,null,null,null )
    }

    fun deleteUser() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }
}