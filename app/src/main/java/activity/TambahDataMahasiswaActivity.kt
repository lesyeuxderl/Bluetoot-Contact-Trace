package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.bluetootcontacttrace.DaftarMahasiswaActivity
import com.example.bluetootcontacttrace.R
import database.DAtabaseHelper
import model.ModelMahasiswa
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_tambah_data_mahasiswa.*

class TambahDataMahasiswaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_data_mahasiswa)

        toolbar.title = "SQLite Kotlin2"

        etNama.addTextChangedListener(Watcher(inNama))

        btnInsert.setOnClickListener {

            val nama = etNama.text.toString()



            if (nama.isEmpty()) {
                inNama.error = "Masukan nama Mahasiswa"
                return@setOnClickListener
            }


            val data = ModelMahasiswa()
            data.nama = nama
            val stat = DAtabaseHelper.insertData(data)

            if (stat > 0) {
                etNama.text.clear()
                etNama.clearFocus()
                Toast.makeText(this, "Berhasil Menambah Data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal Menambah Data", Toast.LENGTH_SHORT).show()
            }
            startActivity(Intent(this, MainActivity::class.java))
        }
        btnLihatData.setOnClickListener {
            startActivity(Intent(this, DaftarMahasiswaActivity::class.java))
        }

    }

    private class Watcher(textinput: TextInputLayout) : TextWatcher {

        val input = textinput

        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            input.isErrorEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DAtabaseHelper.closeDatabase()
    }
}