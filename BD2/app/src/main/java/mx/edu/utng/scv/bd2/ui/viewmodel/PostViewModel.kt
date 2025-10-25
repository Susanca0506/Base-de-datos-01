package mx.edu.utng.scv.bd2.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.edu.utng.scv.bd2.data.local.entity.Post
import mx.edu.utng.scv.bd2.data.repository.PostRepository

class PostViewModel(private val repository: PostRepository):
    ViewModel(){

        //Devuelve todas las publicaciones
        val posts: StateFlow<List<Post>> =
            repository.posts.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(
                    5000
                ),
                initialValue = emptyList()
            )

    //Agregar nueva publicación
    fun addPost(content: String){
        viewModelScope.launch {
            val post = Post(content = content)
            repository.insert(post)
        }
    }

    //Borrar post
    fun deletePost(post: Post){
        viewModelScope.launch {
            repository.delete(post)
        }
    }

    //Actualizar Post
    fun updatePost(content: String){
        viewModelScope.launch {
            val post = Post(content = content)
            repository.update(post)
        }
    }

    //Borrar todas las publicaciones
    fun deleteAllPosts(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}