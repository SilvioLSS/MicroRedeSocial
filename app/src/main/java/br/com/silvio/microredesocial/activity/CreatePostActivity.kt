package br.com.silvio.microredesocial.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.appcompat.app.AppCompatActivity
import br.com.silvio.microredesocial.databinding.ActivityCreatePostBinding
import br.com.silvio.microredesocial.utils.Base64Converter
import com.google.firebase.Firebase
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
    private fun publicarPost() {

        val descricao = binding.edtDescricao.text.toString()

        if (descricao.isEmpty()) {
            Toast.makeText(this, "Digite uma descrição", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.imgPost.drawable == null) {
            Toast.makeText(this, "Selecione uma imagem", Toast.LENGTH_SHORT).show()
            return
        }

        val imagemString = Base64Converter.drawableToString(binding.imgPost.drawable)

        val db = Firebase.firestore

        val post = hashMapOf(
            "descricao" to descricao,
            "imageString" to imagemString
        )

        db.collection("posts")
            .add(post)
            .addOnSuccessListener {
                Toast.makeText(this, "Post criado!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao criar post", Toast.LENGTH_SHORT).show()
            }
    }
}