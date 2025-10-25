package mx.edu.utng.scv.bd2.data.local.database

import android.content.Context;

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import kotlin.jvm.Volatile;
import mx.edu.utng.scv.bd2.data.local.dao.PostDao;
import mx.edu.utng.scv.bd2.data.local.entity.Post;

@Database(entities = [Post::class], version = 1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun postDao():PostDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "post_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}
