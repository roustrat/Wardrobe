package com.str.wardrobe.simpleMVVM.views.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedCategory

@Dao
interface CategoryDao {

    @Insert
    fun insertData(data: NamedCategory)

    @Transaction
    @Query("SELECT * FROM namedCategory WHERE name=:name")
    fun getDressCategory(name: String): LiveData<NamedCategory>

    @Transaction
    @Query("SELECT * FROM namedCategory")
    fun getAllCategory(): LiveData<List<NamedCategory?>>

    @Transaction
    @Insert
    fun addCategory(category: NamedCategory)

    @Transaction
    @Update
    fun updateCategory(category: NamedCategory)

}