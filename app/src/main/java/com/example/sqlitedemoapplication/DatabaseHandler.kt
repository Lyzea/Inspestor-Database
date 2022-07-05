package com.example.sqlitedemoapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context):  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "PesticideDatabase"
        private const val TABLE_CONTACTS = "PesticideTable"

        private const val KEY_ID = "_id"
        private const val KEY_PEST_NAME = "pest_name"
        private const val KEY_RECOMMENDED_PESTICIDE = "recommended_pesticide"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        /**actual sql instruction
         * CREATE TABLE PesticideDatabase(_id INTEGER PRIMARY KEY, pest name TEXT, recommended TEXT)
         * here, the table title is EmployeeDatabase with column title: _id, pest name & recommended pesticide which has its own type
         **/
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PEST_NAME + " TEXT,"
                + KEY_RECOMMENDED_PESTICIDE + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    //when we upgrade table for example when we add new column so we need to upgrade to see change
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS" + TABLE_CONTACTS)
        onCreate(db)
    }

    //method to insert data
    fun addPesticideData(mc: MyModelClass): Long{
        val db = this.writableDatabase

        //its like container
        val contentValues = ContentValues()
        contentValues.put(KEY_PEST_NAME, mc.pest_name)
        contentValues.put(KEY_RECOMMENDED_PESTICIDE, mc.recommended_pesticide)

        //inserting row
        val success = db.insert(TABLE_CONTACTS,null, contentValues)

        db.close()
        //insert returns long
        return success
    }

    //method to read data
    fun viewPesticideData(): ArrayList<MyModelClass>{
        val myList: ArrayList<MyModelClass> = ArrayList()

        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e:SQLException){
            return ArrayList()
        }

        var id: Int
        var pest_name: String
        var recommended_pesticide:String

        //cursor goes through row
        if (cursor.moveToFirst()){
            do {
                //getting data from table
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                pest_name = cursor.getString(cursor.getColumnIndex(KEY_PEST_NAME))
                recommended_pesticide = cursor.getString(cursor.getColumnIndex(
                    KEY_RECOMMENDED_PESTICIDE))

                //making class from read data of table
                val mc = MyModelClass(id = id,pest_name = pest_name,recommended_pesticide= recommended_pesticide)
                myList.add(mc)
            } while (cursor.moveToNext())
        }
        return myList
    }

    //function to update record
    fun updatePesticideData(mc: MyModelClass): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_PEST_NAME, mc.pest_name)
        contentValues.put(KEY_RECOMMENDED_PESTICIDE, mc.recommended_pesticide)

        //updating row
        val success = db.update(TABLE_CONTACTS,contentValues, KEY_ID + "=" + mc.id, null )

        db.close()
        return success
    }

    fun deletePesticideData(mc: MyModelClass): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID,mc.id)
        //deleting row
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + mc.id, null)

        db.close()
        return success
    }
}