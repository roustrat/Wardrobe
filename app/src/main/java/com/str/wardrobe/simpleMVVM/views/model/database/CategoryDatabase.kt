package com.str.wardrobe.simpleMVVM.views.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.str.wardrobe.simpleMVVM.views.model.dao.CategoryDao
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedCategory

@Database(entities = [NamedCategory::class], version=1, exportSchema = true)

abstract class CategoryDatabase : RoomDatabase() {

    abstract fun namedCategoryDao(): CategoryDao

}