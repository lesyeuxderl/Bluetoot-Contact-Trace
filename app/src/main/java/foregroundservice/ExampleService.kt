package foregroundservice

import android.app.Notification
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.util.Log
import com.example.bluetootcontacttrace.App.Companion.CHANNEL_ID
import com.example.bluetootcontacttrace.R
import database.HelperBluetooth
import model.ModelBluetooth
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.properties.Delegates

class ExampleService : Service(){


    var g = 1

    // objects declaration
    private var mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    // variables
    private val mDeviceList = java.util.ArrayList<String>()

    companion object {

        val ACTION_STOP = "stop" // nilai stop
    }

    private val TAG: String = ExampleService::class.java.simpleName
    private lateinit var handlerThread : HandlerThread
    private lateinit var handler: Handler

    //Here my objective will be as soon, as people bind to this service, we will increment a KeepAlive counter, which was initially 0,
    // This will be checked after every 10 seconds, that if that counter reaches 0, then we will stop the foreground service

    override fun onCreate() { // start foreground service
        super.onCreate()

        handlerThread = HandlerThread("ServiceBackgroundHandler")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        Log.d("Example Service", "onCreate : RUN")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int { // sama menjalankan method dosomebackground

        //Setting up the foreground service notification

        val notification : Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Foreground service is running")
            .setSmallIcon(R.drawable.ic_android)
            .build()

        startForeground(1, notification)

        doSomeBackgroundWork()


        return START_STICKY

    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


    private fun doSomeBackgroundWork(){
        handler.postDelayed(Runnable { //backgorund runNNNNNNN when app is being minimize
            /*
             *  receiver
            */


            var receiver = object : BroadcastReceiver() { // var rec yg berisi object dng tipe data BR
                override fun onReceive(context: Context, intent: Intent) {

                    mDeviceList.removeAll { true } //remove all the data from mdevicelist


                    var bluetooth = ModelBluetooth()
                    Log.d("onRECEIVE", "onReceive: RUN")
                    val action = intent.action // bernilai act

                    // if remote bluetooth device found
                    if (BluetoothDevice.ACTION_FOUND == action) {

                        //  receive related contents of new bluetooth device to variable device
                        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                        val cekms: Long = 70

                        if (device != null) {
                            // device MAC address
                            var deviceHardwareAddress = device.address // MAC address
                            // add this info to list
                            mDeviceList.add(
                                """${device.address}""".trimIndent()
                            )

// memASUKAN MAC DKK K MMODEL BT
                            bluetooth.mac = (device.address ?: null) as String
                            var systemTime = System.currentTimeMillis()
                            bluetooth.time = formatTimestamp(systemTime)
                            bluetooth.timeDown = formatTimestamp(System.currentTimeMillis())


                            val format = DateTimeFormatter.ofPattern("yyyy/MM/dd    HH:mm")
                            var selisih by Delegates.notNull<Long>() // initialization of selisih
                            var selisihms by Delegates.notNull<Long>()

                            if (HelperBluetooth.getData(bluetooth.mac) == device.address) { // ma ditemukan udh ada di db


                                selisih = ChronoUnit.SECONDS.between(
                                    HelperBluetooth.getSingleData(bluetooth),
                                    HelperBluetooth.getTimeDown(bluetooth) )
                                selisihms = ChronoUnit.MICROS.between( HelperBluetooth.getSingleData(bluetooth),
                                    HelperBluetooth.getTimeDown(bluetooth) )
                                Log.i(
                                    "Selisih",
                                    selisih.toString() + " " + HelperBluetooth.getSingleData(bluetooth) + " " + bluetooth.timeDown
                                )
                            } else {
                                selisih = 0
                                selisihms = 0
                                Log.i("Selisih", selisih.toString())
                            }

                            //  var x = 0

                            for (i in mDeviceList){ // for every mDevice list

                                //       Log.d("FOR", " "+ x + " get Data : "+ HelperBluetooth.getData(i)+" mDeviceList : "+i)
                                if (HelperBluetooth.getData(i) == i){ //jika nilai pd mdevlist udh ada di db
                                    if (selisihms > cekms || selisih < 4) {
                                        bluetooth.selisih = ChronoUnit.MINUTES.between(
                                            HelperBluetooth.getSingleData(bluetooth),
                                            HelperBluetooth.getTimeDown(bluetooth))
                                        HelperBluetooth.updateData(bluetooth)
                                        Log.d("update", "update")
                                    } else {

                                        HelperBluetooth.insertData(bluetooth)
                                        Log.d("insert", "insert Dalam")
                                    }
                                }else{
                                    HelperBluetooth.insertData(bluetooth)
                                    Log.d("insert", "insert" +bluetooth.mac +"   "+bluetooth.time)
                                }
                                //   x = x+1
                            }


                        }
                    }

                }

            }

            // start discovery
            // saat mbtA melakukan sD lalu ada device terscan maka data dari dv trsbt akan dipassing k receiver
            mBluetoothAdapter!!.startDiscovery()
            // register broadcast receiver and listen to specific "ACTION_FOUND" request
            val filter = IntentFilter()
            filter.addAction(BluetoothDevice.ACTION_FOUND)
            this.registerReceiver(receiver, filter, null, null)

            Log.d("G", "Gnya : "+g)
            if (g % 600 == 0 ){ // maka akan menghapus reg rec
                unregisterReceiver(receiver)
                Log.d("UNREGISTERRECEIVER", "UNREGISTERRECEIVER : RECEIVER")
            }
            g++
            Log.d("BroadcastIntent", "BroadcastIntent : RUN")

            Log.d(TAG, "running")
            doSomeBackgroundWork()
        } , 1000*30)
    }



    private fun formatTimestamp(time: Long): String { //mengubah format waktu dr long jd string
        val format = SimpleDateFormat("yyyy/MM/dd    HH:mm", Locale.ENGLISH)
        return format.format(Date(time))
    }

}