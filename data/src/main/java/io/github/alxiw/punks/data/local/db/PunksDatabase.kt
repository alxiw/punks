package io.github.alxiw.punks.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.alxiw.punks.data.local.dao.FavoriteDao
import io.github.alxiw.punks.data.local.model.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
internal abstract class PunksDatabase : RoomDatabase() {
    abstract fun favoriteBeerDao(): FavoriteDao
}
