package adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.row_daftar_mahasiswa.view.*
import model.ModelMahasiswa

class DaftarHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    fun bind(data: ModelMahasiswa, listener: OnItemClickListener, position: Int) = with(itemView) {
        rowAv.text = data.nama.substring(0, 1).capitalize()
        rowNama.text = data.nama


        setOnClickListener { listener.onClick(data, position) }

    }
}