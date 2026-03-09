package br.com.silvio.microredesocial

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.silvio.microredesocial.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFirebase()
        setupListeners()
    }

    fun setupFirebase(){
        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun setupListeners() {
        binding.btnLogin.setOnClickListener { autenticarUsuario() }
    }

    fun autenticarUsuario(){
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Erro no login", Toast.LENGTH_LONG).show()
                }
            }
    }

}
