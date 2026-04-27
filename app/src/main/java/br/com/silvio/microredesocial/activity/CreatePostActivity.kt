package br.com.silvio.microredesocial.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.appcompat.app.AppCompatActivity
import br.com.silvio.microredesocial.databinding.ActivityCreatePostBinding
import br.com.silvio.microredesocial.utils.Base64Converter
import br.com.silvio.microredesocial.utils.LocalizacaoHelper
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class CreatePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePostBinding
    private lateinit var galeria: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGaleria()
        setupListeners()
    }

    // 📷 Configura acesso à galeria
    private fun setupGaleria() {
        galeria = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                binding.imgPost.setImageURI(uri)
            } else {
                Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 🎯 Configura botões
    private fun setupListeners() {

        binding.btnEscolherImagem.setOnClickListener {
            abrirGaleria()
        }

        binding.btnPublicar.setOnClickListener {
            publicarPost()
        }
    }

    // 📂 Abre galeria
    private fun abrirGaleria() {
        galeria.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    // ☁️ Salva post no Firestore
    private fun publicarPost(){

        val descricao=binding.edtDescricao.text.toString()

        if(descricao.isBlank()) return

        val imagemString=
            Base64Converter.drawableToString(
                binding.imgPost.drawable
            )

        val auth= FirebaseAuth.getInstance()

        val helper=LocalizacaoHelper(this)

        if (
            checkSelfPermission(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1001
            )
            return
        }

        helper.obterCidade(
            object: LocalizacaoHelper.Callback{

                override fun onCidadeRecebida(cidade:String){

                    val post= hashMapOf(
                        "descricao" to descricao,
                        "imageString" to imagemString,
                        "cidade" to cidade,
                        "autor" to auth.currentUser?.email,
                        "data" to Timestamp.now()
                    )

                    Firebase.firestore
                        .collection("posts")
                        .add(post)
                        .addOnSuccessListener{
                            Toast.makeText(
                                this@CreatePostActivity,
                                "Post criado",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()
                        }
                }

                override fun onErro(msg:String){
                    Toast.makeText(
                        this@CreatePostActivity,
                        msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }
}