package com.example.bluetootcontacttrace

import activity.DetailMahasiswaDialog
import activity.UpdateDataMahasiswaActivity
import adapter.DaftarAdapter
import adapter.OnItemClickListener
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import database.DAtabaseHelper
import kotlinx.android.synthetic.main.activity_daftar_mahasiswa.*
import model.ModelMahasiswa


class DaftarMahasiswaActivity :  AppCompatActivity() , OnItemClickListener, DetailMahasiswaDialog.OnDialogItemClick {

    private var dataDaftarMahasiswa: MutableList<ModelMahasiswa> = ArrayList()
    private var positionStats = 0
    lateinit private var adapterDaftarMahasiswa: DaftarAdapter // lateinit didefinisikan saat proses app berjalan

    override fun dialogDeleteCallback(data: ModelMahasiswa) {
        this.dataDaftarMahasiswa.remove(data)
        adapterDaftarMahasiswa.notifyDataSetChanged()


        if (this.dataDaftarMahasiswa.size > 0) {
            textEmpty.visibility = View.GONE
        } else {
            textEmpty.visibility = View.VISIBLE
        }

    }

    override fun dialogEditCallback(data: ModelMahasiswa) {

        val bind = Bundle()
        bind.putParcelable("DATA", data) //merubah key yg diedit

        val edit = Intent(this, UpdateDataMahasiswaActivity::class.java)
        edit.putExtras(bind)
        startActivityForResult(edit, 1)
    }

    override fun onClick(data: ModelMahasiswa, position: Int) { // buat ngasi tau kalo user mau buka sesuatu
        DetailMahasiswaDialog.newInstance(data, this).show(supportFragmentManager, "DETAIL")
//         val activityKu = DetailMahasiswaDialog.newInstance(data, this).show(supportFragmentManager, "DETAIL")
        positionStats = position
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_mahasiswa)

        dataDaftarMahasiswa = DAtabaseHelper.getAllData()

        adapterDaftarMahasiswa = DaftarAdapter(dataDaftarMahasiswa, this)

        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapterDaftarMahasiswa

        toolbars.title = "Daftar Mahasiswa"

        if (dataDaftarMahasiswa.size > 0) {
            textEmpty.visibility = View.GONE
        } else {
            textEmpty.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val dataMahasiswa: ModelMahasiswa? = data.extras?.getParcelable("DATA")
                if (dataMahasiswa != null) {
                    dataDaftarMahasiswa[positionStats] = dataMahasiswa
                }
                adapterDaftarMahasiswa.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DAtabaseHelper.closeDatabase()
    }
}