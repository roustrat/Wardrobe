package com.str.wardrobe.simpleMVVM.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "namedCategory",
    primaryKeys = ["name"],
    // Какие поля должны быть уникальными. Возможно излишне из-за того, что name уже ключ
    indices = [
        Index("name", unique = true)
    ])
data class NamedCategory(
    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE) var name: String,
    @ColumnInfo(name = "description") var description: String
) : java.io.Serializable {

}