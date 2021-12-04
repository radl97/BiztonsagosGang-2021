package hu.bme.biztonsagosgang.ciffcaff.util

import android.app.Activity
import android.graphics.Color
import androidx.core.view.WindowCompat

fun Activity.switchLightStatusBar(
    isLight: Boolean
) {
    window.apply {
        WindowCompat.getInsetsController(this, decorView)?.let {
            statusBarColor =
                if (isLight) Color.parseColor("#FFFFFF") else Color.parseColor("#0F4C5C")
            it.isAppearanceLightStatusBars = isLight
        }
    }
}
