package hu.bme.biztonsagosgang.ciffcaff.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
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