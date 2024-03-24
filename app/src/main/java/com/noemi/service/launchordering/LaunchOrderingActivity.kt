package com.noemi.service.launchordering

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Toast
import com.noemi.service.R

class LaunchOrderingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_ordering)
        callForYourLunch()
    }

    private fun callForYourLunch() {
        when(checkTelephonyManager()){
            true -> placeYourOrder()
            else -> {
                finish()
                Toast.makeText(this, getString(R.string.label_error_missing_sim), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkTelephonyManager(): Boolean {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.simState == TelephonyManager.SIM_STATE_READY
    }

    private fun placeYourOrder(){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:+441926839581") // Just Eat
        startActivity(intent)
    }
}