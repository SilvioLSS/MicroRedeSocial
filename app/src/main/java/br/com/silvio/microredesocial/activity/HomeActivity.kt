package br.com.silvio.microredesocial.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.silvio.microredesocial.adapter.PostAdapter
import br.com.silvio.microredesocial.databinding.ActivityHomeBinding
import br.com.silvio.microredesocial.model.Post
import br.com.silvio.microredesocial.utils.Base64Converter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: PostAdapter
    private lateinit var posts: ArrayList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupListeners()

        // carregar feed automaticamente (melhor UX)
        carregarFeed()
    }

    private fun carregarFeed() {

        val db = Firebase.firestore

        db.collection("posts").get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    posts = ArrayList()

                    for (document in task.result.documents) {

                        val descricao = document["descricao"].toString()
                        val imageString = document["imageString"].toString()

                        val bitmap = Base64Converter.stringToBitmap(imageString)

                        posts.add(Post(descricao, bitmap))
                    }

                    adapter = PostAdapter(posts)

                    binding.recyclerView.layoutManager =
                        LinearLayoutManager(this)

                    binding.recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this, "Erro ao carregar feed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setupUI() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.tvWelcome.text = "Olá, ${user.displayName ?: user.email}"
            binding.tvUserEmail.text = user.email ?: "Email não disponível"
        }
    }

    private fun setupListeners() {

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            Toast.makeText(this, "Logout realizado!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.btnNovoPost.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        carregarFeed()
    }
}