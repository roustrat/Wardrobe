package com.str.wardrobe.simpleMVVM.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.str.wardrobe.simpleMVVM.model.dao.CategoryDao
import com.str.wardrobe.simpleMVVM.model.dao.DressDao
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress

@Database(entities = [
    NamedCategory::class,
    NamedDress::class
    ], version=1, exportSchema = true)

abstract class WardrobeDatabase : RoomDatabase() {

    abstract fun namedCategoryDao(): CategoryDao
    abstract fun namedDressDao(): DressDao

}