package com.str.wardrobe.simpleMVVM.views.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.str.wardrobe.simpleMVVM.views.model.dao.CategoryDao
import com.str.wardrobe.simpleMVVM.views.model.dao.DressDao
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedDress

@Database(entities = [
    NamedCategory::class,
    NamedDress::class
    ], version=1, exportSchema = true)

abstract class WardrobeDatabase : RoomDatabase() {

    abstract fun namedCategoryDao(): CategoryDao
    abstract fun namedDressDao(): DressDao

}