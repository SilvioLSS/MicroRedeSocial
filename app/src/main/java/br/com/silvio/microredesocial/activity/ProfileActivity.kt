package br.com.silvio.microredesocial.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.com.silvio.microredesocial.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import br.com.silvio.microredesocial.utils.Base64Converter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    val galeria = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->

        if (uri != null) {
            binding.profilePicture.setImageURI(uri)
        } else {
            Toast.makeText(this, "Nenhuma foto selecionada", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAlterarFoto.setOnClickListener {

            galeria.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )

        }

        binding.btnSalvar.setOnClickListener {
            val user = firebaseAuth.currentUser

            if (user != null) {
                val email = user.email!!
                val username = binding.editUsername.text.toString()
                val nomeCompleto = binding.editNomeCompleto.text.toString()

                val fotoString = Base64Converter.drawableToString(binding.profilePicture.drawable)

                val db = Firebase.firestore

                val dados = hashMapOf(
                    "username" to username,
                    "nomeCompleto" to nomeCompleto,
                    "fotoPerfil" to fotoString
                )

                db.collection("usuarios").document(email)
                    .set(dados)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Perfil salvo!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
            }
        }
    }

    val firebaseAuth = FirebaseAuth.getInstance()


}