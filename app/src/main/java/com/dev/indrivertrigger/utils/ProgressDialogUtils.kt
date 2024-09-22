
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.dev.indrivertrigger.databinding.DialogProgressBinding

object ProgressDialogUtils {

    private var progressDialog: Dialog? = null

    fun showProgressDialog(context: Context) {
        hideProgressDialog()

        val binding = DialogProgressBinding.inflate(LayoutInflater.from(context))

        progressDialog = Dialog(context)
        progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog?.setCancelable(false)
        progressDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog?.setContentView(binding.root)

        /*Glide.with(context)
            .asGif()
            .load(R.drawable.loading)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.progressImageView)*/

        progressDialog?.show()
    }

    fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}
