package activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.bluetootcontacttrace.R
import database.DAtabaseHelper
import kotlinx.android.synthetic.main.activity_update_data_mahasiswa.*
import model.ModelMahasiswa

class UpdateDataMahasiswaActivity : AppCompatActivity() {
    var dataMahasiswa = ModelMahasiswa()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data_mahasiswa)

        bindView()

        etNamaEdit.addTextChangedListener(Watcher(inNamaEdit))

        btnEdit.setOnClickListener {

            val nama = etNamaEdit.text.toString()

            if (nama.isEmpty()) {
                inNamaEdit.error = "Masukan nama Mahasiswa"
                return@setOnClickListener
            }


            dataMahasiswa.nama = nama

            val stat = DAtabaseHelper.updateData(dataMahasiswa)

            if (stat > 0) {
                val bind = Bundle()
                bind.putParcelable("DATA", dataMahasiswa)

                val intent = Intent()
                intent.putExtras(bind)

                setResult(Activity.RESULT_OK, intent)

                Toast.makeText(this, "Berhasil Mengupdate Data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal Mengudate Data", Toast.LENGTH_SHORT).show()
            }
            startActivity(Intent(this, MainActivity::class.java))
        }
        toolbarEdit.title = "Update Data Mahasiswa"
    }

    private fun bindView() {
        val bind = intent.extras
        if (bind != null) {
            dataMahasiswa = bind.getParcelable("DATA")!!
        }

        etNamaEdit.setText(dataMahasiswa.nama)

    }

    private class Watcher(textinput: com.google.android.material.textfield.TextInputLayout) : TextWatcher {

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