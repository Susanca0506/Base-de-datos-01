package mx.edu.utng.scv.bdfinal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import mx.edu.utng.scv.bdfinal.data.local.entity.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    suspend fun getAll(): List<Post>

    @Insert
    suspend fun insert(post: Post)

    @Delete
    suspend fun delete(post: Post)

}