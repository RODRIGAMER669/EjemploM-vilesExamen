package es.etg.pmdm.examenmovilesejemplo.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import es.etg.pmdm.examenmovilesejemplo.ui.MainActivity
import es.etg.pmdm.examenmovilesejemplo.databinding.ActivityLoginBinding
import es.etg.pmdm.examenmovilesejemplo.ui.viewModel.TituloFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(binding.fgTitulo.id, TituloFragment())
            }
        }
    }

    fun aceptar(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}