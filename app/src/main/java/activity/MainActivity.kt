package activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import adapter.DaftarAdapter
import adapter.OnItemClickListener
import com.example.bluetootcontacttrace.R
import database.DAtabaseHelper
import model.ModelMahasiswa
import foregroundservice.ExampleService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), OnItemClickListener {
    var dataMahasiswa = ModelMahasiswa()
    var dataDaftarMahasiswa: MutableList<ModelMahasiswa> = ArrayList()

    //    private var positionStats = 1
    private lateinit var adapterDaftarMahasiswa: DaftarAdapter

    // constants
    val PERMISSION_CODE = 1000
    val REQUEST_ENABLE_BT = 2000

    // UI components
    private var listView: ListView? = null //nilai inisialisasi mASIH null

    // objects declaration
    var mBluetoothAdapter: BluetoothAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataDaftarMahasiswa = DAtabaseHelper.getAllData()

        adapterDaftarMahasiswa = DaftarAdapter(dataDaftarMahasiswa, this)

        dataMahasiswa = dataDaftarMahasiswa[0]

        dialogNama.text = dataMahasiswa.nama.uppercase(Locale.getDefault())

        btnEdit.setOnClickListener {
            dialogEditCallback(dataMahasiswa)
        }

        btnBluetooth.setOnClickListener {
        }

        // bind UI components
        listView = findViewById<View>(R.id.listView) as ListView? // menmapilkan nilai listview dr ID


        // check for permissions
        //akurasi bt
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permission, PERMISSION_CODE)

            checkBluetoothAdapter()

        } else {
            checkBluetoothAdapter()
        }

        registerReceiver(broadcastReceiver, IntentFilter("finishActivity"))

        Service.START_STICKY
    }




    //Setting up broadcast receiver for the service to call this activity to kill this
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            finishAndRemoveTask()
        }
    }

    // ngestop service
    override fun onBackPressed() {
        val intent = Intent(this, ExampleService::class.java)
        intent.action = ExampleService.ACTION_STOP
        startService(intent)
    }


    fun Start(){
        //Also as this is the 1st entry point into the app, we are gonna start the service over here only
        val intent = Intent(this, ExampleService::class.java)
        intent.putExtra("keepAlive", 1)
        //Here we are stating the foreground service no matter what OS it is
        ContextCompat.startForegroundService(this, intent)
        Log.d("Start", "Foreground : RUN")
    }




    fun dialogEditCallback(data: ModelMahasiswa) {

        val bind = Bundle()
        bind.putParcelable("DATA", data)

        val edit = Intent(this, UpdateDataMahasiswaActivity::class.java)
        edit.putExtras(bind)
        startActivityForResult(edit, 1)
    }

    override fun onClick(data: ModelMahasiswa, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_device_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // handle runtime permissions
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) { // switch case, klo udh di if maka else ga jalan
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    checkBluetoothAdapter()

                } else {
                    Toast.makeText(this, "Permission denied. Cannot run app.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    // handle default BluetoothAdapter
    private fun checkBluetoothAdapter() { //tersedia atau tidakny bt
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) { // tidak tersedia
            Toast.makeText(this, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show()
        } else {
            if (!mBluetoothAdapter!!.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                Log.d("checkBluetoothAdapter", "mBluetoothAdapter : "+mBluetoothAdapter)
                Start()

            }
        }
        Log.d("checkBluetoothAdapter", "checkBluetoothAdapter : RUN")
        Service.START_STICKY //klo udh dihome ttp di last activity
    }
    // jalan ketika clas main sudah jalan n permission granted
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // handle bluetooth enable request
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            Start()
        } else {
            Toast.makeText(this, "Impossible to run without Bluetooth", Toast.LENGTH_SHORT).show()
        }
    }
}