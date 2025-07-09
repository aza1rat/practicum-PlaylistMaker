package ru.aza1rat.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val search = findViewById<MaterialButton>(R.id.search)
        val mediaLibrary = findViewById<MaterialButton>(R.id.media_library)
        val settings = findViewById<MaterialButton>(R.id.settings)
        search.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity,"Настройки | Через анонимный класс",
                    Toast.LENGTH_LONG).show()
            }
        })
        mediaLibrary.setOnClickListener {
            Toast.makeText(this@MainActivity,"Медиатека | Через лямбду",
                Toast.LENGTH_LONG).show()
        }
        settings.setOnClickListener {
            Toast.makeText(this@MainActivity,"Настройки | Через лямбду",
                Toast.LENGTH_LONG).show()
        }
    }
}