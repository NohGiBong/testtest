package com.example.testtest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MemberDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "members.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "members"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_AGE = "age"
        const val COLUMN_MOBILE = "mobile"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_AGE INTEGER, " +
                "$COLUMN_MOBILE TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getAllMembers(): List<Member> {
        val members = mutableListOf<Member>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_AGE, COLUMN_MOBILE),
            null, null, null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val age = getInt(getColumnIndexOrThrow(COLUMN_AGE))
                val mobile = getString(getColumnIndexOrThrow(COLUMN_MOBILE))
                members.add(Member(id, name, age, mobile))
            }
        }

        cursor.close()
        db.close()
        return members
    }

    fun updateMember(id: Long, name: String, age: Int, mobile: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_AGE, age)
            put(COLUMN_MOBILE, mobile)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }


    fun deleteMember(member: Member) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_NAME = ?", arrayOf(member.name))
        db.close()
    }
}
