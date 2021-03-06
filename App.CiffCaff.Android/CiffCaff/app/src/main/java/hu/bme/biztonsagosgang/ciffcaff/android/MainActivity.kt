package hu.bme.biztonsagosgang.ciffcaff.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import hu.bme.biztonsagosgang.ciffcaff.R
import hu.bme.biztonsagosgang.ciffcaff.util.switchLightStatusBar

class MainActivity : AppCompatActivity(
    R.layout.activity_main
) {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setUpNavigation()
        }
    }

    private val navController: NavController by lazy { findNavController(R.id.fragment_navigator) }

    private fun setUpNavigation() {
        navController.addOnDestinationChangedListener { _, dest, _ ->
            when(dest.id){
                R.id.caffDetailsFragment -> {
                    switchLightStatusBar(true)
                }
                else -> {
                    switchLightStatusBar(false)
                }
            }
        }
    }
}