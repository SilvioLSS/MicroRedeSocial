package br.com.silvio.microredesocial.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.com.silvio.microredesocial.databinding.ActivityProfileBinding
import br.com.silvio.microredesocial.utils.Base64Converter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var galeria:
            ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGaleria()

        carregarPerfil()

        setupListeners()
    }

    private fun setupGaleria(){

        galeria = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ){ uri ->

            if(uri != null){
                binding.profilePicture.setImageURI(uri)
            }else{
                Toast.makeText(
                    this,
                    "Nenhuma imagem selecionada",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun abrirGaleria(){

        galeria.launch(
            PickVisualMediaRequest(
                ActivityResultContracts
                    .PickVisualMedia
                    .ImageOnly
            )
        )
    }

    private fun setupListeners() {

        binding.btnSalvar.setOnClickListener {
            salvarPerfil()
        }

        // clicar na foto abre galeria
        binding.profilePicture.setOnClickListener{
            abrirGaleria()
        }

        /*
        Se seu XML tiver botão alterar foto:
        binding.btnAlterarFoto.setOnClickListener{
            abrirGaleria()
        }
        */
    }

    private fun salvarPerfil(){

        val user =
            FirebaseAuth
                .getInstance()
                .currentUser ?: return

        val username =
            binding.editUsername.text
                .toString()
                .trim()

        val nomeCompleto =
            binding.editNomeCompleto.text
                .toString()
                .trim()

        if(username.isBlank() ||
            nomeCompleto.isBlank()){

            Toast.makeText(
                this,
                "Preencha todos os campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val fotoString =
            Base64Converter.drawableToString(
                binding.profilePicture.drawable
            )

        val dados = hashMapOf(
            "username" to username,
            "nomeCompleto" to nomeCompleto,
            "fotoPerfil" to fotoString
        )

        Firebase.firestore
            .collection("usuarios")
            .document(user.email!!)
            .set(dados)
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Perfil salvo!",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(
                        this,
                        HomeActivity::class.java
                    )
                )

                finish()
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao salvar",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun carregarPerfil(){

        val email =
            FirebaseAuth.getInstance()
                .currentUser?.email ?: return

        Firebase.firestore
            .collection("usuarios")
            .document(email)
            .get()
            .addOnSuccessListener { doc ->

                if(doc.exists()){

                    binding.editUsername.setText(
                        doc.getString("username") ?: ""
                    )

                    binding.editNomeCompleto.setText(
                        doc.getString("nomeCompleto") ?: ""
                    )
                }
            }
    }
}