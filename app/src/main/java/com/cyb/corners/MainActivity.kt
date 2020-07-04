package com.cyb.corners

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val USER_PREFS = "com.cyb.prefs"
    private fun initButtons(){
        play_button.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        settings_button.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        help_button.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = this.getSharedPreferences(USER_PREFS, 0)
        val editor:SharedPreferences.Editor = prefs.edit()
        editor.putInt("P1_COLOR", Color.BLUE)
        editor.putInt("P2_COLOR", Color.RED)
        editor.commit()
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        initButtons()
    }

}
