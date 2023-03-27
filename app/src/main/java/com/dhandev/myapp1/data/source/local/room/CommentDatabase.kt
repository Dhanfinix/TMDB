package com.dhandev.myapp1.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dhandev.myapp1.data.source.local.entity.CommentEntity

@Database(entities = [CommentEntity::class], version = 1)
abstract class CommentDatabase: RoomDatabase() {
    abstract fun commentDao(): CommentDao

    companion object {
        @Volatile
        private var INSTANCE: CommentDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): CommentDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CommentDatabase::class.java, "comment_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}


