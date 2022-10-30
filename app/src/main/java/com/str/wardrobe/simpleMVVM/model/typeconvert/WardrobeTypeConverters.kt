package com.str.wardrobe.simpleMVVM.model.typeconvert

import androidx.room.TypeConverter
import java.util.*

class WardrobeTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

}