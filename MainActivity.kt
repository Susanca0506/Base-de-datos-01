package mx.edu.utng.scv.bdfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mx.edu.utng.scv.bdfinal.data.local.database.AppDatabase
import mx.edu.utng.scv.bdfinal.data.local.dao.PostDao
import mx.edu.utng.scv.bdfinal.data.local.entity.Post

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getInstance(this)
        postDao = db.postDao()
        setContent {
            MaterialTheme {
                PostScreen(postDao)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(postDao: PostDao) {
    val scope = rememberCoroutineScope()
    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
    var text by remember { mutableStateOf("") }
    var editingPost by remember { mutableStateOf<Post?>(null) }

    // Cargar lista al iniciar
    LaunchedEffect(true) {
        posts = postDao.getAll()
    }

    fun refresh() {
        scope.launch { posts = postDao.getAll() }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("CRUD con Room + Compose") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )

                Button(onClick = {
                    scope.launch {
                        if (editingPost == null) {
                            postDao.insert(Post(content = text))
                        } else {
                            // Idealmente deberías tener postDao.update()
                            postDao.delete(editingPost!!)
                            postDao.insert(Post(content = text))
                            editingPost = null
                        }
                        text = ""
                        refresh()
                    }
                }) {
                    Text(if (editingPost == null) "Agregar" else "Actualizar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(posts) { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(post.content)
                            Row {
                                TextButton(onClick = {
                                    text = post.content
                                    editingPost = post
                                }) {
                                    Text("Editar")
                                }
                                TextButton(onClick = {
                                    scope.launch {
                                        postDao.delete(post)
                                        refresh()
                                    }
                                }) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
