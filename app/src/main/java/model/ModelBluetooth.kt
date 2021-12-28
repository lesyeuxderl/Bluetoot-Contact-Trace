package model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class ModelBluetooth (var id: Int, var mac: String, var time: String, var timeDown: String, var selisih: Long) :
    Parcelable {
    constructor() : this(0, "", "", "", 0)
}