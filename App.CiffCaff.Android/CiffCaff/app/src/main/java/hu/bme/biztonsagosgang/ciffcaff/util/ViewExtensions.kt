package hu.bme.biztonsagosgang.ciffcaff.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import hu.bme.biztonsagosgang.ciffcaff.R
import hu.bme.biztonsagosgang.ciffcaff.android.baseUrl
import hu.bme.biztonsagosgang.ciffcaff.android.hostname


fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}
fun View.gone(){
    this.visibility = View.GONE
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun ImageView.loadUrl(url: String?, fragment: Fragment){
    url?.let{
        Glide
            .with(fragment)
            .load(hostname+url)
            .centerCrop()
            .placeholder(R.drawable.nothing_pic)
            .into(this)
    }

}

fun Context.showDialog(
    titleRes: Int,
    messageRes: Int? = null,
    positiveButtonRes: Int = android.R.string.ok,
    negativeButtonRes: Int? = android.R.string.cancel,
    cancallable: Boolean = false,
    onPositiveResponse: () -> Unit = {},
    onNegativeResponse: () -> Unit = {}
) {
    val builder = AlertDialog.Builder(this)
        .setTitle(titleRes)
    messageRes?.let { it1 -> builder.setMessage(it1) }
    builder.setPositiveButton(positiveButtonRes) { _, _ ->
        onPositiveResponse()
    }
    negativeButtonRes?.let { it1 ->
        builder.setNegativeButton(it1) { _, _ ->
            onNegativeResponse()
        }
    }
    builder.setCancelable(cancallable)
    builder.show()
}


fun Fragment.openSetting() {
    val intent = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        Intent(Intent.ACTION_AUTO_REVOKE_PERMISSIONS)
    } else classicSettingsIntent()
    try {
        startActivity(intent)
    } catch (e: Exception) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) startActivity(classicSettingsIntent())
    }
}

private fun Fragment.classicSettingsIntent() = Intent(
    Settings.ACTION_APPLICATION_DETAILS_SETTINGS
).apply {
    data = Uri.fromParts("package", activity?.packageName, null)
}