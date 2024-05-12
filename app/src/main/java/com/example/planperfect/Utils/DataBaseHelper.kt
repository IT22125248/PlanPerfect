package com.example.planperfect.Utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.planperfect.Model.PlanModel
import java.util.ArrayList;
import java.util.List;

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        private const val DATABASE_NAME = "TODO_DATABASE"
        private const val TABLE_NAME = "TODO_TABLE"
        private const val COL_1 = "ID"
        private const val COL_2 = "TASK"
        private const val COL_3 = "STATUS"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATUS INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTask(model: PlanModel) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_2, model.task)
            put(COL_3, 0)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateTask(id: Int, task: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_2, task)
        }
        db.update(TABLE_NAME, values, "ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun updateStatus(id: Int, status: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_3, status)
        }
        db.update(TABLE_NAME, values, "ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteTask(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllTasks(): kotlin.collections.List<PlanModel> {
        val modelList = mutableListOf<PlanModel>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.query(TABLE_NAME, null, null, null, null, null, null)
        cursor?.use {
            val idIndex = it.getColumnIndex(COL_1)
            val taskIndex = it.getColumnIndex(COL_2)
            val statusIndex = it.getColumnIndex(COL_3)
            while (it.moveToNext()) {
                val id = if (idIndex != -1) it.getInt(idIndex) else 0
                val task = if (taskIndex != -1) it.getString(taskIndex) else ""
                val status = if (statusIndex != -1) it.getInt(statusIndex) else 0
                val PlanModel = PlanModel().apply {
                    this.id = id
                    this.task = task
                    this.status = status
                }
                modelList.add(PlanModel)
            }
        }
        db.close()
        return modelList.toList()
    }
}