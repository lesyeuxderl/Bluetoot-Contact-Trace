package database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import model.ModelMahasiswa

class DAtabaseHelper (ctx: Context) : SQLiteOpenHelper(ctx, DatabaseConstan.DATABASE_NAME, null, DatabaseConstan.DATABASE_VERSION) {

    companion object {
        private lateinit var INSTANCE: DAtabaseHelper
        private lateinit var database: SQLiteDatabase
        private var databaseOpen: Boolean = false

        fun closeDatabase() {
            if (database.isOpen && databaseOpen) {
                database.close()
                databaseOpen = false

                Log.i("Database" , "Database close")
            }
        }

        fun initDatabaseInstance(ctx: Context): DAtabaseHelper {
            INSTANCE = DAtabaseHelper(ctx)
            return INSTANCE
        }

        fun insertData(modelMahasiswa: ModelMahasiswa): Long {

            if (!databaseOpen) {
                database = INSTANCE.writableDatabase
                databaseOpen = true

                Log.i("Database" , "Database Open")
            }

            val values = ContentValues()
            values.put(DatabaseConstan.ROW_NAMA, modelMahasiswa.nama)
            return database.insert(DatabaseConstan.DATABASE_TABEL, null, values)
        }

        fun updateData(modelMahasiswa: ModelMahasiswa): Int {
            if (!databaseOpen) {
                database = INSTANCE.writableDatabase
                databaseOpen = true

                Log.i("Database" , "Database Open")
            }

            val values = ContentValues()
            values.put(DatabaseConstan.ROW_NAMA, modelMahasiswa.nama)
            return database.update(DatabaseConstan.DATABASE_TABEL, values, "${DatabaseConstan.ROW_ID} = ${modelMahasiswa.id}", null)
        }

        fun getAllData(): MutableList<ModelMahasiswa> {
            if (!databaseOpen) {
                database = INSTANCE.writableDatabase
                databaseOpen = true

                Log.i("Database" , "Database Open")
            }

            val data: MutableList<ModelMahasiswa> = ArrayList()
            val cursor = database.rawQuery("SELECT * FROM ${DatabaseConstan.DATABASE_TABEL}", null)
            cursor.use { cur ->
                if (cursor.moveToFirst()) {
                    do {

                        val mahasiswa = ModelMahasiswa()
                        mahasiswa.id = cur.getInt(cur.getColumnIndex(DatabaseConstan.ROW_ID))
                        mahasiswa.nama = cur.getString(cur.getColumnIndex(DatabaseConstan.ROW_NAMA))
                        data.add(mahasiswa)

                    } while (cursor.moveToNext())
                }
            }
            return data
        }

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(DatabaseConstan.QUERY_CREATE)
        Log.i("DATABASE", "DATABASE CREATED")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DatabaseConstan.QUERY_UPGRADE)
        Log.i("DATABASE", "DATABASE UPDATED")
    }

}