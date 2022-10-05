package com.str.wardrobe.simpleMVVM.views.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

/**
 * Represents dress data
 */

@Entity(tableName = "namedDress", primaryKeys = ["id"],
// Какие поля должны быть уникальными. Возможно излишне из-за того, что name уже ключ
    indices = [Index("id", unique = true)])
data class NamedDress(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "id", collate = ColumnInfo.NOCASE) var imgId: Int,
    @ColumnInfo(name = "description") var description: String
)