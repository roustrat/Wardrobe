package com.str.wardrobe.simpleMVVM.views.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.str.wardrobe.simpleMVVM.views.model.dao.DressDao
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedDress

@Database(entities = [NamedDress::class], version=1, exportSchema = true)
abstract class DressDatabase : RoomDatabase() {



    abstract fun namedCategoryDao(): DressDao

}