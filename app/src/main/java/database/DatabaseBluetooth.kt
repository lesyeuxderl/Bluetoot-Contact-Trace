package database

class DatabaseBluetooth {
    companion object {
        val DATABASE_NAME = "DB_Bluetooth"
        val DATABASE_VERSION = 1

        val DATABASE_TABEL = "DB_TABEL_BLUETOOTH"
        val ROW_ID = "_id"
        val ROW_MAC = "mac"
        val ROW_TIME = "time"
        val ROW_TIME_DOWN = "down"
        val ROW_SELISIH = "selisih"


        val QUERY_CREATE = "CREATE TABLE IF NOT EXISTS $DATABASE_TABEL ($ROW_ID INTEGER PRIMARY KEY AUTOINCREMENT, $ROW_MAC TEXT, $ROW_TIME TEXT, $ROW_TIME_DOWN TEXT, $ROW_SELISIH LONG)"
        val QUERY_UPGRADE = "DROP TABLE IF EXISTS $DATABASE_TABEL"
    }
}
