package com.example.mysto.ricknmortybuddykotlin.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_CHARACTERS(ID INTEGER PRIMARY KEY AUTOINCREMENT ,CHARACTERS TEXT)")
        db.execSQL("CREATE TABLE $TABLE_EPISODES(ID INTEGER PRIMARY KEY AUTOINCREMENT ,EPISODES TEXT)")
        db.execSQL("CREATE TABLE $TABLE_LOCATIONS(ID INTEGER PRIMARY KEY AUTOINCREMENT ,LOCATIONS TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CHARACTERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EPISODES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LOCATIONS")
    }

    fun insertAllData(table: String, data: String): Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        var result = 0L

        when (table) {
            "Characters" -> {
                contentValues.put(TABLE_CHARACTERS_COL_2, data)
                result = db.insert(TABLE_CHARACTERS, null, contentValues)
            }
            "Episodes" -> {
                contentValues.put(TABLE_EPISODES_COL_2, data)
                result = db.insert(TABLE_EPISODES, null, contentValues)
            }
            "Locations" -> {
                contentValues.put(TABLE_LOCATIONS_COL_2, data)
                result = db.insert(TABLE_LOCATIONS, null, contentValues)
            }
        }

        db.close()
        return result > -1
    }

    fun getAllData(table: String): Cursor? {

        val db = this.writableDatabase
        var res: Cursor? = null

        when (table) {
            "Characters" -> res = db.rawQuery("SELECT * FROM $TABLE_CHARACTERS", null)
            "Episodes" -> res = db.rawQuery("SELECT * FROM $TABLE_EPISODES", null)
            "Locations" -> res = db.rawQuery("SELECT * FROM $TABLE_LOCATIONS", null)
        }

        return res
    }

    fun updateAllData(table: String, data: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        var result = 0L

        when (table) {
            "Characters" -> {
                contentValues.put(TABLE_CHARACTERS_COL_2, data)
                result = db.update(TABLE_CHARACTERS, contentValues, "ID = ?", arrayOf("1")).toLong()
            }
            "Episodes" -> {
                contentValues.put(TABLE_EPISODES_COL_2, data)
                result = db.update(TABLE_EPISODES, contentValues, "ID = ?", arrayOf("1")).toLong()
            }
            "Locations" -> {
                contentValues.put(TABLE_LOCATIONS_COL_2, data)
                result = db.update(TABLE_LOCATIONS, contentValues, "ID = ?", arrayOf("1")).toLong()
            }
        }

        return result > 0
    }

    fun deleteAllData(table: String): Int? {
        val db = this.writableDatabase
        var result = 0

        when (table) {
            "Characters" -> result = db.delete(TABLE_CHARACTERS, "ID=?", arrayOf("1"))
            "Episodes" -> result = db.delete(TABLE_EPISODES, "ID=?", arrayOf("1"))
            "Locations" -> result = db.delete(TABLE_LOCATIONS, "ID=?", arrayOf("1"))
        }

        return result
    }

    companion object {

        const val DATABASE_NAME = "RickNMortyBuddyKotlin.db"
        const val TABLE_CHARACTERS = "Characters_table"
        const val TABLE_CHARACTERS_COL_2 = "CHARACTERS"

        const val TABLE_EPISODES = "Episodes_table"
        const val TABLE_EPISODES_COL_2 = "EPISODES"

        const val TABLE_LOCATIONS = "Locations_table"
        const val TABLE_LOCATIONS_COL_2 = "LOCATIONS"
    }

}