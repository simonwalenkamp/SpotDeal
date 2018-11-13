package com.walenkamp.spotdeal.DAL

import android.content.Context
import android.database.sqlite.SQLiteStatement
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.walenkamp.spotdeal.Entities.Order

// Database name
private val DATABASE_NAME = "orders.db"

// Version of database
private val DATABASE_VERSION = 1

// name of table
private val TABLE_NAME = "order_table"


class LocalOrderDAO(c: Context) {
    private val contex: Context = c

    // the insert statement used to write to table
    private val INSERT = ("insert into " + TABLE_NAME
            + "(id, supplierId, dealId, customerId, valid) values (?, ?, ?, ?, ?)")

    // OpenHelper instance
    private var openHelper = OpenHelper(contex)

    // SQLLiteDatabase instance
    private val db: SQLiteDatabase = openHelper.writableDatabase

    // Statement (query)
    private val insertStmt: SQLiteStatement = db.compileStatement(INSERT)

    // List of orderHistory
    private val ordersSaved = mutableListOf<Order>()

    // Saves a list of orders
    fun saveOrders(orders: List<Order>) {
        for (o in orders) {
            insertStmt.bindString(1, o.id)
            insertStmt.bindString(2, o.supplierId)
            insertStmt.bindString(3, o.dealId)
            insertStmt.bindString(4, o.customerId)
            insertStmt.bindString(5, o.valid.toString())

            insertStmt.execute()
        }
    }

    // Gets order history
    fun getAllSavedOrders(): List<Order> {
        ordersSaved.clear()
        val cursor = this.db.query(
            TABLE_NAME,
            arrayOf(
                "id",
                "supplierId",
                "dealId",
                "customerId",
                "valid"
            ), null, null, null, null, null, null)

        if(cursor.moveToFirst()) {
            do {
                ordersSaved.add(Order(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)!!.toBoolean()
                ))
            } while (cursor.moveToNext())
        }
        if (!cursor.isClosed) {
            cursor.close()
        }
        return ordersSaved
    }

    // Inner class helps with creating and updating the local database
    private class OpenHelper internal constructor(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE " + TABLE_NAME
                        + " (id INTEGER PRIMARY KEY, name TEXT)"
            )

        }

        override fun onUpgrade(
            db: SQLiteDatabase,
            oldVersion: Int, newVersion: Int
        ) {

            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }

}