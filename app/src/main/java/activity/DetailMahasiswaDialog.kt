package activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bluetootcontacttrace.R
import model.ModelMahasiswa
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import database.DAtabaseHelper
import kotlinx.android.synthetic.main.activity_detail_mahasiswa_dialog.dialogNama
import kotlinx.android.synthetic.main.activity_detail_mahasiswa_dialog.toolbarDialog

class DetailMahasiswaDialog : BottomSheetDialogFragment(){ // extends

    private var dataMahasiswa = ModelMahasiswa()

    companion object {
        lateinit  private  var listeners : OnDialogItemClick // : - pure tipe data, klo = -sama isinya diambil

        fun newInstance(data: ModelMahasiswa, listener: OnDialogItemClick) : DetailMahasiswaDialog { // mereturnkan detailmahasiswadialog

            listeners = listener // nilai awl hasil passing ondialogitemclick
            val detail = DetailMahasiswaDialog()

            val bind = Bundle()
            bind.putParcelable("DATA", data)

            detail.arguments = bind
            return detail

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments

        if (args != null)
            dataMahasiswa = args.getParcelable("DATA")!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_detail_mahasiswa_dialog, container, false) // dengan mereturnkan actrivity detailmahasiswa
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogNama.text = dataMahasiswa.nama.toUpperCase()


        toolbarDialog.inflateMenu(R.menu.dialog_menu)
        toolbarDialog.setOnMenuItemClickListener {

            listeners.dialogEditCallback(dataMahasiswa)
            true
        }
    }

    interface OnDialogItemClick {
        fun dialogEditCallback(data: ModelMahasiswa)
        fun dialogDeleteCallback(data: ModelMahasiswa)
    }

    override fun onDestroy() {
        super.onDestroy()
        DAtabaseHelper.closeDatabase()
    }
}