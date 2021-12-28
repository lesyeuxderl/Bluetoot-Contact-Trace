package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetootcontacttrace.R
import model.ModelMahasiswa

class DaftarAdapter (data: MutableList<ModelMahasiswa>, listener: OnItemClickListener) : RecyclerView.Adapter<DaftarHolder>() {

    private val datas = data
    private val listeners = listener

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: DaftarHolder, position: Int) {
        holder?.bind(datas[position], listeners, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaftarHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_daftar_mahasiswa, parent, false)
        return DaftarHolder(view)
    }
}