package mx.edu.utng.scv.bdfinal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String
)
