package model

import android.os.Parcelable
import android.annotation.SuppressLint
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class ModelMahasiswa (var id: Int, var nama: String) :
    Parcelable {
    constructor() : this(0, "")
}