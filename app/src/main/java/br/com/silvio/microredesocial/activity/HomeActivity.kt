package br.com.silvio.microredesocial.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.silvio.microredesocial.adapter.PostAdapter
import br.com.silvio.microredesocial.databinding.ActivityHomeBinding
import br.com.silvio.microredesocial.model.Post
import br.com.silvio.microredesocial.utils.Base64Converter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.Query

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: PostAdapter
    private lateinit var posts: ArrayList<Post>
    private var ultimoDocumento: DocumentSnapshot?=null
    private var carregando=false
    private var buscaCidade:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupListeners()

        // carregar feed automaticamente (melhor UX)
        carregarFeed()

        binding.recyclerView.addOnScrollListener(
            object: RecyclerView.OnScrollListener(){

                override fun onScrolled(
                    rv:RecyclerView,
                    dx:Int,
                    dy:Int
                ){

                    val lm=
                        rv.layoutManager as LinearLayoutManager

                    if(
                        lm.findLastVisibleItemPosition()
                        >= posts.size-2
                    ){
                        carregarFeed()
                    }

                }})
    }

    private fun carregarFeed(){

        if(carregando) return

        carregando=true

        var query: Query = Firebase.firestore
            .collection("posts")

        if (buscaCidade != null) {
            query = query.whereEqualTo(
                "cidade",
                buscaCidade
            )
        }

        query = query
            .orderBy(
                "data",
                Query.Direction.DESCENDING
            )
            .limit(5)

        if(ultimoDocumento!=null){
            query=query.startAfter(
                ultimoDocumento!!
            )
        }

        query.get()
            .addOnSuccessListener { docs ->

                if(!::posts.isInitialized){
                    posts= arrayListOf()
                }

                if(!docs.isEmpty){

                    ultimoDocumento=
                        docs.documents.last()

                    for(doc in docs.documents){

                        val bitmap=
                            Base64Converter.stringToBitmap(
                                doc["imageString"].toString()
                            )

                        posts.add(
                            Post(
                                descricao=doc["descricao"].toString(),
                                imagem=bitmap,
                                cidade=doc["cidade"].toString(),
                                data=doc.getTimestamp("data"),
                                autor=doc["autor"].toString()
                            )
                        )
                    }

                    if(!::adapter.isInitialized){
                        adapter = PostAdapter(posts)
                        binding.recyclerView.adapter = adapter
                    }else{
                        adapter.notifyDataSetChanged()
                    }

                    if(binding.recyclerView.adapter==null){
                        binding.recyclerView.layoutManager=
                            LinearLayoutManager(this)

                        binding.recyclerView.adapter=
                            adapter
                    }else{
                        adapter.notifyDataSetChanged()
                    }
                }

                carregando=false

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

        binding.btnBuscarCidade.setOnClickListener {

            val cidadeDigitada =
                binding.edtCidadeBusca.text
                    .toString()
                    .trim()

            if(cidadeDigitada.isEmpty()){
                Toast.makeText(
                    this,
                    "Digite uma cidade",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            buscaCidade = cidadeDigitada

            // limpa feed anterior
            if(::posts.isInitialized){
                posts.clear()
            }

            // reseta paginação
            ultimoDocumento = null

            carregarFeed()
        }
    }
}