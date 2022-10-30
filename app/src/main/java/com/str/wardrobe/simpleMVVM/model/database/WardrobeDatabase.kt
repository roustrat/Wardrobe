package com.str.wardrobe.simpleMVVM.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.str.wardrobe.simpleMVVM.model.dao.CategoryDao
import com.str.wardrobe.simpleMVVM.model.dao.DressDao
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import com.str.wardrobe.simpleMVVM.model.typeconvert.WardrobeTypeConverters

@Database(entities = [
    NamedCategory::class,
    NamedDress::class
    ], version=1, exportSchema = true)
@TypeConverters(WardrobeTypeConverters::class)

abstract class WardrobeDatabase : RoomDatabase() {

    abstract fun namedCategoryDao(): CategoryDao
    abstract fun namedDressDao(): DressDao

}