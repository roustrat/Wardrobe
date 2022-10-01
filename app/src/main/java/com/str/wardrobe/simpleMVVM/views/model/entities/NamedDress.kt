package com.str.wardrobe.simpleMVVM.views.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Represents dress data
 */

@Entity(tableName = "namedDress", primaryKeys = ["name"])
data class NamedDress(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "id") var imgId: Int,
    @ColumnInfo(name = "description") var description: String? // Nullабельность надо ли прописывать в [@ColumnInfo]?
)