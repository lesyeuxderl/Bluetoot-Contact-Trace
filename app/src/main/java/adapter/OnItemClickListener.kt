package adapter

import model.ModelMahasiswa

interface OnItemClickListener {
    fun onClick(data : ModelMahasiswa, position : Int)
}