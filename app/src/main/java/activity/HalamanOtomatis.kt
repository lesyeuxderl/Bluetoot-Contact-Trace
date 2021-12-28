package activity

import adapter.DaftarAdapter
import adapter.OnItemClickListener
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import database.DAtabaseHelper
import model.ModelMahasiswa

class HalamanOtomatis : AppCompatActivity(), OnItemClickListener {
    private var dataDaftarMahasiswa: MutableList<ModelMahasiswa> = ArrayList()
    private lateinit var adapterDaftarMahasiswa: DaftarAdapter
    private var dataMahasiswa = ModelMahasiswa()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        dataDaftarMahasiswa = DAtabaseHelper.getAllData()
        adapterDaftarMahasiswa = DaftarAdapter(dataDaftarMahasiswa, this)

        if (this.dataDaftarMahasiswa.size > 0) {
            dataMahasiswa = dataDaftarMahasiswa[0]
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, TambahDataMahasiswaActivity::class.java))
        }
    }

    override fun onClick(data: ModelMahasiswa, position: Int) {
        TODO("Not yet implemented")
    }
}

