package com.example.mynews.ui.Db

import android.content.Context
import androidx.room.*
import com.example.mynews.ui.models.Article

@Database(
    entities = [Article::class],
    version = 2
)

@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun getarticledao(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}