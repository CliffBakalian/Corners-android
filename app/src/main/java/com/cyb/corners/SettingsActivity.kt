package com.cyb.corners

import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private fun initStats(){
        val tg = getSharedPreferences("com.cyb.prefs",0).getInt("TOTAL_GAMES",0)
        val tw = getSharedPreferences("com.cyb.prefs",0).getInt("TOTAL_WINS",0)
        tw_value.text = tw.toString()
        tg_value.text = tg.toString()
        if(tg ==0)
            ratio_value.text = "00.00%"
        else
            ratio_value.text = "%.2f".format((tw.toDouble()/tg.toDouble())*100)+"%"
    }
    private fun initColors(){
        val p1 = getSharedPreferences("com.cyb.prefs",0).getInt("P1_COLOR",Color.BLUE)
        val p2 = getSharedPreferences("com.cyb.prefs",0).getInt("P2_COLOR",Color.RED)
        for(i in 1..3){
            val btn:ImageButton = findViewById(resources.getIdentifier("p1c$i", "id", packageName))
            val btn2:ImageButton = findViewById(resources.getIdentifier("p2c$i", "id", packageName))
            btn.setBackgroundColor(Color.WHITE)
            btn2.setBackgroundColor(Color.WHITE)
        }
        when(p1){
            Color.BLUE->p1c1.setBackgroundColor(Color.BLACK)
            Color.GREEN->p1c2.setBackgroundColor(Color.BLACK)
            Color.MAGENTA->p1c3.setBackgroundColor(Color.BLACK)
        }
        when(p2){
            Color.RED->p2c1.setBackgroundColor(Color.BLACK)
            Color.YELLOW->p2c2.setBackgroundColor(Color.BLACK)
            Color.CYAN->p2c3.setBackgroundColor(Color.BLACK)
        }
    }
    fun changeColors(v: View) {
        val prefs = this.getSharedPreferences("com.cyb.prefs",0)
        val editor = prefs.edit()
        when(v.tag.toString()){
            "#0000FF" -> editor.putInt("P1_COLOR", Color.BLUE)
            "#00FF00" -> editor.putInt("P1_COLOR", Color.GREEN)
            "#FF00FF" -> editor.putInt("P1_COLOR", Color.MAGENTA)
            "#FF0000" -> editor.putInt("P2_COLOR", Color.RED)
            "#FFFF00" -> editor.putInt("P2_COLOR", Color.YELLOW)
            "#00FFFF" -> editor.putInt("P2_COLOR", Color.CYAN)
        }
        editor.apply()
        initColors()
    }
    fun reset(v:View){
        val editor=getSharedPreferences("com.cyb.prefs",0).edit()
        editor.putInt("TOTAL_GAMES", 0)
        editor.putInt("TOTAL_WINS", 0)
        editor.apply()
        initStats()
    }

    fun done(v:View){
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()
        initStats()
        initColors()
    }
}
