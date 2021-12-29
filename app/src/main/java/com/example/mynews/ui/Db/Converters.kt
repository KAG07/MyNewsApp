package com.example.mynews.ui.Db

import androidx.room.TypeConverter
import com.example.mynews.ui.models.Source


public class Converters {
    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }
    @TypeConverter
    fun toSource(name:String): Source {
        return Source(name,name)
    }
}