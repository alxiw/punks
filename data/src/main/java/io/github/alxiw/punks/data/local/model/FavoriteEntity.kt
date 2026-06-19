package io.github.alxiw.punks.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
internal data class FavoriteEntity(
    @PrimaryKey
    val id: Int
)
