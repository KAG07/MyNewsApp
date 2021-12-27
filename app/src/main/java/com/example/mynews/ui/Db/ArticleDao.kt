package com.example.mynews.ui.Db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mynews.ui.models.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long

    @Query("SELECT * FROM articles")
    fun getallarticle():LiveData<List<Article>>

    @Delete
    suspend fun deletearticle()
}