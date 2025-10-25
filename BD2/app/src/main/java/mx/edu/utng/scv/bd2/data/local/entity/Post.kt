package mx.edu.utng.scv.bd2.data.local.entity

import androidx.compose.runtime.MovableContent
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String
)