package database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import model.ModelBluetooth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HelperBluetooth (ctx: Context) : SQLiteOpenHelper(ctx, DatabaseBluetooth.DATABASE_NAME, null, DatabaseBluetooth.DATABASE_VERSION) {

    companion object {
        private lateinit var INSTANCE: HelperBluetooth
        private lateinit var database: SQLiteDatabase
        private var databaseOpen: Boolean = false
        private val updown : Long = 4


        fun initDatabaseInstance(ctx: Context): HelperBluetooth {
            INSTANCE = HelperBluetooth(ctx)
            return INSTANCE
        }

        fun insertData(modelBluetooth: ModelBluetooth) : Long {

            if (!databaseOpen) { // jk database tdk open maka nilai database akan dimasukan sesuai dengan nilai terakhir instance
                database = INSTANCE.writableDatabase
                databaseOpen = true

                Log.i("Database" , "Database Open")
            }

            val values = ContentValues()
            values.put(DatabaseBluetooth.ROW_MAC, modelBluetooth.mac) // memasukan mac ke dlm ROW_MAC
            values.put(DatabaseBluetooth.ROW_TIME, modelBluetooth.time)
            values.put(DatabaseBluetooth.ROW_TIME_DOWN, modelBluetooth.timeDown)
            values.put(DatabaseBluetooth.ROW_SELISIH, 0)
            return database.insert(DatabaseBluetooth.DATABASE_TABEL, null, values) // return long
        }

        fun updateData(modelBluetooth: ModelBluetooth): Int? {
            if (!databaseOpen) {
                database = INSTANCE.writableDatabase
                databaseOpen = true

                Log.i("Database" , "Database Open")
            }

            val values = ContentValues()

            values.put(DatabaseBluetooth.ROW_TIME_DOWN, modelBluetooth.timeDown)
            values.put(DatabaseBluetooth.ROW_SELISIH, modelBluetooth.selisih)

            Log.i("Database Update" , "Return = " +database.update(DatabaseBluetooth.DATABASE_TABEL, values,
                "${DatabaseBluetooth.ROW_MAC} = '${modelBluetooth.mac}' AND ${DatabaseBluetooth.ROW_SELISIH} <= 4", null))
            return null
        }

        fun getSingleData(modelBluetooth: ModelBluetooth): LocalDateTime? {
            if (!databaseOpen) {
                database = INSTANCE.writableDatabase
                databaseOpen = true

                Log.i("Database" , "Database Open")
            }
            val bluetooth = ModelBluetooth()
            val data: MutableList<ModelBluetooth> = ArrayList()


            val cursor = database.rawQuery("SELECT * FROM ${DatabaseBluetooth.DATABASE_TABEL} WHERE ${DatabaseBluetooth.ROW_MAC} = '${modelBluetooth.mac}' IN (SELECT MAX (${DatabaseBluetooth.ROW_ID}) AND ${DatabaseBluetooth.ROW_SELISIH} <= '${updown}'  FROM ${DatabaseBluetooth.DATABASE_TABEL})", null)
            cursor.use { cur ->
                if (cursor.moveToFirst()) {
                    do {
                        bluetooth.id = cur.getInt(cur.getColumnIndex(DatabaseBluetooth.ROW_ID))
                        bluetooth.mac = cur.getString(cur.getColumnIndex(DatabaseBluetooth.ROW_MAC))
                        bluetooth.time = cur.getString(cur.getColumnIndex(DatabaseBluetooth.ROW_TIME))
                        bluetooth.timeDown = cur.getString(cur.getColumnIndex(DatabaseBluetooth.ROW_TIME_DOWN))
                        bluetooth.selisih = cur.getLong(cur.getColumnIndex(DatabaseBluetooth.ROW_SELISIH))
                        data.add(bluetooth)


                    } while (cursor.moveToNext())
                }
            }
            if (bluetooth.time== null){
                return null
                Log.i("getSingle" , "0")
            }else{
                //    val up = bluetooth.time
                Log.i("getSingleNOT NULL" , bluetooth.time)
                val format = DateTimeFormatter.ofPattern("yyyy/MM/dd    HH:mm")
                var mUP = LocalDateTime.parse(bluetooth.time, format)
                return mUP

            }


        }

        fun getTimeDown(modelBluetooth: ModelBluetooth): LocalDateTime {
            if (!databaseOpen) {
                database = INSTANCE.writableDatabase
                databaseOpen = true

                Log.i("Database" , "Database Open")
            }
            val bluetooth = ModelBluetooth()
            val data: MutableList<ModelBluetooth> = ArrayList()


            val cursor = database.rawQuery("SELECT * FROM ${DatabaseBluetooth.DATABASE_TABEL} WHERE ${DatabaseBluetooth.ROW_MAC} = '${modelBluetooth.mac}' IN (SELECT MAX (${DatabaseBluetooth.ROW_ID}) AND ${DatabaseBluetooth.ROW_SELISIH} <= '${updown}'  FROM ${DatabaseBluetooth.DATABASE_TABEL})", null)
//mengarahkan kursor ke dalam tabel db bt dng syarat .....
            cursor.use { cur ->
                if (cursor.moveToFirst()) {
                    do {
                        bluetooth.id = cur.getInt(cur.getColumnIndex(DatabaseBluetooth.ROW_ID))
                        bluetooth.mac = cur.getString(cur.getColumnIndex(DatabaseBluetooth.ROW_MAC))

                        bluetooth.time = cur.getString(cur.getColumnIndex(DatabaseBluetooth.ROW_TIME))
                        bluetooth.timeDown = cur.getString(cur.getColumnIndex(DatabaseBluetooth.ROW_TIME_DOWN))
                        bluetooth.selisih = cur.getLong(cur.getColumnIndex(DatabaseBluetooth.ROW_SELISIH))
                        data.add(bluetooth)


                    } while (cursor.moveToNext())
                }
            }
            Log.i("getSingleNOT NULL" , bluetooth.timeDown)
            val format = DateTimeFormatter.ofPattern("yyyy/MM/dd    HH:mm")
            var mDown = LocalDateTime.parse(bluetooth.timeDown, format)

            return mDown

        }


        fun getData(mac : String):String{
            if (!databaseOpen) {
                database = INSTANCE.writableDatabase
                databaseOpen = true

                Log.i("Database" , "Database Open")
            }
            val bluetooth = ModelBluetooth()
            val data: MutableList<ModelBluetooth> = ArrayList()

            val cursor = database.rawQuery("SELECT * FROM ${DatabaseBluetooth.DATABASE_TABEL} WHERE ${DatabaseBluetooth.ROW_MAC} = '${mac}' AND ${DatabaseBluetooth.ROW_SELISIH} <= '${updown}'", null)
            cursor.use { cur ->
                if (cursor.moveToLast()) {
                    do {
                        bluetooth.id = cur.getInt(cur.getColumnIndex(DatabaseBluetooth.ROW_ID))
                        bluetooth.mac = cur.getString(cur.getColumnIndex(DatabaseBluetooth.ROW_MAC))
                        bluetooth.time = cur.getString(cur.getColumnIndex(DatabaseBluetooth.ROW_TIME))
                        bluetooth.timeDown = cur.getString(cur.getColumnIndex(DatabaseBluetooth.ROW_TIME_DOWN))
                        bluetooth.selisih = cur.getLong(cur.getColumnIndex(DatabaseBluetooth.ROW_SELISIH))
                        data.add(bluetooth)

                        break

                    } while (cursor.moveToPrevious())
                }
            }

            Log.i("Get DATA + TIME UP", " : "+bluetooth.mac+"        "+bluetooth.time)
            return bluetooth.mac
        }


    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(DatabaseBluetooth.QUERY_CREATE)
        Log.i("DATABASE", "DATABASE CREATED")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DatabaseBluetooth.QUERY_UPGRADE)
        Log.i("DATABASE", "DATABASE UPDATED")
    }

}